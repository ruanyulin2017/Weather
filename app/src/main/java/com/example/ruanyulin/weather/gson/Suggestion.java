package com.example.ruanyulin.weather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ruanyulin on 17-8-18.
 */

public class Suggestion {

    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    @SerializedName("sport")
    public Sport sport;
    public class CarWash {
        @SerializedName("txt")
        public String info;
    }

    public class Comfort {
        @SerializedName("txt")
        public String info;
    }

    public class Sport {
        @SerializedName("txt")
        public String info;
    }
}
