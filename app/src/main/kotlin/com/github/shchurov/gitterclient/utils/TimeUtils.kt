package com.github.shchurov.gitterclient.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    private val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US)
    private val calendar1 = Calendar.getInstance()
    private val calendar2 = Calendar.getInstance()

    init {
        isoDateFormat.timeZone = TimeZone.getTimeZone("UTC")
    }

    fun convertIsoToTimestamp(time: String?): Long {
        if (time == null)
            return 0
        return isoDateFormat.parse(time).time
    }

    fun convertTimestampToRelativeString(timestamp: Long, months: Array<String>): String {
        val calendarNow = calendar1
        val calendarThen = calendar2
        setCalendarTime(calendarNow, System.currentTimeMillis())
        setCalendarTime(calendarThen, timestamp)
        val hourMinute = getHourMinuteString(calendarThen)
        if (isNotToday(calendarThen, calendarNow)) {
            val dayMonth = getDayMonthString(calendarThen, months)
            return "$dayMonth $hourMinute"
        } else {
            return hourMinute
        }
    }

    private fun setCalendarTime(calendar: Calendar, time: Long) {
        calendar.timeInMillis = time
    }

    private fun isNotToday(calendarThen: Calendar, calendarNow: Calendar): Boolean {
        return calendarThen.get(Calendar.DAY_OF_YEAR) != calendarNow.get(Calendar.DAY_OF_YEAR)
                || calendarThen.get(Calendar.YEAR) != calendarNow.get(Calendar.YEAR)
    }

    private fun getHourMinuteString(calendar: Calendar): String {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val minuteZero = if (minute < 10) "0" else ""
        return "$hour:$minuteZero$minute"
    }

    private fun getDayMonthString(calendar: Calendar, months: Array<String>): String {
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = months[calendar.get(Calendar.MONTH)]
        return "$day $month"
    }

}