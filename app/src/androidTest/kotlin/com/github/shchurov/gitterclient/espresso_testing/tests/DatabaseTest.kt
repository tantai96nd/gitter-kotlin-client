package com.github.shchurov.gitterclient.espresso_testing.tests

import android.support.test.runner.AndroidJUnit4
import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.database.implementation.DatabaseImpl
import com.github.shchurov.gitterclient.data.database.implementation.RealmInitializer
import io.realm.Realm
import io.realm.RealmConfiguration
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    @Rule
    val tmpFolder = TemporaryFolder()
    private lateinit var database: Database;

    @Before
    fun setUp() {
        val realmInitializer = createRealmInitializer()
        database = DatabaseImpl(realmInitializer)
    }

    private fun createRealmInitializer() = object : RealmInitializer() {
        override fun initRealm() {
            val config = RealmConfiguration.Builder(tmpFolder.newFolder("realm_tmp"))
                    .build()
            Realm.setDefaultConfiguration(config)
        }
    }

    @Test
    fun testGetRooms() {

    }

}