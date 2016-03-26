package com.github.shchurov.gitterclient.data.subscribers

import rx.Subscriber

abstract class DefaultSubscriber<T> : Subscriber<T>() {

    final override fun onError(e: Throwable) {
        val errorMessage = ErrorMessageGenerator.generateErrorMessage(e)
        onFailure(e, errorMessage)
        onFinish()
    }

    final override fun onCompleted() {
        onFinish()
    }

    protected open fun onFailure(e: Throwable, errorMessage: String) {
    }

    protected open fun onFinish() {
    }

}