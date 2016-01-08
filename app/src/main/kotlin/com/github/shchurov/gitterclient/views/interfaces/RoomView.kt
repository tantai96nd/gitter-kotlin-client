package com.github.shchurov.gitterclient.views.interfaces

import android.support.v7.widget.RecyclerView

interface RoomView {

    fun setRecyclerViewAdapter(adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>)

    fun getRoomId(): String

    fun addOnScrollListener(listener: RecyclerView.OnScrollListener)

    fun showInitLoading()

    fun hideInitLoading()

}