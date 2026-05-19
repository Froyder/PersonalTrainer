package com.froyder.personaltrainer.presentation.theme

enum class ColorScheme {
    OCEAN, FOREST, SUNSET, MIDNIGHT, STEEL
}

enum class DarkModePreference {
    SYSTEM, LIGHT, DARK
}

data class ThemePreferences(
    val colorScheme: ColorScheme = ColorScheme.OCEAN,
    val darkMode: DarkModePreference = DarkModePreference.SYSTEM
)