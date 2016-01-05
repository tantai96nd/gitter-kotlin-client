package com.github.shchurov.gitterclient.views.interfaces

import android.content.Context
import android.support.v7.widget.RecyclerView

interface RoomsListView {

    fun setRecyclerViewAdapter(adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>)

    fun getContext(): Context

}