package com.github.shchurov.gitterclient.presentation.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.shchurov.gitterclient.domain.models.Message
import com.github.shchurov.gitterclient.presentation.ui.view_holders.LoadingViewHolder
import com.github.shchurov.gitterclient.presentation.ui.view_holders.MessageViewHolder

class MessagesAdapter(
        private val actionListener: ActionListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_MESSAGE = 0
        private const val TYPE_LOADING = 1
        private const val PAYLOAD_HIDE_UNREAD = 3123
    }

    private val messages: MutableList<Message> = mutableListOf()
    var loading: Boolean = false
        set(value) {
            if (value) {
                notifyItemInserted(itemCount)
            } else {
                notifyItemRemoved(itemCount)
            }
            field = value
        }

    override fun getItemCount() = messages.size + (if (loading) 1 else 0)

    override fun getItemViewType(position: Int) = when {
        loading && position == itemCount - 1 -> TYPE_LOADING
        else -> TYPE_MESSAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_MESSAGE -> {
                val itemView = inflater.inflate(MessageViewHolder.LAYOUT_ID, parent, false)
                MessageViewHolder(itemView, actionListener)
            }
            TYPE_LOADING -> {
                val itemView = inflater.inflate(LoadingViewHolder.LAYOUT_ID, parent, false)
                LoadingViewHolder(itemView)
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (holder.itemViewType == TYPE_MESSAGE) {
            if (PAYLOAD_HIDE_UNREAD in payloads) {
                (holder as MessageViewHolder).hideUnreadIndicator()
            } else {
                onBindViewHolder(holder, position)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == TYPE_MESSAGE) {
            (holder as MessageViewHolder).bindData(messages[position])
        }
    }

    fun addMessages(newMessages: List<Message>) {
        val oldSize = messages.size
        messages.addAll(newMessages)
        notifyItemRangeInserted(oldSize, newMessages.size)
    }

    fun invalidateMessage(message: Message) {
        for (i in messages.indices) {
            if (messages[i] == message) {
                notifyItemChanged(i)
            }
        }
    }

    fun getMessagesInRange(firstPosition: Int, lastPosition: Int): List<Message> {
        val lastMessagePosition = Math.min(lastPosition, messages.size - 1)
        return messages.subList(firstPosition, lastMessagePosition + 1)
    }

    interface ActionListener {

    }

}