package com.github.shchurov.gitterclient.tests

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.github.shchurov.gitterclient.domain.models.Room
import com.github.shchurov.gitterclient.presentation.ui.activities.RoomsListActivity
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class RoomsListActivityTest : BaseActivityTest() {

    @Rule @JvmField
    val activityRule = ActivityTestRule<RoomsListActivity>(RoomsListActivity::class.java)

    @Before
    fun setUp() {
//        fakeRooms = createFakeRoomsList(20)
//        mockDatabase()
//        mockGitterApi()
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

}