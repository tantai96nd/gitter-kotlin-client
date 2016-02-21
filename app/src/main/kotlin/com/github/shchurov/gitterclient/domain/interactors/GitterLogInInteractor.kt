package com.github.shchurov.gitterclient.domain.interactors

import com.github.shchurov.gitterclient.domain.models.User
import rx.Observable

interface GitterLogInInteractor {

    fun logIn(code: String): Observable<User>

}