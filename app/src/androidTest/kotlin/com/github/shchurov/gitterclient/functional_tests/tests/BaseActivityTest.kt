package com.github.shchurov.gitterclient.functional_tests.tests

import com.github.shchurov.gitterclient.App
import com.github.shchurov.gitterclient.dagger.DaggerMockAppComponent
import org.junit.Before

open class BaseActivityTest {

    @Before
    fun baseSetUp() {
        App.appComponent = DaggerMockAppComponent.create()
    }

}