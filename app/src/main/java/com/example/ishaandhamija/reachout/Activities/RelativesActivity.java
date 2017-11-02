package com.example.ishaandhamija.reachout.Activities;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ishaandhamija.reachout.Models.Relative;
import com.example.ishaandhamija.reachout.R;
import com.example.ishaandhamija.reachout.Utils.RelativesAdapter;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RelativesActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fab;
    RecyclerView mList;
    MaterialSpinner spinner1;
    EditText name,age;
    Button btnAdd;
    ArrayList<Relative> relatives;
    ArrayList<String> allRelatives;
    RelativesAdapter relativesAdapter;
    public static final String TAG = "RelativesActivity";
    SharedPreferences sharedpreferences;
    Dialog memberDialog;
    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatives);

        mList = (RecyclerView) findViewById(R.id.members);
        fab = (FloatingActionButton) findViewById(R.id.addMember);
        sharedpreferences = getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);
        relatives = new ArrayList<>();
        allRelatives = new ArrayList<>();
        showAllRelatives();
        relativesAdapter = new RelativesAdapter(this,relatives);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.addItemDecoration(new DividerItemDecoration(RelativesActivity.this,LinearLayoutManager.VERTICAL));
        mList.setAdapter(relativesAdapter);

        fab.setOnClickListener(this);

    }




    @Override
    public void onClick(View view) {
        if(view == fab){
            memberDialog = new Dialog(RelativesActivity.this);
            memberDialog.getWindow().setBackgroundDrawable(null);
            memberDialog.setContentView(R.layout.dialog_add_member);
            WindowManager.LayoutParams lp = memberDialog.getWindow().getAttributes();
            Window window = memberDialog.getWindow();
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            lp.gravity = Gravity.CENTER;
            memberDialog.setCancelable(true);
            memberDialog.show();
            name = (EditText) memberDialog.findViewById(R.id.name);
            age = (EditText) memberDialog.findViewById(R.id.age);
            spinner1 = (MaterialSpinner) memberDialog.findViewById(R.id.spinner);
            spinner1.setItems("A+","A-","B+","B-","O+","O-","AB+","AB-");
            spinner1.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                }
            });
            btnAdd = (Button) memberDialog.findViewById(R.id.btnAdd);
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String mname,mage,mbGroup;
                    mname = name.getText().toString();
                    mage = age.getText().toString();
                    mbGroup = spinner1.getItems().get(spinner1.getSelectedIndex()).toString();
                    Relative relative = new Relative(mname,mage,mbGroup);
                    relatives.add(relative);
                    relativesAdapter.notifyDataSetChanged();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    Set<String> set = new HashSet<String>();
                    set.addAll(allRelatives);
                    editor.putStringSet("RelativesNameSet", set);
                    editor.commit();
                    addRelative(mname,mage,mbGroup);

                    memberDialog.dismiss();
                }
            });
        }
    }
    private void addRelative(String mname,String mage,String mbGroup) {

        String useremail = sharedpreferences.getString("savedEmail"," ");
        String userpassword = sharedpreferences.getString("savedPassword"," ");
        JSONObject json = new JSONObject();
        try {
            json.put("relativeName", mname);
            json.put("relativeAge", mage);
            json.put("relativeBloodgroup", mbGroup);
            json.put("email", useremail);
            json.put("password", userpassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://192.168.43.202:5199/api/updateRelative",
//        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://harshgoyal.xyz:5199/api/updateRelative",
//        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://reach-out-server.herokuapp.com/api/updateRelative",
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG, "onResponse: " + response.toString());
                        } catch (Exception e) {
                            Toast.makeText(RelativesActivity.this, "No response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: " + error);
                        error.printStackTrace();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(RelativesActivity.this);
        requestQueue.add(jsonObjectRequest);

    }

    private void showAllRelatives() {
        String username = sharedpreferences.getString("savedEmail"," ");
        Log.d("checkkk", "showAllRelatives: " + username);
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                "http://192.168.43.202:5199/api/showallrel/"+username,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject relObject = null;
                            try {
                                relObject = response.getJSONObject(i);
                                String relName = relObject.getString("relativeName");
                                String relAge = relObject.getString("relativeAge");
                                String relBloodGroup = relObject.getString("relativeBloodgroup");
                                Relative relative  = new Relative(relName,relAge,relBloodGroup);
                                relatives.add(relative);
                                allRelatives.add(relName);
                                relativesAdapter.notifyDataSetChanged();

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

        RequestQueue requestQueue = Volley.newRequestQueue(RelativesActivity.this);
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
