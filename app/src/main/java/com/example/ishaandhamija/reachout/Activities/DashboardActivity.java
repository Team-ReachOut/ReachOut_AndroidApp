package com.example.ishaandhamija.reachout.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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
import com.example.ishaandhamija.reachout.Models.Relative;
import com.example.ishaandhamija.reachout.R;
import com.example.ishaandhamija.reachout.Utils.DirectionsJSONParser;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.ishaandhamija.reachout.R.id.map;

public class DashboardActivity extends AppCompatActivity implements OnMapReadyCallback{

    GPSTracker gps;

    public static Double latitude, longitude;

    ArrayList<Hospital> hospitalList;
    ArrayList<String> services;

    GetHospitals getHospitals;

    TextView hospitalName, hospitalAddress, hospitalDistance;
    CardView hospitalInfo;
    FloatingActionButton emegencyBtn,btnFilter;

    SharedPreferences sharedpreferences;

//    MyAdapter myAdapter1,myAdapter2;
    ArrayList<String> members;
    ArrayList<String> emergencies;
    MaterialSpinner spinner1,spinner2;
    Button btnSubmit;
    ImageView btnMyLoc;

    ProgressDialog progressDialog;

    ArrayList<Marker> myMarkers;
    Marker myMarker;

    Polyline polyline = null;
    GoogleMap mapp;

    public static GetLocation getLocation;

    public static Boolean gpsIsEnabled = false;

    public static final String TAG = "Hospitals";
    public static final String MyPREFERENCES = "MyPrefs" ;
    private ArrayList<String> allRelatives;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_dashboard);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getSupportActionBar().setTitle("");
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        allRelatives = new ArrayList<>();
        services = new ArrayList<>();
        showAllRelatives();
        getMedicalServices();

        hospitalName = (TextView) findViewById(R.id.hospitalName);
        hospitalAddress = (TextView) findViewById(R.id.hospitalAddress);
        hospitalDistance = (TextView) findViewById(R.id.hospitalDistance);
        hospitalInfo = (CardView) findViewById(R.id.hospitalInfo);
        emegencyBtn = (FloatingActionButton) findViewById(R.id.emergencyBtn);
        btnFilter = (FloatingActionButton) findViewById(R.id.filter);
        btnMyLoc = (ImageView) findViewById(R.id.mylocation);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching your location...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        hospitalList = new ArrayList<>();
        members = new ArrayList<>();
        emergencies = new ArrayList<>();

        gps = new GPSTracker(DashboardActivity.this);

        getLocation = new GetLocation() {
            @Override
            public void onSuccess() {
                if (sharedpreferences.getString("bloodBank", null) == null) {
                    btnFilter.setVisibility(View.VISIBLE);
                    if ((getIntent().getStringExtra("Service") == null) || (getIntent().getStringExtra("Service").equals("All Services"))) {
                        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
//                "http://harshgoyal.xyz:5199/api/showall",
//                "https://reach-out-server.herokuapp.com/api/showall",
//                        "http://192.168.43.202:5199/api/showall",
                                "http://192.168.43.202:5199/api/hospitals",

                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {

                                        for (int i = 0; i < response.length(); i++) {
                                            JSONObject hospitalObject = null;
                                            try {
                                                hospitalObject = response.getJSONObject(i);
                                                Double hospitalLat = hospitalObject.getDouble("lat");
                                                Double hospitalLon = hospitalObject.getDouble("lng");
                                                Log.d("choda", "onResponse: " + hospitalObject);
                                                Log.d(TAG, "checkkkk: " + hospitalObject.getJSONArray("speciality"));
                                                Log.d("bataa", "onResponse: " + hospitalObject.getJSONArray("services"));

                                                hospitalList.add(new Hospital(hospitalObject.getString("name"),
                                                        hospitalObject.getString("phone1"), hospitalObject.getString("phone2"), hospitalObject.getString("phone3"), hospitalObject.getString("address"), hospitalObject.getJSONArray("speciality"), hospitalObject.getJSONArray("services"), hospitalObject.getDouble("lat"), hospitalObject.getDouble("lng")));

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
                    } else {
                        String speciality = getIntent().getStringExtra("Service");
                        speciality = speciality.replaceAll(" ", "%20");
                        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
//                "http://harshgoyal.xyz:5199/api/showall",
//                "https://reach-out-server.herokuapp.com/api/showall",
//                        "http://192.168.43.202:5199/api/showall",
                                "http://192.168.43.202:5199/api/speciality/" + speciality,

                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {

                                        for (int i = 0; i < response.length(); i++) {
                                            JSONObject hospitalObject = null;
                                            try {
                                                hospitalObject = response.getJSONObject(i);
                                                Log.d("yahaaaya", "onResponse: " + hospitalObject.toString());
                                                Double hospitalLat = hospitalObject.getDouble("lat");
                                                Double hospitalLon = hospitalObject.getDouble("lng");

                                                hospitalList.add(new Hospital(hospitalObject.getString("name"),
                                                        hospitalObject.getString("phone1"), hospitalObject.getString("phone2"), hospitalObject.getString("phone3"), hospitalObject.getString("address"), hospitalObject.getJSONArray("speciality"),
                                                        hospitalObject.getJSONArray("services"), hospitalObject.getDouble("lat"),
                                                        hospitalObject.getDouble("lng")));


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
                }
                else {
                    btnFilter.setVisibility(View.GONE);
                    final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
//                "http://harshgoyal.xyz:5199/api/showall",
//                "https://reach-out-server.herokuapp.com/api/showall",
//                        "http://192.168.43.202:5199/api/showall",
                            "http://192.168.43.202:5199/api/bloodbank",

                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {

                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject hospitalObject = null;
                                        try {
                                            hospitalObject = response.getJSONObject(i);
                                            Double hospitalLat = hospitalObject.getDouble("lat");
                                            Double hospitalLon = hospitalObject.getDouble("lng");
                                            Log.d("choda", "onResponse: " + hospitalObject);
                                            Log.d(TAG, "checkkkk: " + hospitalObject.getJSONArray("speciality"));
                                            Log.d("bataa", "onResponse: " + hospitalObject.getJSONArray("services"));

                                            hospitalList.add(new Hospital(hospitalObject.getString("name"),
                                                    hospitalObject.getString("phone1"), hospitalObject.getString("phone2"), hospitalObject.getString("phone3"), hospitalObject.getString("address"), hospitalObject.getJSONArray("speciality"), hospitalObject.getJSONArray("services"), hospitalObject.getDouble("lat"), hospitalObject.getDouble("lng")));

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
            }
        };

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        hospitalList.clear();

        if (gps.getIsGPSTrackingEnabled() && gps.canGetLocation()){
            gpsIsEnabled = true;
            gps.getLocation();
        }
        else{
            gps.showSettingsAlert();
        }

        btnMyLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapp.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));
            }
        });

        emegencyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                emergencyDialog.setCancelable(true);
                emergencyDialog.show();
                spinner1 = (MaterialSpinner) emergencyDialog.findViewById(R.id.spinner1);
                spinner2 = (MaterialSpinner) emergencyDialog.findViewById(R.id.spinner2);
//                Set<String> set = sharedpreferences.getStringSet("RelativesNameSet", null);
//                set.add("Relatives");
//                for (String str : set)
//                    allRelatives.add(str);
                spinner1.setItems(allRelatives);
                spinner2.setItems("Cardiac Arrest", "Severe Accident", "Pneumonia", "Appendicitis", "Bone Fracture","Facial Trauma","Acid Attack","Respiratory failure","Electric Shock","Other");

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
                        Toast.makeText(DashboardActivity.this, "Request Initiated!!", Toast.LENGTH_SHORT).show();
                        emergencyDialog.dismiss();
                    }
                });
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardActivity.this,FilterActivity.class);
                i.putStringArrayListExtra("services",services);
                startActivity(i);
            }
        });
    }




    @Override
    public void onMapReady(final GoogleMap map) {

        mapp = map;

        getHospitals = new GetHospitals() {
            @Override
            public void onSuccess(ArrayList<Hospital> latlonList) {

                myMarkers = new ArrayList<>();

                myMarker = map.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title("My Location")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bluedot)));

                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));

                map.addCircle(new CircleOptions()
                        .center(new LatLng(latitude, longitude))
                        .radius(5000)
                        .strokeWidth(0f)
                        .fillColor(getTransparentColor(Color.parseColor("#4682b4"))));

                for (int i=0;i<latlonList.size();i++){

                    if (sharedpreferences.getString("bloodBank", null) == null) {
                        Marker marker = map.addMarker(new MarkerOptions()
                                .position(new LatLng(hospitalList.get(i).getLat(), hospitalList.get(i).getLon()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder)));

                        myMarkers.add(marker);
                    }
                    else {
                        Marker marker = map.addMarker(new MarkerOptions()
                                .position(new LatLng(hospitalList.get(i).getLat(), hospitalList.get(i).getLon()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bloodbank)));

                        myMarkers.add(marker);
                    }
                }

                progressDialog.dismiss();
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

                        String url = getDirectionsUrl(new LatLng(latitude, longitude),
                                new LatLng(hospitalList.get(i).getLat(), hospitalList.get(i).getLon()));

                        DownloadTask downloadTask = new DownloadTask();

                        downloadTask.execute(url);

                        hospitalName.setText(hospitalList.get(i).getName());
                        hospitalAddress.setText(hospitalList.get(i).getAddress());

                        Double dist = distance(latitude, longitude,
                                hospitalList.get(i).getLat(),
                                hospitalList.get(i).getLon(), 'K');

                        dist = round(dist, 2);

                        hospitalDistance.setText(dist.toString() + " km");

                        int hospitalInfoHeight = hospitalInfo.getMeasuredHeight();
                        int hospitalInfoWidth = hospitalInfo.getMeasuredWidth();
                        setMargins(btnFilter,0,0,42,hospitalInfoHeight+200);

                        hospitalInfo.setVisibility(View.VISIBLE);


                        final int finalI = i;
                        final Double finalDist = dist;
                        hospitalInfo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(DashboardActivity.this, HospitalActivity.class);
                                intent.putExtra("hname", hospitalList.get(finalI).getName());
                                intent.putExtra("haddress", hospitalList.get(finalI).getAddress());
                                intent.putExtra("hphonenumber1", hospitalList.get(finalI).getPhone1());
                                intent.putExtra("hphonenumber2", hospitalList.get(finalI).getPhone2());
                                intent.putExtra("hphonenumber3", hospitalList.get(finalI).getPhone3());
                                intent.putExtra("hdistance", finalDist.toString());
                                intent.putExtra("jsonArrayServices",hospitalList.get(finalI).getSpecialities().toString());
                                intent.putExtra("jsonArraySpecialities", hospitalList.get(finalI).getServices().toString());
                                startActivity(intent);
                            }
                        });

                        return true;
                    }
                }
                return false;
            }
        });
    }

    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
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
        showAllRelatives();
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
        MenuItem bloodBankToggle = menu.findItem(R.id.bloodBankToggle);
        MenuItem editProfile = menu.findItem(R.id.editProfile);
        MenuItem tips = menu.findItem(R.id.tips);
        MenuItem signOut = menu.findItem(R.id.signOut);
        MenuItem relatives = menu.findItem(R.id.relatives);

        if (sharedpreferences.getString("bloodBank", null) == null) {
            bloodBankToggle.setTitle("Blood Banks");
            bloodBankToggle.setIcon(R.drawable.transfusion2);
        }
        else {
            bloodBankToggle.setTitle("Hospitals");
            bloodBankToggle.setIcon(R.drawable.transfusion1);
        }

        bloodBankToggle.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(DashboardActivity.this, DashboardActivity.class);
                if (sharedpreferences.getString("bloodBank", null) == null) {
                    SharedPreferences.Editor prefsEditor = sharedpreferences.edit();
                    prefsEditor.putString("bloodBank", "yes");
                    prefsEditor.commit();
                }
                else {
                    SharedPreferences.Editor prefsEditor = sharedpreferences.edit();
                    prefsEditor.putString("bloodBank", null);
                    prefsEditor.commit();
                }
                startActivity(intent);
                return false;
            }
        });

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

        tips.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(DashboardActivity.this, TipsActivity.class));
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
                gpsIsEnabled = true;
                gps.getLocation();
            }

            else{
                Toast.makeText(DashboardActivity.this, "Turn on Location", Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    public static final double distance(double lat1, double lon1, double lat2, double lon2, char unit)
    {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == 'K') {
            dist = dist * 1.609344;
        }
        else if (unit == 'N') {
            dist = dist * 0.8684;
        }

        return (dist);
    }

    private static final double deg2rad(double deg)
    {
        return (deg * Math.PI / 180.0);
    }

    private static final double rad2deg(double rad)
    {
        return (rad * 180 / Math.PI);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    private void showAllRelatives() {
        String username = sharedpreferences.getString("savedEmail", null);
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                "http://192.168.43.202:5199/api/showallrel/"+username,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        allRelatives.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject relObject = null;
                            try {
                                relObject = response.getJSONObject(i);
                                String relName = relObject.getString("relativeName");
                                String relAge = relObject.getString("relativeAge");
                                String relBloodGroup = relObject.getString("relativeBloodgroup");
                                Relative relative  = new Relative(relName,relAge,relBloodGroup);
                                allRelatives.add(relName);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        Set<String> set = new HashSet<String>();
                        set.addAll(allRelatives);
                        editor.putStringSet("RelativesNameSet", set);
                        editor.commit();
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

    private void getMedicalServices() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://192.168.43.202:5199/api/unique",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("services", "onResponse: " + response+"\t"+response.length());
                        services.add("All Services");
                        for (int i=0;i<response.length();++i){
                            try {
                                Log.d(TAG, "onResponse: "+response.getString(i));
                                services.add(response.getString(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
        requestQueue.add(jsonArrayRequest);
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        String sensor = "sensor=false";

        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);
            }

            if (polyline != null){
                polyline.remove();
            }

            if (lineOptions != null) {
                polyline = mapp.addPolyline(lineOptions);

                Integer i = sharedpreferences.getInt("markerIndex", -1);

                if (i > -1) {
                    DecimalFormat df = new DecimalFormat("#.##");
                }
            }
        }
    }
}