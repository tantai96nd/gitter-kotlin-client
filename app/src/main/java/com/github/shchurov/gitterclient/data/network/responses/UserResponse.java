package com.github.shchurov.gitterclient.data.network.responses;

import com.google.gson.annotations.SerializedName;

public class UserResponse {

    public String id;
    public String username;
    @SerializedName("avatarUrlSmall")
    public String avatar;

}
