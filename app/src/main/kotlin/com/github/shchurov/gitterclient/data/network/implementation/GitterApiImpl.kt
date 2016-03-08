package com.github.shchurov.gitterclient.data.network.implementation

import com.github.shchurov.gitterclient.data.Preferences
import com.github.shchurov.gitterclient.data.network.GitterApi
import com.github.shchurov.gitterclient.domain.models.Message
import com.github.shchurov.gitterclient.domain.models.Room
import rx.Observable
import java.util.*

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
                        val rooms = ArrayList<Room>()
                        response.mapTo(rooms) { converter.convertResponseToRoom(it) }
                    }

    override fun getRoomMessages(roomId: String, limit: Int, beforeId: String?): Observable<MutableList<Message>> =
            retrofitService.getRoomMessages(roomId, limit, beforeId)
                    .map { response ->
                        val messages = ArrayList<Message>()
                        response.mapTo(messages) { converter.convertResponseToMessage(it) }
                    }

    override fun markMessagesAsRead(messageIds: List<String>, roomId: String?) =
            retrofitService.markMessagesAsRead(roomId, preferences.getUserId()!!, messageIds)

    override fun getUser() = retrofitService.getUser()
            .map { response -> converter.convertResponseToUser(response[0]) }

}