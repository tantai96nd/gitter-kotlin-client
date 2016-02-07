package com.github.shchurov.gitterclient.data.database

import com.github.shchurov.gitterclient.domain.models.Room

interface Database {

    fun getMyRooms(): MutableList<Room>

    fun clearMyRooms()

    fun writeMyRooms(rooms: List<Room>)

}