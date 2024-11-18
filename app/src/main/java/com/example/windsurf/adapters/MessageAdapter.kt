package com.example.windsurf.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.windsurf.R
import com.example.windsurf.models.Message
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter : ListAdapter<Message, MessageAdapter.MessageViewHolder>(MessageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageText: TextView = itemView.findViewById(R.id.messageText)
        private val timeText: TextView = itemView.findViewById(R.id.timeText)
        private val messageCard: View = itemView.findViewById(R.id.messageCard)
        private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        
        fun bind(message: Message) {
            messageText.text = message.content
            timeText.text = timeFormat.format(Date(message.timestamp))
            
            // Set layout constraints based on message source
            val params = messageCard.layoutParams as ViewGroup.MarginLayoutParams
            if (message.isFromAi) {
                params.marginStart = 48
                params.marginEnd = 16
                messageCard.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.ai_message_bg))
            } else {
                params.marginStart = 16
                params.marginEnd = 48
                messageCard.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.user_message_bg))
            }
            messageCard.layoutParams = params
        }
    }
}

class MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }
}
