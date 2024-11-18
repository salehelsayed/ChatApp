package com.example.windsurf.api

import com.example.windsurf.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class ChatGPTService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val apiKey = BuildConfig.OPENAI_API_KEY
    private val baseUrl = "https://api.openai.com/v1/chat/completions"

    suspend fun generateResponse(prompt: String, role: String, messageHistory: List<Pair<String, Boolean>>): String {
        return withContext(Dispatchers.IO) {
            try {
                val messages = JSONArray().apply {
                    // System message to set the AI's role
                    put(JSONObject().apply {
                        put("role", "system")
                        put("content", "You are $role. Respond accordingly to maintain this role.")
                    })

                    // Add message history
                    messageHistory.forEach { (content, isFromAi) ->
                        put(JSONObject().apply {
                            put("role", if (isFromAi) "assistant" else "user")
                            put("content", content)
                        })
                    }

                    // Add the current message
                    put(JSONObject().apply {
                        put("role", "user")
                        put("content", prompt)
                    })
                }

                val requestBody = JSONObject().apply {
                    put("model", "gpt-3.5-turbo")
                    put("messages", messages)
                    put("max_tokens", 1000)
                    put("temperature", 0.7)
                }.toString()

                val request = Request.Builder()
                    .url(baseUrl)
                    .addHeader("Authorization", "Bearer $apiKey")
                    .addHeader("Content-Type", "application/json")
                    .post(requestBody.toRequestBody("application/json".toMediaType()))
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string() ?: throw Exception("Empty response")
                
                if (!response.isSuccessful) {
                    throw Exception("API call failed: $responseBody")
                }

                val jsonResponse = JSONObject(responseBody)
                val choices = jsonResponse.getJSONArray("choices")
                if (choices.length() > 0) {
                    choices.getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content")
                        .trim()
                } else {
                    throw Exception("No response from ChatGPT")
                }
            } catch (e: Exception) {
                "Sorry, I encountered an error: ${e.message}"
            }
        }
    }
}
