package com.github.shchurov.gitterclient.data.network.implementation

import com.github.shchurov.gitterclient.data.SharedPreferencesManager
import com.squareup.okhttp.Interceptor
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.logging.HttpLoggingInterceptor
import retrofit.Converter
import retrofit.Retrofit
import retrofit.RxJavaCallAdapterFactory

class RetrofitManager(private val converterFactory: Converter.Factory,
        private val prefsManager: SharedPreferencesManager) {

    private val BASE_URL = "https://api.gitter.im/"
    private val KEY_AUTH_HEADER = "Authorization"

    private val retrofit: Retrofit
    val gitterService: GitterRetrofitService

    init {
        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(converterFactory)
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
            val token = prefsManager.gitterAccessToken
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
