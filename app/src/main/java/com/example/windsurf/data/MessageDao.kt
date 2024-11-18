package com.example.windsurf.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.windsurf.models.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE aiContactId = :contactId ORDER BY timestamp ASC")
    fun getMessagesForContact(contactId: Int): Flow<List<Message>>

    @Insert
    suspend fun insertMessage(message: Message)

    @Query("DELETE FROM messages WHERE aiContactId = :contactId")
    suspend fun deleteAllMessagesForContact(contactId: Int)
}
