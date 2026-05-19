package com.froyder.personaltrainer.data.remote

object ApiConfig {
    val GEMINI_API_KEY: String get() = getGeminiApiKey()
    const val GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/v1beta"
    const val GEMINI_MODEL = "gemini-3.1-flash-lite"
}

expect fun getGeminiApiKey(): String

