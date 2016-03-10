package com.github.shchurov.gitterclient.data.subscribers

import rx.Subscriber

abstract class CustomSubscriber<T> : Subscriber<T>() {

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