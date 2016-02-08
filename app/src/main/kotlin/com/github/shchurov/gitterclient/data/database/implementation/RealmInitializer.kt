package com.github.shchurov.gitterclient.data.database.implementation

import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration

object RealmInitializer {

    private val REALM_SCHEMA_VERSION = 1L

    fun initRealm(context: Context) {
        val config = RealmConfiguration.Builder(context)
                .schemaVersion(REALM_SCHEMA_VERSION)
                .build()
        Realm.setDefaultConfiguration(config)
    }

}