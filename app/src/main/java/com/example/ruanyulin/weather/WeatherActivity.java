package com.example.ruanyulin.weather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ruanyulin.weather.gson.Forecast;
import com.example.ruanyulin.weather.gson.Weather;
import com.example.ruanyulin.weather.util.Http;
import com.example.ruanyulin.weather.util.Utility;

import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    public String biying = "http://api.dujin.org/bing/1366.php";

    private String key = "e81f465dab6a4784af188af62565fc38";

    private String weatherUrl = "http://guolin.tech/api/weather?cityid=";

    private ScrollView scrollView;
    public TextView titlecity;
    private TextView titleupdatetime;
    private TextView degreetext;
    private TextView weatherinfotext;
    private LinearLayout forecastlayout;
    private TextView apitext;
    private TextView pm25text;
    private TextView comforttext;
    private TextView carwashtext;
    private TextView sporttext;

    private TextView aqitext1;
    private TextView aqitext2;
    private TextView aqitext3;

    private TextView suggest1;


    private TextView life1;
    private TextView life2;
    private TextView life3;
    private TextView life4;
    private TextView life5;
    private TextView life6;
    private TextView forcast;


    private Button selectcity;
    private Button about;

    private ImageView bingimg;

    private ImageView bingimg1;

    private ImageView imageView;
    public SwipeRefreshLayout swipeRefreshLayout;

    private TextView warn;

    private TextView warn1;

    public DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorview = getWindow().getDecorView();
            decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        life1 = (TextView) findViewById(R.id.lifetext1);
        life2 = (TextView) findViewById(R.id.lifetext2);
        life3 = (TextView) findViewById(R.id.lifetext3);
        life4 = (TextView) findViewById(R.id.lifetext4);
        life5 = (TextView) findViewById(R.id.lifetext5);
        life6 = (TextView) findViewById(R.id.lifetext6);

        aqitext1 = (TextView) findViewById(R.id.aqitext);
        aqitext2 = (TextView) findViewById(R.id.aqitext1);
        aqitext3 = (TextView) findViewById(R.id.pm25text1);

        suggest1 = (TextView) findViewById(R.id.suggest1);

        forcast = (TextView) findViewById(R.id.forecast);
        warn1 = (TextView)findViewById(R.id.warntext1);

        warn = (TextView) findViewById(R.id.warntext);
        selectcity = (Button) findViewById(R.id.selectbutton);
        about = (Button) findViewById(R.id.aboutbutton);

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

        bingimg = (ImageView) findViewById(R.id.bingimg);
        bingimg1 = (ImageView) findViewById(R.id.bingimg1);
        imageView = (ImageView) findViewById(R.id.chooseimg);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        glideimg();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);



        selectcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeatherActivity.this,Aboutme.class);
                startActivity(intent);
            }
        });
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = preferences.getString("weather",null);

        String weatherId = null;

        //bingimg.setImageAlpha(100);
        bingimg1.setImageAlpha(150);


        if (weatherString != null ){
            Weather weather = Utility.handleWeatherResponse(weatherString);
            weatherId = weather.basic.weatherId;
            //Toast.makeText(WeatherActivity.this,"not",Toast.LENGTH_SHORT).show();
            requestWeather(weatherId);

        } else {//首次打开应用显示北京天气
            requestWeather("CN101010100");
            //Toast.makeText(WeatherActivity.this,"diyic",Toast.LENGTH_SHORT).show();

        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String weaId;
                SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String weatherString1 = preferences1.getString("weather",null);
                if (weatherString1 != null ){
                    Weather weather = Utility.handleWeatherResponse(weatherString1);
                    weaId = weather.basic.weatherId;
                    requestWeather(weaId);

                }
            }
        });
    }


    /**
     * 根据天气id请求城市天气信息
     */
    public void requestWeather(final String weatherId) {
        String weatherurl = weatherUrl + weatherId + "&key=" + key;
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
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responsetext);
                            editor.putString("weather_id",weatherId);
                            editor.apply();
                            showWeatherInfo(weather);
                            Toast.makeText(WeatherActivity.this,"天气更新成功",Toast.LENGTH_SHORT).show();
                        } else {
                                Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();


                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showWeatherInfo(Weather weather) {
        if (weather != null) {

            forcast.setText("未来几天天气预报");
            aqitext1.setText("空气质量");
            aqitext2.setText("空气指数");
            aqitext3.setText("pm2.5");
            suggest1.setText("生活小贴士");
        String cityname = weather.basic.cityName;
        String updatetime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String degree1 = weather.now.temperature;
        String weatherinfo = weather.now.more.info;
        titlecity.setText(cityname);
        titleupdatetime.setText(updatetime);
        degreetext.setText(degree);
        weatherinfotext.setText(weatherinfo);

        TextPaint paint1 = life1.getPaint();
        paint1.setFakeBoldText(true);
        TextPaint paint3 = life3.getPaint();
        paint3.setFakeBoldText(true);
        TextPaint paint5 = life5.getPaint();
        paint5.setFakeBoldText(true);
        //字体
        warn.setTextSize(14);
        warn1.setTextSize(14);
        life1.setTextSize(15);

        life3.setTextSize(15);
        life5.setTextSize(15);

        life1.setText("出门指数");
        life3.setText("穿衣指数");
        life5.setText("运动指数");
        forecastlayout.removeAllViews();
        //获取日期
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String date;

        if (Integer.parseInt(degree1) < 10) {
            warn.setText("天气寒冷，请注意保暖");
            life4.setText("宜穿冬衣");
        } else if (Integer.parseInt(degree1) >= 10 &&Integer.parseInt(degree1) <= 30) {
            warn.setText("天气很舒适");
            life4.setText("宜穿秋衣");
        } else
        if (Integer.parseInt(degree1) > 30 ) {
            if (Integer.parseInt(degree1) > 37) {
                warn.setTextColor(getColor(R.color.red));
                warn1.setTextColor(getColor(R.color.red));
                warn1.setText("高温预警");
                titlecity.setTextColor(getColor(R.color.red));
                degreetext.setTextColor(getColor(R.color.red));
            }
            warn.setText("气温炎热，请注意避暑");
            life4.setText("宜穿夏衣");
        }

        if (weatherinfo.indexOf("雨") != -1) {
            life2.setText("不宜出门");
            life6.setText("不宜运动");
        } else if (weatherinfo.indexOf("晴") != -1) {
            life2.setText("注意防晒");
            life6.setText("不宜运动");
        } else if (weatherinfo.indexOf("多云") != -1) {
            life2.setText("适合出门");
            life6.setText("适合运动");
        } else if (weatherinfo.indexOf("阴") != -1) {
            life2.setText("适宜出门");
            life6.setText("适宜运动");
        }
        int i = 0;
        for (Forecast forecast : weather.forecastList){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastlayout,false);
            TextView datetext = (TextView) view.findViewById(R.id.datetext);
            TextView infotext  = (TextView) view.findViewById(R.id.infotext);
            TextView maxtext = (TextView) view.findViewById(R.id.maxtext);
            date = simpleDateFormat.format(calendar.getTime());
            if (i== 0) {
                datetext.setText("今天" + "(" + forecast.date +")");
                if (forecast.more.info.indexOf("晴") != -1) {
                    warn1.setText("今天晴天，出门请注意防晒");
                } else if (forecast.more.info.indexOf("雨") != -1) {
                    warn1.setText("今天下雨，出门请带伞");
                } else if (forecast.more.info.indexOf("阴") != -1) {
                    warn1.setText("今天阴天，适合出门");
                } else if (forecast.more.info.indexOf("多云") != -1) {
                    //warn1.setText("今天多云");
                }
            } else if (i == 1){
                datetext.setText("明天" + "(" + forecast.date +")");
            } else if (i == 2){
                datetext.setText("后天" + "(" + forecast.date +")");
            }
            infotext.setText(forecast.more.info);
            maxtext.setText(forecast.temperature.max + "℃" + "~" + forecast.temperature.min + "℃");
            forecastlayout.addView(view);
            i++;
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
        }
    }

    protected void glideimg(){

        long time = System.currentTimeMillis();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour < 10) {
            Glide.get(this).clearMemory();
            Glide.get(this).clearDiskCache();
        }

        //choose、title、weather
        Glide.with(this).load(biying).bitmapTransform(new BlurTransformation(WeatherActivity.this,20)).into(imageView);
        Glide.with(this).load(biying).bitmapTransform(new BlurTransformation(WeatherActivity.this,30)).into(bingimg);
        Glide.with(this).load(biying).bitmapTransform(new BlurTransformation(WeatherActivity.this,15)).into(bingimg1);


    }
}
