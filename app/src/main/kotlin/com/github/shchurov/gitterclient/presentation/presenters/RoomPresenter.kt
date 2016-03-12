package com.github.shchurov.gitterclient.presentation.presenters

import com.github.shchurov.gitterclient.data.subscribers.CustomSubscriber
import com.github.shchurov.gitterclient.domain.interactors.GetRoomMessagesInteractor
import com.github.shchurov.gitterclient.domain.interactors.MarkMessageAsReadInteractor
import com.github.shchurov.gitterclient.domain.models.Message
import com.github.shchurov.gitterclient.presentation.ui.RoomView
import com.github.shchurov.gitterclient.presentation.ui.adapters.MessagesAdapter
import com.github.shchurov.gitterclient.utils.compositeSubscribe
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class RoomPresenter @Inject constructor(
        private val view: RoomView,
        private val getMessagesInteractor: GetRoomMessagesInteractor,
        private val markMessageAsReadInteractor: MarkMessageAsReadInteractor
) : MessagesAdapter.ActionListener {

    private val subscriptions = CompositeSubscription()
    private var messages = mutableListOf<Message>()
    private var adapter = MessagesAdapter(messages, this)
    private val roomId = view.getRoomId()

    fun onCreate() {
        setupRecyclerViewAdapter()
        loadMessagesFirstPage()
    }

    private fun setupRecyclerViewAdapter() {
        view.setRecyclerViewAdapter(adapter)
    }

    private fun loadMessagesFirstPage() {
        view.showInitLoading()
        getMessagesInteractor.getFirstPage(roomId)
                .compositeSubscribe(subscriptions, createMessagesSubscriber())
    }

    private fun createMessagesSubscriber(): CustomSubscriber<MutableList<Message>> {
        return object : CustomSubscriber<MutableList<Message>>() {
            override fun onNext(data: MutableList<Message>) {
                messages.addAll(data.reversed())
                adapter.notifyMessagesAdded(messages.size - data.size, data.size)
                if (getMessagesInteractor.hasMorePages()) {
                    view.enablePagingListener()
                } else {
                    view.disablePagingListener()
                }
                view.forceOnReadPositionsChangedCallback()
            }

            override fun onFinish() {
                adapter.loading = false
                view.hideInitLoading()
            }
        }
    }

    fun onDestroy() {
        markMessageAsReadInteractor.flush()
        subscriptions.clear()
    }

    fun onVisiblePositionsChanged(firstPosition: Int, lastPosition: Int) {
        markMessagesAsRead(firstPosition, lastPosition)
    }

    private fun markMessagesAsRead(firstAdapterPosition: Int, lastAdapterPosition: Int) {
        val offset = adapter.messagesOffset
        for (i in (firstAdapterPosition - offset)..(lastAdapterPosition - offset)) {
            if (adapter.isMessageOnPosition(i) && messages[i].unread) {
                markMessageAsReadInteractor.markAsReadLazy(messages[i], roomId)
                adapter.notifyMessageMarkedAsRead(i);
            }
        }
    }

    fun onLoadMoreItems() {
        adapter.loading = true
        getMessagesInteractor.getNextPage()
                .compositeSubscribe(subscriptions, createMessagesSubscriber())
    }

}