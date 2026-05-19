package com.froyder.personaltrainer.data.local

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

lateinit var appContext: Context

actual fun createSettings(): Settings {
    return SharedPreferencesSettings(
        appContext.getSharedPreferences("personal_trainer_prefs", Context.MODE_PRIVATE)
    )
}