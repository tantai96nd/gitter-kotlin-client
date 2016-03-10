package com.github.shchurov.gitterclient.data.network.api.implementation.retrofit

import com.github.shchurov.gitterclient.data.network.model.MessageResponse
import com.github.shchurov.gitterclient.data.network.model.RoomResponse
import com.github.shchurov.gitterclient.data.network.model.TokenResponse
import com.github.shchurov.gitterclient.data.network.model.UserResponse
import retrofit.http.*
import rx.Observable

interface GitterRetrofitService {

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

    @POST("/v1/user/{userId}/rooms/{roomId}/unreadItems")
    @FormUrlEncoded
    fun markMessagesAsRead(
            @Path("roomId") roomId: String?,
            @Path("userId") userId: String,
            @Field("chat") messageIds: List<String>
    ): Observable<Any>

    @GET("/v1/user")
    fun getUser(): Observable<List<UserResponse>>

}