package com.github.shchurov.gitterclient.network

import com.github.shchurov.gitterclient.network.responses.DefaultErrorHandler
import rx.Subscriber

open class RequestSubscriber<T> : Subscriber<T>() {

    final override fun onNext(t: T) {
        onSuccess(t)
        onFinish()
    }

    final override fun onError(e: Throwable) {
        onFailure(e)
        e.printStackTrace()
        onFinish()
    }

    final override fun onCompleted() {
    }

    protected open fun onSuccess(response: T) {
    }

    protected open fun onFailure(e: Throwable) {
        DefaultErrorHandler.handleError(e)
    }

    protected open fun onFinish() {
    }

}