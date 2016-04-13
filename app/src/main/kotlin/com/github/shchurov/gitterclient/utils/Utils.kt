package com.github.shchurov.gitterclient.utils

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.github.shchurov.gitterclient.App
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.subscriptions.CompositeSubscription

fun showToast(textId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(App.context, textId, duration).show()
}

fun getString(textId: Int, vararg args: Any?) = App.context.getString(textId, args)

fun RecyclerView.ViewHolder.findViewById(id: Int) = itemView.findViewById(id)

fun <T> Observable<T>.compositeSubscribe(subscriptions: CompositeSubscription,
                                         subscriber: Subscriber<T>): Subscription {
    val subscription = subscribe(subscriber)
    subscriptions.add(subscription)
    return subscription
}

fun AppCompatActivity.hideSoftInputKeyboard() {
    val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;
    manager.hideSoftInputFromWindow(findViewById(android.R.id.content)?.windowToken, 0)
}

object Utils {

    fun dpToPx(dp: Int) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
            App.context.resources.displayMetrics).toInt()

}