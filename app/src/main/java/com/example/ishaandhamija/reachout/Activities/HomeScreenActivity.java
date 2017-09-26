package com.example.ishaandhamija.reachout.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ishaandhamija.reachout.R;

public class HomeScreenActivity extends AppCompatActivity {

    Button btnLogin, btnSignup;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignup = (Button) findViewById(R.id.btnSignup);
        textView = (TextView) findViewById(R.id.title);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),"fonts/font1.ttf");

        textView.setTypeface(custom_font);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreenActivity.this, LoginActivity.class));
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreenActivity.this, SignUpActivity.class));
            }
        });

    }

}
