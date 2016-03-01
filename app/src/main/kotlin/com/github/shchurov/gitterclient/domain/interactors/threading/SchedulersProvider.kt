package com.github.shchurov.gitterclient.domain.interactors.threading

import rx.Scheduler

interface SchedulersProvider {

    val backgroundScheduler: Scheduler

    val uiScheduler: Scheduler

}