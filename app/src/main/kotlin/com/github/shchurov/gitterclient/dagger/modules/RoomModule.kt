package com.github.shchurov.gitterclient.dagger.modules

import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.network.api.GitterApi
import com.github.shchurov.gitterclient.domain.interactors.GetRoomMessagesInteractor
import com.github.shchurov.gitterclient.domain.interactors.MarkMessageAsReadInteractor
import com.github.shchurov.gitterclient.domain.interactors.threading.SchedulersProvider
import dagger.Module
import dagger.Provides

@Module
class RoomModule(
        private val roomId: String
) {

    @Provides
    fun provideGetRoomMessagesInteractor(gitterApi: GitterApi,
            schedulersProvider: SchedulersProvider): GetRoomMessagesInteractor {
        return GetRoomMessagesInteractor(gitterApi, schedulersProvider, roomId)
    }

    @Provides
    fun provideMarkMessageAsReadInteractor(gitterApi: GitterApi, database: Database,
            schedulersProvider: SchedulersProvider): MarkMessageAsReadInteractor {
        return MarkMessageAsReadInteractor(gitterApi, database, schedulersProvider, roomId)
    }

}