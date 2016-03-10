package com.github.shchurov.gitterclient.data.network.model

import com.google.gson.annotations.SerializedName

class UserResponse {

    constructor()

    constructor(id: String, username: String, avatar: String) {
        this.id = id
        this.username = username
        this.avatar = avatar
    }

    lateinit var id: String
    lateinit var username: String
    @SerializedName("avatarUrlSmall")
    lateinit var avatar: String

}