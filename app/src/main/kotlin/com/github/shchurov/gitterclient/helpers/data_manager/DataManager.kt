package com.github.shchurov.gitterclient.helpers.data_manager

import com.github.shchurov.gitterclient.helpers.realm.RealmManager
import com.github.shchurov.gitterclient.models.Room
import com.github.shchurov.gitterclient.network.GitterApi
import com.github.shchurov.gitterclient.utils.Converters
import com.github.shchurov.gitterclient.utils.applySchedulers
import rx.Observable

object DataManager {

    fun getMyRooms(): Observable<DataWrapper<List<Room>>> {
        val realmObservable = Observable.create<DataWrapper<List<Room>>> { subscriber ->
            val realm = RealmManager.createWrapperInstance()
            val rooms = realm.getMyRooms().map { Converters.roomRealmToUi(it) }
            realm.close()
            subscriber.onNext(DataWrapper(rooms, DataSource.LOCAL))
            subscriber.onCompleted()
        }.applySchedulers()
        val networkObservable = GitterApi.gitterService.getMyRooms()
                .flatMap { roomsResponse ->
                    roomsResponse.sortByDescending { it.lastAccessTime }
                    val realm = RealmManager.createWrapperInstance()
                    val roomsRealm = roomsResponse.map { Converters.roomNetworkToRealm(it) }
                    realm.rewriteRooms(roomsRealm)
                    realm.close()
                    Observable.just(roomsResponse)
                }
                .map { roomsResponse ->
                    val rooms = roomsResponse.map { Converters.roomNetworkToUi(it) }
                    DataWrapper(rooms, DataSource.NETWORK)
                }
                .applySchedulers()
        return Observable.mergeDelayError(realmObservable, networkObservable)
    }

    fun getRoomMessagesFirstPage(roomId: String, limit: Int) =
            GitterApi.gitterService.getRoomMessages(roomId, limit)
                    .map { messagesResponse ->
                        messagesResponse.reverse()
                        val messages = messagesResponse.map { Converters.messageNetworkToUi(it) }
                        DataWrapper(messages, DataSource.NETWORK)
                    }
                    .applySchedulers()

    fun getRoomMessagesNextPage(roomId: String, limit: Int, beforeId: String) =
            GitterApi.gitterService.getRoomMessages(roomId, limit, beforeId)
                    .map { messagesResponse ->
                        messagesResponse.reverse()
                        val messages = messagesResponse.map { Converters.messageNetworkToUi(it) }
                        DataWrapper(messages, DataSource.NETWORK)
                    }
                    .applySchedulers()

}