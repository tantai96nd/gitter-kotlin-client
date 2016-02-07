package com.github.shchurov.gitterclient.data.database.implementation

import com.github.shchurov.gitterclient.helpers.realm.models.RoomRealm
import io.realm.Realm
import io.realm.Sort

class RealmWrapper(val realmInstance: Realm) {

    fun close() {
        realmInstance.close()
    }

    fun getMyRooms() = realmInstance.allObjectsSorted(RoomRealm::class.java,
            RoomRealm.FIELD_LAST_ACCESS_TIME, Sort.DESCENDING)

    fun rewriteRooms(roomsRealm: List<RoomRealm>) {
        executeTransaction {
            realmInstance.clear(RoomRealm::class.java)
            realmInstance.copyToRealm(roomsRealm)
        }
    }

    private fun executeTransaction(transaction: () -> Unit) {
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