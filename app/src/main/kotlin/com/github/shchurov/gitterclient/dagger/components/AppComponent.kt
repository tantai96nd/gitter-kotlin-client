package com.github.shchurov.gitterclient.dagger.components

import com.github.shchurov.gitterclient.dagger.modules.AppModule
import com.github.shchurov.gitterclient.dagger.modules.RoomModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

    fun createRoomComponent(roomModule: RoomModule): RoomComponent

    fun createGeneralScreenComponent(): GeneralScreenComponent

}