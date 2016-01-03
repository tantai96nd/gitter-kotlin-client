package com.github.shchurov.gitterclient.utils

import com.github.shchurov.gitterclient.helpers.realm.models.RoomRealm
import com.github.shchurov.gitterclient.models.Room
import com.github.shchurov.gitterclient.network.responses.RoomResponse

object Converters {

    fun roomRealmToUi(room: RoomRealm): Room = Room(room.id, room.name, room.unreadItems,
            room.mentions)

    fun roomNetworkToUi(room: RoomResponse): Room = Room(room.id!!, room.name!!, room.unreadItems,
            room.mentions)

}