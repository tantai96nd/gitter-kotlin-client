package com.github.shchurov.gitterclient.domain.interactors

import com.github.shchurov.gitterclient.data.preferences.Preferences
import javax.inject.Inject

class CheckAuthInteractor @Inject constructor(
        private val preferences: Preferences
) {

    fun isAuthorized() = preferences.getGitterAccessToken() != null

}