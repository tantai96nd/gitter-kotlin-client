package com.github.shchurov.gitterclient.dagger.components

import com.github.shchurov.gitterclient.dagger.modules.GeneralScreenModule
import com.github.shchurov.gitterclient.dagger.scopes.PerScreen
import com.github.shchurov.gitterclient.presentation.ui.activities.LogInActivity
import com.github.shchurov.gitterclient.presentation.ui.activities.RoomsListActivity
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = arrayOf(GeneralScreenModule::class))
interface GeneralScreenComponent {

    fun inject(activity: LogInActivity)

    fun inject(activity: RoomsListActivity)

}