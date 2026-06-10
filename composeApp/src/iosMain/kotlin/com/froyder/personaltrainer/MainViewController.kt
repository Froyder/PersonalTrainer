package com.froyder.personaltrainer

import androidx.compose.ui.window.ComposeUIViewController
import com.froyder.personaltrainer.data.local.createSettings
import com.froyder.personaltrainer.data.repository.LocalRepository
import io.github.froyder.kmpinappreview.ReviewManager

fun MainViewController() = ComposeUIViewController {
    App(localRepository = LocalRepository(createSettings()), reviewManager = ReviewManager() )
}
