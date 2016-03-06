package com.github.shchurov.gitterclient.domain.interactors

import com.github.shchurov.gitterclient.domain.models.Message
import rx.Observable

interface GetRoomMessagesInteractor {

    companion object {
        const val PAGE_SIZE = 30;
    }

    fun getFirstPage(roomId: String): Observable<MutableList<Message>>

    fun getNextPage(): Observable<MutableList<Message>>

    fun hasMorePages(): Boolean

}