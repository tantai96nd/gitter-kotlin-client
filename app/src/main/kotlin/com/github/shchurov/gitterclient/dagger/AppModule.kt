package com.github.shchurov.gitterclient.dagger

import com.github.shchurov.gitterclient.App
import com.github.shchurov.gitterclient.data.SharedPreferencesManager
import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.database.DatabaseConverter
import com.github.shchurov.gitterclient.data.database.implementation.DatabaseConverterImpl
import com.github.shchurov.gitterclient.data.database.implementation.DatabaseImpl
import com.github.shchurov.gitterclient.data.network.GitterApi
import com.github.shchurov.gitterclient.data.network.NetworkConverter
import com.github.shchurov.gitterclient.data.network.implementation.GitterApiImpl
import com.github.shchurov.gitterclient.data.network.implementation.NetworkConverterImpl
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
    fun provideSharedPrefsManager(): SharedPreferencesManager {
        return SharedPreferencesManager(App.context)
    }

    @Provides
    @Singleton
    fun provideNetworkConverter(): NetworkConverter {
        return NetworkConverterImpl()
    }

    @Provides
    @Singleton
    fun provideGitterApi(prefsManager: SharedPreferencesManager, networkConverter: NetworkConverter): GitterApi {
        val gitterService = RetrofitInitializer.initGitterService(jsonConverterFactory, prefsManager)
        return GitterApiImpl(gitterService, networkConverter)
    }

    @Provides
    @Singleton
    fun provideDatabaseConverter(): DatabaseConverter {
        return DatabaseConverterImpl()
    }

    @Provides
    @Singleton
    fun provideDatabase(converter: DatabaseConverter): Database {
        return DatabaseImpl(converter)
    }

}