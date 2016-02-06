package com.github.shchurov.gitterclient.presentation.presenters.implementations

import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import com.github.shchurov.gitterclient.R
import com.github.shchurov.gitterclient.data.Secrets
import com.github.shchurov.gitterclient.data.SharedPreferencesManager
import com.github.shchurov.gitterclient.data.network.retrofit.RetrofitManager
import com.github.shchurov.gitterclient.data.network.RequestSubscriber
import com.github.shchurov.gitterclient.data.network.responses.TokenResponse
import com.github.shchurov.gitterclient.presentation.presenters.LogInPresenter
import com.github.shchurov.gitterclient.utils.compositeSubscribeWithSchedulers
import com.github.shchurov.gitterclient.utils.showToast
import com.github.shchurov.gitterclient.presentation.ui.activities.RoomsListActivity
import com.github.shchurov.gitterclient.presentation.ui.LogInView
import rx.subscriptions.CompositeSubscription

class LogInPresenterImpl(val view: LogInView) : LogInPresenter {

    companion object {
        const val AUTHORIZATION_ENDPOINT = "https://gitter.im/login/oauth/authorize"
        const val AUTHENTICATION_ENDPOINT = "https://gitter.im/login/oauth/token"
        const val KEY_CLIENT_ID = "client_id"
        val CLIENT_ID = Secrets.gitterOauthKey
        const val KEY_RESPONSE_TYPE = "response_type"
        const val RESPONSE_TYPE = "code"
        const val KEY_REDIRECT_URI = "redirect_uri"
        val REDIRECT_URI = Secrets.gitterRedirectUri
        const val GRANT_TYPE = "authorization_code"
        const val KEY_CODE = "code"
        const val KEY_ERROR = "error"
        const val ERROR_ACCESS_DENIED = "access_denied"
        val AUTH_REQUEST = "${com.github.shchurov.gitterclient.presentation.LogInPresenterImpl.Companion.AUTHORIZATION_ENDPOINT}" +
                "?${com.github.shchurov.gitterclient.presentation.LogInPresenterImpl.Companion.KEY_CLIENT_ID}=${com.github.shchurov.gitterclient.presentation.LogInPresenterImpl.Companion.CLIENT_ID}" +
                "&${com.github.shchurov.gitterclient.presentation.LogInPresenterImpl.Companion.KEY_RESPONSE_TYPE}=${com.github.shchurov.gitterclient.presentation.LogInPresenterImpl.Companion.RESPONSE_TYPE}" +
                "&${com.github.shchurov.gitterclient.presentation.LogInPresenterImpl.Companion.KEY_REDIRECT_URI}=${com.github.shchurov.gitterclient.presentation.LogInPresenterImpl.Companion.REDIRECT_URI}"
    }

    val subscriptions: CompositeSubscription = CompositeSubscription()

    override fun onCreate() {
        if (SharedPreferencesManager.gitterAccessToken == null) {
            view.setWebViewClient(webViewClient)
            view.loadUrl(com.github.shchurov.gitterclient.presentation.LogInPresenterImpl.Companion.AUTH_REQUEST)
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
            if (url == null || !url.startsWith(Secrets.gitterRedirectUri))
                return false;
            webView?.clearHistory()
            val uri = Uri.parse(url);
            val queryKeys = uri.queryParameterNames
            when {
                com.github.shchurov.gitterclient.presentation.LogInPresenterImpl.Companion.KEY_ERROR in queryKeys -> handleAuthError(uri)
                com.github.shchurov.gitterclient.presentation.LogInPresenterImpl.Companion.KEY_CODE in queryKeys -> getAccessToken(uri)
            }
            return true
        }

        override fun onReceivedError(view: WebView?, errorCode: Int, description: String?,
                failingUrl: String?) {
            //TODO:
        }
    }

    private fun handleAuthError(uri: Uri) {
        val error = uri.getQueryParameter(com.github.shchurov.gitterclient.presentation.LogInPresenterImpl.Companion.KEY_ERROR)
        when (error) {
            com.github.shchurov.gitterclient.presentation.LogInPresenterImpl.Companion.ERROR_ACCESS_DENIED -> showToast(R.string.error_access_denied)
            else -> showToast(R.string.unexpected_error)
        }
    }

    private fun getAccessToken(uri: Uri) {
        val code = uri.getQueryParameter(com.github.shchurov.gitterclient.presentation.LogInPresenterImpl.Companion.KEY_CODE)
        RetrofitManager.gitterService.getAccessToken(com.github.shchurov.gitterclient.presentation.LogInPresenterImpl.Companion.AUTHENTICATION_ENDPOINT, com.github.shchurov.gitterclient.presentation.LogInPresenterImpl.Companion.CLIENT_ID,
                Secrets.gitterOauthSecret, code, com.github.shchurov.gitterclient.presentation.LogInPresenterImpl.Companion.REDIRECT_URI, com.github.shchurov.gitterclient.presentation.LogInPresenterImpl.Companion.GRANT_TYPE)
                .compositeSubscribeWithSchedulers(subscriptions, object :
                        RequestSubscriber<TokenResponse>() {
                    override fun onSuccess(response: TokenResponse) {
                        SharedPreferencesManager.gitterAccessToken = response.accessToken
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