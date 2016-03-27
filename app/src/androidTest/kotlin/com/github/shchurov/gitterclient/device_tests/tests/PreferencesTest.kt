package com.github.shchurov.gitterclient.device_tests.tests

import android.support.test.runner.AndroidJUnit4
import com.github.shchurov.gitterclient.App
import com.github.shchurov.gitterclient.data.preferences.implementation.PreferencesImpl
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PreferencesTest {

    companion object {
        private const val PREFS_NAME = "test_preferences"
    }

    private val preferences = PreferencesImpl(PREFS_NAME)

    @After
    fun tearDown() {
        App.context.getSharedPreferences(PREFS_NAME, 0)
                .edit()
                .clear()
                .commit()
    }

    @Test
    fun setAndGetGitterAccessToken() {
        val token = "123456qwerty"
        preferences.setGitterAccessToken(token)
        val retrievedToken = preferences.getGitterAccessToken()

        assertEquals(token, retrievedToken)
    }

    @Test
    fun setAndGetUserId() {
        val id = "asdfgh09876"
        preferences.setUserId(id)
        val retrievedId = preferences.getUserId()

        assertEquals(id, retrievedId)
    }

}