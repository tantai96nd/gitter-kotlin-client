package com.github.shchurov.gitterclient.tests

import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.network.api.GitterApi
import com.github.shchurov.gitterclient.domain.interactors.GetRoomsInteractor
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
        interactor = GetRoomsInteractor(gitterApi, database, schedulersProvider)
    }

    private fun setupMocks() {
        val rooms = createFakeRoomsList(15)
        mockGitterApiRooms(rooms)
        mockDatabaseRooms(rooms)
        rooms.sortByDescending { it.lastAccessTimestamp }
        expectedRooms = rooms
    }

    private fun createFakeRoomsList(size: Int): MutableList<Room> {
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
        val subscriber = createSubscriber()
        interactor.get(true).subscribe(subscriber)
        subscriber.assertNoErrors()
        subscriber.assertValue(expectedRooms)
    }

    private fun createSubscriber() = TestSubscriber<MutableList<Room>>()

    @Test
    fun getLocalAndNetwork() {
        val subscriber = createSubscriber()
        interactor.get(false).subscribe(subscriber)
        subscriber.assertNoErrors()
        subscriber.assertValues(expectedRooms, expectedRooms)
    }

}