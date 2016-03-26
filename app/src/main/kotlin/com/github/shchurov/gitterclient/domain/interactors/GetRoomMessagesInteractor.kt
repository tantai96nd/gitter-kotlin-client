package com.github.shchurov.gitterclient.domain.interactors

import com.github.shchurov.gitterclient.data.network.api.GitterApi
import com.github.shchurov.gitterclient.domain.interactors.threading.SchedulersProvider
import com.github.shchurov.gitterclient.domain.models.Message
import rx.Observable
import rx.functions.Action1
import javax.inject.Inject

class GetRoomMessagesInteractor @Inject constructor(
        private val gitterApi: GitterApi,
        private val schedulersProvider: SchedulersProvider,
        private val roomId: String
) {

    companion object {
        const val PAGE_SIZE = 30
    }

    var hasMorePages = false
        private set
    private lateinit var earliestMessageId: String

    fun getFirstPage(): Observable<MutableList<Message>> {
        return gitterApi.getRoomMessages(roomId, PAGE_SIZE)
                .subscribeOn(schedulersProvider.background)
                .doOnNext(updateStateAction)
                .observeOn(schedulersProvider.main)
    }

    private val updateStateAction = Action1<MutableList<Message>> { messages ->
        hasMorePages = messages.size == PAGE_SIZE
        earliestMessageId = messages.first().id
    }

    fun getNextPage() = gitterApi.getRoomMessages(roomId, PAGE_SIZE, earliestMessageId)
            .subscribeOn(schedulersProvider.background)
            .doOnNext { updateStateAction }
            .observeOn(schedulersProvider.main)

}