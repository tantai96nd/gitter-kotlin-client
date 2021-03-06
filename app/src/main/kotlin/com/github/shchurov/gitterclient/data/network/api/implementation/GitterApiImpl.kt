package com.github.shchurov.gitterclient.data.network.api.implementation

import com.github.shchurov.gitterclient.data.network.api.GitterApi
import com.github.shchurov.gitterclient.data.network.api.implementation.retrofit.GitterRetrofitService
import com.github.shchurov.gitterclient.data.preferences.Preferences
import com.github.shchurov.gitterclient.domain.models.Message
import com.github.shchurov.gitterclient.domain.models.Room
import rx.Observable

class GitterApiImpl(
        private val retrofitService: GitterRetrofitService,
        private val preferences: Preferences
) : GitterApi {

    private val converter = NetworkConverter()

    override fun getAccessToken(url: String, clientId: String, clientSecret: String, code: String, redirectUri: String,
                                grantType: String) =
            retrofitService.getAccessToken(url, clientId, clientSecret, code, redirectUri, grantType)
                    .map { response -> converter.convertResponseToToken(response) };

    override fun getMyRooms(): Observable<MutableList<Room>> =
            retrofitService.getMyRooms()
                    .map { response ->
                        val rooms = mutableListOf<Room>()
                        response.mapTo(rooms) { converter.convertResponseToRoom(it) }
                    }

    override fun getRoomMessages(roomId: String, limit: Int, beforeId: String?): Observable<MutableList<Message>> =
            retrofitService.getRoomMessages(roomId, limit, beforeId)
                    .map { response ->
                        val messages = mutableListOf<Message>()
                        response.mapTo(messages) { converter.convertResponseToMessage(it) }
                    }

    override fun markMessagesAsRead(roomId: String, messageIds: List<String>): Observable<*> {
        return retrofitService.markMessagesAsRead(roomId, preferences.getUserId()!!, messageIds)
    }

    override fun getUser() = retrofitService.getUser()
            .map { response -> converter.convertResponseToUser(response[0]) }

    override fun sendMessage(roomId: String, text: String) = retrofitService.sendMessage(roomId, text)
            .map { response -> converter.convertResponseToMessage(response) }

}