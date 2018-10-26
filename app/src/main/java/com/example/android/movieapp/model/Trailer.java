package com.example.android.movieapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmed on 28/09/18.
 */

public class Trailer {

    @SerializedName("key")
    private String key;
    @SerializedName("name")
    private String name;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
