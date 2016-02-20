package com.github.shchurov.gitterclient.data.database.implementation.realm_models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

@Suppress("RedundantVisibilityModifier")
open class RoomRealm(
        @PrimaryKey
        public open var id: String?,
        public open var name: String?,
        public open var avatar: String?,
        public open var unreadItems: Int,
        public open var mentions: Int,
        public open var lastAccessTimestamp: Long
) : RealmObject() {

    companion object {
        const val FIELD_LAST_ACCESS_TIMESTAMP = "lastAccessTimestamp"
    }

    constructor() : this(null, null, null, 0, 0, 0L)

}