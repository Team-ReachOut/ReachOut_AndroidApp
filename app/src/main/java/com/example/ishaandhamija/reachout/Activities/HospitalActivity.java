package com.example.ishaandhamija.reachout.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.ishaandhamija.reachout.R;

public class HospitalActivity extends AppCompatActivity {

    TextView hName,hAddress,hContact,hDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

        hName = (TextView) findViewById(R.id.hospitalName);
        hAddress = (TextView) findViewById(R.id.hospitalAddress);
        hContact = (TextView) findViewById(R.id.hospitalContact);
        hDistance = (TextView) findViewById(R.id.hospitalDistance);

    }
}
