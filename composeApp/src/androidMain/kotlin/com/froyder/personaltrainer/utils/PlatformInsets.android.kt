package com.froyder.personaltrainer.utils

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeContent
import androidx.compose.runtime.Composable

@Composable
actual fun screenPadding(): WindowInsets = WindowInsets.safeContent