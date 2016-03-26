package com.github.shchurov.gitterclient.presentation.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.shchurov.gitterclient.domain.models.Room
import com.github.shchurov.gitterclient.presentation.ui.view_holders.LoadingViewHolder
import com.github.shchurov.gitterclient.presentation.ui.view_holders.RoomViewHolder

class RoomsAdapter(
        private val actionListener: ActionListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ROOM = 0
        private const val TYPE_LOADING = 1
    }

    var rooms: List<Room>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var loading: Boolean = false
        set(value) {
            field = value
            if (value) {
                notifyItemInserted(0)
            } else {
                notifyItemRemoved(0)
            }
        }

    init {
        setHasStableIds(true)
    }

    private fun offset() = if (loading) 1 else 0

    override fun getItemCount() = offset() + (rooms?.size ?: 0)

    override fun getItemViewType(position: Int) = when {
        loading && position == 0 -> Companion.TYPE_LOADING
        else -> Companion.TYPE_ROOM
    }

    // not 100% reliable (shrinking 24-chars String to Long) but totally safe.
    // Allows to have cool animations when items' positions have been changed
    override fun getItemId(position: Int) = when (getItemViewType(position)) {
        Companion.TYPE_ROOM -> Math.abs(rooms!![position - offset()].idHashCode)
        else -> RecyclerView.NO_ID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            Companion.TYPE_ROOM -> {
                val itemView = inflater.inflate(RoomViewHolder.LAYOUT_ID, parent, false)
                RoomViewHolder(itemView, actionListener)
            }
            Companion.TYPE_LOADING -> {
                val itemView = inflater.inflate(LoadingViewHolder.LAYOUT_ID, parent, false)
                LoadingViewHolder(itemView)
            }
            else -> throw IllegalArgumentException()
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == Companion.TYPE_ROOM) {
            (holder as RoomViewHolder).bindData(rooms!![position - offset()])
        }
    }

    interface ActionListener {

        fun onRoomClick(room: Room)

    }

}