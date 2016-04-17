package com.github.shchurov.gitterclient.domain.interactors

import com.github.shchurov.gitterclient.data.network.api.GitterApi
import com.github.shchurov.gitterclient.domain.interactors.threading.SchedulersProvider
import com.github.shchurov.gitterclient.domain.models.Message
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class SendMessageInteractor @Inject constructor(
        private val gitterApi: GitterApi,
        private val schedulersProvider: SchedulersProvider,
        @Named("roomId") private val roomId: String
) {

    fun sendMessage(text: String): Observable<Message> {
        return Observable.create<Message> { subscriber ->
            Thread.sleep(2000)
            throw RuntimeException()
        }
                //        return gitterApi.sendMessage(roomId, text)
                .subscribeOn(schedulersProvider.background)
                .unsubscribeOn(schedulersProvider.background)
                .observeOn(schedulersProvider.main)
    }

}