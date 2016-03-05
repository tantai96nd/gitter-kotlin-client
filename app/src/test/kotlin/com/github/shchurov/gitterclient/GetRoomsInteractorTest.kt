package com.github.shchurov.gitterclient

import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.network.GitterApi
import com.github.shchurov.gitterclient.domain.interactors.GetRoomsInteractor
import com.github.shchurov.gitterclient.domain.interactors.implementation.GetRoomsInteractorImpl
import com.github.shchurov.gitterclient.domain.models.Room
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner
import rx.Observable
import rx.observers.TestSubscriber
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class GetRoomsInteractorTest {

    @Mock private lateinit var gitterApi: GitterApi
    @Mock private lateinit var database: Database
    private val schedulersProvider = ImmediateSchedulersProvider()

    private lateinit var interactor: GetRoomsInteractor
    private lateinit var expectedRooms: MutableList<Room>

    @Before
    fun setUp() {
        setupMocks()
        interactor = GetRoomsInteractorImpl(gitterApi, database, schedulersProvider)
    }

    private fun setupMocks() {
        val rooms = createRoomsList(15)
        mockGitterApiRooms(rooms)
        mockDatabaseRooms(rooms)
        rooms.sortByDescending { it.lastAccessTimestamp }
        expectedRooms = rooms
    }

    private fun createRoomsList(size: Int): MutableList<Room> {
        val rooms: MutableList<Room> = mutableListOf()
        val random = Random()
        for (i in 0..size) {
            val timestamp = Math.abs(random.nextLong())
            val room = Room("$i", "", null, 0, 0, timestamp)
            rooms.add(room)
        }
        return rooms
    }

    private fun mockGitterApiRooms(rooms: MutableList<Room>) {
        Collections.shuffle(rooms)
        Mockito.`when`(gitterApi.getMyRooms())
                .thenReturn(Observable.just(ArrayList<Room>(rooms)))
    }

    private fun mockDatabaseRooms(rooms: MutableList<Room>) {
        Collections.shuffle(rooms)
        Mockito.`when`(database.getRooms())
                .thenReturn(ArrayList<Room>(rooms))
    }

    @Test
    fun getLocal() {
        val subscriber = TestSubscriber<MutableList<Room>>()
        interactor.getRooms(true).subscribe(subscriber)
        subscriber.assertNoErrors()
        subscriber.assertValue(expectedRooms)
    }

    @Test
    fun getLocalAndNetwork() {
        val subscriber = TestSubscriber<MutableList<Room>>()
        interactor.getRooms(false).subscribe(subscriber)
        subscriber.assertNoErrors()
        subscriber.assertValues(expectedRooms, expectedRooms)
    }

}