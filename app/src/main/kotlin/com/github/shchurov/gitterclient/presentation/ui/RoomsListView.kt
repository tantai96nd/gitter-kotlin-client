package com.github.shchurov.gitterclient.presentation.ui

import com.github.shchurov.gitterclient.domain.models.Room

interface RoomsListView {

    fun goToRoomScreen(id: String, name: String)

    fun showLoading()

    fun hideLoading()

    fun displayRooms(rooms: List<Room>)

}