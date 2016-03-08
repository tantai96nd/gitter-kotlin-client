package com.github.shchurov.gitterclient.unit_testing.tests

import com.github.shchurov.gitterclient.data.network.implementation.NetworkConverter
import com.github.shchurov.gitterclient.data.network.responses.MessageResponse
import com.github.shchurov.gitterclient.data.network.responses.RoomResponse
import com.github.shchurov.gitterclient.data.network.responses.TokenResponse
import com.github.shchurov.gitterclient.data.network.responses.UserResponse
import com.github.shchurov.gitterclient.utils.TimeUtils
import org.junit.Assert.assertTrue
import org.junit.Test

class NetworkConverterTest {

    companion object {
        private const val TOKEN = "token"
        private const val ROOM_ID = "id"
        private const val NAME = "name"
        private const val TIME_ISO = "2016-03-07T21:40:34.209Z"
        private const val UNREAD_ITEMS = 222
        private const val MENTIONS = 333
        private const val URL = "url/url"
        private const val TEXT = "text"
        private const val MESSAGE_ID = "message_id"
        private const val USER_ID = "user_id"
        private const val AVATAR = "avatar"
        private const val USERNAME = "username"
    }

    private val converter = NetworkConverter()

    @Test
    fun convertResponseToToken() {
        val tokenResponse = TokenResponse(TOKEN)
        val converted = converter.convertResponseToToken(tokenResponse)
        assertTrue(converted.accessToken == TOKEN)
    }

    @Test
    fun convertResponseToRoom() {
        val roomResponse = RoomResponse(ROOM_ID, NAME, UNREAD_ITEMS, MENTIONS, URL, TIME_ISO)
        val converted = converter.convertResponseToRoom(roomResponse)
        with(converted) {
            assertTrue(id == ROOM_ID
                    && name == NAME
                    && lastAccessTimestamp == TimeUtils.convertIsoToTimestamp(TIME_ISO)
                    && mentions == MENTIONS
                    && unreadItems == UNREAD_ITEMS
                    && avatar == converter.generateRoomAvatarUrl(URL))
        }
    }

    @Test
    fun convertResponseToUser() {
        val userResponse = UserResponse(USER_ID, USERNAME, AVATAR)
        val converted = converter.convertResponseToUser(userResponse)
        with(converted) {
            assertTrue(id == USER_ID
                    && username == USERNAME
                    && avatar == AVATAR)
        }
    }

    @Test
    fun convertResponseToMessage() {
        val userResponse = UserResponse(USER_ID, USERNAME, AVATAR)
        val messageResponse = MessageResponse(MESSAGE_ID, TEXT, TIME_ISO, userResponse, true)
        val converted = converter.convertResponseToMessage(messageResponse)
        with(converted) {
            assertTrue(user.id == USER_ID
                    && user.username == USERNAME
                    && user.avatar == AVATAR)
            assertTrue(id == MESSAGE_ID
                    && text == TEXT
                    && timestamp == TimeUtils.convertIsoToTimestamp(TIME_ISO)
                    && unread == true)
        }
    }

}