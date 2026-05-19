package com.froyder.personaltrainer.utils.notifications

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.froyder.personaltrainer.data.local.appContext

actual class NotificationScheduler actual constructor() {
    actual fun requestPermission() {
        // On Android 13+ permission must be requested at runtime
        // For now we check — full runtime request needs Activity context
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                appContext, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) {
                // Permission will be requested when user enables notifications in Menu
            }
        }
    }

    actual fun scheduleForDays(days: List<String>, hour: Int, minute: Int, title: String, message: String) {
        NotificationSchedulerHelper.scheduleForDays(appContext, days, hour, minute, title, message)
    }

    actual fun cancelAll() {
        NotificationSchedulerHelper.cancel(appContext)
    }
}