package com.github.shchurov.gitterclient.presentation.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import com.github.shchurov.gitterclient.App
import com.github.shchurov.gitterclient.dagger.components.DaggerActivityComponent
import com.github.shchurov.gitterclient.dagger.modules.ActivityModule
import com.github.shchurov.gitterclient.presentation.presenters.LogInPresenter
import com.github.shchurov.gitterclient.presentation.ui.LogInView
import javax.inject.Inject

class LogInActivity : AppCompatActivity(), LogInView {

    companion object {
        val PAGE_RENDER_TIME = 300L
    }

    @Inject lateinit var presenter: LogInPresenter
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDependencies()
        setupWebView()
        presenter.onCreate()
    }

    private fun initDependencies() {
        val component = DaggerActivityComponent.builder()
                .appComponent(App.appComponent)
                .activityModule(ActivityModule(this))
                .build()
        component.inject(this)
    }

    private fun setupWebView() {
        webView = WebView(this)
        webView.alpha = 0f;
        setContentView(webView)
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
                .duration = 300
    }

    override fun getActivity() = this

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
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