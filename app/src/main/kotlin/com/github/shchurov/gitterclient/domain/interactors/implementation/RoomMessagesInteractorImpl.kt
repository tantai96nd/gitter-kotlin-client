package com.github.shchurov.gitterclient.domain.interactors.implementation

import com.github.shchurov.gitterclient.data.network.GitterApi
import com.github.shchurov.gitterclient.domain.DataSource
import com.github.shchurov.gitterclient.domain.DataWrapper
import com.github.shchurov.gitterclient.domain.interactors.RoomMessagesInteractor
import com.github.shchurov.gitterclient.domain.models.Message
import com.github.shchurov.gitterclient.utils.applySchedulers
import rx.Observable

class RoomMessagesInteractorImpl(private val roomId: String) : RoomMessagesInteractor {

    companion object {
        private const val MESSAGES_LIMIT = 30;
    }

    val gitterApi: GitterApi
    private var hasMorePages = false
    private lateinit var lastMessageId: String

    override fun getRoomMessagesFirstPage(): Observable<DataWrapper<MutableList<Message>>> {
        return gitterApi.getRoomMessages(roomId, MESSAGES_LIMIT)
                .map { handleMessages(it) }
                .applySchedulers()
    }

    private fun handleMessages(messages: MutableList<Message>): DataWrapper<MutableList<Message>> {
        messages.reverse()
        refreshHasMorePages(messages.size)
        refreshLastMessageId(messages)
        return DataWrapper(messages, DataSource.NETWORK)
    }

    private fun refreshHasMorePages(lastPageSize: Int) {
        hasMorePages = lastPageSize == MESSAGES_LIMIT
    }

    private fun refreshLastMessageId(messages: List<Message>) {
        lastMessageId = messages.last().id
    }

    override fun getRoomMessagesNextPage() =
            gitterApi.getRoomMessages(roomId, MESSAGES_LIMIT, lastMessageId)
                    .map { handleMessages(it) }
                    .applySchedulers()

    override fun isHasMorePages() = hasMorePages

}