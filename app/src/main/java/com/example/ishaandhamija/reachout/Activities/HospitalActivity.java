package com.example.ishaandhamija.reachout.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.ishaandhamija.reachout.R;
import com.example.ishaandhamija.reachout.Utils.RecyclerAdapter;

import java.util.ArrayList;

public class HospitalActivity extends AppCompatActivity {

    TextView hName,hAddress,hContact,hDistance;
    RecyclerView recyclerView1, recyclerView2;
    RecyclerAdapter recyclerAdapter1, recyclerAdapter2;
    ArrayList<String> serviceList, specialityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

        hName = (TextView) findViewById(R.id.hospitalName);
        hAddress = (TextView) findViewById(R.id.hospitalAddress);
        hContact = (TextView) findViewById(R.id.hospitalContact);
        hDistance = (TextView) findViewById(R.id.hospitalDistance);
        recyclerView1 = (RecyclerView) findViewById(R.id.rvList1);
        recyclerView2 = (RecyclerView) findViewById(R.id.rvList2);

        hName.setText(getIntent().getStringExtra("hname"));
        hAddress.setText(getIntent().getStringExtra("haddress"));
        hContact.setText(getIntent().getStringExtra("hphonenumber"));
        hDistance.setText(getIntent().getStringExtra("hdistance") + " kms away");

        serviceList = new ArrayList<>();
        serviceList.add("A");
        serviceList.add("B");
        serviceList.add("C");
        recyclerAdapter1 = new RecyclerAdapter(HospitalActivity.this, serviceList);
        recyclerView1.setLayoutManager(new LinearLayoutManager(HospitalActivity.this));
        recyclerView1.setAdapter(recyclerAdapter1);

        specialityList = new ArrayList<>();
        specialityList.add("D");
        specialityList.add("E");
        specialityList.add("F");
        recyclerAdapter2 = new RecyclerAdapter(HospitalActivity.this, specialityList);
        recyclerView2.setLayoutManager(new LinearLayoutManager(HospitalActivity.this));
        recyclerView2.setAdapter(recyclerAdapter2);

    }
}