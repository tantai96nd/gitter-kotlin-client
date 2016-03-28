package com.github.shchurov.gitterclient.dagger.modules

import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.database.implementation.DatabaseImpl
import com.github.shchurov.gitterclient.data.database.implementation.RealmInitializer
import com.github.shchurov.gitterclient.data.network.api.GitterApi
import com.github.shchurov.gitterclient.data.network.api.implementation.GitterApiImpl
import com.github.shchurov.gitterclient.data.network.api.implementation.retrofit.RetrofitInitializer
import com.github.shchurov.gitterclient.data.preferences.Preferences
import com.github.shchurov.gitterclient.data.preferences.implementation.PreferencesImpl
import com.github.shchurov.gitterclient.domain.interactors.threading.SchedulersProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class AppModule() {

    @Provides
    @Singleton
    open fun providePreferences(): Preferences {
        return PreferencesImpl("gitter_preferences")
    }

    @Provides
    @Singleton
    open fun provideGitterApi(preferences: Preferences): GitterApi {
        val gitterService = RetrofitInitializer.initGitterService(preferences)
        return GitterApiImpl(gitterService, preferences)
    }

    @Provides
    @Singleton
    fun provideRealmInitializer(): RealmInitializer {
        return RealmInitializer()
    }

    @Provides
    @Singleton
    open fun provideDatabase(realmInitializer: RealmInitializer): Database {
        return DatabaseImpl(realmInitializer)
    }

    @Provides
    @Singleton
    fun provideSchedulersProvider(): SchedulersProvider {
        return SchedulersProvider()
    }

}