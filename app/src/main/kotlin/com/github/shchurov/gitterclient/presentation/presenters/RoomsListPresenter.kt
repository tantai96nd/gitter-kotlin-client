package com.github.shchurov.gitterclient.presentation.presenters

import com.github.shchurov.gitterclient.data.subscribers.CustomSubscriber
import com.github.shchurov.gitterclient.domain.interactors.GetRoomsInteractor
import com.github.shchurov.gitterclient.domain.interactors.UpdateRoomLastAccessTimeInteractor
import com.github.shchurov.gitterclient.domain.models.Room
import com.github.shchurov.gitterclient.presentation.ui.RoomsListView
import com.github.shchurov.gitterclient.presentation.ui.adapters.RoomsAdapter
import com.github.shchurov.gitterclient.utils.compositeSubscribe
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class RoomsListPresenter @Inject constructor(
        private val view: RoomsListView,
        private val getRoomsInteractor: GetRoomsInteractor,
        private val updateRoomLastAccessTimeInteractor: UpdateRoomLastAccessTimeInteractor
) : RoomsAdapter.ActionListener {

    private val subscriptions = CompositeSubscription()
    private val rooms: MutableList<Room> = arrayListOf()
    private val adapter = RoomsAdapter(rooms, this)

    fun onCreate() {
        view.setRecyclerViewAdapter(adapter)
        loadRooms(false)
    }

    fun onRestart() {
        // delay is required to show nice moving animation
        view.postDelayed({ loadRooms(true) }, 300)
    }

    private fun loadRooms(localOnly: Boolean) {
        if (!localOnly) {
            adapter.loading = true
        }
        getRoomsInteractor.getRooms(localOnly)
                .compositeSubscribe(subscriptions, object : CustomSubscriber<MutableList<Room>>() {
                    override fun onNext(data: MutableList<Room>) {
                        rooms.clear()
                        rooms.addAll(data)
                        adapter.notifyDataSetChanged()
                    }

                    override fun onFinish() {
                        if (!localOnly) {
                            adapter.loading = false
                        }
                    }
                })
    }

    fun onDestroy() {
        subscriptions.clear()
    }

    override fun onRoomClick(room: Room) {
        updateRoomLastAccessTimeInteractor.update(room, System.currentTimeMillis())
        view.goToRoomScreen(room.id, room.name)
    }

}