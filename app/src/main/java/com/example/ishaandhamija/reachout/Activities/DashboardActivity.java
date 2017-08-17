package com.example.ishaandhamija.reachout.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ishaandhamija.reachout.R;
import com.example.ishaandhamija.reachout.Utils.GPSTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.interfaces.DSAKey;
import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    TextView Name, BloodGroup;
    Button btn;
    ListView listView;

    GPSTracker gps;

    Double latitude, longitude;

    ArrayList<String> hospitalList;



    public static final String TAG = "Hospitals";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Name = (TextView) findViewById(R.id.Name);
        BloodGroup = (TextView) findViewById(R.id.BloodGroup);
        btn = (Button) findViewById(R.id.btn);
        listView = (ListView) findViewById(R.id.listView);

        hospitalList = new ArrayList<>();

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this,
                 android.R.layout.simple_list_item_1,
                 android.R.id.text1,
                 hospitalList);

        Name.setText(getIntent().getStringExtra("name"));
        BloodGroup.setText(getIntent().getStringExtra("bloodgroup"));

        gps = new GPSTracker(DashboardActivity.this);

        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }
        else{
            gps.showSettingsAlert();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Log.d(TAG, "onClick: " + latitude.toString() + "\t" + longitude.toString());
                hospitalList.clear();

                final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                        "http://192.168.43.202:5199/api/showall",
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject hospitalObject = null;
                                    try {
                                        hospitalObject = response.getJSONObject(i);
                                        Double hospitalLat = Double.parseDouble(hospitalObject.get("lat").toString());
                                        Double hospitalLon = Double.parseDouble(hospitalObject.get("lon").toString());

                                        Double d = distance(latitude, longitude, hospitalLat, hospitalLon);

                                        if (d < 5.0){
                                            hospitalList.add((String) hospitalObject.get("name"));
//                                            Log.d(TAG, "onResponse: " + hospitalObject.get("name"));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                arrayAdapter.notifyDataSetChanged();
                                listView.setAdapter(arrayAdapter);

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                );

                RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
                requestQueue.add(jsonArrayRequest);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        gps = new GPSTracker(DashboardActivity.this);

        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }
        else{
            gps.showSettingsAlert();
        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
