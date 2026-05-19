package com.froyder.personaltrainer

import androidx.compose.ui.window.ComposeUIViewController
import com.froyder.personaltrainer.data.local.createSettings
import com.froyder.personaltrainer.data.repository.LocalRepository

fun MainViewController() = ComposeUIViewController {
    App(localRepository = LocalRepository(createSettings()))
}