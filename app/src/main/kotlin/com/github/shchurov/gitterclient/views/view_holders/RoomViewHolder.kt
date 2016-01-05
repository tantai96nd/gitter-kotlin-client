package com.github.shchurov.gitterclient.views.view_holders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.github.shchurov.gitterclient.R
import com.github.shchurov.gitterclient.models.Room
import com.github.shchurov.gitterclient.utils.GlideCircleTransformation
import com.github.shchurov.gitterclient.views.adapters.RoomsListAdapter

class RoomViewHolder(itemView: View, private val actionListener: RoomsListAdapter.ActionListener) :
        RecyclerView.ViewHolder(itemView) {

    companion object {
        const val LAYOUT_ID = R.layout.room_item
    }

    private val tvName: TextView
    private val ivAvatar: ImageView
    private lateinit var room: Room

    init {
        tvName = itemView.findViewById(R.id.tvName) as TextView
        ivAvatar = itemView.findViewById(R.id.ivAvatar) as ImageView
        itemView.setOnClickListener { actionListener.onRoomClick(room) }
    }

    fun bindData(room: Room) {
        this.room = room
        tvName.text = room.name
        Glide.with(itemView.context)
                .load(room.avatar)
                .placeholder(R.drawable.avatar_loading)
                .transform(GlideCircleTransformation)
                .into(ivAvatar)
    }

}