package com.github.shchurov.gitterclient.data.network.responses

import com.google.gson.annotations.SerializedName

class MessageResponse {

    var id: String? = null
    var text: String? = null
    @SerializedName("sent")
    var timeString: String? = null
    @SerializedName("fromUser")
    var user: UserResponse? = null
    var unread: Boolean = false

}