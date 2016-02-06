package com.github.shchurov.gitterclient.domain.models

class Message(val id: String, var text: String, val time: Long, val user: User, var unread: Boolean)