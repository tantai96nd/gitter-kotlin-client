package com.github.shchurov.gitterclient.views.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.shchurov.gitterclient.models.Room
import com.github.shchurov.gitterclient.views.view_holders.RoomViewHolder

class RoomsAdapter(private val rooms: List<Room>,
        private val actionListener: RoomsAdapter.ActionListener) :
        RecyclerView.Adapter<RoomViewHolder>() {

    override fun getItemCount() = rooms.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder? {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(RoomViewHolder.LAYOUT_ID, parent, false)
        return RoomViewHolder(itemView, actionListener)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bindData(rooms[position])
    }

    interface ActionListener {

        fun onRoomClick()

    }

}