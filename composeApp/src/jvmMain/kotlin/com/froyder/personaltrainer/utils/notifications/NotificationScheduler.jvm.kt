package com.froyder.personaltrainer.utils.notifications

actual class NotificationScheduler actual constructor() {
    actual fun requestPermission() {}
    actual fun scheduleForDays(
        days: List<String>,
        hour: Int,
        minute: Int,
        title: String,
        message: String
    ) {}
    actual fun cancelAll() {}
}