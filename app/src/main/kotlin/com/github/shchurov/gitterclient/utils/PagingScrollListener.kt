package com.github.shchurov.gitterclient.utils

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log

abstract class PagingScrollListener(private val offscreenItemsThreshold: Int) :
        RecyclerView.OnScrollListener() {

    private var layoutManager: LinearLayoutManager? = null
    private var loading: Boolean = false
    var enabled: Boolean = true

    protected abstract fun onLoadMoreItems()

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (layoutManager == null) {
            layoutManager = recyclerView.layoutManager as LinearLayoutManager
        }
        if (!enabled)
            return
        val lastVisible = layoutManager!!.findLastVisibleItemPosition()
        val totalCount = layoutManager!!.itemCount
        if (!loading && totalCount <= lastVisible + offscreenItemsThreshold) {
            loading = true
            onLoadMoreItems()
        }
    }

    fun notifyLoadingFinished() {
        loading = false
    }

}