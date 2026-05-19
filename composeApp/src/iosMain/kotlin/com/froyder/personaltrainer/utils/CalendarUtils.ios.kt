package com.froyder.personaltrainer.utils

import platform.Foundation.*

actual fun getCurrentMonthCalendar(workoutTimestamps: List<Long>): Triple<String, Int, List<CalendarDay?>> {
    val calendar = NSCalendar.currentCalendar
    val now = NSDate()
    val components = calendar.components(
        NSCalendarUnitDay or NSCalendarUnitMonth or NSCalendarUnitYear,
        fromDate = now
    )
    val today = components.day.toInt()
    val currentMonth = components.month.toInt()
    val currentYear = components.year.toInt()

    val formatter = NSDateFormatter().apply {
        dateFormat = "MMMM"
    }
    val monthName = formatter.stringFromDate(now)

    // Get workout days this month
    val workoutDays = workoutTimestamps.map { ts ->
        val date = NSDate.dateWithTimeIntervalSince1970(ts / 1000.0)
        val c = calendar.components(
            NSCalendarUnitDay or NSCalendarUnitMonth or NSCalendarUnitYear,
            fromDate = date
        )
        if (c.month.toInt() == currentMonth && c.year.toInt() == currentYear)
            c.day.toInt() else null
    }.filterNotNull().toSet()

    // First day of month
    val firstComponents = NSDateComponents().apply {
        day = 1
        month = currentMonth.toLong()
        year = currentYear.toLong()
    }
    val firstDate = calendar.dateFromComponents(firstComponents) ?: now
    val firstDayComponents = calendar.components(NSCalendarUnitWeekday, fromDate = firstDate)
    val offset = ((firstDayComponents.weekday.toInt() - 2 + 7) % 7)

    val lastDayComponents = NSDateComponents().apply {
        month = (currentMonth % 12 + 1).toLong()
        year = if (currentMonth == 12) (currentYear + 1).toLong() else currentYear.toLong()
        day = 0
    }
    val lastDate = calendar.dateFromComponents(lastDayComponents) ?: now
    val daysInMonth = calendar.components(NSCalendarUnitDay, fromDate = lastDate).day.toInt()

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