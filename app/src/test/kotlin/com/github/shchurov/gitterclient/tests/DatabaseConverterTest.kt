package com.github.shchurov.gitterclient.tests

import com.github.shchurov.gitterclient.data.database.implementation.DatabaseConverter
import com.github.shchurov.gitterclient.data.database.implementation.realm_models.RoomRealm
import com.github.shchurov.gitterclient.domain.models.Room
import org.junit.Assert.assertTrue
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
    fun convertRealmToRoom() {
        val realmRoom = RoomRealm(ID, NAME, AVATAR, UNREAD_ITEMS, MENTIONS, TIMESTAMP)
        val converted = converter.convertRealmToRoom(realmRoom)
        with (converted) {
            assertTrue(id == ID
                    && name == NAME
                    && avatar == AVATAR
                    && unreadItems == UNREAD_ITEMS
                    && mentions == MENTIONS
                    && lastAccessTimestamp == TIMESTAMP)
        }
    }

    @Test
    fun convertRoomToRealm() {
        val room = Room(ID, NAME, AVATAR, UNREAD_ITEMS, MENTIONS, TIMESTAMP)
        val converted = converter.convertRoomToRealm(room)
        with (converted) {
            assertTrue(id == ID
                    && name == NAME
                    && avatar == AVATAR
                    && unreadItems == UNREAD_ITEMS
                    && mentions == MENTIONS
                    && lastAccessTimestamp == TIMESTAMP)
        }
    }

}