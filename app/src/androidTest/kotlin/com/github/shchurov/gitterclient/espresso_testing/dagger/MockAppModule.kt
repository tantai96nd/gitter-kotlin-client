package com.github.shchurov.gitterclient.espresso_testing.dagger

import com.github.shchurov.gitterclient.data.Preferences
import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.network.api.GitterApi
import dagger.Module
import dagger.Provides
import org.mockito.Mockito
import javax.inject.Singleton

@Module
class MockAppModule() {

    @Provides
    @Singleton
    fun provideSharedPrefsManager(): Preferences {
        return Mockito.mock(Preferences::class.java)
    }

    @Provides
    @Singleton
    fun provideGitterApi(preferences: Preferences): GitterApi {
        return Mockito.mock(GitterApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(): Database {
        return Mockito.mock(Database::class.java)
    }

}