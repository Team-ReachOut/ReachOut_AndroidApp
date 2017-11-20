package com.example.ishaandhamija.reachout.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.ishaandhamija.reachout.R;
import com.example.ishaandhamija.reachout.Utils.RecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class HospitalActivity extends AppCompatActivity {

    TextView hName,hAddress,hContact,hDistance,tvNoService,tvNoSpeciality;
    RecyclerView recyclerView1, recyclerView2;
    RecyclerAdapter recyclerAdapter1, recyclerAdapter2;
    ArrayList<String> serviceList, specialityList;
    String number="";
    JSONArray array2,array1;

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
        tvNoService = (TextView) findViewById(R.id.noservices);
        tvNoSpeciality = (TextView) findViewById(R.id.nospeciality);
        tvNoService.setVisibility(View.INVISIBLE);
        tvNoSpeciality.setVisibility(View.INVISIBLE);

        serviceList = new ArrayList<>();
        specialityList = new ArrayList<>();

        hName.setText(getIntent().getStringExtra("hname"));
        hAddress.setText(getIntent().getStringExtra("haddress"));
        hDistance.setText(getIntent().getStringExtra("hdistance") + " kms away");

        Intent i = getIntent();

        String phone1 = i.getStringExtra("hphonenumber1");
        String phone2 = i.getStringExtra("hphonenumber2");
        String phone3 = i.getStringExtra("hphonenumber3");
        if (!phone1.equals("")){
            number = number+"Enquiries : "+phone1;
        }
        if (!phone2.equals("")){
            number = number + "\n"+ "Appointment : "+ phone2;
        }
        if (!phone3.equals("")){
            number = number + "\n" + "Emergency : " + phone3;
        }
        hContact.setText(number);


        String jsonArray1 = i.getStringExtra("jsonArrayServices");
        String jsonArray2 = i.getStringExtra("jsonArraySpecialities");

//        if (jsonArray1.length() == 0)tvNoService.setVisibility(View.VISIBLE);
//        if (jsonArray2.length() == 0)tvNoSpeciality.setVisibility(View.VISIBLE);


        try {
            array1 = new JSONArray(jsonArray1);
            if (array1.length() ==0 )tvNoService.setVisibility(View.VISIBLE);

            array2 = new JSONArray(jsonArray2);
            if (array2.length() == 0)tvNoSpeciality.setVisibility(View.VISIBLE);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        for (int k=0;k< array1.length();++k){
            try {
                serviceList.add(array1.getString(k));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for (int t=0;t<array2.length();++t){
            try {
                specialityList.add(array2.getString(t));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        recyclerAdapter1 = new RecyclerAdapter(HospitalActivity.this, serviceList);
        recyclerView1.setLayoutManager(new LinearLayoutManager(HospitalActivity.this));
        recyclerView1.setAdapter(recyclerAdapter1);



        recyclerAdapter2 = new RecyclerAdapter(HospitalActivity.this, specialityList);
        recyclerView2.setLayoutManager(new LinearLayoutManager(HospitalActivity.this));
        recyclerView2.setAdapter(recyclerAdapter2);

    }
}