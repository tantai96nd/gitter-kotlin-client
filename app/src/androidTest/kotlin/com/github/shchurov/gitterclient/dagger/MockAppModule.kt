package com.github.shchurov.gitterclient.dagger

import com.github.shchurov.gitterclient.data.Preferences
import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.network.api.GitterApi
import com.github.shchurov.gitterclient.domain.interactors.threading.ImmediateSchedulersProvider
import com.github.shchurov.gitterclient.domain.interactors.threading.SchedulersProvider
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
    fun provideGitterApi(): GitterApi {
        return Mockito.mock(GitterApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(): Database {
        return Mockito.mock(Database::class.java)
    }

    @Provides
    @Singleton
    fun provideSchedulersProvider(): SchedulersProvider {
        return ImmediateSchedulersProvider()
    }

}