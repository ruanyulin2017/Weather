package com.example.ruanyulin.weather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by ruanyulin on 17-8-17.
 */

public class County extends DataSupport{
    private String countyName;
    private int id;
    private int cityId;
    private String weatherId;

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCountyName(){
        return countyName;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setCityId(int cityId){
        this.cityId = cityId;
    }

    public int getCityId(){
        return cityId;
    }

    public void setWeatherId(String weatherId){
        this.weatherId = weatherId;
    }

    public String getWeatherId(){
        return weatherId;
    }

}
