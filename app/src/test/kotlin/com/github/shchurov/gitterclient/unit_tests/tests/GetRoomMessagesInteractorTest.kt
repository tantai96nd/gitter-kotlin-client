package com.github.shchurov.gitterclient.unit_tests.tests

import com.github.shchurov.gitterclient.data.network.api.GitterApi
import com.github.shchurov.gitterclient.domain.interactors.GetRoomMessagesInteractor
import com.github.shchurov.gitterclient.domain.models.Message
import com.github.shchurov.gitterclient.domain.models.User
import com.github.shchurov.gitterclient.unit_tests.helpers.TestSchedulersProvider
import com.github.shchurov.gitterclient.unit_tests.helpers.eq
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.runners.MockitoJUnitRunner
import rx.Observable
import rx.observers.TestSubscriber

@RunWith(MockitoJUnitRunner::class)
class GetRoomMessagesInteractorTest {

    companion object {
        private const val ROOM_ID = "room_id"
    }

    @Mock private lateinit var gitterApi: GitterApi
    private lateinit var interactor: GetRoomMessagesInteractor
    private lateinit var messages: List<Message>
    private val limitCaptor = ArgumentCaptor.forClass(Int::class.java)

    @Before
    fun setUp() {
        messages = createMessagesList(100)
        interactor = GetRoomMessagesInteractor(gitterApi, TestSchedulersProvider(), ROOM_ID)
    }

    private fun createMessagesList(size: Int): List<Message> {
        val list: MutableList<Message> = mutableListOf()
        for (i in 0..size - 1) {
            val user = User("user_id$i", "username$i", "avatar$i")
            list.add(Message("message_id$i", "text$i", i.toLong(), user, true))
        }
        return list.reversed()
    }

    @Test
    fun testGetFirstAndNextPage() {
        mockGitterApiRegular()
        var subscriber = TestSubscriber<List<Message>>()
        interactor.getFirstPage()
                .subscribe(subscriber)
        subscriber.awaitTerminalEvent()

        subscriber.assertNoErrors()
        subscriber.assertValueCount(1)
        verify(gitterApi).getRoomMessages(eq(ROOM_ID), anyInt(), eq(null))
        val firstPage = subscriber.onNextEvents[0]
        assertEquals(limitCaptor.value, firstPage.size)
        assertTrue(interactor.hasMorePages)

        subscriber = TestSubscriber<List<Message>>()
        interactor.getNextPage()
                .subscribe(subscriber)
        subscriber.awaitTerminalEvent()

        subscriber.assertNoErrors()
        subscriber.assertValueCount(1)
        verify(gitterApi).getRoomMessages(eq(ROOM_ID), anyInt(), eq(firstPage.first().id))
        assertEquals(limitCaptor.value, subscriber.onNextEvents[0].size)
    }

    private fun mockGitterApiRegular() {
        `when`(gitterApi.getRoomMessages(eq(ROOM_ID), limitCaptor.capture(), anyString()))
                .thenAnswer { invocation ->
                    Observable.just(messages.subList(0, limitCaptor.value))
                }
    }

    @Test
    fun testNoMorePages() {
        mockGitterApiOneLess()
        val subscriber = TestSubscriber<List<Message>>()
        interactor.getFirstPage()
                .subscribe(subscriber)
        subscriber.awaitTerminalEvent()

        assertFalse(interactor.hasMorePages)

    }

    private fun mockGitterApiOneLess() {
        `when`(gitterApi.getRoomMessages(eq(ROOM_ID), limitCaptor.capture(), anyString()))
                .thenAnswer { invocation ->
                    Observable.just(messages.subList(0, limitCaptor.value - 1))
                }
    }

}