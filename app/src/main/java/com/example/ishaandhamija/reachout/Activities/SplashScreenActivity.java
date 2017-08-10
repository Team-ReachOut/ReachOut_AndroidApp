package com.example.ishaandhamija.reachout.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.ishaandhamija.reachout.R;

public class SplashScreenActivity extends AppCompatActivity {

    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        title = (TextView) findViewById(R.id.title);

        Typeface typeFace = Typeface.createFromAsset(getAssets(),"fonts/font2.ttf");
        title.setTypeface(typeFace);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, HomeScreenActivity.class));
            }
        }, 2000);

    }
}