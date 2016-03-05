package com.github.shchurov.gitterclient.domain

import com.github.shchurov.gitterclient.data.network.implementation.helpers.NetworkErrorHandler
import rx.Subscriber

abstract class DataSubscriber<T> : Subscriber<T>() {

    final override fun onError(e: Throwable) {
        onFailure(e)
        onFinish()
    }

    final override fun onCompleted() {
        onFinish()
    }

    protected open fun onFailure(e: Throwable) {
        NetworkErrorHandler.handleError(e)
    }

    protected open fun onFinish() {
    }

}