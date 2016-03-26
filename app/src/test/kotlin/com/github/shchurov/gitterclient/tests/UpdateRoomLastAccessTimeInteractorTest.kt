package com.github.shchurov.gitterclient.tests

import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.domain.models.Room
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UpdateRoomLastAccessTimeInteractorTest {

    companion object {
        private const val TIMESTAMP = 12345L
        private const val ROOM_ID = "random_id123"
    }

    @Mock private lateinit var database: Database
    private lateinit var interactor: UpdateRoomLastAccessTimeInteractor

    @Before
    fun setUp() {
        interactor = UpdateRoomLastAccessTimeInteractor(database)
    }

    @Test
    fun updateLastAccessTime() {
        val room = Room(ROOM_ID, "", null, 0, 0, 0)
        interactor.update(room, TIMESTAMP)
        assertEquals(room.lastAccessTimestamp, TIMESTAMP)
        verify(database, times(1)).updateRoomLastAccessTime(ROOM_ID, TIMESTAMP)
    }

}