package com.github.shchurov.gitterclient.domain.interactors

import com.github.shchurov.gitterclient.domain.models.Token
import rx.Observable

interface GetTokenInteractor {

    fun getAccessToken(code: String): Observable<Token>

}