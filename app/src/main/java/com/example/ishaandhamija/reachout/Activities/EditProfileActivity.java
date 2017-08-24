package com.example.ishaandhamija.reachout.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.ishaandhamija.reachout.R;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    EditText name, age, sex, bloodgroup, address, contactno, email, password;
    Button btn_save;
    RadioGroup radioGroup;
    RadioButton selectedRadio;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        radioGroup = (RadioGroup) findViewById(R.id.radioSex);
        bloodgroup = (EditText) findViewById(R.id.bloodgroup);
        address = (EditText) findViewById(R.id.address);
        contactno = (EditText) findViewById(R.id.contactno);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        btn_save = (Button) findViewById(R.id.save);
        selectedRadio = (RadioButton) findViewById(R.id.male);
        
        
        showProfile();


    }

    private void showProfile() {

        Intent i = getIntent();
        String uname = i.getStringExtra("name");
        String uage = i.getStringExtra("age");
        String ubloodgroup = i.getStringExtra("bloodgroup");
        String uaddress = i.getStringExtra("address");
        String ucontactno = i.getStringExtra("contactno");
        String uemail = i.getStringExtra("email");
        String upassword = i.getStringExtra("password");
        String usex = i.getStringExtra("sex");

        name.setText(uname);
        name.requestFocus();
        age.setText(uage);
        bloodgroup.setText(ubloodgroup);
        address.setText(uaddress);
        contactno.setText(ucontactno);
        email.setText(uemail);
        password.setText(upassword);






    }

    @Override
    public void onClick(View view) {

    }
}
