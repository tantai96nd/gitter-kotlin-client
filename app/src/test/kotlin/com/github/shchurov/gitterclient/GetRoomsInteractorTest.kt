package com.github.shchurov.gitterclient

import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.network.GitterApi
import com.github.shchurov.gitterclient.domain.DataSource
import com.github.shchurov.gitterclient.domain.DataSubscriber
import com.github.shchurov.gitterclient.domain.interactors.GetRoomsInteractor
import com.github.shchurov.gitterclient.domain.interactors.implementation.GetRoomsInteractorImpl
import com.github.shchurov.gitterclient.domain.interactors.threading.SchedulersProvider
import com.github.shchurov.gitterclient.domain.models.Room
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import rx.Observable
import rx.schedulers.Schedulers

class GetRoomsInteractorTest {

    private lateinit var interactor: GetRoomsInteractor

    private val networkRooms = mutableListOf(
            Room("id1", "Room 1", null, 0, 0, 1),
            Room("id2", "Room 2", null, 0, 0, 2),
            Room("id3", "Room 3", null, 0, 0, 3)
    )

    private val localRooms = mutableListOf(
            Room("id1", "Room 1", null, 0, 0, 3),
            Room("id2", "Room 2", null, 0, 0, 2),
            Room("id3", "Room 3", null, 0, 0, 1)
    )

    @Before
    fun setUp() {
        val gitterApi = mockGitterApi()
        val database = mockDatabase()
        val schedulersProvider = mockSchedulersProvider()
        interactor = GetRoomsInteractorImpl(gitterApi, database, schedulersProvider)
    }

    private fun mockGitterApi(): GitterApi {
        val gitterApi = Mockito.mock(GitterApi::class.java)
        Mockito.`when`(gitterApi.getMyRooms())
                .thenReturn(Observable.just(networkRooms))
        return gitterApi
    }

    private fun mockDatabase(): Database {
        val database = Mockito.mock(Database::class.java)
        Mockito.`when`(database.getRooms())
                .thenReturn(localRooms)
        return database
    }

    private fun mockSchedulersProvider(): SchedulersProvider {
        val provider = Mockito.mock(SchedulersProvider::class.java)
        Mockito.`when`(provider.backgroundScheduler)
                .thenReturn(Schedulers.immediate())
        Mockito.`when`(provider.uiScheduler)
                .thenReturn(Schedulers.immediate())
        return provider
    }

    @Test
    fun localOnly() {
        interactor.getRooms(true).subscribe(object : DataSubscriber<MutableList<Room>>() {
            override fun onData(data: MutableList<Room>, source: DataSource) {
                assertEquals(source, DataSource.LOCAL)
                assertEquals(data[0], localRooms[0])
                assertEquals(data[1], localRooms[1])
                assertEquals(data[2], localRooms[2])
            }
        })
    }

}