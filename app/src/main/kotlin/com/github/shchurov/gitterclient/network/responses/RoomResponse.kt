package com.github.shchurov.gitterclient.network.responses

import com.github.shchurov.gitterclient.utils.Utils
import com.google.gson.annotations.SerializedName

class RoomResponse {

    var id: String? = null
    var name: String? = null
    var unreadItems: Int = 0
    var mentions: Int = 0
    var url: String? = null
    @SerializedName("lastAccessTime")
    var lastAccessTimeString: String? = null
    val lastAccessTime by lazy {
        Utils.convertTimeStringToLong(lastAccessTimeString)
    }
    val avatar by lazy {
        // taken from the official gitter app, is reliable?
        val s = url!!.split("/")[1]
        "https://avatars.githubusercontent.com/" + s + "?s=120";
    }

    //    "id": "53307860c3599d1de448e19d",
    //    "name": "Andrew Newdigate",
    //    "topic": "",
    //    "oneToOne": true,
    //    "user": {
    //        "id": "53307831c3599d1de448e19a",
    //        "username": "suprememoocow",
    //        "displayName": "Andrew Newdigate",
    //        "url": "/suprememoocow",
    //        "avatarUrlSmall": "https://avatars.githubusercontent.com/u/594566?",
    //        "avatarUrlMedium": "https://avatars.githubusercontent.com/u/594566?"
    //    },
    //    "unreadItems": 0,
    //    "mentions": 0,
    //    "lurk": false,
    //    "url": "/suprememoocow",
    //    "githubType": "ONETOONE"

}