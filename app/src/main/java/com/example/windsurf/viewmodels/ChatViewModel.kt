package com.example.windsurf.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.windsurf.api.ChatGPTService
import com.example.windsurf.data.AppDatabase
import com.example.windsurf.models.Message
import kotlinx.coroutines.launch

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val messageDao = database.messageDao()
    private val chatGPTService = ChatGPTService()
    private var currentContactId: Int = -1
    private var currentRole: String = ""

    val messages: LiveData<List<Message>> by lazy {
        messageDao.getMessagesForContact(currentContactId).asLiveData()
    }

    fun setContactId(contactId: Int) {
        currentContactId = contactId
    }

    fun setRole(role: String) {
        currentRole = role
    }

    fun sendMessage(content: String, aiContactId: Int) {
        viewModelScope.launch {
            // Add user message
            val userMessage = Message(
                content = content,
                isFromAi = false,
                aiContactId = aiContactId
            )
            messageDao.insertMessage(userMessage)

            // Get message history
            val messageHistory = messages.value?.map { 
                Pair(it.content, it.isFromAi) 
            } ?: emptyList()

            // Generate AI response
            val aiResponse = chatGPTService.generateResponse(content, currentRole, messageHistory)

            // Add AI response
            val aiMessage = Message(
                content = aiResponse,
                isFromAi = true,
                aiContactId = aiContactId
            )
            messageDao.insertMessage(aiMessage)
        }
    }
}
