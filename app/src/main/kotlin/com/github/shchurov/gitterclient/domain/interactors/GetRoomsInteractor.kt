package com.github.shchurov.gitterclient.domain.interactors

import com.github.shchurov.gitterclient.domain.models.Room
import rx.Observable

interface GetRoomsInteractor {

    fun getRooms(localOnly: Boolean): Observable<MutableList<Room>>

}