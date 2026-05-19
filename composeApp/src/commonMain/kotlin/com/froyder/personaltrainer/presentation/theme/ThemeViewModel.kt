package com.froyder.personaltrainer.presentation.theme

import androidx.lifecycle.ViewModel
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ThemeViewModel(private val settings: Settings) : ViewModel() {

    private val _preferences = MutableStateFlow(loadPreferences())
    val preferences = _preferences.asStateFlow()

    private fun loadPreferences(): ThemePreferences {
        val schemeName = settings.getStringOrNull("color_scheme") ?: ColorScheme.OCEAN.name
        val darkModeName = settings.getStringOrNull("dark_mode") ?: DarkModePreference.SYSTEM.name
        return ThemePreferences(
            colorScheme = ColorScheme.valueOf(schemeName),
            darkMode = DarkModePreference.valueOf(darkModeName)
        )
    }

    fun setColorScheme(scheme: ColorScheme) {
        settings.putString("color_scheme", scheme.name)
        _preferences.value = _preferences.value.copy(colorScheme = scheme)
    }

    fun setDarkMode(mode: DarkModePreference) {
        settings.putString("dark_mode", mode.name)
        _preferences.value = _preferences.value.copy(darkMode = mode)
    }
}