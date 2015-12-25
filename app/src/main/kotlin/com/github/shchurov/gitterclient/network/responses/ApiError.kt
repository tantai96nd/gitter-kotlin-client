package com.github.shchurov.gitterclient.network.responses

import com.google.gson.annotations.SerializedName

class ApiError {

    val error: String? = null
    @SerializedName("error_description")
    val description: String? = null

}