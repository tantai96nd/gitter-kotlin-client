package com.github.shchurov.gitterclient.data.network.implementation

import com.github.shchurov.gitterclient.data.network.GitterApi
import com.github.shchurov.gitterclient.data.network.NetworkConverter
import com.github.shchurov.gitterclient.domain.models.Message
import com.github.shchurov.gitterclient.domain.models.Room
import rx.Observable
import java.util.*

class GitterApiImpl(private val retrofitService: GitterRetrofitService,
        private val converter: NetworkConverter) : GitterApi {

    override fun getAccessToken(url: String, clientId: String, clientSecret: String, code: String, redirectUri: String,
            grantType: String) =
            retrofitService.getAccessToken(url, clientId, clientSecret, code, redirectUri, grantType)
                    .map { response -> converter.convertToken(response) };

    override fun getMyRooms(): Observable<MutableList<Room>> =
            retrofitService.getMyRooms()
                    .map { response ->
                        val rooms = ArrayList<Room>()
                        response.mapTo(rooms) { converter.convertRoom(it) }
                    }

    override fun getRoomMessages(roomId: String, limit: Int, beforeId: String?): Observable<MutableList<Message>> =
            retrofitService.getRoomMessages(roomId, limit, beforeId)
                    .map { response ->
                        val messages = ArrayList<Message>()
                        response.mapTo(messages) { converter.convertMessage(it) }
                    }

}