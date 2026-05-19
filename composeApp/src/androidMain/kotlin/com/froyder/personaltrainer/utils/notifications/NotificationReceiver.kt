package com.froyder.personaltrainer.utils.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.froyder.personaltrainer.R

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Personal Trainer"
        val message = intent.getStringExtra("message") ?: "Time to work out!"

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "workout_reminder",
                "Workout Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, "workout_reminder")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .build()

        manager.notify(1, notification)

        // Reschedule for next week
        val prefs = context.getSharedPreferences("notifications", Context.MODE_PRIVATE)
        val hour = prefs.getInt("hour", 9)
        val minute = prefs.getInt("minute", 0)
        val days = prefs.getStringSet("days", emptySet())?.toList() ?: emptyList()
        val requestCode = intent.getIntExtra("requestCode", 0)

        // Only reschedule this specific day
        if (requestCode < days.size) {
            val title = intent.getStringExtra("title") ?: "Personal Trainer"
            val message = intent.getStringExtra("message") ?: "Time to work out!"
            NotificationSchedulerHelper.scheduleForDays(context, days, hour, minute, title, message)
        }
    }
}