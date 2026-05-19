package com.froyder.personaltrainer.utils.notifications

expect class NotificationScheduler() {
    fun requestPermission()
    fun scheduleForDays(days: List<String>, hour: Int, minute: Int, title: String, message: String)
    fun cancelAll()
}