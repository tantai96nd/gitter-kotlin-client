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

    fun convertToken(tokenResponse: TokenResponse): Token

    fun convertRoom(roomResponse: RoomResponse): Room

    fun convertMessage(messageResponse: MessageResponse): Message

    fun convertUser(userResponse: UserResponse): User

}