package com.github.shchurov.gitterclient.presentation.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import com.github.shchurov.gitterclient.App
import com.github.shchurov.gitterclient.dagger.modules.GeneralScreenModule
import com.github.shchurov.gitterclient.presentation.presenters.LogInPresenter
import com.github.shchurov.gitterclient.presentation.ui.LogInView
import com.github.shchurov.gitterclient.utils.showToast
import javax.inject.Inject

class LogInActivity : AppCompatActivity(), LogInView {

    @Inject lateinit var presenter: LogInPresenter
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDependencies()
        setupUi()
        presenter.attach(this)
    }

    private fun initDependencies() {
        App.component.createGeneralScreenComponent(GeneralScreenModule(this))
                .inject(this)
    }

    private fun setupUi() {
        webView = WebView(this)
        webView.setWebViewClient(webViewClient)
        setContentView(webView)
    }

    private val webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(webView: WebView?, url: String?): Boolean {
            return presenter.onWebViewOverrideLoading(url)
        }

        @Suppress("OverridingDeprecatedMember")
        override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
            return presenter.onWebViewError()
        }
    }

    override fun onDestroy() {
        presenter.detach()
        super.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun loadUrl(url: String) {
        webView.loadUrl(url)
    }

    override fun clearWebViewHistory() {
        webView.clearHistory()
    }

    override fun showError(stringId: Int) {
        showToast(stringId)
    }

}