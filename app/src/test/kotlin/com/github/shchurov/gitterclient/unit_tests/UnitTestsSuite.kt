package com.github.shchurov.gitterclient.unit_tests

import com.github.shchurov.gitterclient.unit_tests.tests.*
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
        GetRoomMessagesInteractorTest::class,
        GetRoomsInteractorTest::class,
        AuthInteractorTest::class,
        MarkMessageAsReadInteractorTest::class,
        DatabaseConverterTest::class,
        NetworkConverterTest::class)
class UnitTestsSuite