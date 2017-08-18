package com.example.ruanyulin.weather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ruanyulin on 17-8-18.
 */

public class Weather {
    public String status;
    public Basic basic;
    public API api;
    //public Forecast forecast;
    public Now now;
    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
