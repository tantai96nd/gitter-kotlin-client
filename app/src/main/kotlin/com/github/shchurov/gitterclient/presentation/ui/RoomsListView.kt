package com.github.shchurov.gitterclient.presentation.ui

import android.support.v7.widget.RecyclerView

interface RoomsListView {

    fun setRecyclerViewAdapter(adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>)

    fun postDelayed(runnable: () -> Unit, delay: Long)

    fun goToRoomScreen(id: String, name: String)

}