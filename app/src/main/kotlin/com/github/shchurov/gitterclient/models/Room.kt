package com.github.shchurov.gitterclient.models

class Room(val id: String, val name: String, val avatar: String, var unreadItems: Int,
        var mentions: Int, var lastTimeAccess: Long)