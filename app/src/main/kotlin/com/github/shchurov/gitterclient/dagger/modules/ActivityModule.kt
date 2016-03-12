package com.github.shchurov.gitterclient.dagger.modules

import android.app.Activity
import com.github.shchurov.gitterclient.dagger.scopes.PerScreen
import com.github.shchurov.gitterclient.presentation.ui.LogInView
import com.github.shchurov.gitterclient.presentation.ui.RoomView
import com.github.shchurov.gitterclient.presentation.ui.RoomsListView
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: Activity) {

    @Provides
    @PerScreen
    fun provideLogInView(): LogInView = activity as LogInView

    @Provides
    @PerScreen
    fun provideRoomsListView(): RoomsListView = activity as RoomsListView

    @Provides
    @PerScreen
    fun provideRoomView(): RoomView = activity as RoomView

}