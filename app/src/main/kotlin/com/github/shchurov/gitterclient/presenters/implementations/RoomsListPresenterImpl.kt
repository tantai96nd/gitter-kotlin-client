package com.github.shchurov.gitterclient.presenters.implementations

import com.github.shchurov.gitterclient.helpers.data_manager.DataManager
import com.github.shchurov.gitterclient.helpers.data_manager.DataSource
import com.github.shchurov.gitterclient.helpers.data_manager.DataSubscriber
import com.github.shchurov.gitterclient.models.Room
import com.github.shchurov.gitterclient.presenters.interfaces.RoomsListPresenter
import com.github.shchurov.gitterclient.utils.compositeSubscribe
import com.github.shchurov.gitterclient.views.activities.RoomActivity
import com.github.shchurov.gitterclient.views.adapters.RoomsListAdapter
import com.github.shchurov.gitterclient.views.interfaces.RoomsListView
import rx.subscriptions.CompositeSubscription

class RoomsListPresenterImpl(val view: RoomsListView) : RoomsListPresenter,
        RoomsListAdapter.ActionListener {

    private val subscriptions = CompositeSubscription()
    private val rooms: MutableList<Room> = arrayListOf()
    private val adapter = RoomsListAdapter(rooms, this)

    override fun onCreate() {
        view.setRecyclerViewAdapter(adapter)
        loadRooms()
    }

    private fun loadRooms() {
        DataManager.getMyRooms()
                .compositeSubscribe(subscriptions, object : DataSubscriber<List<Room>>() {
                    override fun onData(data: List<Room>, source: DataSource) {
                        rooms.clear()
                        rooms.addAll(data)
                        adapter.notifyDataSetChanged()
                    }
                })
    }

    override fun onDestroy() {
        subscriptions.clear()
    }

    override fun onRoomClick(room: Room) {
        RoomActivity.start(view.getContext(), room.id, room.name)
    }

}