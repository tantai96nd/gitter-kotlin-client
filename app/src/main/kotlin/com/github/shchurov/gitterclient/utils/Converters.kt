package com.github.shchurov.gitterclient.utils

import com.github.shchurov.gitterclient.helpers.realm.models.MessageRealm
import com.github.shchurov.gitterclient.helpers.realm.models.RoomRealm
import com.github.shchurov.gitterclient.helpers.realm.models.UserRealm
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

    fun messageRealmToUi(message: MessageRealm) = with(message) {
        val user = User(user.id, user.username, user.avatar)
        Message(id, text, time, user, isUnread)
    }

    fun messageNetworkToUi(message: MessageResponse) = with(message) {
        val user = User(user!!.id!!, user!!.username!!, user!!.avatar!!)
        Message(id!!, text!!, time, user, unread)
    }

    fun messageNetworkToRealm(message: MessageResponse, roomId: String) = with(message) {
        val user = UserRealm(user!!.id, user!!.username, user!!.avatar)
        MessageRealm(id, roomId, text, time, user, unread)
    }

}