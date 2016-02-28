package com.github.shchurov.gitterclient.domain.interactors.implementation

import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.network.GitterApi
import com.github.shchurov.gitterclient.data.network.implementation.helpers.RequestSubscriber
import com.github.shchurov.gitterclient.domain.interactors.MarkMessageAsReadInteractor
import com.github.shchurov.gitterclient.domain.models.Message
import rx.schedulers.Schedulers
import java.util.*

class MarkMessageAsReadInteractorImpl(
        private val gitterApi: GitterApi,
        private val database: Database
) : MarkMessageAsReadInteractor {

    companion object {
        private const val MAX_MESSAGES_BEFORE_REQUEST = 20
    }

    private val ids = ArrayList<String>()

    override fun markAsReadLazy(message: Message, roomId: String) {
        if (!message.unread)
            return
        message.unread = false
        database.decrementRoomUnreadItems(roomId)
        ids.add(message.id)
        if (ids.size == MAX_MESSAGES_BEFORE_REQUEST) {
            sendRequest(roomId)
        }
    }

    override fun flush(roomId: String) {
        if (ids.size > 0) {
            sendRequest(roomId)
        }
    }

    private fun sendRequest(roomId: String) {
        val copyIds = ArrayList(ids)
        ids.clear()
        gitterApi.markMessagesAsRead(copyIds, roomId)
                .subscribeOn(Schedulers.io())
                .subscribe(RequestSubscriber())
    }

}