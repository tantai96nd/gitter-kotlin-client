package com.github.shchurov.gitterclient.presentation.navigators

import android.support.v7.app.AppCompatActivity
import com.github.shchurov.gitterclient.presentation.ui.activities.RoomActivity
import javax.inject.Inject

class RoomsListNavigator @Inject constructor(
        private val activity: AppCompatActivity
) {

    fun goToRoomScreen(id: String, name: String) {
        RoomActivity.start(activity, id, name)
    }

}