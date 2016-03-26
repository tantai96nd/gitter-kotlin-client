package com.github.shchurov.gitterclient.unit_tests.helpers

import com.github.shchurov.gitterclient.domain.interactors.threading.SchedulersProvider
import rx.Scheduler
import rx.schedulers.Schedulers

class ImmediateSchedulersProvider : SchedulersProvider() {

    override val background: Scheduler
        get() = Schedulers.io()

    override val main: Scheduler
        get() = Schedulers.immediate()

}
