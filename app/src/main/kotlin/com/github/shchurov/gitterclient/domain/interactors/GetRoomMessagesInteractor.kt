package com.github.shchurov.gitterclient.domain.interactors

import com.github.shchurov.gitterclient.domain.DataWrapper
import com.github.shchurov.gitterclient.domain.models.Message
import rx.Observable

interface GetRoomMessagesInteractor {

    fun getFirstPage(roomId: String): Observable<DataWrapper<MutableList<Message>>>

    fun getNextPage(): Observable<DataWrapper<MutableList<Message>>>

    fun hasMorePages(): Boolean

}