package com.github.shchurov.gitterclient.dagger.components

import com.github.shchurov.gitterclient.dagger.modules.ActivityModule
import com.github.shchurov.gitterclient.dagger.scopes.PerScreen
import com.github.shchurov.gitterclient.presentation.ui.activities.LogInActivity
import com.github.shchurov.gitterclient.presentation.ui.activities.RoomActivity
import com.github.shchurov.gitterclient.presentation.ui.activities.RoomsListActivity
import dagger.Component

@PerScreen
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(ActivityModule::class))
interface ActivityComponent {

    fun inject(activity: LogInActivity)

    fun inject(activity: RoomActivity)

    fun inject(activity: RoomsListActivity)

}