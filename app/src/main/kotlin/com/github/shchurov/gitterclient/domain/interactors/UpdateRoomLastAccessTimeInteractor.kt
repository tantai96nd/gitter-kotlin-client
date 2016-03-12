package com.github.shchurov.gitterclient.domain.interactors

import com.github.shchurov.gitterclient.dagger.scopes.PerScreen
import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.domain.models.Room
import javax.inject.Inject

@PerScreen
class UpdateRoomLastAccessTimeInteractor @Inject constructor(
        private val database: Database
) {

    fun update(room: Room, timestamp: Long) {
        room.lastAccessTimestamp = timestamp
        database.updateRoomLastAccessTime(room.id, timestamp)
    }

}