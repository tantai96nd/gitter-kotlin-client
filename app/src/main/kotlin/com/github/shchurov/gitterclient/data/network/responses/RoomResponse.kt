package com.github.shchurov.gitterclient.data.network.responses

import com.google.gson.annotations.SerializedName

class RoomResponse {

    var id: String? = null
    var name: String? = null
    var unreadItems: Int = 0
    var mentions: Int = 0
    var url: String? = null
    @SerializedName("lastAccessTime")
    var lastAccessTimeString: String? = null
//    val avatar by lazy {
//        // taken from the official gitter app, is reliable?
//        val s = url!!.split("/")[1]
//        "https://avatars.githubusercontent.com/" + s + "?s=120";
//    }

}