package com.github.shchurov.gitterclient.data.network.implementation

import com.github.shchurov.gitterclient.data.Preferences
import com.squareup.okhttp.Interceptor
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.logging.HttpLoggingInterceptor
import retrofit.GsonConverterFactory
import retrofit.Retrofit
import retrofit.RxJavaCallAdapterFactory

object RetrofitInitializer {

    private const val BASE_URL = "https://api.gitter.im/"
    private const val KEY_AUTH_HEADER = "Authorization"

    val converterFactory = GsonConverterFactory.create()

    fun initGitterService(preferences: Preferences)
            : GitterRetrofitService {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(converterFactory)
                .client(createClient(preferences))
                .build()
        return retrofit.create(GitterRetrofitService::class.java)
    }

    private fun createClient(preferences: Preferences): OkHttpClient {
        val okHttpClient = OkHttpClient()
        setupAuth(okHttpClient, preferences)
        setupLogging(okHttpClient)
        return okHttpClient
    }

    private fun setupLogging(client: OkHttpClient) {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor)
    }

    private fun setupAuth(client: OkHttpClient, preferences: Preferences) {
        val interceptor = Interceptor { chain ->
            val original = chain.request();
            val token = preferences.getGitterAccessToken()
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
