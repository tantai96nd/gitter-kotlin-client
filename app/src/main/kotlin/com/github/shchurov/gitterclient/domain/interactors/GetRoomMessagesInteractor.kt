package com.github.shchurov.gitterclient.domain.interactors

import com.github.shchurov.gitterclient.domain.models.Message
import rx.Observable

interface GetRoomMessagesInteractor {

    fun getFirstPage(roomId: String): Observable<MutableList<Message>>

    fun getNextPage(): Observable<MutableList<Message>>

    fun hasMorePages(): Boolean

}