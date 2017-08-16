package com.example.ishaandhamija.reachout.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.ishaandhamija.reachout.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button btn_login;

    public static final String TAG = "volley";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        btn_login = (Button) findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject json = new JSONObject();
                try {
                    json.put("email", email.getText().toString());
                    json.put("password", password.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://192.168.43.202:5000/api/show",
                        json,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response == null){
                                        Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(LoginActivity.this, "Name : " + response.get("name") + "\n" + "BloodGroup : " + response.get("bloodgroup"), Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                                        i.putExtra("name", response.get("name").toString());
                                        i.putExtra("bloodgroup", response.get("bloodgroup").toString());
                                        startActivity(i);
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
                            }
                        });
                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                requestQueue.add(jsonObjectRequest);
            }
        });

    }
}
