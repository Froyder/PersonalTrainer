package com.froyder.personaltrainer.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

@Composable
actual fun isSystemDarkMode(): Boolean = isSystemInDarkTheme()