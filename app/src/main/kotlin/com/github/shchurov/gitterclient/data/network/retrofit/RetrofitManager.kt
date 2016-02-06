package com.github.shchurov.gitterclient.data.network.retrofit

import com.github.shchurov.gitterclient.data.SharedPreferencesManager
import com.github.shchurov.gitterclient.data.network.retrofit.GitterRetrofitService
import com.squareup.okhttp.Interceptor
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.logging.HttpLoggingInterceptor
import retrofit.GsonConverterFactory
import retrofit.Retrofit
import retrofit.RxJavaCallAdapterFactory

object RetrofitManager {

    private val BASE_URL = "https://api.gitter.im/"
    private val KEY_AUTH_HEADER = "Authorization"

    private val retrofit: Retrofit
    val gitterService: GitterRetrofitService

    init {
        retrofit = retrofit.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(retrofit.RxJavaCallAdapterFactory.create())
                .addConverterFactory(retrofit.GsonConverterFactory.create())
                .client(createClient())
                .build()
        gitterService = retrofit.create(GitterRetrofitService::class.java)
    }

    private fun createClient(): OkHttpClient {
        val okHttpClient = OkHttpClient()
        setupAuth(okHttpClient)
        setupLogging(okHttpClient)
        return okHttpClient
    }

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

    fun <T> getConverter(clazz: Class<T>) = retrofit.responseConverter<T>(clazz, arrayOfNulls(1))

}
