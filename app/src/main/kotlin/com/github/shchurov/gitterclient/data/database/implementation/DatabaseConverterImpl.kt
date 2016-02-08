package com.github.shchurov.gitterclient.data.database.implementation

import com.github.shchurov.gitterclient.data.database.DatabaseConverter
import com.github.shchurov.gitterclient.data.database.implementation.realm_models.RoomRealm
import com.github.shchurov.gitterclient.domain.models.Room

class DatabaseConverterImpl : DatabaseConverter {

    override fun convertToRoom(roomRealm: RoomRealm) = with(roomRealm) {
        Room(id!!, name!!, avatar, unreadItems, mentions, lastAccessTimestamp)
    }

    override fun convertFromRoom(room: Room) = with(room) {
        RoomRealm(id, name, avatar, unreadItems, mentions, lastAccessTimestamp)
    }

}