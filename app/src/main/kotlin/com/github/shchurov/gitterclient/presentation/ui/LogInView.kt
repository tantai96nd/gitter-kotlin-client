package com.github.shchurov.gitterclient.presentation.ui

interface LogInView {

    fun loadUrl(url: String)

    fun clearWebViewHistory()

    fun showError(stringId: Int)

}