package com.github.shchurov.gitterclient.utils

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

abstract class PagingScrollListener(private val offscreenItemsThreshold: Int) : RecyclerView.OnScrollListener() {

    private var layoutManager: LinearLayoutManager? = null
    var enabled: Boolean = true

    protected abstract fun onLoadMoreItems()

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (!enabled)
            return
        initLayoutManagerIfRequired(recyclerView)
        if (isThresholdPassed()) {
            enabled = false
            onLoadMoreItems()
        }
    }

    private fun initLayoutManagerIfRequired(recyclerView: RecyclerView) {
        if (layoutManager == null) {
            layoutManager = recyclerView.layoutManager as LinearLayoutManager
        }
    }

    private fun isThresholdPassed(): Boolean {
        val lastVisible = layoutManager!!.findLastVisibleItemPosition()
        val totalCount = layoutManager!!.itemCount
        return totalCount <= lastVisible + offscreenItemsThreshold
    }

}