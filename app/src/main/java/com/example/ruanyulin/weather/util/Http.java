package com.example.ruanyulin.weather.util;

import android.widget.Toast;

import com.example.ruanyulin.weather.MainActivity;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by ruanyulin on 17-8-17.
 */

public class Http {
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
