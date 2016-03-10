package com.github.shchurov.gitterclient.espresso_testing.dagger

import com.github.shchurov.gitterclient.dagger.modules.AppModule
import com.github.shchurov.gitterclient.data.Preferences
import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.network.api.GitterApi
import com.github.shchurov.gitterclient.domain.interactors.threading.SchedulersProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface MockAppComponent {

    fun database(): Database

    fun preferences(): Preferences

    fun gitterApi(): GitterApi

    fun schedulersProvider(): SchedulersProvider

}