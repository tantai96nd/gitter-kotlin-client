package com.github.shchurov.gitterclient.data.network.responses;

import com.google.gson.annotations.SerializedName;

public class RoomResponse {

    public String id;
    public String name;
    public int unreadItems;
    public int mentions;
    public String url;
    @SerializedName("lastAccessTime")
    public String lastAccessTimeIso;

}
