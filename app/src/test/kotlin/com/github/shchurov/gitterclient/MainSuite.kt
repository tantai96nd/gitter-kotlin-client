package com.github.shchurov.gitterclient

import com.github.shchurov.gitterclient.tests.*
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(GetRoomMessagesInteractorTest::class, GetRoomsInteractorTest::class,
        GitterLogInInteractorTest::class, MarkMessageAsReadInteractorTest::class,
        UpdateRoomLastAccessTimeInteractorTest::class)
class MainSuite