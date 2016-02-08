package com.github.shchurov.gitterclient.data.network

import com.github.shchurov.gitterclient.data.network.responses.MessageResponse
import com.github.shchurov.gitterclient.data.network.responses.RoomResponse
import com.github.shchurov.gitterclient.data.network.responses.TokenResponse
import com.github.shchurov.gitterclient.data.network.responses.UserResponse
import com.github.shchurov.gitterclient.domain.models.Message
import com.github.shchurov.gitterclient.domain.models.Room
import com.github.shchurov.gitterclient.domain.models.Token
import com.github.shchurov.gitterclient.domain.models.User

interface NetworkConverter {

    fun convertToToken(tokenResponse: TokenResponse): Token

    fun convertToRoom(roomResponse: RoomResponse): Room

    fun convertToMessage(messageResponse: MessageResponse): Message

    fun convertToUser(userResponse: UserResponse): User

}