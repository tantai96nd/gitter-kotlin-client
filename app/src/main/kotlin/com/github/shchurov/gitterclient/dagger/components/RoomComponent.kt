package com.github.shchurov.gitterclient.dagger.components

import com.github.shchurov.gitterclient.dagger.modules.RoomModule
import com.github.shchurov.gitterclient.dagger.scopes.PerScreen
import com.github.shchurov.gitterclient.presentation.ui.activities.RoomActivity
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = arrayOf(RoomModule::class))
interface RoomComponent {

    fun inject(roomActivity: RoomActivity)

}