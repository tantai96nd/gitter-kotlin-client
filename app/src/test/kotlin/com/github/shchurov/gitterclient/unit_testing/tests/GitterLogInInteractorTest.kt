package com.github.shchurov.gitterclient.unit_testing.tests

import com.github.shchurov.gitterclient.data.Preferences
import com.github.shchurov.gitterclient.data.network.api.GitterApi
import com.github.shchurov.gitterclient.domain.interactors.implementation.GitterLogInInteractorImpl
import com.github.shchurov.gitterclient.domain.interactors.threading.ImmediateSchedulersProvider
import com.github.shchurov.gitterclient.domain.models.Token
import com.github.shchurov.gitterclient.domain.models.User
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.runners.MockitoJUnitRunner
import rx.Observable
import rx.observers.TestSubscriber

@RunWith(MockitoJUnitRunner::class)
class GitterLogInInteractorTest {

    companion object {
        private const val ACCESS_TOKEN = "random_token123"
        private const val USER_ID = "random_user_id123"
    }

    @Mock private lateinit var gitterApi: GitterApi
    @Mock private lateinit var preferences: Preferences
    private val schedulersProvider = ImmediateSchedulersProvider()
    private lateinit var interactor: GitterLogInInteractorImpl

    @Before
    fun setUp() {
        setupMocks()
        interactor = GitterLogInInteractorImpl(gitterApi, preferences, schedulersProvider)
    }

    private fun setupMocks() {
        mockGitterApi()
    }

    private fun mockGitterApi() {
        `when`(gitterApi.getAccessToken(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(Token(ACCESS_TOKEN)))
        `when`(gitterApi.getUser())
                .thenReturn(Observable.just(User(USER_ID, "", "")))
    }

    @Test
    fun logIn() {
        val subscriber = TestSubscriber<User>()
        interactor.logIn("random_code").subscribe(subscriber)
        subscriber.assertNoErrors()
        verify(preferences, times(1)).setGitterAccessToken(ACCESS_TOKEN)
        verify(preferences, times(1)).setUserId(USER_ID)
    }

}