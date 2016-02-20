package com.github.shchurov.gitterclient.presentation.ui.view_holders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.github.shchurov.gitterclient.R
import com.github.shchurov.gitterclient.domain.models.Room
import com.github.shchurov.gitterclient.presentation.ui.adapters.RoomsAdapter
import com.github.shchurov.gitterclient.utils.GlideCircleTransformation
import com.github.shchurov.gitterclient.utils.findViewById

class RoomViewHolder(itemView: View, private val actionListener: RoomsAdapter.ActionListener) :
        RecyclerView.ViewHolder(itemView) {

    companion object {
        const val LAYOUT_ID = R.layout.room_item
    }

    private val tvName = findViewById(R.id.tvName) as TextView
    private val ivAvatar = findViewById(R.id.ivAvatar) as ImageView
    private val tvUnread = findViewById(R.id.tvUnread) as TextView
    private val tvMentions = findViewById(R.id.tvMentions) as TextView
    private lateinit var room: Room

    init {
        itemView.setOnClickListener { actionListener.onRoomClick(room) }
    }

    fun bindData(room: Room) {
        this.room = room
        loadAvatar()
        setupName()
        setupUnreadItems()
        setupMentions()
    }

    private fun loadAvatar() {
        Glide.with(itemView.context)
                .load(room.avatar)
                .placeholder(R.drawable.avatar_loading)
                .transform(GlideCircleTransformation)
                .into(ivAvatar)
    }

    private fun setupName() {
        tvName.text = room.name
    }

    private fun setupUnreadItems() {
        if (room.unreadItems > 0) {
            tvUnread.visibility = View.VISIBLE
            tvUnread.text = room.unreadItems.toString()
        } else {
            tvUnread.visibility = View.GONE
        }
    }

    private fun setupMentions() {
        if (room.mentions > 0) {
            tvMentions.visibility = View.VISIBLE
            tvMentions.text = room.mentions.toString()
        } else {
            tvMentions.visibility = View.GONE
        }
    }

}