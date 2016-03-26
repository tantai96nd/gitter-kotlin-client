package com.github.shchurov.gitterclient.domain.interactors

import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.network.api.GitterApi
import com.github.shchurov.gitterclient.data.subscribers.EmptySubscriber
import com.github.shchurov.gitterclient.domain.interactors.threading.SchedulersProvider
import com.github.shchurov.gitterclient.domain.models.Message
import java.util.*
import javax.inject.Inject

class MarkMessageAsReadInteractor @Inject constructor(
        private val gitterApi: GitterApi,
        private val database: Database,
        private val schedulersProvider: SchedulersProvider,
        private val roomId: String
) {

    companion object {
        private const val MAX_PENDING_MESSAGES = 20
    }

    private val ids = mutableListOf<String>()

    fun markAsReadLazy(message: Message) {
        message.unread = false
        database.decrementRoomUnreadItems(roomId)
        ids.add(message.id)
        if (ids.size == MAX_PENDING_MESSAGES) {
            sendRequest(roomId)
        }
    }

    fun flush() {
        if (ids.size > 0) {
            sendRequest(roomId)
        }
    }

    private fun sendRequest(roomId: String) {
        val copyIds = ArrayList(ids)
        ids.clear()
        gitterApi.markMessagesAsRead(copyIds, roomId)
                .subscribeOn(schedulersProvider.background)
                .subscribe(EmptySubscriber())
    }

}