package com.github.shchurov.gitterclient.presentation.ui.view_holders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.github.shchurov.gitterclient.R
import com.github.shchurov.gitterclient.domain.models.Message
import com.github.shchurov.gitterclient.presentation.ui.adapters.MessagesAdapter
import com.github.shchurov.gitterclient.utils.GlideCircleTransformation
import com.github.shchurov.gitterclient.utils.TimeUtils
import com.github.shchurov.gitterclient.utils.findViewById

class MessageViewHolder(itemView: View, private val actionListener: MessagesAdapter.ActionListener) :
        RecyclerView.ViewHolder(itemView) {

    companion object {
        const val LAYOUT_ID = R.layout.message_item
    }

    private val ivAvatar = findViewById(R.id.ivAvatar) as ImageView
    private val tvUsername = findViewById(R.id.tvUsername) as TextView
    private val tvTime = findViewById(R.id.tvTime) as TextView
    private val tvMessage = findViewById(R.id.tvMessage) as TextView
    private lateinit var message: Message

    fun bindData(message: Message) {
        this.message = message
        Glide.with(itemView.context)
                .load(message.user.avatar)
                .transform(GlideCircleTransformation)
                .into(ivAvatar)
        tvUsername.text = message.user.username
        tvTime.text = TimeUtils.convertTimestampToString(message.time)
        tvMessage.text = message.text
    }

}