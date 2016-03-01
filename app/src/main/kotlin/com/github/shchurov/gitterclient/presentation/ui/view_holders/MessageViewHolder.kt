package com.github.shchurov.gitterclient.presentation.ui.view_holders

import android.animation.Animator
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.github.shchurov.gitterclient.App
import com.github.shchurov.gitterclient.R
import com.github.shchurov.gitterclient.domain.models.Message
import com.github.shchurov.gitterclient.presentation.ui.adapters.MessagesAdapter
import com.github.shchurov.gitterclient.utils.GlideCircleTransformation
import com.github.shchurov.gitterclient.utils.SimpleAnimatorListener
import com.github.shchurov.gitterclient.utils.TimeUtils
import com.github.shchurov.gitterclient.utils.findViewById

class MessageViewHolder(
        itemView: View,
        private val actionListener: MessagesAdapter.ActionListener
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        const val LAYOUT_ID = R.layout.message_item
    }

    private val months = App.context.resources.getStringArray(R.array.months)
    private val ivAvatar = findViewById(R.id.ivAvatar) as ImageView
    private val tvUsername = findViewById(R.id.tvUsername) as TextView
    private val tvTime = findViewById(R.id.tvTime) as TextView
    private val tvMessage = findViewById(R.id.tvMessage) as TextView
    private val vUnread = findViewById(R.id.vUnread)
    private lateinit var message: Message

    fun bindData(message: Message) {
        this.message = message
        loadAvatar()
        setupUsername()
        setupTime()
        setupMessage()
        setupUnread()
    }

    private fun loadAvatar() {
        Glide.with(itemView.context)
                .load(message.user.avatar)
                .transform(GlideCircleTransformation)
                .into(ivAvatar)
    }

    private fun setupUsername() {
        tvUsername.text = message.user.username
    }

    private fun setupTime() {
        tvTime.text = TimeUtils.convertTimestampToRelativeString(message.timestamp, months)
    }

    private fun setupMessage() {
        tvMessage.text = message.text
    }

    private fun setupUnread() {
        vUnread.visibility = if (message.unread) View.VISIBLE else View.GONE
    }

    fun hideUnreadIndicator() {
        if (vUnread.visibility != View.VISIBLE)
            return
        runHideUnreadAnimation()
    }

    private fun runHideUnreadAnimation() {
        setIsRecyclable(false)
        vUnread.animate()
                .alpha(0f)
                .setDuration(600)
                .setListener(object : SimpleAnimatorListener() {
                    override fun onAnimationEnd(p0: Animator?) {
                        resetAfterHideUnreadAnimation()
                    }
                })
    }

    private fun resetAfterHideUnreadAnimation() {
        setIsRecyclable(true)
        vUnread.visibility = View.GONE
        vUnread.alpha = 1f
    }

}