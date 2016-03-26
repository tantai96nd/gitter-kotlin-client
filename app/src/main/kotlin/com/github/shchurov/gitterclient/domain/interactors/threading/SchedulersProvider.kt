package com.github.shchurov.gitterclient.domain.interactors.threading

import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class SchedulersProvider {

    val background: Scheduler
        get() = Schedulers.io()

    val main: Scheduler
        get() = AndroidSchedulers.mainThread()

}