package com.github.shchurov.gitterclient.presentation.presenters.implementations

import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import com.github.shchurov.gitterclient.R
import com.github.shchurov.gitterclient.data.Preferences
import com.github.shchurov.gitterclient.data.Secrets
import com.github.shchurov.gitterclient.data.network.implementation.helpers.RequestSubscriber
import com.github.shchurov.gitterclient.domain.interactors.GitterLogInInteractor
import com.github.shchurov.gitterclient.domain.models.User
import com.github.shchurov.gitterclient.presentation.presenters.LogInPresenter
import com.github.shchurov.gitterclient.presentation.ui.LogInView
import com.github.shchurov.gitterclient.presentation.ui.activities.RoomsListActivity
import com.github.shchurov.gitterclient.utils.compositeSubscribe
import com.github.shchurov.gitterclient.utils.showToast
import rx.subscriptions.CompositeSubscription

class LogInPresenterImpl(
        private val view: LogInView,
        private val preferences: Preferences,
        private val gitterLogInInteractor: GitterLogInInteractor
) : LogInPresenter {

    companion object {
        const val AUTHORIZATION_ENDPOINT = "https://gitter.im/login/oauth/authorize"
        const val KEY_CLIENT_ID = "client_id"
        const val KEY_RESPONSE_TYPE = "response_type"
        const val RESPONSE_TYPE = "code"
        const val KEY_REDIRECT_URI = "redirect_uri"
        const val KEY_CODE = "code"
        const val KEY_ERROR = "error"
        const val ERROR_ACCESS_DENIED = "access_denied"
        @Suppress("ConvertToStringTemplate")
        val AUTH_REQUEST = "$AUTHORIZATION_ENDPOINT" +
                "?$KEY_CLIENT_ID=${Secrets.gitterOauthKey}" +
                "&$KEY_RESPONSE_TYPE=$RESPONSE_TYPE" +
                "&$KEY_REDIRECT_URI=${Secrets.gitterRedirectUri}"
    }

    val subscriptions: CompositeSubscription = CompositeSubscription()

    override fun onCreate() {
        if (preferences.gitterAccessToken == null) {
            view.setWebViewClient(webViewClient)
            view.loadUrl(AUTH_REQUEST)
        } else {
            startRoomsActivity()
        }
    }

    private fun startRoomsActivity() {
        RoomsListActivity.start(view.getActivity())
        view.getActivity().finish()
    }

    private val webViewClient = object : WebViewClient() {
        override fun onPageFinished(webView: WebView?, url: String?) {
            view.showWebView()
        }

        override fun shouldOverrideUrlLoading(webView: WebView?, url: String?): Boolean {
            if (isUrlShouldBeHandled(url)) {
                webView?.clearHistory()
                handleUrl(url!!)
                return true
            } else {
                return false
            }
        }

        private fun isUrlShouldBeHandled(url: String?) = url != null && url.startsWith(Secrets.gitterRedirectUri)

        private fun handleUrl(url: String) {
            val uri = Uri.parse(url);
            val queryKeys = uri.queryParameterNames
            when {
                KEY_ERROR in queryKeys -> handleAuthError(uri.getQueryParameter(KEY_ERROR))
                KEY_CODE in queryKeys -> logIn(uri.getQueryParameter(KEY_CODE))
            }
        }

        override fun onReceivedError(view: WebView?, errorCode: Int, description: String?,
                failingUrl: String?) {
            //TODO:
        }
    }

    private fun handleAuthError(error: String) {
        when (error) {
            ERROR_ACCESS_DENIED -> showToast(R.string.error_access_denied)
            else -> showToast(R.string.unexpected_error)
        }
    }

    private fun logIn(code: String) {
        gitterLogInInteractor.logIn(code)
                .compositeSubscribe(subscriptions, object : RequestSubscriber<User>() {
                    override fun onSuccess(data: User) {
                        startRoomsActivity()
                    }

                    override fun onFailure(e: Throwable) {
                        //TODO:
                    }
                })
    }

    override fun onDestroy() {
        subscriptions.clear()
    }

}