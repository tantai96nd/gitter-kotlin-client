package com.github.shchurov.gitterclient.domain.interactors

import com.github.shchurov.gitterclient.domain.DataWrapper
import com.github.shchurov.gitterclient.domain.models.Message
import rx.Observable

interface RoomMessagesInteractor {

    fun getRoomMessagesFirstPage(): Observable<DataWrapper<MutableList<Message>>>

    fun getRoomMessagesNextPage(): Observable<DataWrapper<MutableList<Message>>>

    fun isHasMorePages(): Boolean

}