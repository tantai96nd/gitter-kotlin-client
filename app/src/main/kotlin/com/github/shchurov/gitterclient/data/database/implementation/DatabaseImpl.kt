package com.github.shchurov.gitterclient.data.database.implementation

import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.database.DatabaseConverter
import com.github.shchurov.gitterclient.data.database.implementation.realm_models.RoomRealm
import com.github.shchurov.gitterclient.domain.models.Room
import io.realm.Realm
import io.realm.Sort
import java.util.*

class DatabaseImpl(private val converter: DatabaseConverter) : Database {

    override fun getMyRooms(): MutableList<Room> {
        val realm = Realm.getDefaultInstance()
        val realmRooms = realm.allObjectsSorted(RoomRealm::class.java, RoomRealm.FIELD_LAST_ACCESS_TIMESTAMP,
                Sort.DESCENDING)
        val rooms = ArrayList<Room>()
        return realmRooms.mapTo(rooms) { converter.convertToRoom(it) }
    }

    override fun saveMyRooms(rooms: List<Room>) {
        val realmRooms = rooms.map { converter.convertFromRoom(it) }
        executeTransaction { realm -> realm.copyToRealm(realmRooms) }
    }

    override fun clearMyRooms() {
        executeTransaction { realm -> realm.clear(RoomRealm::class.java) }
    }

    private fun executeTransaction(transaction: (Realm) -> Unit) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        try {
            transaction(realm)
            realm.commitTransaction()
        } catch (e: Exception) {
            e.printStackTrace()
            realm.cancelTransaction()
        }
        realm.close()
    }

}