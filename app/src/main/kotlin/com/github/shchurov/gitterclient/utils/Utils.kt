package com.github.shchurov.gitterclient.utils

import android.widget.Toast
import com.github.shchurov.gitterclient.App
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.text.SimpleDateFormat

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

    fun convertTimeStringToLong(time: String?): Long {
        if (time == null)
            return 0
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        return df.parse(time).time
    }

}