package com.github.shchurov.gitterclient.data.database.implementation

import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.database.implementation.realm_models.RoomRealm
import com.github.shchurov.gitterclient.domain.models.Room
import io.realm.Realm
import io.realm.Sort

class DatabaseImpl(initializer: RealmInitializer) : Database {

    private val converter = DatabaseConverter()

    init {
        initializer.initRealm()
    }

    override fun getRooms(): MutableList<Room> {
        val realm = getRealmInstance()
        val realmRooms = realm.allObjectsSorted(RoomRealm::class.java, RoomRealm.FIELD_LAST_ACCESS_TIMESTAMP,
                Sort.DESCENDING)
        val rooms: MutableList<Room> = mutableListOf()
        realmRooms.mapTo(rooms) { converter.convertRealmToRoom(it) }
        realm.close()
        return rooms
    }

    override fun saveRooms(rooms: List<Room>) {
        val realmRooms = rooms.map { converter.convertRoomToRealm(it) }
        executeTransaction { realm -> realm.copyToRealm(realmRooms) }
    }

    override fun updateRoomLastAccessTime(roomId: String, timestamp: Long) {
        executeTransaction { realm ->
            getRoom(roomId, realm).lastAccessTimestamp = timestamp
        }
    }

    private fun getRoom(roomId: String, realm: Realm) =
            realm.where(RoomRealm::class.java)
                    .equalTo(RoomRealm.FIELD_ID, roomId)
                    .findFirst()

    override fun decrementRoomUnreadItems(roomId: String) {
        executeTransaction { realm ->
            val realmRoom = getRoom(roomId, realm)
            realmRoom.unreadItems--
        }
    }

    override fun clearRooms() {
        executeTransaction { realm -> realm.clear(RoomRealm::class.java) }
    }

    private fun executeTransaction(transaction: (Realm) -> Unit) {
        val realm = getRealmInstance()
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

    private fun getRealmInstance() = Realm.getDefaultInstance()

}