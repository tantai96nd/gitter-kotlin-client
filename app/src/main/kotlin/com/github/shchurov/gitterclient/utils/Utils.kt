package com.github.shchurov.gitterclient.utils

import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.widget.Toast
import com.github.shchurov.gitterclient.App
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

fun showToast(textId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(App.context, textId, duration).show()
}

fun showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(App.context, text, duration).show()
}

fun getString(textId: Int) = App.context.getString(textId)

fun RecyclerView.ViewHolder.findViewById(id: Int) = itemView.findViewById(id)

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