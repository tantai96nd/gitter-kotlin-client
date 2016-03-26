package com.github.shchurov.gitterclient.unit_tests.tests

import com.github.shchurov.gitterclient.data.network.api.GitterApi
import com.github.shchurov.gitterclient.data.preferences.implementation.PreferencesImpl
import com.github.shchurov.gitterclient.domain.interactors.AuthInteractor
import com.github.shchurov.gitterclient.domain.models.Token
import com.github.shchurov.gitterclient.domain.models.User
import com.github.shchurov.gitterclient.unit_tests.helpers.ImmediateSchedulersProvider
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.runners.MockitoJUnitRunner
import rx.Observable
import rx.observers.TestSubscriber

@RunWith(MockitoJUnitRunner::class)
class AuthInteractorTest {

    companion object {
        private const val ACCESS_TOKEN = "access_token"
        private const val USER_ID = "user_id"
    }

    private lateinit var interactor: AuthInteractor
    @Mock private lateinit var gitterApi: GitterApi
    @Mock private lateinit var preferences: PreferencesImpl
    private val schedulersProvider = ImmediateSchedulersProvider()

    @Before
    fun setUp() {
        mockGitterApi()
        interactor = AuthInteractor(gitterApi, preferences, schedulersProvider)
    }

    private fun mockGitterApi() {
        `when`(gitterApi.getAccessToken(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(Token(ACCESS_TOKEN)))
        `when`(gitterApi.getUser())
                .thenReturn(Observable.just(User(USER_ID, "", "")))
    }

    @Test
    fun testLogIn() {
        val subscriber = TestSubscriber<User>()
        val mainThreadName = Thread.currentThread().name
        interactor.logIn("random_code").subscribe(subscriber)

        subscriber.assertNoErrors()
        verify(preferences).setGitterAccessToken(ACCESS_TOKEN)
        verify(preferences).setUserId(USER_ID)
        Assert.assertNotEquals(mainThreadName, subscriber.lastSeenThread.name)
    }

}