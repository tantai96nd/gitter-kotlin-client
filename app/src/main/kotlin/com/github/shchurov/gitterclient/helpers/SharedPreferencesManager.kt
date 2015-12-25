package com.github.shchurov.gitterclient.helpers

import com.github.shchurov.gitterclient.App
import kotlin.reflect.KProperty

object SharedPreferencesManager {

    private val PREFS_NAME = "gitter_kotlin_client"

    private val GITTER_ACCESS_TOKEN_KEY = "gitter_access_token"
    private val prefs = App.context.getSharedPreferences(PREFS_NAME, 0)

    var gitterAccessToken: String? by PreferenceField(GITTER_ACCESS_TOKEN_KEY, null as String?)

    private class PreferenceField<T>(val key: String, val defaultValue: T?) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            with(prefs) {
                val result: Any? = when (defaultValue) {
                    is String? -> getString(key, defaultValue)
                    is Int -> getInt(key, defaultValue)
                    is Long -> getLong(key, defaultValue)
                    else -> throw UnsupportedOperationException()
                }
                return result as T
            }
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            val editor = prefs.edit()
            with (editor) {
                when (value) {
                    is String -> putString(key, value)
                    is Int -> putInt(key, value)
                    is Long -> putLong(key, value)
                    else -> throw UnsupportedOperationException()
                }
                commit()
            }
        }
    }

}