package com.example.ruanyulin.weather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ScrollingView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ruanyulin.weather.gson.Forecast;
import com.example.ruanyulin.weather.gson.Weather;
import com.example.ruanyulin.weather.util.Http;
import com.example.ruanyulin.weather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView scrollView;
    private TextView titlecity;
    private TextView titleupdatetime;
    private TextView degreetext;
    private TextView weatherinfotext;
    private LinearLayout forecastlayout;
    private TextView apitext;
    private TextView pm25text;
    private TextView comforttext;
    private TextView carwashtext;
    private TextView sporttext;

    private Button selectcity;

    private ImageView bingimg;

    private ImageView bingimg1;
    public SwipeRefreshLayout swipeRefreshLayout;

    private TextView warn;

    public DrawerLayout drawerLayout;

    String weatherId = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorview = getWindow().getDecorView();
            decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        warn = (TextView) findViewById(R.id.warntext);
        selectcity = (Button) findViewById(R.id.selectbutton);

        scrollView = (ScrollView) findViewById(R.id.weatherlayout);
        titlecity = (TextView) findViewById(R.id.titlecity);
        titleupdatetime = (TextView) findViewById(R.id.titleupdatetime);
        degreetext = (TextView) findViewById(R.id.degreetext);
        weatherinfotext = (TextView) findViewById(R.id.weatherinfotext);
        forecastlayout = (LinearLayout) findViewById(R.id.forecastlayout);
        apitext = (TextView) findViewById(R.id.apitext);
        pm25text = (TextView) findViewById(R.id.pm25text);
        comforttext = (TextView) findViewById(R.id.comfortinfo);
        carwashtext = (TextView) findViewById(R.id.carwashtext);
        sporttext = (TextView) findViewById(R.id.sporttext);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        bingimg = (ImageView) findViewById(R.id.bingimg);

        bingimg1 = (ImageView) findViewById(R.id.bingimg1);

        selectcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*final int flag1 = 1;
                Intent intent = new Intent(WeatherActivity.this,MainActivity.class);
                intent.putExtra("flag",flag1);
                //Toast.makeText(WeatherActivity.this,"select",Toast.LENGTH_SHORT).show();
                startActivity(intent);
                //finish();*/
                drawerLayout.openDrawer(GravityCompat.START);

            }
        });
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = preferences.getString("weather",null);


        Glide.with(this).load("http://api.dujin.org/bing/1920.php").into(bingimg1);
        Glide.with(this).load("http://api.dujin.org/bing/1920.php").into(bingimg);
        if (weatherString != null){
            Weather weather = Utility.handleWeatherResponse(weatherString);
            //weatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        } else {
            weatherId = getIntent().getStringExtra("weather_id");
            //weatherLayout.setV
            scrollView.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
        final String finalWeatherId = weatherId;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(finalWeatherId);
            }
        });
    }

    /**
     * 根据天气id请求城市天气信息
     */
    public void requestWeather(final String weatherId) {
        String weatherurl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=e81f465dab6a4784af188af62565fc38";
        Http.sendOkHttpRequest(weatherurl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String responsetext = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responsetext);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responsetext);
                            editor.apply();
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
        Glide.with(this).load("http://api.dujin.org/bing/1920.php").into(bingimg);
        Glide.with(this).load("http://api.dujin.org/bing/1920.php").into(bingimg1);
        //loadBingPic();
    }

    private void showWeatherInfo(Weather weather) {
        String cityname = weather.basic.cityName;
        String updatetime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String degree1 = weather.now.temperature;
        String weatherinfo = weather.now.more.info;
        titlecity.setText(cityname);
        titleupdatetime.setText(updatetime);
        degreetext.setText(degree);
        weatherinfotext.setText(weatherinfo);
        forecastlayout.removeAllViews();
        if (Integer.parseInt(degree1) < 10) {
            warn.setText("天气寒冷，请注意保暖");
        } else if (Integer.parseInt(degree1) >= 10 &&Integer.parseInt(degree1) <= 30) {
            warn.setText("天气很舒适");
        } else
        if (Integer.parseInt(degree1) > 30) {
            warn.setText("气温炎热，请注意避暑");
        }
        for (Forecast forecast : weather.forecastList){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastlayout,false);
            TextView datetext = (TextView) view.findViewById(R.id.datetext);
            TextView infotext  = (TextView) view.findViewById(R.id.infotext);
            TextView maxtext = (TextView) view.findViewById(R.id.maxtext);
            TextView mintext = (TextView) view.findViewById(R.id.mintext);
            datetext.setText(forecast.date);
            infotext.setText(forecast.more.info);
            maxtext.setText(forecast.temperature.max);
            mintext.setText(forecast.temperature.min);
            forecastlayout.addView(view);
        }
        if (weather.api != null) {
            apitext.setText(weather.api.city.api);
            pm25text.setText(weather.api.city.pm25);
        }
        String comfort = "舒适度:\n" + weather.suggestion.sport.info;
        String carwash = "洗车指数:\n" + weather.suggestion.carWash.info;
        String sport = "运动指数:\n" + weather.suggestion.sport.info;
        comforttext.setText(comfort);
        carwashtext.setText(carwash);
        sporttext.setText(sport);
        scrollView.setVisibility(View.VISIBLE);
    }
}
