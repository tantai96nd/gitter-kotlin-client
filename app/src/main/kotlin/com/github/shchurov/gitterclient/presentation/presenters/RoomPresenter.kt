package com.github.shchurov.gitterclient.presentation.presenters

import com.github.shchurov.gitterclient.data.subscribers.DefaultSubscriber
import com.github.shchurov.gitterclient.domain.interactors.GetRoomMessagesInteractor
import com.github.shchurov.gitterclient.domain.interactors.MarkMessageAsReadInteractor
import com.github.shchurov.gitterclient.domain.models.Message
import com.github.shchurov.gitterclient.presentation.ui.RoomView
import com.github.shchurov.gitterclient.utils.compositeSubscribe
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class RoomPresenter @Inject constructor(
        private val getMessagesInteractor: GetRoomMessagesInteractor,
        private val markMessageAsReadInteractor: MarkMessageAsReadInteractor
) : BasePresenter<RoomView>() {

    private val subscriptions = CompositeSubscription()

    override fun onAttach() {
        loadMessagesFirstPage()
    }

    private fun loadMessagesFirstPage() {
        getView().showInitLoading()
        getMessagesInteractor.getFirstPage()
                .compositeSubscribe(subscriptions, createMessagesSubscriber())
    }

    private fun createMessagesSubscriber(): DefaultSubscriber<MutableList<Message>> {
        return object : DefaultSubscriber<MutableList<Message>>() {
            override fun onNext(data: MutableList<Message>) {
                getView().addMessages(data)
                if (getMessagesInteractor.hasMorePages) {
                    getView().enablePagingListener()
                } else {
                    getView().disablePagingListener()
                }
            }

            override fun onFinish() {
                getView().hideLoadingMore()
                getView().hideInitLoading()
            }
        }
    }

    override fun onDetach() {
        markMessageAsReadInteractor.flush()
        subscriptions.clear()
    }

    fun onVisibleMessagesChanged(visibleMessages: List<Message>) {
        markMessagesAsRead(visibleMessages)
    }

    private fun markMessagesAsRead(messages: List<Message>) {
        for (message in messages) {
            if (message.unread) {
                markMessageAsReadInteractor.markAsReadLazy(message)
                getView().invalidateMessage(message)
            }
        }
    }

    fun onLoadMoreItems() {
        getView().showLoadingMore()
        getMessagesInteractor.getNextPage()
                .compositeSubscribe(subscriptions, createMessagesSubscriber())
    }

}