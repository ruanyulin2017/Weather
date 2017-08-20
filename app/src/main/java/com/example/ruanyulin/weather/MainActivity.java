package com.example.ruanyulin.weather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int flag;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
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
}
