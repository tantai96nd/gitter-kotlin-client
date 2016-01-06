package com.github.shchurov.gitterclient.views.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import com.github.shchurov.gitterclient.presenters.implementations.LogInPresenterImpl
import com.github.shchurov.gitterclient.presenters.interfaces.LogInPresenter
import com.github.shchurov.gitterclient.views.interfaces.LogInView

class LogInActivity : AppCompatActivity(), LogInView {

    companion object {
        val PAGE_RENDER_TIME = 300L
    }

    private lateinit var presenter: LogInPresenter
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView = WebView(this)
        webView.alpha = 0f;
        setContentView(webView)
        presenter = LogInPresenterImpl(this)
        presenter.onCreate()
    }

    override fun setWebViewClient(webViewClient: WebViewClient) {
        webView.setWebViewClient(webViewClient)
    }

    override fun loadUrl(url: String) {
        webView.loadUrl(url)
    }

    override fun showWebView() {
        webView.animate()
                .alpha(1f)
                .setStartDelay(PAGE_RENDER_TIME)
                .setDuration(300)
    }

    override fun getActivity() = this

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK
                && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}