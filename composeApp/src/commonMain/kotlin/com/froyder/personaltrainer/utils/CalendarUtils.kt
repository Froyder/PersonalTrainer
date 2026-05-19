package com.froyder.personaltrainer.utils

data class CalendarDay(
    val dayOfMonth: Int,
    val hasWorkout: Boolean,
    val isToday: Boolean
)

expect fun getCurrentMonthCalendar(workoutTimestamps: List<Long>): Triple<String, Int, List<CalendarDay?>>
// Returns: month name, first day of week offset, list of days (nulls for empty slots)