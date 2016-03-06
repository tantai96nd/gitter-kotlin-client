package com.github.shchurov.gitterclient.domain.interactors

import com.github.shchurov.gitterclient.domain.models.Message

interface MarkMessageAsReadInteractor {

    fun markAsReadLazy(message: Message, roomId: String)

    fun flush()

}