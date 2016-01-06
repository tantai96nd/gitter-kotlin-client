package com.github.shchurov.gitterclient.views.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.shchurov.gitterclient.models.Room
import com.github.shchurov.gitterclient.views.view_holders.LoadingViewHolder
import com.github.shchurov.gitterclient.views.view_holders.RoomViewHolder

class RoomsListAdapter(private val rooms: List<Room>,
        private val actionListener: RoomsListAdapter.ActionListener) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ROOM = 0
        private const val TYPE_LOADING = 1
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

    override fun getItemCount() = offset() + rooms.size

    override fun getItemViewType(position: Int) = when {
        loading && position == 0 -> TYPE_LOADING
        else -> TYPE_ROOM
    }

    // not 100% reliable but totally safe. Allows to have cool animations when items'
    // positions have been changed since last update
    override fun getItemId(position: Int) = when (getItemViewType(position)) {
        TYPE_ROOM -> Math.abs(rooms[position - offset()].idHashCode)
        else -> RecyclerView.NO_ID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_ROOM -> {
                val itemView = inflater.inflate(RoomViewHolder.LAYOUT_ID, parent, false)
                RoomViewHolder(itemView, actionListener)
            }
            TYPE_LOADING -> {
                val itemView = inflater.inflate(LoadingViewHolder.LAYOUT_ID, parent, false)
                LoadingViewHolder(itemView)
            }
            else -> throw IllegalArgumentException()
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == TYPE_ROOM) {
            (holder as RoomViewHolder).bindData(rooms[position - offset()])
        }
    }

    private fun offset() = if (loading) 1 else 0

    interface ActionListener {

        fun onRoomClick(room: Room)

    }

}