package com.github.shchurov.gitterclient.tests

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
import java.util.*

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    @Rule @JvmField
    val tmpFolder = TemporaryFolder()
    private lateinit var realmConfig: RealmConfiguration
    private lateinit var database: Database
    private lateinit var fakeRooms: List<Room>

    @Before
    fun setUp() {
        fakeRooms = createFakeRoomsList(100)
        val realmInitializer = createRealmInitializer()
        database = DatabaseImpl(realmInitializer)
    }

    private fun createFakeRoomsList(size: Int): List<Room> {
        val rooms = mutableListOf<Room>()
        val random = Random()
        for (i in 0..(size - 1)) {
            val room = Room("$i", "room$i", "avatar$i", random.nextInt(), random.nextInt(), random.nextLong())
            rooms.add(room)
        }
        return rooms
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
    fun saveAndGetRooms() {
        database.insertRooms(fakeRooms)
        val retrievedRooms = database.getRooms()
        retrievedRooms.sortBy { it.id.toInt() }
        assertEquals(fakeRooms, retrievedRooms)
    }

    @Test
    fun clearRooms() {
        database.insertRooms(fakeRooms)
        database.clearRooms()
        val retrievedRooms = database.getRooms()
        assertTrue(retrievedRooms.isEmpty())
    }

    @Test
    fun updateRoomLastAccessTime() {
        val testRoom = fakeRooms[4]
        testRoom.lastAccessTimestamp = 1234
        database.insertRooms(fakeRooms)
        database.updateRoomLastAccessTime(testRoom.id, 3456L)
        val retrievedRoom = database.getRooms().filter { it.id == testRoom.id }.first()
        assertTrue(retrievedRoom.lastAccessTimestamp == 3456L)
    }

    @Test
    fun decrementRoomUnreadItems() {
        val testRoom = fakeRooms[4]
        testRoom.unreadItems = 1234
        database.insertRooms(fakeRooms)
        database.decrementRoomUnreadItems(testRoom.id)
        val retrievedRoom = database.getRooms().filter { it.id == testRoom.id }.first()
        assertEquals(retrievedRoom.unreadItems, 1233)
    }

}