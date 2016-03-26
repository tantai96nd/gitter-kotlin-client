package com.github.shchurov.gitterclient.unit_tests.tests

import com.github.shchurov.gitterclient.data.preferences.Preferences
import com.github.shchurov.gitterclient.domain.interactors.CheckAuthInteractor
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CheckAuthInteractorTest {

    private lateinit var interactor: CheckAuthInteractor
    @Mock private lateinit var preferences: Preferences

    @Before
    fun setUp() {
        interactor = CheckAuthInteractor(preferences)
    }

    @Test
    fun testAuthorized() {
        `when`(preferences.getGitterAccessToken())
                .thenReturn("access_token")
        val real = interactor.isAuthorized()

        assertTrue(real)
    }

    @Test
    fun notAuthorized() {
        `when`(preferences.getGitterAccessToken())
                .thenReturn(null)
        val real = interactor.isAuthorized()

        assertFalse(real)
    }

}