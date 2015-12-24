package com.github.shchurov.gitterclient.activities

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import com.github.shchurov.gitterclient.network.GitterApi
import com.github.shchurov.gitterclient.network.TokenResponse
import com.github.shchurov.gitterclient.utils.getGitterAuthSecret
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class LogInActivity : AppCompatActivity() {

    companion object {
        val AUTH_ENDPOINT_1 = "https://gitter.im/login/oauth/authorize"
        val AUTH_ENDPOINT_2 = "https://gitter.im/login/oauth/token"
        val KEY_CLIENT_ID = "client_id"
        val VAL_CLIENT_ID = "c6682aae249694fd09c3f959179fa480960ea13f"
        val KEY_RESPONSE_TYPE = "response_type"
        val VAL_RESPONSE_TYPE = "code"
        val KEY_REDIRECT_URI = "redirect_uri"
        val VAL_REDIRECT_URI = "https://github.com/shchurov/gitter-client"
        val KEY_SECRET = "client_secret"
        val VAL_SECRET = getGitterAuthSecret()
        val KEY_GRANT_TYPE = "grant_type"
        val VAL_GRANT_TYPE = "authorization_code"
        val KEY_CODE = "code"
        val KEY_ERROR = "error"
        val AUTH_REQUEST_1 = "$AUTH_ENDPOINT_1?$KEY_CLIENT_ID=$VAL_CLIENT_ID&$KEY_RESPONSE_TYPE=" +
                "$VAL_RESPONSE_TYPE&$KEY_REDIRECT_URI=$VAL_REDIRECT_URI"
        val AUTH_REQUEST_2 = "$AUTH_ENDPOINT_2?$KEY_CLIENT_ID=$VAL_CLIENT_ID&$KEY_SECRET=" +
                "$VAL_SECRET&$KEY_REDIRECT_URI=$VAL_REDIRECT_URI&$KEY_GRANT_TYPE=$VAL_GRANT_TYPE"
        val PAGE_RENDER_TIME = 300L
    }

    lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView = WebView(this)
        webView.alpha = 0f;
        setContentView(webView)

        webView.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                webView.animate()
                        .alpha(1f)
                        .setStartDelay(PAGE_RENDER_TIME)
                        .setDuration(300)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null && url.startsWith(VAL_REDIRECT_URI)) {
                    webView.clearHistory()
                    val uri = Uri.parse(url);
                    val queryKeys = uri.queryParameterNames
                    when {
                        KEY_ERROR in queryKeys -> webView.loadUrl(AUTH_REQUEST_1)
                        KEY_CODE in queryKeys -> {
                            val code = uri.getQueryParameter(KEY_CODE)
                            GitterApi.instance.gitterService.getAccessToken(VAL_CLIENT_ID,
                                    getGitterAuthSecret(), code, VAL_REDIRECT_URI, VAL_GRANT_TYPE)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(object : Subscriber<TokenResponse>() {
                                        override fun onNext(t: TokenResponse?) {
                                            Log.d("OLOLO", "TOKEN: " + t?.accessToken)
                                        }

                                        override fun onCompleted() {
                                        }

                                        override fun onError(e: Throwable?) {
                                            throw UnsupportedOperationException()
                                        }
                                    })
                        }
                    }
                }
                return true
            }
        })
        webView.loadUrl(AUTH_REQUEST_1)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK
                && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}