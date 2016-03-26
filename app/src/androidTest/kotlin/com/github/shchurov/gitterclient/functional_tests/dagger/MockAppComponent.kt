package com.github.shchurov.gitterclient.functional_tests.dagger

import com.github.shchurov.gitterclient.dagger.components.AppComponent
import com.github.shchurov.gitterclient.dagger.modules.AppModule
import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.network.api.GitterApi
import com.github.shchurov.gitterclient.data.preferences.implementation.PreferencesImpl
import com.github.shchurov.gitterclient.domain.interactors.threading.SchedulersProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface MockAppComponent : AppComponent {

    override fun database(): Database

    override fun preferences(): PreferencesImpl

    override fun gitterApi(): GitterApi

    override fun schedulersProvider(): SchedulersProvider

}