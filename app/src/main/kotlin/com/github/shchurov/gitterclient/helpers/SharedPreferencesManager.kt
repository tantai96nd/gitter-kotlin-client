package com.github.shchurov.gitterclient.helpers

import com.github.shchurov.gitterclient.App
import kotlin.reflect.KProperty

object SharedPreferencesManager {

    private const val PREFS_NAME = "gitter_kotlin_client"

    private const val GITTER_ACCESS_TOKEN_KEY = "gitter_access_token"
    private val prefs = App.context.getSharedPreferences(PREFS_NAME, 0)

    var gitterAccessToken: String? by CachedPreferenceField(GITTER_ACCESS_TOKEN_KEY,
            null as String?)

    private open class PreferenceField<T>(val key: String, val defaultValue: T?) {
        operator open fun getValue(thisRef: Any?, property: KProperty<*>): T? {
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

        operator open fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
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

    private class CachedPreferenceField<T>(key: String, defaultValue: T)
            : PreferenceField<T>(key, defaultValue) {

        var cache: T? = null

        override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            if (cache == null) {
                cache = super.getValue(thisRef, property)
            }
            return cache
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            super.setValue(thisRef, property, value)
            cache = value
        }
    }

}