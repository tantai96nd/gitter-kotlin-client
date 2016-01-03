package com.github.shchurov.gitterclient.helpers.realm

import com.github.shchurov.gitterclient.helpers.realm.models.RoomRealm
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort

class RealmWrapper(val realmInstance: Realm) {

    fun getMyRooms(): RealmResults<RoomRealm> {
        return realmInstance.allObjectsSorted(RoomRealm::class.java,
                RoomRealm.FIELD_ORDER, Sort.ASCENDING);
    }

    fun close() {
        realmInstance.close()
    }

}