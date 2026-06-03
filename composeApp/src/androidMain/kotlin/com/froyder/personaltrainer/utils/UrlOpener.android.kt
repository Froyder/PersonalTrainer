package com.froyder.personaltrainer.utils

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import com.froyder.personaltrainer.data.local.appContext

actual fun openUrl(url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        appContext.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        // Fallback to browser if market:// scheme not available
        val fallbackUrl = url.replace(
            "market://details?id=",
            "https://play.google.com/store/apps/details?id="
        )
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        appContext.startActivity(intent)
    }
}