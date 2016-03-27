package com.github.shchurov.gitterclient.device_tests.tests

import android.support.test.runner.AndroidJUnit4
import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.database.implementation.DatabaseImpl
import com.github.shchurov.gitterclient.data.database.implementation.RealmInitializer
import com.github.shchurov.gitterclient.domain.models.Room
import io.realm.Realm
import io.realm.RealmConfiguration
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    @Rule @JvmField
    val tmpFolder = TemporaryFolder()
    private lateinit var realmConfig: RealmConfiguration
    private lateinit var database: Database
    private lateinit var rooms: List<Room>

    @Before
    fun setUp() {
        rooms = createRoomsList(100)
        val realmInitializer = createRealmInitializer()
        database = DatabaseImpl(realmInitializer)
    }

    private fun createRoomsList(size: Int): List<Room> {
        val list: MutableList<Room> = mutableListOf()
        for (i in 0..(size - 1)) {
            list.add(Room("$i", "room$i", "avatar$i", i, i, i.toLong()))
        }
        return list
    }

    private fun createRealmInitializer() = object : RealmInitializer() {
        override fun initRealm() {
            realmConfig = RealmConfiguration.Builder(tmpFolder.newFolder("realm_tmp"))
                    .build()
            Realm.setDefaultConfiguration(realmConfig)
        }
    }

    @After
    fun tearDown() {
        Realm.deleteRealm(realmConfig);
    }

    @Test
    fun testInsertAndGetRooms() {
        database.insertRooms(rooms)
        val retrievedRooms = database.getRooms()

        assertTrue(rooms.containsAll(retrievedRooms))
    }

    @Test
    fun testClearRooms() {
        database.insertRooms(rooms)
        database.clearRooms()
        val retrievedRooms = database.getRooms()

        assertTrue(retrievedRooms.isEmpty())
    }

    @Test
    fun testUpdateRoomLastAccessTime() {
        val room = rooms[0]
        database.insertRooms(rooms)
        val newTimestamp = 3456L
        database.updateRoomLastAccessTime(room.id, newTimestamp)

        val retrievedRoom = database.getRooms().filter { it.id == room.id }.first()
        assertEquals(newTimestamp, retrievedRoom.lastAccessTimestamp)
    }

    @Test
    fun decrementRoomUnreadItems() {
        val room = rooms[0]
        room.unreadItems = 1234
        database.insertRooms(rooms)
        database.decrementRoomUnreadItems(room.id)

        val retrievedRoom = database.getRooms().filter { it.id == room.id }.first()
        assertEquals(1233, retrievedRoom.unreadItems)
    }

}