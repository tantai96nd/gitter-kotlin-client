package com.github.shchurov.gitterclient.utils

import com.github.shchurov.gitterclient.App
import com.github.shchurov.gitterclient.R
import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    private val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US)
    private val calendarThen = Calendar.getInstance()
    private val calendarNow = Calendar.getInstance()
    private val months = App.context.resources.getStringArray(R.array.months)

    init {
        isoDateFormat.timeZone = TimeZone.getTimeZone("UTC")
    }

    fun convertIsoToTimestamp(time: String?): Long {
        if (time == null)
            return 0
        return isoDateFormat.parse(time).time
    }

    fun convertTimestampToString(time: Long): String {
        setCalendarTime(calendarThen, time)
        val hourMinute = getHourMinuteString(calendarThen)
        if (isNotToday(calendarThen)) {
            val dayMonth = getDayMonthString(calendarThen)
            return "$dayMonth $hourMinute"
        } else {
            return hourMinute
        }
    }

    private fun setCalendarTime(calendar: Calendar, time: Long) {
        calendar.timeInMillis = time
    }

    private fun isNotToday(calendar: Calendar): Boolean {
        setCalendarTime(calendarNow, System.currentTimeMillis())
        return calendar.get(Calendar.DAY_OF_YEAR) != calendarNow.get(Calendar.DAY_OF_YEAR)
                || calendar.get(Calendar.YEAR) != calendarNow.get(Calendar.YEAR)
    }

    private fun getHourMinuteString(calendar: Calendar): String {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val minuteZero = if (minute < 10) "0" else ""
        return "$hour:$minuteZero$minute"
    }

    private fun getDayMonthString(calendar: Calendar): String {
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = months[calendar.get(Calendar.MONTH)]
        return "$day $month"
    }

}