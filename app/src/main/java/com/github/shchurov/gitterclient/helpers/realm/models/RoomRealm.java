package com.github.shchurov.gitterclient.helpers.realm.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

@SuppressWarnings("unused")
public class RoomRealm extends RealmObject {

    public static final String FIELD_LAST_ACCESS_TIME = "lastAccessTime";

    @PrimaryKey
    private String id;
    private String name;
    private String avatar;
    private int unreadItems;
    private int mentions;
    private long lastAccessTime;

    public RoomRealm() {
    }

    public RoomRealm(String id, String name, String avatar, int unreadItems, int mentions,
            long lastAccessTime) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.unreadItems = unreadItems;
        this.mentions = mentions;
        this.lastAccessTime = lastAccessTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getUnreadItems() {
        return unreadItems;
    }

    public void setUnreadItems(int unreadItems) {
        this.unreadItems = unreadItems;
    }

    public int getMentions() {
        return mentions;
    }

    public void setMentions(int mentions) {
        this.mentions = mentions;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

}
