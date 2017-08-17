package com.example.ishaandhamija.reachout.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

public class SignUpActivity extends AppCompatActivity {

    EditText name, age, sex, bloodgroup, address, contactno, email, password;
    Button sign_up_button, btn_login;
    RadioGroup radioGroup;
    RadioButton radiobtnSex;
    int selectedSexId;
    ProgressDialog progressDialog;

    public static final String TAG = "volley";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
//        sex = (EditText) findViewById(R.id.sex);
        radioGroup = (RadioGroup) findViewById(R.id.radioSex);
        bloodgroup = (EditText) findViewById(R.id.bloodgroup);
        address = (EditText) findViewById(R.id.address);
        contactno = (EditText) findViewById(R.id.contactno);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        sign_up_button = (Button) findViewById(R.id.sign_up_button);
        btn_login = (Button) findViewById(R.id.sign_in_button);
        progressDialog = new ProgressDialog(this);

        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateFields()) {
                    progressDialog.setTitle("Registering..");
                    progressDialog.show();

                    fetchJson();
                }
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

    }



    private void fetchJson() {

        JSONObject json = new JSONObject();
        try {
            json.put("name", name.getText().toString());
            json.put("bloodgroup", bloodgroup.getText().toString());
            json.put("email", email.getText().toString());
            json.put("phonenumber", contactno.getText().toString());
            json.put("address", address.getText().toString());
            json.put("age", age.getText().toString());
            json.put("sex",radiobtnSex.getText().toString());
//            json.put("sex", sex.getText().toString());
            json.put("password", password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://192.168.43.202:5199/api/addone",
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Toast.makeText(SignUpActivity.this, "Signed Up", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onResponse: ");
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(SignUpActivity.this, "Dikkat", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(SignUpActivity.this);
        requestQueue.add(jsonObjectRequest);
    }


    private boolean validateFields() {

        selectedSexId = radioGroup.getCheckedRadioButtonId();
        radiobtnSex = (RadioButton) findViewById(selectedSexId);

        if (radiobtnSex == null){
            Log.d(TAG, "validateFields: Sex");
            Toast.makeText(this, "Please enter the sex", Toast.LENGTH_SHORT).show();
            return false;
        }


        if(TextUtils.isEmpty(name.getText().toString())){
            name.setError("Enter the name!");
            return false;
        }
        if(TextUtils.isEmpty(age.getText().toString())){
            age.setError("Enter the age!");
            return false;
        }
//        if(TextUtils.isEmpty(sex.getText().toString())){
//            sex.setError("Enter the sex!");
//            return false;
//        }
        if(TextUtils.isEmpty(bloodgroup.getText().toString())){
            bloodgroup.setError("Enter the bloodgroup!");
            return false;
        }
        if(TextUtils.isEmpty(address.getText().toString())){
            address.setError("Enter the address!");
            return false;
        }
        if(TextUtils.isEmpty(contactno.getText().toString())){
            contactno.setError("Enter the contact no.!");
            return false;
        }
        if(TextUtils.isEmpty(email.getText().toString())){
            email.setError("Enter the email!");
            return false;
        }
        if(TextUtils.isEmpty(password.getText().toString())){
            password.setError("Enter the password!");
            return false;
        }
        return true;
    }
}
