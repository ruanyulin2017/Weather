package com.example.ruanyulin.weather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private int flag;
    private View choose;
    private View weather;
    private ImageView chooseimg;
    private ImageView weatherimg;
    private ImageView titleimg;
    private TextView textView;

    private String biying = "http://api.dujin.org/bing/1366.php";

    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        //textView = (TextView) findViewById(R.id.welcometext);
        //textView.setText(getString(R.string.version));
        if (Build.VERSION.SDK_INT >= 21) {
            View decorview = getWindow().getDecorView();
            decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,WeatherActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        },0);
/*
        getFlag();
        if (preferences.getString("weather",null) != null && flag == 0){
            Intent intent = new Intent(this,WeatherActivity.class);
            startActivity(intent);
            finish();
        } else if (flag != 0) {
            Toast.makeText(this,"00",Toast.LENGTH_SHORT).show();
        }
    }

    public void getFlag(){
       flag = getIntent().getIntExtra("flag",0);

    }
*/
    }
}
