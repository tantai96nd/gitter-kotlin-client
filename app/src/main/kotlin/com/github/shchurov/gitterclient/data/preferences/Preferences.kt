package com.github.shchurov.gitterclient.data.preferences

interface Preferences {

    fun setGitterAccessToken(accessToken: String)

    fun getGitterAccessToken(): String?

    fun setUserId(userId: String)

    fun getUserId(): String?

}