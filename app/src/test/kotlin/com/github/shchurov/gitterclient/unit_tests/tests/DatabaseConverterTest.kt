package com.github.shchurov.gitterclient.unit_tests.tests

import com.github.shchurov.gitterclient.data.database.implementation.DatabaseConverter
import com.github.shchurov.gitterclient.data.database.implementation.realm_models.RoomRealm
import com.github.shchurov.gitterclient.domain.models.Room
import org.junit.Assert.assertEquals
import org.junit.Test

class DatabaseConverterTest {

    companion object {
        private const val ID = "id"
        private const val NAME = "name"
        private const val AVATAR = "avatar"
        private const val UNREAD_ITEMS = 111
        private const val MENTIONS = 222
        private const val TIMESTAMP = 333L
    }

    private val converter = DatabaseConverter()

    @Test
    fun testConvertRealmToRoom() {
        val realmRoom = RoomRealm(ID, NAME, AVATAR, UNREAD_ITEMS, MENTIONS, TIMESTAMP)
        val converted = converter.convertRealmToRoom(realmRoom)

        with (converted) {
            assertEquals(ID, id);
            assertEquals(NAME, name);
            assertEquals(AVATAR, avatar)
            assertEquals(UNREAD_ITEMS, unreadItems)
            assertEquals(MENTIONS, mentions)
            assertEquals(TIMESTAMP, lastAccessTimestamp)
        }
    }

    @Test
    fun testConvertRoomToRealm() {
        val room = Room(ID, NAME, AVATAR, UNREAD_ITEMS, MENTIONS, TIMESTAMP)
        val converted = converter.convertRoomToRealm(room)

        with (converted) {
            assertEquals(ID, id)
            assertEquals(NAME, name)
            assertEquals(AVATAR, avatar)
            assertEquals(UNREAD_ITEMS, unreadItems)
            assertEquals(MENTIONS, mentions)
            assertEquals(TIMESTAMP, lastAccessTimestamp)
        }
    }

}