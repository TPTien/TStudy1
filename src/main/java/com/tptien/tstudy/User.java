package com.tptien.tstudy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("response")
    @Expose
    private String Response;

    @SerializedName("displayname")
    @Expose
    private String displayName;

    public String getResponse() {
        return Response;
    }

    public String getDisplayName() {
        return displayName;
    }
}
