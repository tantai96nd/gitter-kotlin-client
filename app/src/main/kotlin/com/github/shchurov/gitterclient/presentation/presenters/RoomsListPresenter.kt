package com.github.shchurov.gitterclient.presentation.presenters

import com.github.shchurov.gitterclient.data.subscribers.DefaultSubscriber
import com.github.shchurov.gitterclient.domain.interactors.GetRoomsInteractor
import com.github.shchurov.gitterclient.domain.models.Room
import com.github.shchurov.gitterclient.presentation.navigators.RoomsListNavigator
import com.github.shchurov.gitterclient.presentation.ui.RoomsListView
import com.github.shchurov.gitterclient.utils.compositeSubscribe
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class RoomsListPresenter @Inject constructor(
        private val getRoomsInteractor: GetRoomsInteractor,
        private val navigator: RoomsListNavigator
) : BasePresenter<RoomsListView>() {

    private val subscriptions = CompositeSubscription()

    override fun onAttach() {
        loadRooms(false)
    }

    private fun loadRooms(localOnly: Boolean) {
        getView().showLoading()
        getRoomsInteractor.get(localOnly)
                .compositeSubscribe(subscriptions, object : DefaultSubscriber<MutableList<Room>>() {
                    override fun onNext(data: MutableList<Room>) {
                        getView().displayRooms(data)
                    }

                    override fun onFinish() {
                        getView().hideLoading()
                    }
                })
    }

    fun onReturnFromBackground() {
        loadRooms(true)
    }

    override fun onDetach() {
        subscriptions.clear()
        super.onDetach()
    }

    fun onRoomClick(room: Room) {
        navigator.goToRoomScreen(room.id, room.name)
    }

}