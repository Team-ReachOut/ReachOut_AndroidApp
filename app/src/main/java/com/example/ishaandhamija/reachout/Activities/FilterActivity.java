package com.example.ishaandhamija.reachout.Activities;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ishaandhamija.reachout.R;
import com.example.ishaandhamija.reachout.Utils.ServicesAdapter;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity {

    public static final String TAG = "FilterActivity";
    RecyclerView listServices;
    MaterialSearchView searchView;
    Toolbar myToolbar;
    ArrayList<String> services;
    ServicesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        listServices = (RecyclerView) findViewById(R.id.rvList);
        myToolbar.setTitle("Services");
        setSupportActionBar(myToolbar);
        searchView.setHint("Search...");
        searchView.setHintTextColor(Color.GRAY);
        services = new ArrayList<>();
        Intent i = getIntent();
        services = i.getStringArrayListExtra("services");
        listServices.setLayoutManager(new LinearLayoutManager(this));
        listServices.addItemDecoration(new DividerItemDecoration(FilterActivity.this,LinearLayoutManager.VERTICAL));
        adapter = new ServicesAdapter(this,services);
        listServices.setAdapter(adapter);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> filteredList = new ArrayList<>();
                for (int i=0; i<services.size();++i) {
                    String text = services.get(i);
                    if (text.contains(newText)||text.toLowerCase().contains(newText.toLowerCase())) {
                        filteredList.add(services.get(i));
                    }
                }
                listServices.setLayoutManager(new LinearLayoutManager(FilterActivity.this));
                adapter = new ServicesAdapter(FilterActivity.this,filteredList);
                listServices.setAdapter(adapter);
                adapter.notifyDataSetChanged();  // data set changed
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                Log.d(TAG, "onSearchViewShown: ");
            }

            @Override
            public void onSearchViewClosed() {
                Log.d(TAG, "onSearchViewClosed: ");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }
}
