package com.example.windsurf

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.windsurf.adapters.MessageAdapter
import com.example.windsurf.databinding.ActivityChatBinding
import com.example.windsurf.viewmodels.ChatViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val contactId = intent.getIntExtra("contact_id", -1)
        val contactRole = intent.getStringExtra("contact_role") ?: "AI Assistant"

        viewModel.setContactId(contactId)
        viewModel.setRole(contactRole)

        setupToolbar(contactId, contactRole)
        setupRecyclerView()
        setupMessageInput(contactId)
        observeMessages()
        
        if (viewModel.isAuthenticationRequired()) {
            showAuthenticationDialog()
        }
    }

    private fun setupToolbar(contactId: Int, contactRole: String) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        binding.contactName.text = "AI Contact $contactId"
        binding.contactRole.text = contactRole
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter()
        layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
            reverseLayout = false
        }
        
        binding.chatRecyclerView.apply {
            adapter = messageAdapter
            layoutManager = this@ChatActivity.layoutManager
            setHasFixedSize(true)
        }
    }

    private fun setupMessageInput(contactId: Int) {
        binding.sendButton.setOnClickListener {
            val content = binding.messageInput.text?.toString()?.trim() ?: ""
            if (content.isNotEmpty()) {
                viewModel.sendMessage(content, contactId)
                binding.messageInput.setText("")
                scrollToBottom()
            }
        }
    }

    private fun observeMessages() {
        viewModel.messages.observe(this) { messages ->
            messageAdapter.submitList(messages) {
                scrollToBottom()
            }
        }
    }

    private fun scrollToBottom() {
        if (messageAdapter.itemCount > 0) {
            binding.chatRecyclerView.post {
                binding.chatRecyclerView.smoothScrollToPosition(messageAdapter.itemCount - 1)
            }
        }
    }

    private fun showAuthenticationDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Authentication Required")
            .setMessage("Please authenticate to access the chat")
            .setCancelable(false)
            .setPositiveButton("Authenticate") { _, _ ->
                authenticateUser()
            }
            .setNegativeButton("Cancel") { _, _ ->
                finish()
            }
            .show()
    }

    private fun authenticateUser() {
        viewModel.authenticate(
            onSuccess = {
                Toast.makeText(this, "Authentication successful", Toast.LENGTH_SHORT).show()
            },
            onError = { error ->
                Toast.makeText(this, "Authentication failed: $error", Toast.LENGTH_LONG).show()
                finish()
            }
        )
    }

    override fun onPause() {
        super.onPause()
        viewModel.clearAuthentication()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
