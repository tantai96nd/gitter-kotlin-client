package com.github.shchurov.gitterclient.domain.interactors.threading

import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

open class SchedulersProvider {

    open val background: Scheduler
        get() = Schedulers.io()

    open val main: Scheduler
        get() = AndroidSchedulers.mainThread()

}