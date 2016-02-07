package com.github.shchurov.gitterclient.data.network.responses;

import com.google.gson.annotations.SerializedName;

public class MessageResponse {

    public String id;
    public String text;
    @SerializedName("sent")
    public String timeIso;
    @SerializedName("fromUser")
    public UserResponse user;
    public boolean unread;


}
