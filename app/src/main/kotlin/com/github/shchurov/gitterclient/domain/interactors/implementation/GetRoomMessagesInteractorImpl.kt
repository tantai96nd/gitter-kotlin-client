package com.github.shchurov.gitterclient.domain.interactors.implementation

import com.github.shchurov.gitterclient.data.network.GitterApi
import com.github.shchurov.gitterclient.domain.DataSource
import com.github.shchurov.gitterclient.domain.DataWrapper
import com.github.shchurov.gitterclient.domain.interactors.GetRoomMessagesInteractor
import com.github.shchurov.gitterclient.domain.interactors.threading.SchedulersProvider
import com.github.shchurov.gitterclient.domain.models.Message
import rx.Observable

class GetRoomMessagesInteractorImpl(
        private val gitterApi: GitterApi,
        private val schedulersProvider: SchedulersProvider
) : GetRoomMessagesInteractor {

    companion object {
        private const val MESSAGES_LIMIT = 30;
    }

    private var hasMorePages = false
    private lateinit var roomId: String
    private lateinit var lastMessageId: String

    override fun getFirstPage(roomId: String): Observable<DataWrapper<MutableList<Message>>> {
        this.roomId = roomId
        return gitterApi.getRoomMessages(roomId, MESSAGES_LIMIT)
                .subscribeOn(schedulersProvider.backgroundScheduler)
                .map { handleMessages(it) }
                .observeOn(schedulersProvider.uiScheduler)
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

    override fun getNextPage() =
            gitterApi.getRoomMessages(roomId, MESSAGES_LIMIT, lastMessageId)
                    .subscribeOn(schedulersProvider.backgroundScheduler)
                    .map { handleMessages(it) }
                    .observeOn(schedulersProvider.uiScheduler)

    override fun hasMorePages() = hasMorePages

}