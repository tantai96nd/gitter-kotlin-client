package com.github.shchurov.gitterclient.network.responses

import com.google.gson.annotations.SerializedName

class UserResponse {

    var id: String? = null
    var username: String? = null
    @SerializedName("avatarUrlSmall")
    var avatar: String? = null

}