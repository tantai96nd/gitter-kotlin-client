package com.github.shchurov.gitterclient.unit_tests.tests

import com.github.shchurov.gitterclient.data.network.api.implementation.NetworkConverter
import com.github.shchurov.gitterclient.data.network.model.MessageResponse
import com.github.shchurov.gitterclient.data.network.model.RoomResponse
import com.github.shchurov.gitterclient.data.network.model.TokenResponse
import com.github.shchurov.gitterclient.data.network.model.UserResponse
import com.github.shchurov.gitterclient.utils.TimeUtils
import org.junit.Assert.assertEquals
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
        private const val UNREAD = true
    }

    private val converter = NetworkConverter()

    @Test
    fun testConvertResponseToToken() {
        val tokenResponse = TokenResponse(TOKEN)
        val converted = converter.convertResponseToToken(tokenResponse)

        assertEquals(TOKEN, converted.accessToken)
    }

    @Test
    fun testConvertResponseToRoom() {
        val roomResponse = RoomResponse(ROOM_ID, NAME, UNREAD_ITEMS, MENTIONS, URL, TIME_ISO)
        val converted = converter.convertResponseToRoom(roomResponse)

        with(converted) {
            assertEquals(ROOM_ID, id)
            assertEquals(NAME, name)
            assertEquals(TimeUtils.convertIsoToTimestamp(TIME_ISO), lastAccessTimestamp)
            assertEquals(MENTIONS, mentions)
            assertEquals(UNREAD_ITEMS, unreadItems)
            assertEquals(converter.generateRoomAvatarUrl(URL), avatar)
        }
    }

    @Test
    fun testConvertResponseToUser() {
        val userResponse = UserResponse(USER_ID, USERNAME, AVATAR)
        val converted = converter.convertResponseToUser(userResponse)

        with(converted) {
            assertEquals(USER_ID, id)
            assertEquals(USERNAME, username)
            assertEquals(AVATAR, avatar)
        }
    }

    @Test
    fun testConvertResponseToMessage() {
        val userResponse = UserResponse(USER_ID, USERNAME, AVATAR)
        val messageResponse = MessageResponse(MESSAGE_ID, TEXT, TIME_ISO, userResponse, UNREAD)
        val converted = converter.convertResponseToMessage(messageResponse)

        with(converted) {
            with(user) {
                assertEquals(USER_ID, id)
                assertEquals(USERNAME, username)
                assertEquals(AVATAR, avatar)
            }
            assertEquals(MESSAGE_ID, id)
            assertEquals(TEXT, text)
            assertEquals(TimeUtils.convertIsoToTimestamp(TIME_ISO), timestamp)
            assertEquals(UNREAD, unread)
        }
    }

}