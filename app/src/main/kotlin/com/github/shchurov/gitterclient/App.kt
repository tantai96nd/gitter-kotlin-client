package com.github.shchurov.gitterclient

import android.app.Application
import android.content.Context
import com.github.shchurov.gitterclient.data.database.implementation.RealmManager

class App : Application() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        RealmManager.initRealm(this)
    }

}