package com.github.shchurov.gitterclient.presenters.implementations

import com.github.shchurov.gitterclient.helpers.data_manager.DataManager
import com.github.shchurov.gitterclient.helpers.data_manager.DataSource
import com.github.shchurov.gitterclient.helpers.data_manager.DataSubscriber
import com.github.shchurov.gitterclient.models.Message
import com.github.shchurov.gitterclient.presenters.interfaces.RoomPresenter
import com.github.shchurov.gitterclient.utils.compositeSubscribe
import com.github.shchurov.gitterclient.views.adapters.RoomAdapter
import com.github.shchurov.gitterclient.views.interfaces.RoomView
import rx.subscriptions.CompositeSubscription

class RoomPresenterImpl(val view: RoomView) : RoomPresenter, RoomAdapter.ActionListener {

    private val subscriptions = CompositeSubscription()
    private lateinit var roomId: String
    private val messages: MutableList<Message> = arrayListOf()
    private val adapter = RoomAdapter(messages, this)

    override fun onCreate() {
        roomId = view.getRoomId()
        view.setRecyclerViewAdapter(adapter)
        loadMessages()
    }

    private fun loadMessages() {
        DataManager.getRoomMessages(roomId)
                .compositeSubscribe(subscriptions, object : DataSubscriber<List<Message>>() {
                    override fun onData(data: List<Message>, source: DataSource) {
                        messages.clear()
                        messages.addAll(data)
                        adapter.notifyDataSetChanged()
                    }
                })
    }

    override fun onDestroy() {
        subscriptions.clear()
    }

}