package com.github.shchurov.gitterclient.dagger.modules

import com.github.shchurov.gitterclient.App
import com.github.shchurov.gitterclient.data.Preferences
import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.database.implementation.DatabaseImpl
import com.github.shchurov.gitterclient.data.network.GitterApi
import com.github.shchurov.gitterclient.data.network.implementation.GitterApiImpl
import com.github.shchurov.gitterclient.data.network.implementation.RetrofitInitializer
import com.github.shchurov.gitterclient.data.network.implementation.helpers.NetworkErrorHandler
import dagger.Module
import dagger.Provides
import retrofit.Converter
import retrofit.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule() {

    private val jsonConverterFactory: Converter.Factory

    init {
        jsonConverterFactory = GsonConverterFactory.create()
        NetworkErrorHandler.setConverterFactory(jsonConverterFactory)
    }

    @Provides
    @Singleton
    fun provideSharedPrefsManager(): Preferences {
        return Preferences(App.Companion.context)
    }

    @Provides
    @Singleton
    fun provideGitterApi(preferences: Preferences): GitterApi {
        val gitterService = RetrofitInitializer.initGitterService(jsonConverterFactory, preferences)
        return GitterApiImpl(gitterService, preferences)
    }

    @Provides
    @Singleton
    fun provideDatabase(): Database {
        return DatabaseImpl()
    }

}