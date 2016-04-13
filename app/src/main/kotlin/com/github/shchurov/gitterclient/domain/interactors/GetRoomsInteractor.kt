package com.github.shchurov.gitterclient.domain.interactors

import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.network.api.GitterApi
import com.github.shchurov.gitterclient.domain.interactors.threading.SchedulersProvider
import com.github.shchurov.gitterclient.domain.models.Room
import rx.Observable
import javax.inject.Inject

class GetRoomsInteractor @Inject constructor(
        private val gitterApi: GitterApi,
        private val database: Database,
        private val schedulersProvider: SchedulersProvider
) {

    fun get(localOnly: Boolean): Observable<MutableList<Room>> {
        val localObservable = getRoomsLocal()
        if (localOnly) {
            return localObservable
        } else {
            val networkObservable = getRoomsServer()
            return Observable.concat(localObservable, networkObservable)
        }
    }

    private fun getRoomsLocal() = Observable.create<MutableList<Room>> { subscriber ->
        val rooms = database.getRooms()
        rooms.sortByDescending { it.lastAccessTimestamp }
        subscriber.onNext(rooms)
        subscriber.onCompleted()
    }
            .subscribeOn(schedulersProvider.background)
            .unsubscribeOn(schedulersProvider.background)
            .observeOn(schedulersProvider.main)


    private fun getRoomsServer() = gitterApi.getMyRooms()
            .subscribeOn(schedulersProvider.background)
            .unsubscribeOn(schedulersProvider.background)
            .map { rooms ->
                rooms.sortByDescending { it.lastAccessTimestamp }
                rewriteDatabaseRooms(rooms)
                rooms
            }
            .observeOn(schedulersProvider.main)

    private fun rewriteDatabaseRooms(rooms: List<Room>) {
        database.clearRooms()
        database.insertRooms(rooms)
    }

}