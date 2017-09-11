package com.example.ishaandhamija.reachout.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ishaandhamija.reachout.Models.User;
import com.example.ishaandhamija.reachout.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button btn_login,btn_signup;
    ProgressDialog progressDialog;
    CoordinatorLayout coordinatorLayout;

    SharedPreferences sharedpreferences;

    public static final String TAG = "volley";
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        progressDialog = new ProgressDialog(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    progressDialog.setMessage("Logging in...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    fetchJson();
                }
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(i);
            }
        });

    }

    private boolean validateFields() {

        String pwd = password.getText().toString();
        String emailId = email.getText().toString();

        if(TextUtils.isEmpty(email.getText().toString())){
            Snackbar snackbar  = Snackbar.make(coordinatorLayout,"Please enter the email!",Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        if(emailId.indexOf('@') == -1){
            email.requestFocus();
            email.setError("Please enter a valid email");
            return false;
        }
        if(TextUtils.isEmpty(password.getText().toString())){
            Snackbar snackbar  = Snackbar.make(coordinatorLayout,"Please enter the password!",Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        if(pwd.length() < 6 ){
            password.requestFocus();
            password.setError("Password too short");
            return  false;
        }


        return true;
    }


    private void fetchJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("email", email.getText().toString());
            json.put("password", password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://harshgoyal.xyz:5199/api/show",
//        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://reach-out-server.herokuapp.com/api/show",
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response == null){
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                progressDialog.dismiss();
                                String name = response.get("name").toString();
                                String age = response.get("age").toString();
                                String bloodgroup = response.get("bloodgroup").toString();
                                String address = response.get("address").toString() ;
                                String contactno = response.get("phonenumber").toString();
                                String email = response.get("email").toString();
                                String password = response.get("password").toString();
                                String sex = response.get("sex").toString();


                                User currentUser  = new User(name,age,bloodgroup,address,contactno,email,password,sex);

                                SharedPreferences.Editor editor = sharedpreferences.edit();

                                editor.putString("savedEmail", email);
                                editor.putString("savedPassword", password);
                                editor.commit();

                                Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.putExtra("name", name);
                                i.putExtra("bloodgroup", bloodgroup);
                                i.putExtra("age", age);
                                i.putExtra("address", address);
                                i.putExtra("contactno", contactno);
                                i.putExtra("email", email);
                                i.putExtra("password", password);
                                i.putExtra("sex", sex);

                                startActivity(i);
                                finish();
                            }
                        } catch (Exception e) {
                            Log.d(TAG, "onResponse: " + e.toString());
                            Toast.makeText(LoginActivity.this, "Dikkat", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(jsonObjectRequest);
    }
}

