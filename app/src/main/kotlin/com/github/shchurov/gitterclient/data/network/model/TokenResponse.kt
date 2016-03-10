package com.github.shchurov.gitterclient.data.network.model

import com.google.gson.annotations.SerializedName

class TokenResponse {

    constructor()

    constructor(accessToken: String) {
        this.accessToken = accessToken
    }

    @SerializedName("access_token")
    lateinit var accessToken: String

}