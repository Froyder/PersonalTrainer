package com.froyder.personaltrainer.data.remote

import platform.Foundation.NSBundle

actual fun getGeminiApiKey(): String =
    NSBundle.mainBundle.objectForInfoDictionaryKey("GeminiApiKey") as? String
        ?: error("GeminiApiKey not found in Info.plist")