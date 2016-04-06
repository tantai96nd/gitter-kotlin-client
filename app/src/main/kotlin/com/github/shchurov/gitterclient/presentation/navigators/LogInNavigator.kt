package com.github.shchurov.gitterclient.presentation.navigators

import android.support.v7.app.AppCompatActivity
import com.github.shchurov.gitterclient.presentation.ui.activities.RoomsListActivity
import javax.inject.Inject

class LogInNavigator @Inject constructor(
        private val activity: AppCompatActivity
) {

    fun goToRoomsListScreen() {
        RoomsListActivity.start(activity)
        activity.finish()
    }

}