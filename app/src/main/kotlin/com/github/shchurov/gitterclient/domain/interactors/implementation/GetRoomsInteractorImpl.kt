package com.github.shchurov.gitterclient.domain.interactors.implementation

import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.network.GitterApi
import com.github.shchurov.gitterclient.domain.DataSource
import com.github.shchurov.gitterclient.domain.DataWrapper
import com.github.shchurov.gitterclient.domain.interactors.GetRoomsInteractor
import com.github.shchurov.gitterclient.domain.models.Room
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class GetRoomsInteractorImpl(
        private val gitterApi: GitterApi,
        private val database: Database
) : GetRoomsInteractor {

    override fun getRooms(localOnly: Boolean): Observable<DataWrapper<MutableList<Room>>> {
        val localObservable = getMyRoomsLocal()
        if (localOnly) {
            return localObservable
        } else {
            val networkObservable = getMyRoomsNetwork()
            return Observable.mergeDelayError(localObservable, networkObservable)
        }
    }

    private fun getMyRoomsLocal() = Observable.create<DataWrapper<MutableList<Room>>> { subscriber ->
        val rooms = database.getRooms()
        subscriber.onNext(DataWrapper(rooms, DataSource.LOCAL))
        subscriber.onCompleted()
    }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    private fun getMyRoomsNetwork() = gitterApi.getMyRooms()
            .subscribeOn(Schedulers.io())
            .map { rooms ->
                rooms.sortByDescending { it.lastAccessTimestamp }
                saveRoomsToDatabase(rooms)
                DataWrapper(rooms, DataSource.NETWORK)
            }
            .observeOn(AndroidSchedulers.mainThread())

    private fun saveRoomsToDatabase(rooms: List<Room>) {
        database.clearRooms()
        database.saveRooms(rooms)
    }

}