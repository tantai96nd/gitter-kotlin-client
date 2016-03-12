package com.github.shchurov.gitterclient

import android.app.Application
import android.content.Context
import com.github.shchurov.gitterclient.dagger.components.AppComponent
import com.github.shchurov.gitterclient.dagger.components.DaggerAppComponent

class App : Application() {

    companion object {
        @JvmStatic var appComponent: AppComponent? = null
        lateinit var context: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        initDagger()
    }

    private fun initDagger() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.create()
        }
    }

}