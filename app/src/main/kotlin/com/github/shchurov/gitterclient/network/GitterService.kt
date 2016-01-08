package com.github.shchurov.gitterclient.network

import com.github.shchurov.gitterclient.network.responses.MessageResponse
import com.github.shchurov.gitterclient.network.responses.RoomResponse
import com.github.shchurov.gitterclient.network.responses.TokenResponse
import retrofit.http.*
import rx.Observable

interface GitterService {

    @POST
    @FormUrlEncoded
    fun getAccessToken(
            @Url url: String,
            @Field("client_id") clientId: String,
            @Field("client_secret") clientSecret: String,
            @Field("code") code: String,
            @Field("redirect_uri") redirectUri: String,
            @Field("grant_type") grantType: String
    ): Observable<TokenResponse>

    @GET("v1/rooms")
    fun getMyRooms(): Observable<MutableList<RoomResponse>>

    @GET("v1/rooms/{roomId}/chatMessages")
    fun getRoomMessages(
            @Path("roomId") roomId: String,
            @Query("limit") limit: Int,
            @Query("beforeId") beforeId: String? = null
    ): Observable<MutableList<MessageResponse>>

}