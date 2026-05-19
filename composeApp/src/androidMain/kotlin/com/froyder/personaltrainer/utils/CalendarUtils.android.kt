package com.froyder.personaltrainer.utils

import java.util.Calendar
import java.util.Locale

actual fun getCurrentMonthCalendar(workoutTimestamps: List<Long>): Triple<String, Int, List<CalendarDay?>> {
    val calendar = Calendar.getInstance()
    val today = calendar.get(Calendar.DAY_OF_MONTH)
    val monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) ?: ""

    // Get days that had workouts this month
    val workoutDays = workoutTimestamps.map { ts ->
        val c = Calendar.getInstance().apply { timeInMillis = ts }
        if (c.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
            c.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
            c.get(Calendar.DAY_OF_MONTH)
        } else null
    }.filterNotNull().toSet()

    // Get first day of month
    val firstDay = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, 1)
    }
    // Convert to 0=Mon offset
    val offset = ((firstDay.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY + 7) % 7)
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    val days = mutableListOf<CalendarDay?>()
    repeat(offset) { days.add(null) }
    (1..daysInMonth).forEach { day ->
        days.add(CalendarDay(
            dayOfMonth = day,
            hasWorkout = day in workoutDays,
            isToday = day == today
        ))
    }

    return Triple(monthName, offset, days)
}