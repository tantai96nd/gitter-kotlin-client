package com.github.shchurov.gitterclient.presentation

import com.github.shchurov.gitterclient.domain.models.Message
import com.github.shchurov.gitterclient.domain.models.Room
import com.github.shchurov.gitterclient.domain.models.User
import com.github.shchurov.gitterclient.helpers.realm.models.RoomRealm

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