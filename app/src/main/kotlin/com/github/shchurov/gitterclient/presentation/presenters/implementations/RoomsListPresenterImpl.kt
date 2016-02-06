package com.github.shchurov.gitterclient.presentation.presenters.implementations

import com.github.shchurov.gitterclient.domain.DataSource
import com.github.shchurov.gitterclient.domain.DataSubscriber
import com.github.shchurov.gitterclient.domain.models.Room
import com.github.shchurov.gitterclient.presentation.presenters.RoomsListPresenter
import com.github.shchurov.gitterclient.utils.compositeSubscribe
import com.github.shchurov.gitterclient.presentation.ui.activities.RoomActivity
import com.github.shchurov.gitterclient.presentation.ui.adapters.RoomsAdapter
import com.github.shchurov.gitterclient.presentation.ui.RoomsListView
import rx.subscriptions.CompositeSubscription

class RoomsListPresenterImpl(val view: RoomsListView) : RoomsListPresenter,
        RoomsAdapter.ActionListener {

    private val subscriptions = CompositeSubscription()
    private val rooms: MutableList<Room> = arrayListOf()
    private val adapter = RoomsAdapter(rooms, this)

    override fun onCreate() {
        view.setRecyclerViewAdapter(adapter)
        loadRooms(false)
    }

    override fun onRestart() {
        loadRooms(true)
    }

    private fun loadRooms(localOnly: Boolean) {
        if (!localOnly) {
            adapter.loading = true
        }
        DataManager.getMyRooms(localOnly)
                .compositeSubscribe(subscriptions, object : DataSubscriber<List<Room>>() {
                    override fun onData(data: List<Room>, source: DataSource) {
                        rooms.clear()
                        rooms.addAll(data)
                        adapter.notifyDataSetChanged()
                    }

                    override fun onFinish() {
                        if (!localOnly) {
                            adapter.loading = false
                        }
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