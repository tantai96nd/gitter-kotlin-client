package com.github.shchurov.gitterclient.presentation.presenters.implementations

import com.github.shchurov.gitterclient.domain.DataSource
import com.github.shchurov.gitterclient.domain.DataSubscriber
import com.github.shchurov.gitterclient.domain.models.Message
import com.github.shchurov.gitterclient.presentation.presenters.RoomPresenter
import com.github.shchurov.gitterclient.utils.PagingScrollListener
import com.github.shchurov.gitterclient.utils.compositeSubscribe
import com.github.shchurov.gitterclient.presentation.ui.adapters.MessagesAdapter
import com.github.shchurov.gitterclient.presentation.ui.RoomView
import rx.subscriptions.CompositeSubscription

class RoomPresenterImpl(val view: RoomView) : RoomPresenter, MessagesAdapter.ActionListener {

    companion object {
        private const val PAGING_THRESHOLD = 10
        private const val MESSAGES_LIMIT = 30;

    }

    private val subscriptions = CompositeSubscription()
    private lateinit var roomId: String
    private val messages: MutableList<Message> = arrayListOf()
    private val adapter = MessagesAdapter(messages, this)

    override fun onCreate() {
        roomId = view.getRoomId()
        view.setRecyclerViewAdapter(adapter)
        pagingListener.enabled = false
        view.addOnScrollListener(pagingListener)
        loadMessagesFirstPage()
    }

    private val pagingListener = object : PagingScrollListener(com.github.shchurov.gitterclient.presentation.RoomPresenterImpl.Companion.PAGING_THRESHOLD) {
        override fun onLoadMoreItems() {
            loadMessagesNext()
        }
    }

    private fun loadMessagesFirstPage() {
        view.showInitLoading()
        DataManager.getRoomMessagesFirstPage(roomId, com.github.shchurov.gitterclient.presentation.RoomPresenterImpl.Companion.MESSAGES_LIMIT)
                .compositeSubscribe(subscriptions, object : DataSubscriber<List<Message>>() {
                    override fun onData(data: List<Message>, source: DataSource) {
                        messages.addAll(data)
                        adapter.notifyMessagesAdded(0, data.size)
                        pagingListener.enabled = data.size == com.github.shchurov.gitterclient.presentation.RoomPresenterImpl.Companion.MESSAGES_LIMIT
                    }

                    override fun onFinish() {
                        view.hideInitLoading()
                    }
                })
    }

    private fun loadMessagesNext() {
        adapter.loading = true
        val beforeId = messages.last().id
        DataManager.getRoomMessagesNextPage(roomId, com.github.shchurov.gitterclient.presentation.RoomPresenterImpl.Companion.MESSAGES_LIMIT, beforeId)
                .compositeSubscribe(subscriptions, object : DataSubscriber<List<Message>>() {
                    override fun onData(data: List<Message>, source: DataSource) {
                        messages.addAll(data)
                        adapter.notifyMessagesAdded(messages.size - data.size, data.size)
                        pagingListener.enabled = data.size == com.github.shchurov.gitterclient.presentation.RoomPresenterImpl.Companion.MESSAGES_LIMIT
                        pagingListener.notifyLoadingFinished()
                    }

                    override fun onFinish() {

                    }
                })
    }

    override fun onDestroy() {
        subscriptions.clear()
    }


}