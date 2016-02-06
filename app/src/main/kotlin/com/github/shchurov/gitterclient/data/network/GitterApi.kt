package com.github.shchurov.gitterclient.data.network

import com.github.shchurov.gitterclient.data.network.responses.MessageResponse
import com.github.shchurov.gitterclient.data.network.responses.RoomResponse
import com.github.shchurov.gitterclient.data.network.responses.TokenResponse
import retrofit.http.Path
import retrofit.http.Query
import rx.Observable

interface GitterApi {

    fun getAccessToken(url: String, clientId: String, clientSecret: String, code: String,
            redirectUri: String, grantType: String
    ): Observable<TokenResponse>

    fun getMyRooms(): Observable<MutableList<RoomResponse>>

    fun getRoomMessages(
            @Path("roomId") roomId: String,
            @Query("limit") limit: Int,
            @Query("beforeId") beforeId: String? = null
    ): Observable<MutableList<MessageResponse>>

}