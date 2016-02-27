package com.github.shchurov.gitterclient.utils

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

abstract class VisiblePositionsScrollListener : RecyclerView.OnScrollListener() {

    private var layoutManager: LinearLayoutManager? = null

    fun forceCallback(recyclerView: RecyclerView) {
        handleNewPosition(recyclerView)
    }

    private fun handleNewPosition(recyclerView: RecyclerView) {
        if (layoutManager == null) {
            layoutManager = recyclerView.layoutManager as LinearLayoutManager
        }
        val first = layoutManager!!.findFirstVisibleItemPosition()
        val last = layoutManager!!.findLastVisibleItemPosition()
        if (first != RecyclerView.NO_POSITION && last != RecyclerView.NO_POSITION) {
            onVisiblePositionsChanged(first, last)
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState != RecyclerView.SCROLL_STATE_IDLE)
            return
        handleNewPosition(recyclerView)
    }

    abstract fun onVisiblePositionsChanged(firstPosition: Int, lastPosition: Int)

}