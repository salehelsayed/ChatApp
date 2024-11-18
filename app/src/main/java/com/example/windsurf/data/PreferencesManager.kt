package com.example.windsurf.data

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    fun saveAIRole(contactId: Int, role: String) {
        sharedPreferences.edit().putString(getKeyForContact(contactId), role).apply()
    }

    fun getAIRole(contactId: Int): String {
        return sharedPreferences.getString(getKeyForContact(contactId), DEFAULT_ROLE) ?: DEFAULT_ROLE
    }

    private fun getKeyForContact(contactId: Int): String {
        return "ai_role_$contactId"
    }

    companion object {
        private const val PREFS_NAME = "windsurf_preferences"
        private const val DEFAULT_ROLE = "Click to configure role"
    }
}
