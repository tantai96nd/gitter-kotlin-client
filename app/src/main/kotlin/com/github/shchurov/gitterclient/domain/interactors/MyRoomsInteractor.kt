package com.github.shchurov.gitterclient.domain.interactors

import com.github.shchurov.gitterclient.domain.DataWrapper
import com.github.shchurov.gitterclient.domain.models.Room
import rx.Observable

interface MyRoomsInteractor {

    fun getMyRooms(localOnly: Boolean): Observable<DataWrapper<MutableList<Room>>>

}