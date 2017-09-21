package com.example.ishaandhamija.reachout.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ishaandhamija.reachout.Interfaces.GetHospitals;
import com.example.ishaandhamija.reachout.Interfaces.GetLocation;
import com.example.ishaandhamija.reachout.Models.Hospital;
import com.example.ishaandhamija.reachout.R;
import com.example.ishaandhamija.reachout.Utils.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements OnMapReadyCallback{

    GPSTracker gps;

    public static Double latitude, longitude;

    ArrayList<Hospital> hospitalList;

    GetHospitals getHospitals;

    TextView hospitalName, hospitalAddress, hospitalDistance;
    CardView hospitalInfo;
    FloatingActionButton emegencyBtn;

    SharedPreferences sharedpreferences;

//    MyAdapter myAdapter1,myAdapter2;
    ArrayList<String> members;
    ArrayList<String> emergencies;
    MaterialSpinner spinner1,spinner2;
    Button btnSubmit;



    ArrayList<Marker> myMarkers;
    Marker myMarker;

    public static GetLocation getLocation;

    public static Boolean gpsIsEnabled = false;

    public static final String TAG = "Hospitals";
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_dashboard);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getSupportActionBar().setTitle("");

        hospitalName = (TextView) findViewById(R.id.hospitalName);
        hospitalAddress = (TextView) findViewById(R.id.hospitalAddress);
        hospitalDistance = (TextView) findViewById(R.id.hospitalDistance);
        hospitalInfo = (CardView) findViewById(R.id.hospitalInfo);
        emegencyBtn = (FloatingActionButton) findViewById(R.id.emergencyBtn);



        hospitalList = new ArrayList<>();
        members = new ArrayList<>();
        emergencies = new ArrayList<>();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        gps = new GPSTracker(DashboardActivity.this);

        getLocation = new GetLocation() {
            @Override
            public void onSuccess() {
                final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
//                "http://harshgoyal.xyz:5199/api/showall",
//                "https://reach-out-server.herokuapp.com/api/showall",
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

                                        Double d = distance(latitude, longitude, hospitalLat, hospitalLon, 'M');

                                        if (d < 5000.0){
                                            hospitalList.add(new Hospital(hospitalObject.getString("name"), hospitalObject.getString("email"),
                                                    hospitalObject.getLong("phonenumber"), hospitalObject.getString("address"),
                                                    hospitalObject.getString("password"), hospitalObject.getString("driver_name"),
                                                    hospitalObject.getLong("driver_pnumber"), hospitalObject.getString("ambulance"),
                                                    hospitalObject.getString("lat"), hospitalObject.getString("lon")));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                getHospitals.onSuccess(hospitalList);

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
        };

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        hospitalList.clear();

        if (gps.getIsGPSTrackingEnabled() && gps.canGetLocation()){
            gpsIsEnabled = true;
            gps.getLocation();
        }
        else{
            gps.showSettingsAlert();
        }

        emegencyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DashboardActivity.this, "Emergency Btn", Toast.LENGTH_SHORT).show();
                final Dialog emergencyDialog = new Dialog(DashboardActivity.this);
                emergencyDialog.getWindow().setBackgroundDrawable(null);
                emergencyDialog.setContentView(R.layout.emergency_dialog);
                WindowManager.LayoutParams lp = emergencyDialog.getWindow().getAttributes();
                Window window = emergencyDialog.getWindow();
                lp.copyFrom(window.getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
                lp.gravity = Gravity.CENTER;
                emergencyDialog.setCancelable(false);
                emergencyDialog.show();
                spinner1 = (MaterialSpinner) emergencyDialog.findViewById(R.id.spinner1);
                spinner2 = (MaterialSpinner) emergencyDialog.findViewById(R.id.spinner2);
                spinner1.setItems("Ice", "Jelly Bean", "KitKat", "Lollipop", "Marshmallow");
                spinner2.setItems("Cardiac Arrest", "Severe Accident", "Pneumonia", "Appendicitis", "Bone Fracture","Facial Trauma","Acid Attack","Respiratory failure","Electric Shock");
                spinner1.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
//                        Toast.makeText(DashboardActivity.this,"Clicked : "+item , Toast.LENGTH_SHORT).show();
                    }
                });
                btnSubmit = (Button) emergencyDialog.findViewById(R.id.btnSubmit);
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(DashboardActivity.this, "Response Recorded!!", Toast.LENGTH_SHORT).show();
                        emergencyDialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap map) {

        getHospitals = new GetHospitals() {
            @Override
            public void onSuccess(ArrayList<Hospital> latlonList) {

                myMarkers = new ArrayList<>();

                myMarker = map.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title("My Location")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bluedot)));

                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12.0f));

                map.addCircle(new CircleOptions()
                        .center(new LatLng(latitude, longitude))
                        .radius(5000)
                        .strokeWidth(0f)
                        .fillColor(getTransparentColor(Color.parseColor("#4682b4"))));

                for (int i=0;i<latlonList.size();i++){
                    Marker marker = map.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(hospitalList.get(i).getLat()), Double.parseDouble(hospitalList.get(i).getLon())))
                            .title(hospitalList.get(i).getName())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder)));

                    myMarkers.add(marker);
                }
            }
        };

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                if (marker.equals(myMarker)){
                    return true;
                }

                for (int i=0;i<myMarkers.size();i++){
                    if (marker.equals(myMarkers.get(i))){

                        hospitalName.setText(hospitalList.get(i).getName());
                        hospitalAddress.setText(hospitalList.get(i).getAddress());

                        Double dist = distance(latitude, latitude,
                                Double.parseDouble(hospitalList.get(i).getLat()),
                                Double.parseDouble(hospitalList.get(i).getLon()), 'M');

                        dist = milesTokm(dist);
                        dist = round(dist, 2);

                        hospitalDistance.setText(dist.toString() + " km");

                        hospitalInfo.setVisibility(View.VISIBLE);

                        Log.d(TAG, "onMarkerClick: " + dist.toString());

                        return true;
                    }
                }
                return false;
            }
        });
    }

    private int getTransparentColor(int color){
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        alpha *= 0.25;

        return Color.argb(alpha, red, green, blue);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.action_menu_items,menu);
        MenuItem editProfile = menu.findItem(R.id.editProfile);
        MenuItem signOut = menu.findItem(R.id.signOut);
        MenuItem relatives = menu.findItem(R.id.relatives);

        editProfile.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                    Intent i = new Intent(DashboardActivity.this, EditProfileActivity.class);
                    Log.d(TAG, "onMenuItemClick: " + getIntent().getStringExtra("name"));
                    i.putExtra("name", getIntent().getStringExtra("name"));
                    i.putExtra("bloodgroup", getIntent().getStringExtra("bloodgroup"));
                    i.putExtra("age", getIntent().getStringExtra("age"));
                    i.putExtra("address", getIntent().getStringExtra("address"));
                    i.putExtra("contactno", getIntent().getStringExtra("contactno"));
                    i.putExtra("email", getIntent().getStringExtra("email"));
                    i.putExtra("password", getIntent().getStringExtra("password"));
                    i.putExtra("sex", getIntent().getStringExtra("sex"));
                    startActivity(i);

                return false;
            }
        });

        relatives.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(DashboardActivity.this,RelativesActivity.class));
                return false;
            }
        });

        signOut.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("savedEmail", null);
                editor.putString("savedPassword", null);
                editor.commit();

                Intent intent = new Intent(DashboardActivity.this, HomeScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                finish();

                return false;
            }
        });

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 9876){

            gps = new GPSTracker(DashboardActivity.this);

            if (gps.getIsGPSTrackingEnabled()){
//                latitude = gps.getLatitude();
//                longitude = gps.getLongitude();
//                getLocation.onSuccess();
                gpsIsEnabled = true;
                gps.getLocation();
            }

            else{
                Toast.makeText(DashboardActivity.this, "Turn on Location", Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

//    private double distance(double lat1, double lon1, double lat2, double lon2) {
//        double theta = lon1 - lon2;
//        double dist = Math.sin(deg2rad(lat1))
//                * Math.sin(deg2rad(lat2))
//                + Math.cos(deg2rad(lat1))
//                * Math.cos(deg2rad(lat2))
//                * Math.cos(deg2rad(theta));
//        dist = Math.acos(dist);
//        dist = rad2deg(dist);
//        dist = dist * 60 * 1.1515;
//        return (dist);
//    }

//    private double deg2rad(double deg) {
//        return (deg * Math.PI / 180.0);
//    }
//
//    private double rad2deg(double rad) {
//        return (rad * 180.0 / Math.PI);
//    }

    private double milesTokm(double distanceInMiles) {
        return distanceInMiles / 1000;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


}

