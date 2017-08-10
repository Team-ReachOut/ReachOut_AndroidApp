package com.example.ishaandhamija.reachout.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.ishaandhamija.reachout.R;

public class abc extends AppCompatActivity {

    TextView Name, BloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abc);

        Name = (TextView) findViewById(R.id.Name);
        BloodGroup = (TextView) findViewById(R.id.BloodGroup);

        Name.setText(getIntent().getStringExtra("name"));
        BloodGroup.setText(getIntent().getStringExtra("bloodgroup"));

    }
}
