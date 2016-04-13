package com.github.shchurov.gitterclient.dagger.modules

import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class RoomModule(
        private val roomId: String
) {

    @Provides
    @Named("roomId")
    fun provideRoomId() = roomId

}