package com.example.windsurf.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.windsurf.security.SecurityManager

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    private var _content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFromAi: Boolean,
    val aiContactId: Int
) {
    var content: String
        get() = _content
        set(value) {
            _content = SecurityManager.sanitizeInput(value)
        }

    companion object {
        fun createEncrypted(
            content: String,
            isFromAi: Boolean,
            aiContactId: Int,
            securityManager: SecurityManager
        ): Message {
            val sanitizedContent = SecurityManager.sanitizeInput(content)
            val encryptedContent = securityManager.encryptMessage(sanitizedContent)
            return Message(
                _content = encryptedContent,
                isFromAi = isFromAi,
                aiContactId = aiContactId
            )
        }

        fun decrypt(message: Message, securityManager: SecurityManager): Message {
            val decryptedContent = securityManager.decryptMessage(message._content)
            return message.copy(_content = decryptedContent)
        }
    }
}
