package com.github.shchurov.gitterclient.models

class Room(val id: String, val name: String, val avatar: String, var unreadItems: Int,
        var mentions: Int, var lastTimeAccess: Long) {

    val idHashCode by lazy {
        var hash: Long = 0
        for (i in 0..id.length - 1) {
            hash = 63 * hash + id[i].toLong()
        }
        hash
    }

}