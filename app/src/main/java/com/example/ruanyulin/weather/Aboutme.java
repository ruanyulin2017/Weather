package com.example.ruanyulin.weather;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Aboutme extends AppCompatActivity {

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutme);
        textView1 = (TextView) findViewById(R.id.abouttext1);
        textView2 = (TextView) findViewById(R.id.abouttext2);
        textView3 = (TextView) findViewById(R.id.abouttext3);
        textView4 = (TextView) findViewById(R.id.abouttext4);
        textView5 = (TextView) findViewById(R.id.abouttext5);
        imageView = (ImageView) findViewById(R.id.aboutimg);
        textView1.setShadowLayer(4F,5F,-5F, Color.GRAY);
        textView1.setText(getString(R.string.app_name));
        textView2.setText(getString(R.string.danwei));
        textView3.setText("@" + getString(R.string.auther));
        textView4.setText(getString(R.string.version));
        textView5.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        textView5.setText( getString(R.string.GitHub));
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.about));

        textView5.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                textView5.setTextColor(getColor(R.color.clicked));
                textView5.setShadowLayer(5F,4F,-4F, Color.GRAY);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getString(R.string.github)));
                startActivity(intent);
            }
        });
    }
}
