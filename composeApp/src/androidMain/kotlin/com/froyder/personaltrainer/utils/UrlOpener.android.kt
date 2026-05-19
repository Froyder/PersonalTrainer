package com.froyder.personaltrainer.utils

import android.content.Intent
import android.net.Uri
import com.froyder.personaltrainer.data.local.appContext

actual fun openUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    appContext.startActivity(intent)
}