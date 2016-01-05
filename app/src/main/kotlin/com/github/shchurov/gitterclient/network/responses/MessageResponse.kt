package com.github.shchurov.gitterclient.network.responses

import com.github.shchurov.gitterclient.utils.Utils
import com.google.gson.annotations.SerializedName

class MessageResponse {

    var id: String? = null
    var text: String? = null
    @SerializedName("sent")
    var timeString: String? = null
    val time by lazy {
        Utils.convertTimeStringToLong(timeString)
    }
    @SerializedName("fromUser")
    var user: UserResponse? = null
    var unread: Boolean = false

}