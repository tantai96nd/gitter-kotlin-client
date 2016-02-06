package com.github.shchurov.gitterclient.domain.interactors

import com.github.shchurov.gitterclient.data.network.retrofit.RetrofitManager
import com.github.shchurov.gitterclient.data.network.retrofit.GitterRetrofitService
import com.github.shchurov.gitterclient.domain.DataSource
import com.github.shchurov.gitterclient.domain.DataWrapper
import com.github.shchurov.gitterclient.presentation.Converters
import com.github.shchurov.gitterclient.utils.applySchedulers

class RoomMessagesInteractor(val gitterService: GitterRetrofitService) {

    fun getRoomMessagesFirstPage(roomId: String, limit: Int) =
            gitterService.getRoomMessages(roomId, limit)
                    .map { messagesResponse ->
                        messagesResponse.reverse()
                        val messages = messagesResponse.map { Converters.messageNetworkToUi(it) }
                        DataWrapper(messages, DataSource.NETWORK)
                    }
                    .applySchedulers()

    fun getRoomMessagesNextPage(roomId: String, limit: Int, beforeId: String) =
            RetrofitManager.gitterService.getRoomMessages(roomId, limit, beforeId)
                    .map { messagesResponse ->
                        messagesResponse.reverse()
                        val messages = messagesResponse.map { Converters.messageNetworkToUi(it) }
                        DataWrapper(messages, DataSource.NETWORK)
                    }
                    .applySchedulers()

}