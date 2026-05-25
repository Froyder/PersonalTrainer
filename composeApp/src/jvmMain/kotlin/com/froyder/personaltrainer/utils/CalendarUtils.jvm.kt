package com.froyder.personaltrainer.utils

actual fun getCurrentMonthCalendar(
    workoutTimestamps: List<Long>
): Triple<String, Int, List<CalendarDay?>> = Triple("January", 0, emptyList())