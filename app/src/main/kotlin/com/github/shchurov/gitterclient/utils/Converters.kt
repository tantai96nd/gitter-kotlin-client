package com.github.shchurov.gitterclient.utils

import com.github.shchurov.gitterclient.helpers.realm.models.RoomRealm
import com.github.shchurov.gitterclient.models.Message
import com.github.shchurov.gitterclient.models.Room
import com.github.shchurov.gitterclient.models.User
import com.github.shchurov.gitterclient.network.responses.MessageResponse
import com.github.shchurov.gitterclient.network.responses.RoomResponse

object Converters {

    fun roomRealmToUi(room: RoomRealm) = with(room) {
        Room(id, name, avatar, unreadItems, mentions, lastAccessTime)
    }

    fun roomNetworkToUi(room: RoomResponse) = with(room) {
        Room(id!!, name!!, avatar, unreadItems, mentions, lastAccessTime)
    }

    fun roomNetworkToRealm(room: RoomResponse) = with(room) {
        RoomRealm(id, name, avatar, unreadItems, mentions, lastAccessTime)
    }

    fun messageNetworkToUi(message: MessageResponse) = with(message) {
        val user = User(user!!.id!!, user!!.username!!, user!!.avatar!!)
        Message(id!!, text!!, time, user, unread)
    }

}