package com.github.shchurov.gitterclient.views.view_holders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.github.shchurov.gitterclient.R
import com.github.shchurov.gitterclient.models.Message
import com.github.shchurov.gitterclient.utils.GlideCircleTransformation
import com.github.shchurov.gitterclient.utils.TimeUtils
import com.github.shchurov.gitterclient.views.adapters.RoomAdapter

class MessageViewHolder(itemView: View, private val actionListener: RoomAdapter.ActionListener) :
        RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT_ID = R.layout.message_item
    }

    private lateinit var ivAvatar: ImageView
    private lateinit var tvUsername: TextView
    private lateinit var tvTime: TextView
    private lateinit var tvMessage: TextView
    private lateinit var message: Message

    init {
        ivAvatar = itemView.findViewById(R.id.ivAvatar) as ImageView
        tvUsername = itemView.findViewById(R.id.tvUsername) as TextView
        tvTime = itemView.findViewById(R.id.tvTime) as TextView
        tvMessage = itemView.findViewById(R.id.tvMessage) as TextView
    }

    fun bindData(message: Message) {
        this.message = message
        Glide.with(itemView.context)
                .load(message.user.avatar)
                .transform(GlideCircleTransformation)
                .into(ivAvatar)
        tvUsername.text = message.user.username
        tvTime.text = TimeUtils.convertLongToString(message.time)
        tvMessage.text = message.text
    }

}