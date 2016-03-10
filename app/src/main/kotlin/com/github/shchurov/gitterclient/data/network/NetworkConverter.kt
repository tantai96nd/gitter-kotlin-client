package com.github.shchurov.gitterclient.data.network

import com.github.shchurov.gitterclient.data.network.model.MessageResponse
import com.github.shchurov.gitterclient.data.network.model.RoomResponse
import com.github.shchurov.gitterclient.data.network.model.TokenResponse
import com.github.shchurov.gitterclient.data.network.model.UserResponse
import com.github.shchurov.gitterclient.domain.models.Message
import com.github.shchurov.gitterclient.domain.models.Room
import com.github.shchurov.gitterclient.domain.models.Token
import com.github.shchurov.gitterclient.domain.models.User
import com.github.shchurov.gitterclient.utils.TimeUtils

class NetworkConverter {

    fun convertResponseToToken(tokenResponse: TokenResponse) = Token(tokenResponse.accessToken)

    fun convertResponseToRoom(roomResponse: RoomResponse): Room {
        with(roomResponse) {
            val avatar = generateRoomAvatarUrl(url)
            val lastAccessTimestamp = TimeUtils.convertIsoToTimestamp(lastAccessTimeIso)
            return Room(id, name, avatar, unreadItems, mentions, lastAccessTimestamp)
        }
    }

    fun convertResponseToMessage(messageResponse: MessageResponse): Message {
        with(messageResponse) {
            val timestamp = TimeUtils.convertIsoToTimestamp(messageResponse.timeIso)
            val user = convertResponseToUser(user)
            return Message(id, text, timestamp, user, unread)
        }
    }

    fun convertResponseToUser(userResponse: UserResponse) = with(userResponse) {
        User(id, username, avatar)
    }

    fun generateRoomAvatarUrl(roomUrl: String): String {
        // taken from the official gitter app, is reliable?
        val s = roomUrl.split("/")[1]
        return "https://avatars.githubusercontent.com/" + s + "?s=120";
    }

}