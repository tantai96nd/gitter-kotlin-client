package com.github.shchurov.gitterclient

import com.github.shchurov.gitterclient.domain.interactors.threading.SchedulersProvider
import rx.Scheduler
import rx.schedulers.Schedulers

class ImmediateSchedulersProvider: SchedulersProvider {

    override val backgroundScheduler: Scheduler
        get() = Schedulers.immediate()
    override val uiScheduler: Scheduler
        get() = Schedulers.immediate()

}