package com.example.ishaandhamija.reachout.Activities;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ishaandhamija.reachout.Interfaces.GetHospitals;
import com.example.ishaandhamija.reachout.Models.Hospital;
import com.example.ishaandhamija.reachout.R;
import com.example.ishaandhamija.reachout.Utils.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.interfaces.DSAKey;
import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements OnMapReadyCallback{

    GPSTracker gps;

    Double latitude, longitude;

    ArrayList<Hospital> hospitalList;

    GetHospitals getHospitals;

    public static final String TAG = "Hospitals";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        hospitalList = new ArrayList<>();

        gps = new GPSTracker(DashboardActivity.this);

        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }
        else{
            gps.showSettingsAlert();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        hospitalList.clear();

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                "http://192.168.43.202:5199/api/showall",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        ArrayList<LatLng> latlonList = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject hospitalObject = null;
                            try {
                                hospitalObject = response.getJSONObject(i);
                                Double hospitalLat = Double.parseDouble(hospitalObject.get("lat").toString());
                                Double hospitalLon = Double.parseDouble(hospitalObject.get("lon").toString());

                                Double d = distance(latitude, longitude, hospitalLat, hospitalLon);

                                if (d < 5.0){
                                    hospitalList.add(new Hospital(hospitalObject.getString("name"), hospitalObject.getString("email"),
                                            hospitalObject.getLong("phonenumber"), hospitalObject.getString("address"),
                                            hospitalObject.getString("password"), hospitalObject.getString("driver_name"),
                                            hospitalObject.getLong("driver_pnumber"), hospitalObject.getString("ambulance"),
                                            hospitalObject.getString("lat"), hospitalObject.getString("lon")));

                                    latlonList.add(new LatLng(Double.parseDouble(hospitalObject.getString("lat")),
                                            Double.parseDouble(hospitalObject.getString("lon"))));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        getHospitals.onSuccess(latlonList);

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

    @Override
    public void onMapReady(final GoogleMap map) {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("My Location"));

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12.0f));

        map.addCircle(new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius(5000)
                .strokeWidth(0f)
                .fillColor(getTransparentColor(Color.parseColor("#4682b4"))));

        getHospitals = new GetHospitals() {
            @Override
            public void onSuccess(ArrayList<LatLng> latlonList) {
                for (int i=0;i<latlonList.size();i++){
                    map.addMarker(new MarkerOptions().position(latlonList.get(i)));
                }
            }
        };

    }

    private int getTransparentColor(int color){
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        alpha *= 0.5;

        return Color.argb(alpha, red, green, blue);
    }

    @Override
    protected void onResume() {
        super.onResume();

        gps = new GPSTracker(DashboardActivity.this);

        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }
        else{
            Toast.makeText(gps, "Turn on Location", Toast.LENGTH_SHORT).show();
            finish();
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
