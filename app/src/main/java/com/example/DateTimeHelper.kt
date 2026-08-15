package com.example

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateTimeHelper {
    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date())
    }

    fun getCurrentFormattedDateLong(): String {
        val sdf = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    fun getUpcomingDates(daysCount: Int = 6): List<String> {
        val list = mutableListOf<String>()
        val calendar = Calendar.getInstance()
        val sdfDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val sdfDay = SimpleDateFormat("EEE", Locale.getDefault())

        for (i in 0 until daysCount) {
            val dateStr = sdfDate.format(calendar.time)
            val dayStr = if (i == 0) "Today" else if (i == 1) "Tomorrow" else sdfDay.format(calendar.time)
            list.add("$dateStr, $dayStr")
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        return list
    }
}
