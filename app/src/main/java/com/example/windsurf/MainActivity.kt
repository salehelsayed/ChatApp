package com.example.windsurf

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.windsurf.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.example.windsurf.data.PreferencesManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferencesManager = PreferencesManager(this)
        loadSavedRoles()
        setupContact1()
        setupContact2()
    }

    private fun loadSavedRoles() {
        binding.contact1Role.text = preferencesManager.getAIRole(1)
        binding.contact2Role.text = preferencesManager.getAIRole(2)
    }

    private fun setupContact1() {
        binding.contact1Card.setOnClickListener {
            startChat(1)
        }

        binding.contact1Config.setOnClickListener {
            showConfigDialog(1, binding.contact1Role)
        }
    }

    private fun setupContact2() {
        binding.contact2Card.setOnClickListener {
            startChat(2)
        }

        binding.contact2Config.setOnClickListener {
            showConfigDialog(2, binding.contact2Role)
        }
    }

    private fun showConfigDialog(contactId: Int, roleTextView: TextView) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_ai_config, null)
        val roleInput = dialogView.findViewById<TextInputEditText>(R.id.roleInput)
        
        // Set the current role in the input field
        val currentRole = preferencesManager.getAIRole(contactId)
        roleInput.setText(currentRole)
        roleInput.setSelection(currentRole.length) // Place cursor at the end
        
        MaterialAlertDialogBuilder(this)
            .setTitle("Configure AI Role")
            .setView(dialogView)
            .setPositiveButton("Save") { dialog, _ ->
                val newRole = roleInput.text?.toString()?.trim() ?: ""
                if (newRole.isNotEmpty()) {
                    preferencesManager.saveAIRole(contactId, newRole)
                    roleTextView.text = newRole
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun startChat(contactId: Int) {
        val intent = Intent(this, ChatActivity::class.java).apply {
            putExtra("contact_id", contactId)
            putExtra("contact_role", 
                if (contactId == 1) 
                    binding.contact1Role.text 
                else 
                    binding.contact2Role.text
            )
        }
        startActivity(intent)
    }
}