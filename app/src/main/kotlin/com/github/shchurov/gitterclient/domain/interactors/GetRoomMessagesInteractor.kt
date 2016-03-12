package com.github.shchurov.gitterclient.domain.interactors

import com.github.shchurov.gitterclient.dagger.scopes.PerScreen
import com.github.shchurov.gitterclient.data.network.api.GitterApi
import com.github.shchurov.gitterclient.domain.interactors.threading.SchedulersProvider
import com.github.shchurov.gitterclient.domain.models.Message
import rx.Observable
import javax.inject.Inject

@PerScreen
class GetRoomMessagesInteractor @Inject constructor(
        private val gitterApi: GitterApi,
        private val schedulersProvider: SchedulersProvider
) {

    companion object {
        const val PAGE_SIZE = 30
    }

    private var hasMorePages = false
    private lateinit var roomId: String
    private lateinit var earliestMessageId: String

    fun getFirstPage(roomId: String): Observable<MutableList<Message>> {
        this.roomId = roomId
        return gitterApi.getRoomMessages(roomId, PAGE_SIZE)
                .subscribeOn(schedulersProvider.backgroundScheduler)
                .map { handleMessages(it) }
                .observeOn(schedulersProvider.uiScheduler)
    }

    private fun handleMessages(messages: MutableList<Message>): MutableList<Message> {
        refreshHasMorePages(messages.size)
        refreshEarliestMessageId(messages)
        return messages
    }

    private fun refreshHasMorePages(lastPageSize: Int) {
        hasMorePages = lastPageSize == PAGE_SIZE
    }

    private fun refreshEarliestMessageId(messages: List<Message>) {
        earliestMessageId = messages.first().id
    }

    fun getNextPage() = gitterApi.getRoomMessages(roomId, PAGE_SIZE, earliestMessageId)
            .subscribeOn(schedulersProvider.backgroundScheduler)
            .map { handleMessages(it) }
            .observeOn(schedulersProvider.uiScheduler)

    fun hasMorePages() = hasMorePages

}