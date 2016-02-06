package com.github.shchurov.gitterclient.domain.models

class Room(val id: String, val name: String, val avatar: String, var unreadItems: Int,
        var mentions: Int, var lastTimeAccess: Long) {

    /**
     * used in RoomsAdapter for animations
     */
    val idHashCode by lazy {
        var hash = 0L
        for (i in 0..id.length - 1) {
            hash = 61 * hash + id[i].toLong()
        }
        hash
    }

}