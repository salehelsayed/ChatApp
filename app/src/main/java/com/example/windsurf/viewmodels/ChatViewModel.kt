package com.example.windsurf.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.windsurf.api.ChatGPTService
import com.example.windsurf.data.AppDatabase
import com.example.windsurf.models.Message
import com.example.windsurf.security.SecurityManager
import kotlinx.coroutines.launch

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val messageDao = database.messageDao()
    private val chatGPTService = ChatGPTService()
    private val securityManager = SecurityManager(application)
    private var currentContactId: Int = -1
    private var currentRole: String = ""
    private var isAuthenticated = MutableLiveData(false)

    val messages: LiveData<List<Message>> by lazy {
        messageDao.getMessagesForContact(currentContactId).asLiveData().map { messages ->
            messages.map { message -> Message.decrypt(message, securityManager) }
        }
    }

    fun setContactId(contactId: Int) {
        currentContactId = contactId
    }

    fun setRole(role: String) {
        currentRole = role
    }

    fun sendMessage(content: String) {
        if (!isAuthenticated.value!!) {
            return
        }
        
        viewModelScope.launch {
            // Create and save user message
            val userMessage = Message.createEncrypted(
                content = content,
                isFromAi = false,
                aiContactId = currentContactId,
                securityManager = securityManager
            )
            messageDao.insert(userMessage)

            // Get message history for context
            val messageHistory = messages.value?.takeLast(10)?.map { 
                Pair(it.content, it.isFromAi)
            } ?: emptyList()

            try {
                // Get AI response
                val response = chatGPTService.generateResponse(content, currentRole, messageHistory)
                
                // Create and save AI message
                val aiMessage = Message.createEncrypted(
                    content = response,
                    isFromAi = true,
                    aiContactId = currentContactId,
                    securityManager = securityManager
                )
                messageDao.insert(aiMessage)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun authenticate(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (securityManager.isBiometricAvailable()) {
            // Get the Application context
            val context = getApplication<Application>()
            if (context is android.app.Activity) {
                securityManager.showBiometricPrompt(
                    activity = context,
                    onSuccess = {
                        isAuthenticated.value = true
                        onSuccess()
                    },
                    onError = { error ->
                        isAuthenticated.value = false
                        onError(error)
                    }
                )
            }
        } else {
            // If biometric is not available, consider the user authenticated
            isAuthenticated.value = true
            onSuccess()
        }
    }

    fun isAuthenticationRequired(): Boolean {
        return securityManager.isBiometricAvailable()
    }

    fun clearAuthentication() {
        isAuthenticated.value = false
    }
}
