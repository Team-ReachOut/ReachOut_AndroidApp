package com.example.ishaandhamija.reachout.Interfaces;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by ishaandhamija on 24/08/17.
 */

public interface GetHospitals {

    public void onSuccess(ArrayList<LatLng> latlonList);

}