package com.github.shchurov.gitterclient.helpers.realm.models;

import io.realm.RealmObject;

public class RoomRealm extends RealmObject {

    public static final String FIELD_ORDER = "order";

    private String id;
    private String name;
    private int unreadItems;
    private int mentions;
    private int order;

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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
