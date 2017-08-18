package com.example.ruanyulin.weather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ruanyulin on 17-8-18.
 */

public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }
}
