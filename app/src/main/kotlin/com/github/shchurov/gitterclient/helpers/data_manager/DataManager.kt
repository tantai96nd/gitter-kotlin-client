package com.github.shchurov.gitterclient.helpers.data_manager

import com.github.shchurov.gitterclient.helpers.realm.RealmManager
import com.github.shchurov.gitterclient.models.Room
import com.github.shchurov.gitterclient.network.GitterApi
import com.github.shchurov.gitterclient.utils.Converters
import rx.Observable

object DataManager {

    fun getMyRooms(): Observable<List<Room>> {
        val realmObservable = Observable.create<List<Room>> { subscriber ->
            val realm = RealmManager.createWrapperInstance()
            val rooms = realm.getMyRooms().map { Converters.roomRealmToUi(it) }
            subscriber.onNext(rooms)
            subscriber.onCompleted()
        }
        val networkObservable = GitterApi.gitterService.getMyRooms().map { roomsResponse ->
            roomsResponse.map { Converters.roomNetworkToUi(it) }
        }
        return Observable.concat(realmObservable, networkObservable)
    }

}