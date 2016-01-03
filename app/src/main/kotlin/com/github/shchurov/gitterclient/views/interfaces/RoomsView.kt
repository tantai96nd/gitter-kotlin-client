package com.github.shchurov.gitterclient.views.interfaces

import android.support.v7.widget.RecyclerView

interface RoomsView {

    fun setRecyclerViewAdapter(adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>)

}