package com.froyder.personaltrainer.utils.notifications

import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNCalendarNotificationTrigger
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNUserNotificationCenter
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitMinute
import platform.Foundation.NSCalendarUnitSecond

actual class NotificationScheduler actual constructor() {

    actual fun requestPermission() {
        UNUserNotificationCenter.currentNotificationCenter()
            .requestAuthorizationWithOptions(
                UNAuthorizationOptionAlert or
                        UNAuthorizationOptionSound or
                        UNAuthorizationOptionBadge
            ) { _, _ -> }
    }

    actual fun scheduleForDays(days: List<String>, hour: Int, minute: Int, title: String, message: String) {
        val center = UNUserNotificationCenter.currentNotificationCenter()
        center.removeAllPendingNotificationRequests()

        val dayMap = mapOf(
            "Monday" to 2L, "Tuesday" to 3L, "Wednesday" to 4L,
            "Thursday" to 5L, "Friday" to 6L, "Saturday" to 7L, "Sunday" to 1L
        )

        days.forEachIndexed { index, day ->
            val weekday = dayMap[day] ?: return@forEachIndexed

            val content = UNMutableNotificationContent().apply {
                setTitle(title)
                setBody(message)
                setSound(platform.UserNotifications.UNNotificationSound.defaultSound())
            }

            val components = NSCalendar.currentCalendar.components(
                NSCalendarUnitHour or NSCalendarUnitMinute or NSCalendarUnitSecond,
                fromDate = platform.Foundation.NSDate()
            ).apply {
                setWeekday(weekday)
                setHour(hour.toLong())
                setMinute(minute.toLong())
                setSecond(0)
            }

            val trigger = UNCalendarNotificationTrigger.triggerWithDateMatchingComponents(
                components,
                repeats = true
            )

            val request = UNNotificationRequest.requestWithIdentifier(
                "workout_reminder_$index",
                content = content,
                trigger = trigger
            )

            center.addNotificationRequest(request) { _ -> }
        }
    }

    actual fun cancelAll() {
        UNUserNotificationCenter.currentNotificationCenter()
            .removeAllPendingNotificationRequests()
    }
}