package com.github.shchurov.gitterclient.tests

import com.github.shchurov.gitterclient.ImmediateSchedulersProvider
import com.github.shchurov.gitterclient.data.network.GitterApi
import com.github.shchurov.gitterclient.domain.interactors.GetRoomMessagesInteractor
import com.github.shchurov.gitterclient.domain.interactors.implementation.GetRoomMessagesInteractorImpl
import com.github.shchurov.gitterclient.domain.models.Message
import com.github.shchurov.gitterclient.domain.models.User
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.runners.MockitoJUnitRunner
import rx.Observable
import rx.observers.TestSubscriber
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class GetRoomMessagesInteractorTest {

    companion object {
        private const val PAGE_SIZE = GetRoomMessagesInteractor.PAGE_SIZE
    }

    @Mock private lateinit var gitterApi: GitterApi
    private val schedulersProvider = ImmediateSchedulersProvider()
    private lateinit var mockedMessages: MutableList<Message>;
    private lateinit var interactor: GetRoomMessagesInteractorImpl

    @Before
    fun setUp() {
        setupMocks()
        interactor = GetRoomMessagesInteractorImpl(gitterApi, schedulersProvider)
    }

    private fun setupMocks() {
        mockedMessages = createMockedMessagesList(1000)
        mockGitterApi()
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

    private fun mockGitterApi() {
        `when`(gitterApi.getRoomMessages(anyString(), eq(PAGE_SIZE), anyString()))
                .thenAnswer { invocation ->
                    val beforeId = invocation.arguments[2] as String?
                    val end: Int = beforeId?.toInt() ?: mockedMessages.size
                    val start = Math.max(0, end - PAGE_SIZE)
                    val answerProjection = mockedMessages.subList(start, end)
                    Observable.just(ArrayList<Message>(answerProjection))
                }
    }

    @Test
    fun getFirstAndNextPages() {
        checkFirstPage()
        var total = PAGE_SIZE
        while (total < mockedMessages.size) {
            assertTrue(interactor.hasMorePages())
            checkNextPage(total)
            total += PAGE_SIZE
        }
    }

    private fun checkFirstPage() {
        val subscriber = createSubscriber()
        interactor.getFirstPage("random id").subscribe(subscriber)
        subscriber.assertNoErrors()
        val expected = mockedMessages.subList(mockedMessages.size - PAGE_SIZE, mockedMessages.size)
        subscriber.assertValue(expected)
    }

    private fun createSubscriber() = TestSubscriber<MutableList<Message>>()

    private fun checkNextPage(offset: Int) {
        val end = mockedMessages.size - offset
        val start = Math.max(0, end - PAGE_SIZE)
        val expected = mockedMessages.subList(start, end)
        val subscriber = createSubscriber()
        interactor.getNextPage().subscribe(subscriber)
        subscriber.assertNoErrors()
        subscriber.assertValue(expected)
    }

}