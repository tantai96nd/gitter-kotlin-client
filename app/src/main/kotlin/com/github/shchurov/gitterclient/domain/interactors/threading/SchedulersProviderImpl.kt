package com.github.shchurov.gitterclient.domain.interactors.threading

import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class SchedulersProviderImpl : SchedulersProvider {

    override val backgroundScheduler: Scheduler
        get() = Schedulers.io()

    override val uiScheduler: Scheduler
        get() = AndroidSchedulers.mainThread()

}