package com.github.shchurov.gitterclient.presentation.presenters.implementations

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
        private val getMessagesInteractor: GetRoomMessagesInteractor,
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
        getMessagesInteractor.getFirstPage(roomId)
                .compositeSubscribe(subscriptions, createMessagesSubscriber())
    }

    private fun createMessagesSubscriber(): DataSubscriber<MutableList<Message>> {
        return object : DataSubscriber<MutableList<Message>>() {
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

    override fun onDestroy() {
        markMessageAsReadInteractor.flush()
        subscriptions.clear()
    }

    override fun onVisiblePositionsChanged(firstPosition: Int, lastPosition: Int) {
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

    override fun onLoadMoreItems() {
        adapter.loading = true
        getMessagesInteractor.getNextPage()
                .compositeSubscribe(subscriptions, createMessagesSubscriber())
    }

}