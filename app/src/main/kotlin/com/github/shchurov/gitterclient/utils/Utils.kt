package com.github.shchurov.gitterclient.utils

import android.util.Log
import android.util.TypedValue
import android.widget.Toast
import com.github.shchurov.gitterclient.App
import com.github.shchurov.gitterclient.R
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*

fun showToast(textId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(App.context, textId, duration).show()
}

fun showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(App.context, text, duration).show()
}

fun getString(textId: Int) = App.context.getString(textId)

fun <T> Observable<T>.compositeSubscribeWithSchedulers(subscriptions: CompositeSubscription,
        subscriber: Subscriber<T>) = applySchedulers().compositeSubscribe(subscriptions, subscriber)

fun <T> Observable<T>.applySchedulers() = observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())

fun <T> Observable<T>.compositeSubscribe(subscriptions: CompositeSubscription,
        subscriber: Subscriber<T>): Subscription {
    val subscription = subscribe(subscriber)
    subscriptions.add(subscription)
    return subscription
}

object Utils {

    fun dpToPx(dp: Int) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
            App.context.resources.displayMetrics).toInt()

}

object TimeUtils {

    private val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US)
    private val calendar = Calendar.getInstance()
    private val calendarNow = Calendar.getInstance()
    private val months = App.context.resources.getStringArray(R.array.months)

    init {
        isoDateFormat.timeZone = TimeZone.getTimeZone("UTC")
    }

    fun convertIsoToLong(time: String?): Long {
        if (time == null)
            return 0
        return isoDateFormat.parse(time).time
    }

    fun convertLongToString(time: Long): String {
        calendar.timeInMillis = time
        calendarNow.timeInMillis = System.currentTimeMillis()
        val hour = calendar.get(HOUR_OF_DAY)
        val minute = calendar.get(MINUTE)
        val minuteZero = if (minute < 10) "0" else ""
        val hourMinute = "$hour:$minuteZero$minute"
        if (calendar.get(DAY_OF_YEAR) != calendarNow.get(DAY_OF_YEAR)
                || calendar.get(YEAR) != calendarNow.get(YEAR)) {
            val day = calendar.get(DAY_OF_MONTH)
            val month = months[calendar.get(MONTH)]
            return "$day $month $hourMinute"
        } else {
            return hourMinute
        }
    }

}