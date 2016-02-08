package com.github.shchurov.gitterclient.data.database

import com.github.shchurov.gitterclient.data.database.implementation.realm_models.RoomRealm
import com.github.shchurov.gitterclient.domain.models.Room

interface DatabaseConverter {

    fun convertToRoom(roomRealm: RoomRealm): Room

    fun convertFromRoom(room: Room): RoomRealm

}