package com.github.shchurov.gitterclient.presentation.ui

import android.support.v7.widget.RecyclerView

interface RoomView {

    fun setRecyclerViewAdapter(adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>)

    fun getRoomId(): String

    fun showInitLoading()

    fun hideInitLoading()

    fun enablePagingListener()

    fun disablePagingListener()

    fun forceOnReadPositionsChangedCallback()

}