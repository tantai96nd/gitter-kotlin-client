package com.github.shchurov.gitterclient.domain.interactors

import com.github.shchurov.gitterclient.dagger.scopes.PerScreen
import com.github.shchurov.gitterclient.data.Preferences
import com.github.shchurov.gitterclient.data.Secrets
import com.github.shchurov.gitterclient.data.network.api.GitterApi
import com.github.shchurov.gitterclient.domain.interactors.threading.SchedulersProvider
import com.github.shchurov.gitterclient.domain.models.User
import rx.Observable
import javax.inject.Inject

@PerScreen
class GitterLogInInteractor @Inject constructor(
        private val gitterApi: GitterApi,
        private val preferences: Preferences,
        private val schedulersProvider: SchedulersProvider
) {

    companion object {
        const val AUTHENTICATION_ENDPOINT = "https://gitter.im/login/oauth/token"
        const val GRANT_TYPE = "authorization_code"
    }

    fun logIn(code: String): Observable<User> {
        return gitterApi.getAccessToken(AUTHENTICATION_ENDPOINT, Secrets.gitterOauthKey, Secrets.gitterOauthSecret,
                code, Secrets.gitterRedirectUri, GRANT_TYPE)
                .subscribeOn(schedulersProvider.backgroundScheduler)
                .flatMap { token ->
                    preferences.setGitterAccessToken(token.accessToken)
                    gitterApi.getUser()
                }
                .doOnNext { user ->
                    preferences.setUserId(user.id)
                }
                .observeOn(schedulersProvider.uiScheduler)
    }

}