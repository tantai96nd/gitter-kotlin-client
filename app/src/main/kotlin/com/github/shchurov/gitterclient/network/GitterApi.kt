package com.github.shchurov.gitterclient.network

import retrofit.GsonConverterFactory
import retrofit.Retrofit
import retrofit.RxJavaCallAdapterFactory

class GitterApi {

    companion object {
        val BASE_URL = "https://gitter.im/"
        val instance: GitterApi by lazy {
            GitterApi()
        }
    }

    lateinit var gitterService: GitterService

    init {
        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        gitterService = retrofit.create(GitterService::class.java)
    }

}
