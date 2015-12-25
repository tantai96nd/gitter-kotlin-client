package com.github.shchurov.gitterclient.presenters.implementations

import android.net.Uri
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.github.shchurov.gitterclient.network.GitterApi
import com.github.shchurov.gitterclient.network.RequestSubscriber
import com.github.shchurov.gitterclient.network.responses.TokenResponse
import com.github.shchurov.gitterclient.presenters.interfaces.LogInPresenter
import com.github.shchurov.gitterclient.helpers.Secrets
import com.github.shchurov.gitterclient.helpers.SharedPreferencesManager
import com.github.shchurov.gitterclient.utils.subscribeWithSchedulers
import com.github.shchurov.gitterclient.views.interfaces.LogInView
import rx.subscriptions.CompositeSubscription

class LogInPresenterImpl(val view: LogInView) : LogInPresenter {

    companion object {
        val AUTH_ENDPOINT = "https://gitter.im/login/oauth/authorize"
        val KEY_CLIENT_ID = "client_id"
        val CLIENT_ID = Secrets.gitterOauthKey
        val KEY_RESPONSE_TYPE = "response_type"
        val RESPONSE_TYPE = "code"
        val KEY_REDIRECT_URI = "redirect_uri"
        val REDIRECT_URI = Secrets.gitterRedirectUri
        val GRANT_TYPE = "authorization_code"
        val KEY_CODE = "code"
        val KEY_ERROR = "error"
        val AUTH_REQUEST = "$AUTH_ENDPOINT" +
                "?$KEY_CLIENT_ID=$CLIENT_ID" +
                "&$KEY_RESPONSE_TYPE=$RESPONSE_TYPE" +
                "&$KEY_REDIRECT_URI=$REDIRECT_URI"
    }

    val subscriptions: CompositeSubscription = CompositeSubscription()

    override fun onCreate() {
        Log.d("OLOLO", "TOKEN: " + SharedPreferencesManager.gitterAccessToken)
        view.setWebViewClient(webViewClient)
        view.loadUrl(AUTH_REQUEST)
    }

    private val webViewClient = object : WebViewClient() {
        override fun onPageFinished(webView: WebView?, url: String?) {
            view.showWebView()
        }

        override fun shouldOverrideUrlLoading(webView: WebView?, url: String?): Boolean {
            if (url == null || !url.startsWith(Secrets.gitterRedirectUri))
                return false;
            webView!!.clearHistory()
            val uri = Uri.parse(url);
            val queryKeys = uri.queryParameterNames
            when {
                KEY_ERROR in queryKeys -> view.loadUrl(AUTH_REQUEST)
                KEY_CODE in queryKeys -> getAccessToken(uri)
            }
            return true
        }
    }

    private fun getAccessToken(uri: Uri) {
        val code = uri.getQueryParameter(KEY_CODE)
        GitterApi.gitterService.getAccessToken(CLIENT_ID,
                Secrets.gitterOauthSecret, code, REDIRECT_URI, GRANT_TYPE)
                .subscribeWithSchedulers(object : RequestSubscriber<TokenResponse>() {
                    override fun onSuccess(response: TokenResponse) {
                        SharedPreferencesManager.gitterAccessToken = response.accessToken
                    }

                    override fun onFailure(e: Throwable) {
                        super.onFailure(e)
                    }

                    override fun onFinish() {
                        super.onFinish()
                    }
                })
    }

    override fun onDestroy() {
        subscriptions.clear()
    }

}