package com.github.shchurov.gitterclient.data.network.responses

import com.google.gson.annotations.SerializedName

class ErrorResponse {

    var error: String? = null
    @SerializedName("error_description")
    var description: String? = null

}