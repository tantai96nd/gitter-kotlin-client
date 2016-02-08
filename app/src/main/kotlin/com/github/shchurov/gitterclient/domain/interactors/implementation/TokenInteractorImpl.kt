package com.github.shchurov.gitterclient.domain.interactors.implementation

import com.github.shchurov.gitterclient.App
import com.github.shchurov.gitterclient.data.Secrets
import com.github.shchurov.gitterclient.data.network.GitterApi
import com.github.shchurov.gitterclient.domain.interactors.TokenInteractor
import com.github.shchurov.gitterclient.domain.models.Token
import rx.Observable
import javax.inject.Inject

class TokenInteractorImpl() : TokenInteractor {

    companion object {
        const val AUTHENTICATION_ENDPOINT = "https://gitter.im/login/oauth/token"
        const val GRANT_TYPE = "authorization_code"
    }

    @Inject
    lateinit var gitterApi: GitterApi

    init {
        App.appComponent.inject(this)
    }

    override fun getAccessToken(code: String): Observable<Token> {
        return gitterApi.getAccessToken(AUTHENTICATION_ENDPOINT, Secrets.gitterOauthKey, Secrets.gitterOauthSecret,
                code, Secrets.gitterRedirectUri, GRANT_TYPE)
    }

}