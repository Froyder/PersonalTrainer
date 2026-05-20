package com.froyder.personaltrainer.utils.notifications

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.froyder.personaltrainer.data.local.appContext
import com.froyder.personaltrainer.mainActivity

actual class NotificationScheduler actual constructor() {
    actual fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    appContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    mainActivity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }

    actual fun scheduleForDays(days: List<String>, hour: Int, minute: Int, title: String, message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                // Fall back to inexact alarm
                NotificationSchedulerHelper.scheduleForDaysInexact(appContext, days, hour, minute, title, message)
                return
            }
        }
        NotificationSchedulerHelper.scheduleForDays(appContext, days, hour, minute, title, message)
    }

    actual fun cancelAll() {
        NotificationSchedulerHelper.cancel(appContext)
    }
}