package com.github.shchurov.gitterclient.unit_tests.tests

import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.network.api.GitterApi
import com.github.shchurov.gitterclient.domain.interactors.MarkMessageAsReadInteractor
import com.github.shchurov.gitterclient.domain.models.Message
import com.github.shchurov.gitterclient.domain.models.User
import com.github.shchurov.gitterclient.unit_tests.helpers.TestSchedulersProvider
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.runners.MockitoJUnitRunner
import rx.Observable

@RunWith(MockitoJUnitRunner::class)
class MarkMessageAsReadInteractorTest {

    companion object {
        private const val ROOM_ID = "room_id"
    }

    @Mock private lateinit var gitterApi: GitterApi
    @Mock private lateinit var database: Database
    private lateinit var interactor: MarkMessageAsReadInteractor
    private lateinit var messages: MutableList<Message>

    @Before
    fun setUp() {
        messages = createMessagesList(50)
        mockGitterApi()
        interactor = MarkMessageAsReadInteractor(gitterApi, database, TestSchedulersProvider(), ROOM_ID)
    }

    private fun mockGitterApi() {
        `when`(gitterApi.markMessagesAsRead(anyListOf(String::class.java), eq(ROOM_ID)))
                .thenReturn(Observable.empty<Any>())
    }

    private fun createMessagesList(size: Int): MutableList<Message> {
        val messages = mutableListOf<Message>()
        for (i in 0..(size - 1)) {
            val user = User("user_id$i", "username$i", "avatar$i")
            messages.add(Message("message_id$i", "text$i", i.toLong(), user, true))
        }
        return messages
    }

    @Test
    fun markAsRead() {
        for (message in messages) {
            interactor.markAsReadLazy(message)
        }
        interactor.flush()

        val listClass = List::class.java as Class<List<String>>
        val captor = ArgumentCaptor.forClass(listClass)
        verify(gitterApi, atLeastOnce()).markMessagesAsRead(capture(captor), eq(ROOM_ID))
        verify(database, times(messages.size)).decrementRoomUnreadItems(ROOM_ID)
        val expectedIds = messages.map { it.id }
        val realIds = captor.allValues.flatten()
        assertEquals(expectedIds, realIds)
    }

    fun capture(captor: ArgumentCaptor<List<String>>): List<String> {
        captor.capture()
        return emptyList()
    }

}