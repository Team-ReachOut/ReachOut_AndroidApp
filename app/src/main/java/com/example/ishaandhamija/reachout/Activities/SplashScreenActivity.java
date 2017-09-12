package com.example.ishaandhamija.reachout.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
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

public class SplashScreenActivity extends AppCompatActivity {

    TextView title;
    String savedEmail, savedPassword;
    public static final String MyPREFERENCES = "MyPrefs" ;

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        title = (TextView) findViewById(R.id.title);

        Typeface typeFace = Typeface.createFromAsset(getAssets(),"fonts/font2.ttf");
        title.setTypeface(typeFace);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        savedEmail = sharedpreferences.getString("savedEmail", null);
        savedPassword = sharedpreferences.getString("savedPassword", null);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (savedEmail == null && savedPassword == null) {

                    Intent intent = new Intent(SplashScreenActivity.this, HomeScreenActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                    finish();
                }
                else {
                    fetchJson();
                }
            }
        }, 2000);
    }

    private void fetchJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("email", savedEmail);
            json.put("password", savedPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://192.168.43.202:5199/api/show",
//        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://harshgoyal.xyz:5199/api/show",
//        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://reach-out-server.herokuapp.com/api/show",
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response == null){
//                                progressDialog.dismiss();
//                                Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                            }
                            else{
//                                progressDialog.dismiss();
                                String name = response.get("name").toString();
                                String age = response.get("age").toString();
                                String bloodgroup = response.get("bloodgroup").toString();
                                String address = response.get("address").toString() ;
                                String contactno = response.get("phonenumber").toString();
                                String email = response.get("email").toString();
                                String password = response.get("password").toString();
                                String sex = response.get("sex").toString();

                                User currentUser  = new User(name,age,bloodgroup,address,contactno,email,password,sex);

                                Intent i = new Intent(SplashScreenActivity.this, DashboardActivity.class);
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
                            Toast.makeText(SplashScreenActivity.this, "Dikkat", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(SplashScreenActivity.this);
        requestQueue.add(jsonObjectRequest);
    }

}