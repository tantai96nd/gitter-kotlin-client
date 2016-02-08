package com.github.shchurov.gitterclient.data.network.implementation

import android.util.Log
import com.github.shchurov.gitterclient.data.network.NetworkConverter
import com.github.shchurov.gitterclient.data.network.responses.MessageResponse
import com.github.shchurov.gitterclient.data.network.responses.RoomResponse
import com.github.shchurov.gitterclient.data.network.responses.TokenResponse
import com.github.shchurov.gitterclient.data.network.responses.UserResponse
import com.github.shchurov.gitterclient.domain.models.Message
import com.github.shchurov.gitterclient.domain.models.Room
import com.github.shchurov.gitterclient.domain.models.Token
import com.github.shchurov.gitterclient.domain.models.User
import com.github.shchurov.gitterclient.utils.TimeUtils

class NetworkConverterImpl : NetworkConverter {

    override fun convertToToken(tokenResponse: TokenResponse) = Token(tokenResponse.accessToken)

    override fun convertToRoom(roomResponse: RoomResponse): Room {
        with(roomResponse) {
            val avatar = generateRoomAvatarUrl(url)
            val lastAccessTimestamp = TimeUtils.convertIsoToTimestamp(lastAccessTimeIso)
            return Room(id, name, avatar, unreadItems, mentions, lastAccessTimestamp)
        }
    }

    override fun convertToMessage(messageResponse: MessageResponse): Message {
        with(messageResponse) {
            val timestamp = TimeUtils.convertIsoToTimestamp(messageResponse.timeIso)
            val user = convertToUser(user)
            return Message(id, text, timestamp, user, unread)
        }
    }

    override fun convertToUser(userResponse: UserResponse) = with(userResponse) {
        User(id, username, avatar)
    }

    private fun generateRoomAvatarUrl(roomUrl: String): String {
        // taken from the official gitter app, is reliable?
        val s = roomUrl.split("/")[1]
        return "https://avatars.githubusercontent.com/" + s + "?s=120";
    }

}