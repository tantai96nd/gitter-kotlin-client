package com.github.shchurov.gitterclient.helpers.realm.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

@SuppressWarnings("unused")
public class MessageRealm extends RealmObject {

    public static final String FIELD_ROOM_ID = "roomId";
    public static final String FIELD_TIME = "time";

    @PrimaryKey
    private String id;
    private String roomId;
    private String text;
    private long time;
    private UserRealm user;
    private boolean unread;

    public MessageRealm() {
    }

    public MessageRealm(String id, String roomId, String text, long time, UserRealm user,
            boolean unread) {
        this.id = id;
        this.roomId = roomId;
        this.text = text;
        this.time = time;
        this.user = user;
        this.unread = unread;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public UserRealm getUser() {
        return user;
    }

    public void setUser(UserRealm user) {
        this.user = user;
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }

}
