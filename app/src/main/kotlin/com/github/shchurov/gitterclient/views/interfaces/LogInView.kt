package com.github.shchurov.gitterclient.views.interfaces

import android.app.Activity
import android.webkit.WebViewClient

interface LogInView {

    fun setWebViewClient(webViewClient: WebViewClient)

    fun loadUrl(url: String)

    fun showWebView()

    fun getActivity(): Activity

}