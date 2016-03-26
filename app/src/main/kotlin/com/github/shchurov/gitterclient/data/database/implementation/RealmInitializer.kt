package com.github.shchurov.gitterclient.data.database.implementation

import com.github.shchurov.gitterclient.App
import io.realm.Realm
import io.realm.RealmConfiguration

open class RealmInitializer {

    private val REALM_SCHEMA_VERSION = 2L

    open fun initRealm() {
        val config = RealmConfiguration.Builder(App.context)
                .schemaVersion(REALM_SCHEMA_VERSION)
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(config)
    }

}