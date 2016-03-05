package com.github.shchurov.gitterclient

import com.github.shchurov.gitterclient.data.network.GitterApi
import com.github.shchurov.gitterclient.domain.interactors.implementation.GetRoomMessagesInteractorImpl
import com.github.shchurov.gitterclient.domain.models.Message
import com.github.shchurov.gitterclient.domain.models.User
import org.junit.Assert.assertTrue
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
class GetRoomMessagesInteractorTest {

    @Mock private lateinit var gitterApi: GitterApi
    private val schedulersProvider = ImmediateSchedulersProvider()
    private val pageSize = GetRoomMessagesInteractorImpl.MESSAGES_LIMIT
    private lateinit var mockedMessages: MutableList<Message>;
    private lateinit var interactor: GetRoomMessagesInteractorImpl

    @Before
    fun setUp() {
        setupMocks()
        interactor = GetRoomMessagesInteractorImpl(gitterApi, schedulersProvider)
    }

    private fun setupMocks() {
        mockedMessages = createMockedMessagesList(1000)
        Mockito.`when`(gitterApi.getRoomMessages(Mockito.anyString(), Mockito.eq(pageSize), Mockito.anyString()))
                .thenAnswer { invocation ->
                    val beforeId = invocation.arguments[2] as String?
                    val end: Int = beforeId?.toInt() ?: mockedMessages.size
                    val start = Math.max(0, end - pageSize)
                    val answerProjection = mockedMessages.subList(start, end)
                    Observable.just(ArrayList<Message>(answerProjection))
                }
    }

    private fun createMockedMessagesList(size: Int): MutableList<Message> {
        val messages: MutableList<Message> = mutableListOf()
        for (i in 0..(size - 1)) {
            val user = User("$i", "", "")
            val message = Message("$i", "", 0, user, false)
            messages.add(message)
        }
        return messages
    }

    @Test
    fun getFirstAndNextPages() {
        checkFirstPage()
        var total = pageSize
        while (total < mockedMessages.size) {
            assertTrue(interactor.hasMorePages())
            checkNextPage(total)
            total += pageSize
        }
    }

    private fun checkFirstPage() {
        val subscriber = createSubscriber()
        interactor.getFirstPage("random id").subscribe(subscriber)
        subscriber.assertNoErrors()
        val expected = mockedMessages.subList(mockedMessages.size - pageSize, mockedMessages.size)
        subscriber.assertValue(expected)
    }

    private fun createSubscriber() = TestSubscriber<MutableList<Message>>()

    private fun checkNextPage(offset: Int) {
        val end = mockedMessages.size - offset
        val start = Math.max(0, end - pageSize)
        val expected = mockedMessages.subList(start, end)
        val subscriber = createSubscriber()
        interactor.getNextPage().subscribe(subscriber)
        subscriber.assertNoErrors()
        subscriber.assertValue(expected)
    }

}