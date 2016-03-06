package com.github.shchurov.gitterclient.domain.interactors.implementation

import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.domain.interactors.UpdateRoomLastAccessTimeInteractor
import com.github.shchurov.gitterclient.domain.models.Room

class UpdateRoomLastAccessTimeInteractorImpl(
        private val database: Database
) : UpdateRoomLastAccessTimeInteractor {

    override fun update(room: Room, timestamp: Long) {
        room.lastAccessTimestamp = timestamp
        database.updateRoomLastAccessTime(room.id, timestamp)
    }

}