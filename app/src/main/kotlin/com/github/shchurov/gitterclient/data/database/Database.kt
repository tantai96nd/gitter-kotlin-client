package com.github.shchurov.gitterclient.data.database

import com.github.shchurov.gitterclient.domain.models.Room

interface Database {

    fun getRooms(): MutableList<Room>

    fun clearRooms()

    fun saveRooms(rooms: List<Room>)

    fun updateRoomLastAccessTime(roomId: String, timestamp: Long)

    fun decrementRoomUnreadItems(roomId: String)

}