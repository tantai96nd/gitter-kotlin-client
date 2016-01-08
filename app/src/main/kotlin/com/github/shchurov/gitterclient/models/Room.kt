package com.github.shchurov.gitterclient.models

import com.github.shchurov.gitterclient.utils.IdHashCodePropertyDelegate

class Room(val id: String, val name: String, val avatar: String, var unreadItems: Int,
        var mentions: Int, var lastTimeAccess: Long) {

    val idHashCode by IdHashCodePropertyDelegate(id)

}