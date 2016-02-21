package com.github.shchurov.gitterclient.presentation.presenters

interface RoomPresenter : BasePresenter {

    fun onLoadMoreItems()

    fun onReadPositionsChanged(firstPosition: Int, lastPosition: Int)

}