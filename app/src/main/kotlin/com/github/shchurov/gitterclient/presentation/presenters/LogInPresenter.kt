package com.github.shchurov.gitterclient.presentation.presenters

import android.net.Uri
import com.github.shchurov.gitterclient.R
import com.github.shchurov.gitterclient.data.Secrets
import com.github.shchurov.gitterclient.data.subscribers.DefaultSubscriber
import com.github.shchurov.gitterclient.domain.interactors.AuthInteractor
import com.github.shchurov.gitterclient.domain.interactors.CheckAuthInteractor
import com.github.shchurov.gitterclient.domain.models.User
import com.github.shchurov.gitterclient.presentation.navigators.LogInNavigator
import com.github.shchurov.gitterclient.presentation.ui.LogInView
import com.github.shchurov.gitterclient.utils.compositeSubscribe
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class LogInPresenter @Inject constructor(
        private val checkAuthInteractor: CheckAuthInteractor,
        private val authInteractor: AuthInteractor,
        private val navigator: LogInNavigator
) : BasePresenter<LogInView>() {

    companion object {
        const val AUTHORIZATION_ENDPOINT = "https://gitter.im/login/oauth/authorize"
        const val KEY_CLIENT_ID = "client_id"
        const val KEY_RESPONSE_TYPE = "response_type"
        const val RESPONSE_TYPE = "code"
        const val KEY_REDIRECT_URI = "redirect_uri"
        const val KEY_CODE = "code"
        const val KEY_ERROR = "error"
        const val ERROR_ACCESS_DENIED = "access_denied"
        @Suppress("RemoveCurlyBracesFromTemplate")
        val AUTH_URL = "${AUTHORIZATION_ENDPOINT}" +
                "?${KEY_CLIENT_ID}=${Secrets.gitterOauthKey}" +
                "&${KEY_RESPONSE_TYPE}=${RESPONSE_TYPE}" +
                "&${KEY_REDIRECT_URI}=${Secrets.gitterRedirectUrl}"
    }

    val subscriptions: CompositeSubscription = CompositeSubscription()

    override fun onAttach() {
        if (checkAuthInteractor.isAuthorized()) {
            navigator.goToRoomsListScreen()
        } else {
            getView().loadUrl(AUTH_URL)
        }
    }

    override fun onDetach() {
        subscriptions.clear()
    }

    private fun logIn(code: String) {
        authInteractor.logIn(code)
                .compositeSubscribe(subscriptions, object : DefaultSubscriber<User>() {
                    override fun onNext(data: User) {
                        navigator.goToRoomsListScreen()
                    }

                    override fun onFailure(e: Throwable, errorMessage: String) {
                        //TODO:
                    }
                })
    }

    fun onWebViewOverrideLoading(url: String?): Boolean {
        if (url != null && isRedirectUrl(url)) {
            getView().clearWebViewHistory()
            handleRedirectUrl(url)
            return true
        } else {
            return false
        }
    }

    private fun isRedirectUrl(url: String) = url.startsWith(Secrets.gitterRedirectUrl)

    private fun handleRedirectUrl(url: String) {
        val uri = Uri.parse(url);
        val queryKeys = uri.queryParameterNames
        when {
            KEY_ERROR in queryKeys -> handleAuthError(uri.getQueryParameter(KEY_ERROR))
            KEY_CODE in queryKeys -> logIn(uri.getQueryParameter(KEY_CODE))
        }
    }

    private fun handleAuthError(error: String) {
        when (error) {
            ERROR_ACCESS_DENIED -> getView().showError(R.string.error_access_denied)
            else -> getView().showError(R.string.unexpected_error)
        }
    }

    fun onWebViewError() {
        //TODO:
    }


}