package com.github.shchurov.gitterclient.unit_tests.tests

import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MarkMessageAsReadInteractorTest {

//    companion object {
//        private const val ROOM_ID = "random_id123"
//    }
//
//    @Mock private lateinit var gitterApi: GitterApi
//    @Mock private lateinit var database: Database
//    private val schedulersProvider = ImmediateSchedulersProvider()
//    private lateinit var fakeMessages: MutableList<Message>;
//    private lateinit var interactor: MarkMessageAsReadInteractor
//    private val sentToServerIds: MutableSet<String> = mutableSetOf()
//
//    @Before
//    fun setUp() {
//        setupMocks()
//        interactor = MarkMessageAsReadInteractor(gitterApi, database, schedulersProvider)
//    }
//
//    private fun setupMocks() {
//        fakeMessages = createFakeMessagesList(1000)
//        mockGitterApi()
//    }
//
//    @Suppress("UNCHECKED_CAST")
//    private fun mockGitterApi() {
//        `when`(gitterApi.markMessagesAsRead(anyListOf(String::class.java), eq(ROOM_ID)))
//                .thenAnswer { invocation ->
//                    val ids = invocation.arguments[0] as MutableList<String>
//                    sentToServerIds.addAll(ids)
//                    Observable.empty<Any>()
//                }
//    }
//
//    private fun createFakeMessagesList(size: Int): MutableList<Message> {
//        val messages = mutableListOf<Message>()
//        for (i in 0..(size - 1)) {
//            val user = User("$i", "", "")
//            val message = Message("$i", "", 0, user, true)
//            messages.add(message)
//        }
//        return messages
//    }
//
//    @Test
//    fun markAsRead() {
//        val n = fakeMessages.size / 2
//        val random = Random()
//        val markedIds: MutableSet<String> = mutableSetOf()
//        for (i in 0..n) {
//            val message = fakeMessages[random.nextInt(fakeMessages.size)]
//            markedIds.add(message.id)
//            interactor.markAsReadLazy(message, ROOM_ID)
//            assertFalse(message.unread)
//        }
//        verify(database, times(markedIds.size)).decrementRoomUnreadItems(ROOM_ID)
//        interactor.flush()
//        assertEquals(markedIds, sentToServerIds)
//    }

}