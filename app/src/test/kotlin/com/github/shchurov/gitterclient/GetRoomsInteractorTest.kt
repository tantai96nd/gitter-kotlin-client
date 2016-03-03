package com.github.shchurov.gitterclient

import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.network.GitterApi
import com.github.shchurov.gitterclient.domain.DataSource
import com.github.shchurov.gitterclient.domain.DataSubscriber
import com.github.shchurov.gitterclient.domain.interactors.GetRoomsInteractor
import com.github.shchurov.gitterclient.domain.interactors.implementation.GetRoomsInteractorImpl
import com.github.shchurov.gitterclient.domain.models.Room
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner
import rx.Observable

@RunWith(MockitoJUnitRunner::class)
class GetRoomsInteractorTest {

    @Mock private lateinit var gitterApi: GitterApi
    @Mock private lateinit var database: Database
    private val schedulersProvider = ImmediateSchedulersProvider()


    private lateinit var interactor: GetRoomsInteractor

    private val networkRooms = mutableListOf(
            Room("id1", "Room 1", null, 0, 0, 3),
            Room("id2", "Room 2", null, 0, 0, 1),
            Room("id3", "Room 3", null, 0, 0, 2)
    )

    private val localRooms = mutableListOf(
            Room("id1", "Room 1", null, 0, 0, 2),
            Room("id2", "Room 2", null, 0, 0, 1),
            Room("id3", "Room 3", null, 0, 0, 3)
    )

    @Before
    fun setUp() {
        setupMocks()
        interactor = GetRoomsInteractorImpl(gitterApi, database, schedulersProvider)
    }

    private fun setupMocks() {
        Mockito.`when`(gitterApi.getMyRooms())
                .thenReturn(Observable.just(networkRooms))
        Mockito.`when`(database.getRooms())
                .thenReturn(localRooms)
    }

    @Test
    fun localOnly() {
        interactor.getRooms(true).subscribe(object : DataSubscriber<MutableList<Room>>() {
            override fun onData(data: MutableList<Room>, source: DataSource) {
                assertEquals(source, DataSource.LOCAL)
                checkSorting(data)
            }
        })
    }

    private fun checkSorting(data: MutableList<Room>) {
        assertTrue(data[0].lastAccessTimestamp == 3L)
        assertTrue(data[1].lastAccessTimestamp == 2L)
        assertTrue(data[2].lastAccessTimestamp == 1L)
    }

    @Test
    fun localAndNetwork() {
        var calls = 0
        interactor.getRooms(false).subscribe(object : DataSubscriber<MutableList<Room>>() {
            override fun onData(data: MutableList<Room>, source: DataSource) {
                calls++
                checkSorting(data)
            }

            override fun onFinish() {
                assertEquals(calls, 2)
            }
        })
    }

}