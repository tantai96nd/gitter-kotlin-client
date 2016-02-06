package com.github.shchurov.gitterclient.domain.interactors

import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.network.retrofit.GitterRetrofitService
import com.github.shchurov.gitterclient.domain.DataSource
import com.github.shchurov.gitterclient.domain.DataWrapper
import com.github.shchurov.gitterclient.domain.models.Room
import com.github.shchurov.gitterclient.presentation.Converters
import com.github.shchurov.gitterclient.utils.applySchedulers
import rx.Observable
import java.util.*

class MyRoomsInteractor(val database: Database, val gitterService: GitterRetrofitService) {

    fun getMyRooms(localOnly: Boolean): Observable<DataWrapper<List<Room>>> {
        val localObservable = getMyRoomsLocal()
        if (localOnly)
            return localObservable
        val networkObservable = getMyRoomsNetwork()
        return Observable.mergeDelayError(localObservable, networkObservable)
    }

    private fun getMyRoomsLocal() = Observable.create<DataWrapper<List<Room>>> { subscriber ->
        val rooms = database.getMyRooms()
        subscriber.onNext(DataWrapper(rooms, DataSource.LOCAL))
        subscriber.onCompleted()
    }
            .applySchedulers()

    private fun getMyRoomsNetwork() = gitterService.getMyRooms()
            .flatMap { roomsResponse ->
                val rooms = ArrayList<Room>()
                roomsResponse.mapTo(rooms) { Converters.roomNetworkToUi(it) }
                rooms.sortByDescending { it.lastTimeAccess }
                database.clearMyRooms()
                database.writeMyRooms(rooms)
                val wrapper = DataWrapper<List<Room>>(rooms, DataSource.NETWORK)
                Observable.just(wrapper)
            }
            .applySchedulers()

}