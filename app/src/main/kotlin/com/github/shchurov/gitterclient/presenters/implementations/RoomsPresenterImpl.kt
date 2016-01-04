package com.github.shchurov.gitterclient.presenters.implementations

import com.github.shchurov.gitterclient.helpers.data_manager.DataManager
import com.github.shchurov.gitterclient.helpers.data_manager.DataSource
import com.github.shchurov.gitterclient.helpers.data_manager.DataSubscriber
import com.github.shchurov.gitterclient.models.Room
import com.github.shchurov.gitterclient.presenters.interfaces.RoomsPresenter
import com.github.shchurov.gitterclient.utils.compositeSubscribe
import com.github.shchurov.gitterclient.views.adapters.RoomsAdapter
import com.github.shchurov.gitterclient.views.interfaces.RoomsView
import rx.subscriptions.CompositeSubscription

class RoomsPresenterImpl(val view: RoomsView) : RoomsPresenter, RoomsAdapter.ActionListener {

    private val subscriptions = CompositeSubscription()
    private val rooms: MutableList<Room> = arrayListOf()
    private val adapter = RoomsAdapter(rooms, this)

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

                    override fun onFinish() {
                        super.onFinish()
                    }
                })
    }

    override fun onDestroy() {
        subscriptions.clear()
    }

    override fun onRoomClick() {

    }

}