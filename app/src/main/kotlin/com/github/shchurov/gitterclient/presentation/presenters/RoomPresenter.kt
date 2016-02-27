package com.github.shchurov.gitterclient.presentation.presenters

interface RoomPresenter : BasePresenter {

    fun onLoadMoreItems()

    fun onVisiblePositionsChanged(firstPosition: Int, lastPosition: Int)

}