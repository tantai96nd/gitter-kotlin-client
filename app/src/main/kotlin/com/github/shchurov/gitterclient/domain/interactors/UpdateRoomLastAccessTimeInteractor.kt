package com.github.shchurov.gitterclient.domain.interactors

import com.github.shchurov.gitterclient.domain.models.Room

interface UpdateRoomLastAccessTimeInteractor {

    fun update(room: Room)

}