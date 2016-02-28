package com.github.shchurov.gitterclient.presentation.presenters

interface RoomPresenter {

    fun onLoadMoreItems()

    fun onVisiblePositionsChanged(firstPosition: Int, lastPosition: Int)

    fun onCreate()

    fun onDestroy()

}