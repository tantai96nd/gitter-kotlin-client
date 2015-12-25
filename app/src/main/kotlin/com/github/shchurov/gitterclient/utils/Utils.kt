package com.github.shchurov.gitterclient.utils

import android.widget.Toast
import com.github.shchurov.gitterclient.App
import com.github.shchurov.gitterclient.network.RequestSubscriber
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

fun showToast(textId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(App.context, textId, duration).show()
}

fun showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(App.context, text, duration).show()
}

fun getString(textId: Int) = App.context.getString(textId)

fun <T> Observable<T>.subscribeWithSchedulers(subscriber: RequestSubscriber<T>) {
    observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(subscriber)
}