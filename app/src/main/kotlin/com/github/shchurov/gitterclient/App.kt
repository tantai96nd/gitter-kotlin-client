package com.github.shchurov.gitterclient

import android.app.Application
import android.content.Context
import com.github.shchurov.gitterclient.dagger.AppComponent
import com.github.shchurov.gitterclient.dagger.DaggerAppComponent
import com.github.shchurov.gitterclient.data.database.implementation.RealmInitializer

class App : Application() {

    companion object {
        @JvmStatic lateinit var appComponent: AppComponent
        lateinit var context: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        initRealm()
        initDagger()
    }

    private fun initRealm() {
        RealmInitializer.initRealm(this)
    }

    private fun initDagger() {
        appComponent = DaggerAppComponent.create()
    }

}