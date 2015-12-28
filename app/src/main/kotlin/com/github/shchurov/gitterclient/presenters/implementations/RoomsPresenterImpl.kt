package com.github.shchurov.gitterclient.presenters.implementations

import android.util.Log
import com.github.shchurov.gitterclient.network.GitterApi
import com.github.shchurov.gitterclient.network.RequestSubscriber
import com.github.shchurov.gitterclient.network.responses.RoomResponse
import com.github.shchurov.gitterclient.presenters.interfaces.RoomsPresenter
import com.github.shchurov.gitterclient.utils.subscribeWithSchedulers
import com.github.shchurov.gitterclient.views.interfaces.RoomsView
import rx.subscriptions.CompositeSubscription

class RoomsPresenterImpl(val view: RoomsView) : RoomsPresenter {

    private val subscriptions = CompositeSubscription()

    override fun onCreate() {
        loadRooms()
    }

    private fun loadRooms() {
        GitterApi.gitterService.getMyRooms()
                .subscribeWithSchedulers(object : RequestSubscriber<MutableList<RoomResponse>>() {
                    override fun onSuccess(response: MutableList<RoomResponse>) {
                        for (room in response) {
                            Log.d("OLOLO", "ROOM: " + room.name)
                        }
                    }

                    override fun onFailure(e: Throwable) {
                        super.onFailure(e)
                    }

                    override fun onFinish() {
                        super.onFinish()
                    }
                })
    }

    override fun onDestroy() {
        subscriptions.clear()
    }

}