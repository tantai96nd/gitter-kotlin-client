package com.github.shchurov.gitterclient.data.network.responses;

import com.google.gson.annotations.SerializedName;

public class ErrorResponse {

    public String error;
    @SerializedName("error_description")
    public String description;

}
