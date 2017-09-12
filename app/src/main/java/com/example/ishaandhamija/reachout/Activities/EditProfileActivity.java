package com.example.ishaandhamija.reachout.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ishaandhamija.reachout.R;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    EditText name, age, bloodgroup, address, contactno, email, password;
    Button btn_save;
    CircleImageView profilePic;
    RadioGroup radioGroup;
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profilePic = (CircleImageView) findViewById(R.id.profilePic);
        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        radioGroup = (RadioGroup) findViewById(R.id.radioSex);
        bloodgroup = (EditText) findViewById(R.id.bloodgroup);
        address = (EditText) findViewById(R.id.address);
        contactno = (EditText) findViewById(R.id.contactno);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        btn_save = (Button) findViewById(R.id.save);

        btn_save.setOnClickListener(this);

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
        email.setEnabled(false);
        email.setTextColor(Color.GRAY);
        password.setText(upassword);

        if(usex.equals("Male")){
            radioGroup.check(R.id.male);
        }
        else if(usex.equals("Female")){
            radioGroup.check(R.id.female);
        }
        else{
            radioGroup.check(R.id.other);
        }


    }

    @Override
    public void onClick(View view) {

        if(view == btn_save){
            saveChangedInfo();
        }

    }

    private void saveChangedInfo() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());

        JSONObject json = new JSONObject();
        try {
            json.put("name", name.getText().toString());
            json.put("bloodgroup", bloodgroup.getText().toString());
            json.put("email", email.getText().toString());
            json.put("phonenumber", contactno.getText().toString());
            json.put("address", address.getText().toString());
            json.put("age", age.getText().toString());
            json.put("sex",radioButton.getText().toString());
            json.put("password", password.getText().toString());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://192.168.43.202:5199/api/update",
//        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://harshgoyal.xyz:5199/api/update",
//        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://reach-out-server.herokuapp.com/api/addone",
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(EditProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        requestQueue.add(jsonObjectRequest);

    }
}
