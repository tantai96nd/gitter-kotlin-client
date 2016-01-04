package com.github.shchurov.gitterclient.helpers.realm

import com.github.shchurov.gitterclient.helpers.realm.models.RoomRealm
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort

class RealmWrapper(val realmInstance: Realm) {

    fun close() {
        realmInstance.close()
    }

    fun getMyRooms(): RealmResults<RoomRealm> {
        return realmInstance.allObjectsSorted(RoomRealm::class.java,
                RoomRealm.FIELD_LAST_ACCESS_TIME, Sort.DESCENDING);
    }

    fun putMyRooms(roomsRealm: List<RoomRealm>) {
        executeTransaction { realmInstance.copyToRealmOrUpdate(roomsRealm) }
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