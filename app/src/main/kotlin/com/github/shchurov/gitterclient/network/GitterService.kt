package com.github.shchurov.gitterclient.network

import com.github.shchurov.gitterclient.network.responses.TokenResponse
import retrofit.http.Field
import retrofit.http.FormUrlEncoded
import retrofit.http.POST
import rx.Observable

interface GitterService {

    @POST("login/oauth/token")
    @FormUrlEncoded
    fun getAccessToken(
            @Field("client_id") clientId: String,
            @Field("client_secret") clientSecret: String,
            @Field("code") code: String,
            @Field("redirect_uri") redirectUri: String,
            @Field("grant_type") grantType: String
    ): Observable<TokenResponse>

}