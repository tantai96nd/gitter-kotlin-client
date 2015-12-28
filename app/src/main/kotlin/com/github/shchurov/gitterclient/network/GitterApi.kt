package com.github.shchurov.gitterclient.network

import com.github.shchurov.gitterclient.helpers.SharedPreferencesManager
import com.squareup.okhttp.Interceptor
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.logging.HttpLoggingInterceptor
import retrofit.GsonConverterFactory
import retrofit.Retrofit
import retrofit.RxJavaCallAdapterFactory

object GitterApi {

    private const val BASE_URL = "https://api.gitter.im/"
    private const val KEY_AUTH_HEADER = "Authorization"

    val retrofit: Retrofit
    val gitterService: GitterService

    init {
        val okHttpClient = OkHttpClient()
        setupAuth(okHttpClient)
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

    private fun setupAuth(client: OkHttpClient) {
        val interceptor = Interceptor { chain ->
            val original = chain.request();
            val token = SharedPreferencesManager.gitterAccessToken
            if (token != null) {
                val modified = original.newBuilder()
                        .header(KEY_AUTH_HEADER, "Bearer $token")
                        .method(original.method(), original.body())
                        .build();
                chain.proceed(modified)
            } else {
                chain.proceed(original);
            }
        }
        client.interceptors().add(interceptor)
    }

}
