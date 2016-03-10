package com.github.shchurov.gitterclient.data.subscribers

import rx.Subscriber

class EmptySubscriber<T> : Subscriber<T>() {

    override fun onError(p0: Throwable?) {
    }

    override fun onCompleted() {
    }

    override fun onNext(p0: T) {
    }

}