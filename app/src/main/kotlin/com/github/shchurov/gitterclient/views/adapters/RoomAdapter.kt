package com.github.shchurov.gitterclient.views.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.shchurov.gitterclient.models.Message
import com.github.shchurov.gitterclient.views.view_holders.MessageViewHolder

class RoomAdapter(private val messages: List<Message>,
        private val actionListener: RoomAdapter.ActionListener) :
        RecyclerView.Adapter<MessageViewHolder>() {

    override fun getItemCount() = messages.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder? {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(MessageViewHolder.LAYOUT_ID, parent, false)
        return MessageViewHolder(itemView, actionListener)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bindData(messages[position])
    }

    interface ActionListener {

    }

}