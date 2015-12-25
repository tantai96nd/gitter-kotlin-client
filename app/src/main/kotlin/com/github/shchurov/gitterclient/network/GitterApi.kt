package com.github.shchurov.gitterclient.network

import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.logging.HttpLoggingInterceptor
import retrofit.GsonConverterFactory
import retrofit.Retrofit
import retrofit.RxJavaCallAdapterFactory
import java.lang.reflect.Type

object GitterApi {

    private val BASE_URL = "https://gitter.im/"

    val retrofit: Retrofit
    val gitterService: GitterService

    init {
        val okHttpClient = OkHttpClient()
        setupLogging(okHttpClient)
        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        gitterService = retrofit.create(GitterService::class.java)
    }

    fun <T> getConverter(clazz: Class<T>) = retrofit.responseConverter<T>(clazz, arrayOfNulls(1))

    private fun setupLogging(client: OkHttpClient) {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor)
    }

}
