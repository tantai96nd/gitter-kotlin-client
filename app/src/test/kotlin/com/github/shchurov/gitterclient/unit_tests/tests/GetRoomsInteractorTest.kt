package com.github.shchurov.gitterclient.unit_tests.tests

import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.network.api.GitterApi
import com.github.shchurov.gitterclient.domain.interactors.GetRoomsInteractor
import com.github.shchurov.gitterclient.domain.models.Room
import com.github.shchurov.gitterclient.unit_tests.helpers.TestSchedulersProvider
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
    private lateinit var interactor: GetRoomsInteractor
    private lateinit var expectedLocalRooms: MutableList<Room>
    private lateinit var expectedNetworkRooms: MutableList<Room>

    @Before
    fun setUp() {
        expectedLocalRooms = createRoomsList(20)
        expectedNetworkRooms = createRoomsList(30)
        mockDatabase()
        mockGitterApi()
        interactor = GetRoomsInteractor(gitterApi, database, TestSchedulersProvider())
    }

    private fun createRoomsList(size: Int): MutableList<Room> {
        val list: MutableList<Room> = mutableListOf()
        val random = Random()
        for (i in 0..size - 1) {
            list.add(Room("id$i", "name$i", "avatar$i", random.nextInt(), random.nextInt(), random.nextLong()))
        }
        list.sortByDescending { it.lastAccessTimestamp }
        return list
    }

    private fun mockGitterApi() {
        val rooms = ArrayList(expectedNetworkRooms)
        Collections.shuffle(rooms)
        Mockito.`when`(gitterApi.getMyRooms())
                .thenReturn(Observable.just(rooms))
    }

    private fun mockDatabase() {
        val rooms = ArrayList(expectedLocalRooms)
        Collections.shuffle(rooms)
        Mockito.`when`(database.getRooms())
                .thenReturn(rooms)
    }

    @Test
    fun testLocal() {
        val subscriber = TestSubscriber<MutableList<Room>>()
        interactor.get(true).subscribe(subscriber)
        subscriber.awaitTerminalEvent()

        subscriber.assertNoErrors()
        subscriber.assertValue(expectedLocalRooms)
    }

    @Test
    fun testLocalAndNetwork() {
        val subscriber = TestSubscriber<MutableList<Room>>()
        interactor.get(false).subscribe(subscriber)
        subscriber.awaitTerminalEvent()

        subscriber.assertNoErrors()
        subscriber.assertValues(expectedLocalRooms, expectedNetworkRooms)
    }

}