package com.froyder.personaltrainer.data.local

import com.russhwolf.settings.PropertiesSettings
import com.russhwolf.settings.Settings
import java.util.Properties

// Settings
actual fun createSettings(): Settings = PropertiesSettings(Properties())