package com.github.shchurov.gitterclient.presentation.ui

import com.github.shchurov.gitterclient.domain.models.Message

interface RoomView {

    fun getRoomId(): String

    fun getRoomName(): String

    fun setToolbarTitle(title: String)

    fun showInitLoading()

    fun hideInitLoading()

    fun enablePagingListener()

    fun disablePagingListener()

    fun showLoadingMore()

    fun hideLoadingMore()

    fun addMessages(messages: List<Message>)

    fun invalidateMessage(message: Message)

}