package com.github.shchurov.gitterclient.domain.interactors

import com.github.shchurov.gitterclient.dagger.scopes.PerScreen
import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.network.api.GitterApi
import com.github.shchurov.gitterclient.data.subscribers.EmptySubscriber
import com.github.shchurov.gitterclient.domain.interactors.threading.SchedulersProvider
import com.github.shchurov.gitterclient.domain.models.Message
import java.util.*
import javax.inject.Inject

@PerScreen
class MarkMessageAsReadInteractor @Inject constructor(
        private val gitterApi: GitterApi,
        private val database: Database,
        private val schedulersProvider: SchedulersProvider
) {

    companion object {
        private const val MAX_MESSAGES_BEFORE_REQUEST = 20
    }

    private val ids = mutableListOf<String>()
    private lateinit var roomId: String

    fun markAsReadLazy(message: Message, roomId: String) {
        if (!message.unread)
            return
        this.roomId = roomId
        message.unread = false
        database.decrementRoomUnreadItems(roomId)
        ids.add(message.id)
        if (ids.size == MAX_MESSAGES_BEFORE_REQUEST) {
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
                .subscribeOn(schedulersProvider.backgroundScheduler)
                .subscribe(EmptySubscriber())
    }

}