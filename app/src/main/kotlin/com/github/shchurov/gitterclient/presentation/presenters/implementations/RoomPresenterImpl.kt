package com.github.shchurov.gitterclient.presentation.presenters.implementations

import com.github.shchurov.gitterclient.domain.DataSource
import com.github.shchurov.gitterclient.domain.DataSubscriber
import com.github.shchurov.gitterclient.domain.interactors.GetRoomMessagesInteractor
import com.github.shchurov.gitterclient.domain.models.Message
import com.github.shchurov.gitterclient.presentation.presenters.RoomPresenter
import com.github.shchurov.gitterclient.presentation.ui.RoomView
import com.github.shchurov.gitterclient.presentation.ui.adapters.MessagesAdapter
import com.github.shchurov.gitterclient.utils.compositeSubscribe
import rx.subscriptions.CompositeSubscription
import java.util.*

class RoomPresenterImpl(
        private val view: RoomView,
        private val messagesInteractorGet: GetRoomMessagesInteractor
) : RoomPresenter, MessagesAdapter.ActionListener {

    private val subscriptions = CompositeSubscription()
    private var messages = ArrayList<Message>()
    private var adapter = MessagesAdapter(messages, this)

    override fun onCreate() {
        view.setRecyclerViewAdapter(adapter)
        loadMessagesFirstPage()
    }

    private fun loadMessagesFirstPage() {
        view.showInitLoading()
        messagesInteractorGet.getFirstPage(view.getRoomId())
                .compositeSubscribe(subscriptions, object : DataSubscriber<MutableList<Message>>() {
                    override fun onData(data: MutableList<Message>, source: DataSource) {
                        messages.addAll(data)
                        adapter.notifyMessagesAdded(0, data.size)
                        if (!messagesInteractorGet.hasMorePages()) {
                            view.disablePagingListener()
                        }
                    }

                    override fun onFinish() {
                        view.hideInitLoading()
                    }
                })
    }

    override fun onLoadMoreItems() {
        loadMessagesNext()
    }

    private fun loadMessagesNext() {
        adapter.loading = true
        messagesInteractorGet.getNextPage()
                .compositeSubscribe(subscriptions, object : DataSubscriber<MutableList<Message>>() {
                    override fun onData(data: MutableList<Message>, source: DataSource) {
                        messages.addAll(data)
                        adapter.notifyMessagesAdded(messages.size - data.size, data.size)
                        if (messagesInteractorGet.hasMorePages()) {
                            view.enablePagingListener()
                        }
                    }

                    override fun onFinish() {
                        adapter.loading = false
                    }
                })
    }

    override fun onDestroy() {
        subscriptions.clear()
    }


}