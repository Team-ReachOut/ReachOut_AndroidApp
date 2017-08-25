package com.example.ishaandhamija.reachout.Activities;

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


        name.setText("Sarthak Mathur");
        name.requestFocus();
        age.setText("20");
        bloodgroup.setText("B+");
        address.setText("Noida");
        contactno.setText("9565689120");
        email.setText("sm@gmail.com");
        password.setText("hello");
        selectedRadio.setChecked(true);

    }

    private void showProfile() {


    }

    @Override
    public void onClick(View view) {

    }
}
