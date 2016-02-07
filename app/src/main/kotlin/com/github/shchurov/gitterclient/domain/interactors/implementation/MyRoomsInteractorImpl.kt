package com.github.shchurov.gitterclient.domain.interactors.implementation

import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.network.GitterApi
import com.github.shchurov.gitterclient.domain.DataSource
import com.github.shchurov.gitterclient.domain.DataWrapper
import com.github.shchurov.gitterclient.domain.interactors.MyRoomsInteractor
import com.github.shchurov.gitterclient.domain.models.Room
import com.github.shchurov.gitterclient.utils.applySchedulers
import rx.Observable

class MyRoomsInteractorImpl() : MyRoomsInteractor {

    val gitterApi: GitterApi
    val database: Database

    override fun getMyRooms(localOnly: Boolean): Observable<DataWrapper<MutableList<Room>>> {
        val localObservable = getMyRoomsLocal()
        if (localOnly)
            return localObservable
        val networkObservable = getMyRoomsNetwork()
        return Observable.mergeDelayError(localObservable, networkObservable)
    }

    private fun getMyRoomsLocal() = Observable.create<DataWrapper<MutableList<Room>>> { subscriber ->
        val rooms = database.getMyRooms()
        subscriber.onNext(DataWrapper(rooms, DataSource.LOCAL))
        subscriber.onCompleted()
    }
            .applySchedulers()

    private fun getMyRoomsNetwork() = gitterApi.getMyRooms()
            .flatMap { rooms ->
                rooms.sortByDescending { it.lastAccessTimestamp }
                database.clearMyRooms()
                database.writeMyRooms(rooms)
                val wrapper = DataWrapper(rooms, DataSource.NETWORK)
                Observable.just(wrapper)
            }
            .applySchedulers()

}