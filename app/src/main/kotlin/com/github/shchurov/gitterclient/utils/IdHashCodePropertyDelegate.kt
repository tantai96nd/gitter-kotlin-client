package com.github.shchurov.gitterclient.utils

import kotlin.reflect.KProperty

class IdHashCodePropertyDelegate(val id: String) {

    private var hash: Long = 0

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Long {
        if (hash == 0L) {
            for (i in 0..id.length - 1) {
                hash = 61 * hash + id[i].toLong()
            }
        }
        return hash
    }

}