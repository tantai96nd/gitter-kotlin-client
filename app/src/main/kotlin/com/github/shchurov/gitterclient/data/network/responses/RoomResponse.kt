package com.github.shchurov.gitterclient.data.network.responses

import com.google.gson.annotations.SerializedName

class RoomResponse {

    constructor()

    constructor(id: String, name: String, unreadItems: Int, mentions: Int, url: String, lastAccessTimeIso: String) {
        this.id = id
        this.name = name
        this.unreadItems = unreadItems
        this.mentions = mentions
        this.url = url
        this.lastAccessTimeIso = lastAccessTimeIso
    }

    lateinit var id: String
    lateinit var name: String
    var unreadItems: Int = 0
    var mentions: Int = 0
    lateinit var url: String
    @SerializedName("lastAccessTime")
    lateinit var lastAccessTimeIso: String

}