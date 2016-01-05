package com.github.shchurov.gitterclient.helpers.realm

import com.github.shchurov.gitterclient.helpers.realm.models.MessageRealm
import com.github.shchurov.gitterclient.helpers.realm.models.RoomRealm
import io.realm.Realm
import io.realm.Sort

class RealmWrapper(val realmInstance: Realm) {

    fun close() {
        realmInstance.close()
    }

    fun getMyRooms() = realmInstance.allObjectsSorted(RoomRealm::class.java,
            RoomRealm.FIELD_LAST_ACCESS_TIME, Sort.DESCENDING)

    fun putMyRooms(roomsRealm: List<RoomRealm>) {
        executeTransaction { realmInstance.copyToRealmOrUpdate(roomsRealm) }
    }

    fun getRoomMessages(roomId: String) = realmInstance.where(MessageRealm::class.java)
            .equalTo(MessageRealm.FIELD_ROOM_ID, roomId)
            .findAllSorted(MessageRealm.FIELD_TIME, Sort.ASCENDING)

    fun putRoomMessages(messagesRealm: List<MessageRealm>) {
        executeTransaction { }
    }

    fun executeTransaction(transaction: () -> Unit) {
        realmInstance.beginTransaction()
        try {
            transaction()
            realmInstance.commitTransaction()
        } catch (e: Exception) {
            e.printStackTrace()
            realmInstance.cancelTransaction()
        }
    }

}