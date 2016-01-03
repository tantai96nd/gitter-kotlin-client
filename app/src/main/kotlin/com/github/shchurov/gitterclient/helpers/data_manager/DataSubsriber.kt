package com.github.shchurov.gitterclient.helpers.data_manager

import com.github.shchurov.gitterclient.network.DefaultErrorHandler
import rx.Subscriber

open class DataSubsriber<T> : Subscriber<T>() {

    private var localEmitted: Boolean = false;

    final override fun onNext(t: T) {
        if (localEmitted) {
            onData(t, DataSource.NETWORK)
        } else {
            onData(t, DataSource.LOCAL)
            localEmitted = true
        }
    }

    final override fun onError(e: Throwable) {
        onFailure(e)
        e.printStackTrace()
        onFinish()
    }

    final override fun onCompleted() {
        onFinish()
    }

    protected open fun onData(data: T, source: DataSource) {
    }

    protected open fun onFailure(e: Throwable) {
        DefaultErrorHandler.handleError(e)
    }

    protected open fun onFinish() {
    }

}