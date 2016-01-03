package com.github.shchurov.gitterclient.presenters.implementations

import android.util.Log
import com.github.shchurov.gitterclient.helpers.data_manager.DataManager
import com.github.shchurov.gitterclient.helpers.data_manager.DataSource
import com.github.shchurov.gitterclient.helpers.data_manager.DataSubsriber
import com.github.shchurov.gitterclient.models.Room
import com.github.shchurov.gitterclient.network.GitterApi
import com.github.shchurov.gitterclient.network.RequestSubscriber
import com.github.shchurov.gitterclient.network.responses.RoomResponse
import com.github.shchurov.gitterclient.presenters.interfaces.RoomsPresenter
import com.github.shchurov.gitterclient.utils.customSubscribe
import com.github.shchurov.gitterclient.views.interfaces.RoomsView
import rx.subscriptions.CompositeSubscription

class RoomsPresenterImpl(val view: RoomsView) : RoomsPresenter {

    private val subscriptions = CompositeSubscription()

    override fun onCreate() {
        loadRooms()
    }

    private fun loadRooms() {
        DataManager.getMyRooms()
                .customSubscribe(subscriptions, object : DataSubsriber<List<Room>>() {
                    override fun onData(data: List<Room>, source: DataSource) {
                        Log.d("OLOLO", "SOURCE: " + source)
                        for (room in data) {
                            Log.d("OLOLO", room.name + " - " + room.id)
                        }
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