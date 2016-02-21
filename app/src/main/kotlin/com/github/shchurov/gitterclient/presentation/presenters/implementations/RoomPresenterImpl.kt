package com.github.shchurov.gitterclient.presentation.presenters.implementations

import com.github.shchurov.gitterclient.domain.DataSource
import com.github.shchurov.gitterclient.domain.DataSubscriber
import com.github.shchurov.gitterclient.domain.interactors.GetRoomMessagesInteractor
import com.github.shchurov.gitterclient.domain.interactors.MarkMessageAsReadInteractor
import com.github.shchurov.gitterclient.domain.models.Message
import com.github.shchurov.gitterclient.presentation.presenters.RoomPresenter
import com.github.shchurov.gitterclient.presentation.ui.RoomView
import com.github.shchurov.gitterclient.presentation.ui.adapters.MessagesAdapter
import com.github.shchurov.gitterclient.utils.compositeSubscribe
import rx.subscriptions.CompositeSubscription
import java.util.*

class RoomPresenterImpl(
        private val view: RoomView,
        private val messagesInteractorGet: GetRoomMessagesInteractor,
        private val markMessageAsReadInteractor: MarkMessageAsReadInteractor
) : RoomPresenter, MessagesAdapter.ActionListener {

    private val subscriptions = CompositeSubscription()
    private var messages = ArrayList<Message>()
    private var adapter = MessagesAdapter(messages, this)
    private val roomId = view.getRoomId()

    override fun onCreate() {
        setupRecyclerViewAdapter()
        loadMessagesFirstPage()
    }

    private fun setupRecyclerViewAdapter() {
        view.setRecyclerViewAdapter(adapter)
    }

    private fun loadMessagesFirstPage() {
        view.showInitLoading()
        messagesInteractorGet.getFirstPage(roomId)
                .compositeSubscribe(subscriptions, object : DataSubscriber<MutableList<Message>>() {
                    override fun onData(data: MutableList<Message>, source: DataSource) {
                        messages.addAll(data)
                        adapter.notifyMessagesAdded(0, data.size)
                        if (!messagesInteractorGet.hasMorePages()) {
                            view.disablePagingListener()
                        }
                        view.forceOnReadPositionsChangedCallback()
                    }

                    override fun onFinish() {
                        view.hideInitLoading()
                    }
                })
    }

    override fun onDestroy() {
        markMessageAsReadInteractor.flush(roomId)
        subscriptions.clear()
    }

    override fun onReadPositionsChanged(firstPosition: Int, lastPosition: Int) {
        markMessagesAsRead(firstPosition, lastPosition)
    }

    private fun markMessagesAsRead(firstAdapterPosition: Int, lastAdapterPosition: Int) {
        val offset = adapter.messagesOffset
        for (i in (firstAdapterPosition - offset)..(lastAdapterPosition - offset)) {
            markMessageAsReadInteractor.markAsReadLazy(messages[i], roomId)
        }
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


}