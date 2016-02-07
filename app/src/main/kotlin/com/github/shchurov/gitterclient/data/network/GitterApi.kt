package com.github.shchurov.gitterclient.data.network

import com.github.shchurov.gitterclient.domain.models.Message
import com.github.shchurov.gitterclient.domain.models.Room
import com.github.shchurov.gitterclient.domain.models.Token
import rx.Observable

interface GitterApi {

    fun getAccessToken(url: String, clientId: String, clientSecret: String, code: String,
            redirectUri: String, grantType: String): Observable<Token>

    fun getMyRooms(): Observable<MutableList<Room>>

    fun getRoomMessages(roomId: String, limit: Int, beforeId: String? = null)
            : Observable<MutableList<Message>>

}