package com.github.shchurov.gitterclient.domain.interactors.implementation

import com.github.shchurov.gitterclient.data.Preferences
import com.github.shchurov.gitterclient.data.Secrets
import com.github.shchurov.gitterclient.data.network.GitterApi
import com.github.shchurov.gitterclient.domain.interactors.GitterLogInInteractor
import com.github.shchurov.gitterclient.domain.interactors.threading.SchedulersProvider
import com.github.shchurov.gitterclient.domain.models.User
import rx.Observable

class GitterLogInInteractorImpl(
        private val gitterApi: GitterApi,
        private val preferences: Preferences,
        private val schedulersProvider: SchedulersProvider
) : GitterLogInInteractor {

    companion object {
        const val AUTHENTICATION_ENDPOINT = "https://gitter.im/login/oauth/token"
        const val GRANT_TYPE = "authorization_code"
    }

    override fun logIn(code: String): Observable<User> {
        return gitterApi.getAccessToken(AUTHENTICATION_ENDPOINT, Secrets.gitterOauthKey, Secrets.gitterOauthSecret,
                code, Secrets.gitterRedirectUri, GRANT_TYPE)
                .subscribeOn(schedulersProvider.backgroundScheduler)
                .flatMap { token ->
                    preferences.gitterAccessToken = token.accessToken
                    gitterApi.getUser()
                }
                .doOnNext { user ->
                    preferences.userId = user.id
                }
                .observeOn(schedulersProvider.uiScheduler)
    }

}