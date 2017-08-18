package com.example.ruanyulin.weather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by ruanyulin on 17-8-17.
 */

public class City extends DataSupport {
    private String cityName;
    private int id;
    private int cityCode;
    private int provinceId;

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName(){
        return cityName;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setCityCode(int cityCode){
        this.cityCode = cityCode;
    }

    public int getCityCode(){
        return cityCode;
    }

    public void setProvindeId(int provinceId){
        this.provinceId = provinceId;
    }

    public int getProvindeId(){
        return provinceId;
    }



}
