package com.github.shchurov.gitterclient.domain.interactors.implementation

import com.github.shchurov.gitterclient.App
import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.network.GitterApi
import com.github.shchurov.gitterclient.domain.DataSource
import com.github.shchurov.gitterclient.domain.DataWrapper
import com.github.shchurov.gitterclient.domain.interactors.MyRoomsInteractor
import com.github.shchurov.gitterclient.domain.models.Room
import com.github.shchurov.gitterclient.utils.applySchedulers
import rx.Observable
import javax.inject.Inject

class MyRoomsInteractorImpl() : MyRoomsInteractor {

    @Inject
    lateinit var gitterApi: GitterApi
    @Inject
    lateinit var database: Database

    init {
        App.appComponent.inject(this)
    }

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
                database.saveMyRooms(rooms)
                val wrapper = DataWrapper(rooms, DataSource.NETWORK)
                Observable.just(wrapper)
            }
            .applySchedulers()

}