package com.github.shchurov.gitterclient.helpers.data_manager

import com.github.shchurov.gitterclient.network.DefaultErrorHandler
import rx.Subscriber

open class DataSubscriber<T> : Subscriber<DataWrapper<T>>() {

    final override fun onNext(wrapper: DataWrapper<T>) {
        onData(wrapper.data, wrapper.source)
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