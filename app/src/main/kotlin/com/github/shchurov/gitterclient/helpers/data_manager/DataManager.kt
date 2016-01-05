package com.github.shchurov.gitterclient.helpers.data_manager

import com.github.shchurov.gitterclient.helpers.realm.RealmManager
import com.github.shchurov.gitterclient.models.Message
import com.github.shchurov.gitterclient.models.Room
import com.github.shchurov.gitterclient.network.GitterApi
import com.github.shchurov.gitterclient.utils.Converters
import com.github.shchurov.gitterclient.utils.applySchedulers
import rx.Observable

object DataManager {

    fun getMyRooms(): Observable<List<Room>> {
        val realmObservable = Observable.create<List<Room>> { subscriber ->
            val realm = RealmManager.createWrapperInstance()
            val rooms = realm.getMyRooms().map { Converters.roomRealmToUi(it) }
            realm.close()
            subscriber.onNext(rooms)
            subscriber.onCompleted()
        }.applySchedulers()
        val networkObservable = GitterApi.gitterService.getMyRooms()
                .flatMap { roomsResponse ->
                    val realm = RealmManager.createWrapperInstance()
                    val roomsRealm = roomsResponse.map { Converters.roomNetworkToRealm(it) }
                    realm.putMyRooms(roomsRealm)
                    realm.close()
                    Observable.just(roomsResponse)
                }
                .map { roomsResponse ->
                    roomsResponse.map { Converters.roomNetworkToUi(it) }
                }.applySchedulers()
        return Observable.mergeDelayError(realmObservable, networkObservable)
    }

    fun getRoomMessages(roomId: String): Observable<List<Message>> {
        var realmObservable = Observable.create<List<Message>> { subscriber ->
            val realm = RealmManager.createWrapperInstance()
            val messages = realm.getRoomMessages(roomId).map { Converters.messageRealmToUi(it) }
            realm.close()
            subscriber.onNext(messages)
            subscriber.onCompleted()
        }.applySchedulers()
        val networkObservable = GitterApi.gitterService.getRoomMessages(roomId, 20)
                .flatMap { messagesResponse ->
                    val realm = RealmManager.createWrapperInstance()
                    val messagesRealm = messagesResponse
                            .map { Converters.messageNetworkToRealm(it, roomId) }
                    realm.putRoomMessages(messagesRealm)
                    realm.close()
                    Observable.just(messagesResponse)
                }
                .map { messagesResponse ->
                    messagesResponse.map { Converters.messageNetworkToUi(it) }
                }.applySchedulers()
        return Observable.mergeDelayError(realmObservable, networkObservable)
    }

}