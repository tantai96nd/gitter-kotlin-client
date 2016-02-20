package com.github.shchurov.gitterclient.data.database.implementation

import com.github.shchurov.gitterclient.data.database.implementation.realm_models.RoomRealm
import com.github.shchurov.gitterclient.domain.models.Room

class DatabaseConverter {

    fun convertRealmToRoom(roomRealm: RoomRealm) = with(roomRealm) {
        Room(id!!, name!!, avatar, unreadItems, mentions, lastAccessTimestamp)
    }

    fun convertRoomToRealm(room: Room) = with(room) {
        RoomRealm(id, name, avatar, unreadItems, mentions, lastAccessTimestamp)
    }

}