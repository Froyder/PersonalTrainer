package com.froyder.personaltrainer.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// --- Ocean ---
private val OceanPrimaryLight = Color(0xFF1A5F9E)
private val OceanSecondaryLight = Color(0xFF4A90D9)
private val OceanTertiaryLight = Color(0xFF2196A6)
private val OceanBackgroundLight = Color(0xFFF5F9FF)
private val OceanSurfaceLight = Color(0xFFFFFFFF)

private val OceanPrimaryDark = Color(0xFF4A90D9)
private val OceanSecondaryDark = Color(0xFF1A5F9E)
private val OceanTertiaryDark = Color(0xFF00BCD4)
private val OceanBackgroundDark = Color(0xFF0D1B2A)
private val OceanSurfaceDark = Color(0xFF1A2E40)

val OceanLightColors = lightColorScheme(
    primary = OceanPrimaryLight,
    secondary = OceanSecondaryLight,
    tertiary = OceanTertiaryLight,
    background = OceanBackgroundLight,
    surface = OceanSurfaceLight,
    primaryContainer = OceanSecondaryLight.copy(alpha = 0.2f),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF1A1A2E),
    onSurface = Color(0xFF1A1A2E)
)

val OceanDarkColors = darkColorScheme(
    primary = OceanPrimaryDark,
    secondary = OceanSecondaryDark,
    tertiary = OceanTertiaryDark,
    background = OceanBackgroundDark,
    surface = OceanSurfaceDark,
    primaryContainer = OceanPrimaryDark.copy(alpha = 0.3f),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFFE0EEFF),
    onSurface = Color(0xFFE0EEFF)
)

// --- Forest ---
private val ForestPrimaryLight = Color(0xFF2E7D32)
private val ForestSecondaryLight = Color(0xFF66BB6A)
private val ForestTertiaryLight = Color(0xFF558B2F)
private val ForestBackgroundLight = Color(0xFFF5FBF5)
private val ForestSurfaceLight = Color(0xFFFFFFFF)

private val ForestPrimaryDark = Color(0xFF66BB6A)
private val ForestSecondaryDark = Color(0xFF2E7D32)
private val ForestTertiaryDark = Color(0xFF8BC34A)
private val ForestBackgroundDark = Color(0xFF0D1F0E)
private val ForestSurfaceDark = Color(0xFF1A2E1B)

val ForestLightColors = lightColorScheme(
    primary = ForestPrimaryLight,
    secondary = ForestSecondaryLight,
    tertiary = ForestTertiaryLight,
    background = ForestBackgroundLight,
    surface = ForestSurfaceLight,
    primaryContainer = ForestSecondaryLight.copy(alpha = 0.2f),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF1A2E1A),
    onSurface = Color(0xFF1A2E1A)
)

val ForestDarkColors = darkColorScheme(
    primary = ForestPrimaryDark,
    secondary = ForestSecondaryDark,
    tertiary = ForestTertiaryDark,
    background = ForestBackgroundDark,
    surface = ForestSurfaceDark,
    primaryContainer = ForestPrimaryDark.copy(alpha = 0.3f),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFFE0F5E0),
    onSurface = Color(0xFFE0F5E0)
)

// --- Sunset ---
private val SunsetPrimaryLight = Color(0xFFE64A19)
private val SunsetSecondaryLight = Color(0xFFFF7043)
private val SunsetTertiaryLight = Color(0xFFF4511E)
private val SunsetBackgroundLight = Color(0xFFFFF8F5)
private val SunsetSurfaceLight = Color(0xFFFFFFFF)

private val SunsetPrimaryDark = Color(0xFFFF7043)
private val SunsetSecondaryDark = Color(0xFFE64A19)
private val SunsetTertiaryDark = Color(0xFFFFCA28)
private val SunsetBackgroundDark = Color(0xFF1F0D08)
private val SunsetSurfaceDark = Color(0xFF2E1510)

val SunsetLightColors = lightColorScheme(
    primary = SunsetPrimaryLight,
    secondary = SunsetSecondaryLight,
    tertiary = SunsetTertiaryLight,
    background = SunsetBackgroundLight,
    surface = SunsetSurfaceLight,
    primaryContainer = SunsetSecondaryLight.copy(alpha = 0.2f),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF2E1510),
    onSurface = Color(0xFF2E1510)
)

val SunsetDarkColors = darkColorScheme(
    primary = SunsetPrimaryDark,
    secondary = SunsetSecondaryDark,
    tertiary = SunsetTertiaryDark,
    background = SunsetBackgroundDark,
    surface = SunsetSurfaceDark,
    primaryContainer = SunsetPrimaryDark.copy(alpha = 0.3f),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFFFFE8E0),
    onSurface = Color(0xFFFFE8E0)
)

// --- Midnight ---
private val MidnightPrimaryLight = Color(0xFF4527A0)
private val MidnightSecondaryLight = Color(0xFF7C4DFF)
private val MidnightTertiaryLight = Color(0xFF3D5AFE)
private val MidnightBackgroundLight = Color(0xFFF8F5FF)
private val MidnightSurfaceLight = Color(0xFFFFFFFF)

private val MidnightPrimaryDark = Color(0xFF7C4DFF)
private val MidnightSecondaryDark = Color(0xFF4527A0)
private val MidnightTertiaryDark = Color(0xFF536DFE)
private val MidnightBackgroundDark = Color(0xFF0D0A1F)
private val MidnightSurfaceDark = Color(0xFF1A1530)

val MidnightLightColors = lightColorScheme(
    primary = MidnightPrimaryLight,
    secondary = MidnightSecondaryLight,
    tertiary = MidnightTertiaryLight,
    background = MidnightBackgroundLight,
    surface = MidnightSurfaceLight,
    primaryContainer = MidnightSecondaryLight.copy(alpha = 0.2f),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF1A1530),
    onSurface = Color(0xFF1A1530)
)

val MidnightDarkColors = darkColorScheme(
    primary = MidnightPrimaryDark,
    secondary = MidnightSecondaryDark,
    tertiary = MidnightTertiaryDark,
    background = MidnightBackgroundDark,
    surface = MidnightSurfaceDark,
    primaryContainer = MidnightPrimaryDark.copy(alpha = 0.3f),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFFEDE8FF),
    onSurface = Color(0xFFEDE8FF)
)

// --- Steel ---
private val SteelPrimaryLight = Color(0xFF37474F)
private val SteelSecondaryLight = Color(0xFF607D8B)
private val SteelTertiaryLight = Color(0xFF546E7A)
private val SteelBackgroundLight = Color(0xFFF5F7F8)
private val SteelSurfaceLight = Color(0xFFFFFFFF)

private val SteelPrimaryDark = Color(0xFF90A4AE)
private val SteelSecondaryDark = Color(0xFF607D8B)
private val SteelTertiaryDark = Color(0xFF37474F)
private val SteelBackgroundDark = Color(0xFF0D1215)
private val SteelSurfaceDark = Color(0xFF1A2226)

val SteelLightColors = lightColorScheme(
    primary = SteelPrimaryLight,
    secondary = SteelSecondaryLight,
    tertiary = SteelTertiaryLight,
    background = SteelBackgroundLight,
    surface = SteelSurfaceLight,
    primaryContainer = SteelSecondaryLight.copy(alpha = 0.2f),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF1A2226),
    onSurface = Color(0xFF1A2226)
)

val SteelDarkColors = darkColorScheme(
    primary = SteelPrimaryDark,
    secondary = SteelSecondaryDark,
    tertiary = SteelTertiaryDark,
    background = SteelBackgroundDark,
    surface = SteelSurfaceDark,
    primaryContainer = SteelPrimaryDark.copy(alpha = 0.3f),
    onPrimary = Color(0xFF1A2226),
    onSecondary = Color.White,
    onBackground = Color(0xFFDDE4E8),
    onSurface = Color(0xFFDDE4E8)
)

// --- Resolver ---
fun resolveColorScheme(
    scheme: ColorScheme,
    isDark: Boolean
): androidx.compose.material3.ColorScheme = when (scheme) {
    ColorScheme.OCEAN -> if (isDark) OceanDarkColors else OceanLightColors
    ColorScheme.FOREST -> if (isDark) ForestDarkColors else ForestLightColors
    ColorScheme.SUNSET -> if (isDark) SunsetDarkColors else SunsetLightColors
    ColorScheme.MIDNIGHT -> if (isDark) MidnightDarkColors else MidnightLightColors
    ColorScheme.STEEL -> if (isDark) SteelDarkColors else SteelLightColors
}