package com.github.shchurov.gitterclient.unit_tests.helpers

import org.mockito.Mockito

fun <T> eq(t: T): T {
    return Mockito.eq<T>(t)
}