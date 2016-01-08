package com.github.shchurov.gitterclient.views.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.shchurov.gitterclient.models.Message
import com.github.shchurov.gitterclient.views.view_holders.LoadingViewHolder
import com.github.shchurov.gitterclient.views.view_holders.MessageViewHolder

class MessagesAdapter(private val messages: List<Message>,
        private val actionListener: MessagesAdapter.ActionListener) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_MESSAGE = 0
        private const val TYPE_LOADING = 1
    }

    var loading: Boolean = false
        set(value) {
            if (value) {
                notifyItemInserted(itemCount)
            } else {
                notifyItemRemoved(itemCount)
            }
            field = value
        }

    private fun offsetEnd() = (if (loading) 1 else 0)

    override fun getItemCount() = messages.size + offsetEnd()

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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == TYPE_MESSAGE) {
            (holder as MessageViewHolder).bindData(messages[position])
        }
    }

    fun notifyMessagesAdded(oldCount: Int, count: Int) {
        notifyItemRangeInserted(oldCount, count)
    }

    interface ActionListener {

    }

}