package com.github.shchurov.gitterclient.domain.interactors.implementation

import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.network.GitterApi
import com.github.shchurov.gitterclient.domain.interactors.GetRoomsInteractor
import com.github.shchurov.gitterclient.domain.interactors.threading.SchedulersProvider
import com.github.shchurov.gitterclient.domain.models.Room
import rx.Observable

class GetRoomsInteractorImpl(
        private val gitterApi: GitterApi,
        private val database: Database,
        private val schedulersProvider: SchedulersProvider
) : GetRoomsInteractor {

    override fun getRooms(localOnly: Boolean): Observable<MutableList<Room>> {
        val localObservable = getMyRoomsLocal()
        if (localOnly) {
            return localObservable
        } else {
            val networkObservable = getMyRoomsNetwork()
            return Observable.concat(localObservable, networkObservable)
        }
    }

    private fun getMyRoomsLocal() = Observable.create<MutableList<Room>> { subscriber ->
        val rooms = database.getRooms()
        rooms.sortByDescending { it.lastAccessTimestamp }
        subscriber.onNext(rooms)
        subscriber.onCompleted()
    }
            .subscribeOn(schedulersProvider.backgroundScheduler)
            .observeOn(schedulersProvider.uiScheduler)


    private fun getMyRoomsNetwork() = gitterApi.getMyRooms()
            .subscribeOn(schedulersProvider.backgroundScheduler)
            .map { rooms ->
                rooms.sortByDescending { it.lastAccessTimestamp }
                saveRoomsToDatabase(rooms)
                rooms
            }
            .observeOn(schedulersProvider.uiScheduler)

    private fun saveRoomsToDatabase(rooms: List<Room>) {
        database.clearRooms()
        database.saveRooms(rooms)
    }

}