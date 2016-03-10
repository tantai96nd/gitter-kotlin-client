package com.github.shchurov.gitterclient.data.network.model

import com.google.gson.annotations.SerializedName

class MessageResponse {

    constructor()

    constructor(id: String, text: String, timeIso: String, user: UserResponse, unread: Boolean) {
        this.id = id
        this.text = text
        this.timeIso = timeIso
        this.user = user
        this.unread = unread
    }

    lateinit var id: String
    lateinit var text: String
    @SerializedName("sent")
    lateinit var timeIso: String
    @SerializedName("fromUser")
    lateinit var user: UserResponse
    var unread: Boolean = false

}