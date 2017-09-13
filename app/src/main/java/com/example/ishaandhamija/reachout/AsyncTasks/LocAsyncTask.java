package com.example.ishaandhamija.reachout.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ishaandhamija.reachout.Activities.DashboardActivity;
import com.example.ishaandhamija.reachout.Interfaces.GetLocation;
import com.example.ishaandhamija.reachout.Utils.GPSTracker;

/**
 * Created by ishaandhamija on 13/09/17.
 */

public class LocAsyncTask extends AsyncTask<Void, Void, Boolean> {

    Context ctx;
    GPSTracker gpsTracker;
    GetLocation getLocation;
    ProgressDialog progressDialog;

    public LocAsyncTask(Context ctx, GPSTracker gpsTracker, GetLocation getLocation){
        this.ctx = ctx;
        this.gpsTracker = gpsTracker;
        this.getLocation = getLocation;
        progressDialog = new ProgressDialog(ctx);
    }

    @Override
    protected void onPreExecute() {

        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Fetching Your Location...");
        progressDialog.show();

        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        while (gpsTracker.getLocation() == null) {
            Log.d("locFetch", "doInBackground: " + "Fetching Location");
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean s) {

//        DashboardActivity.latitude = gpsTracker.getLatitude();
//        DashboardActivity.longitude = gpsTracker.getLongitude();
        getLocation.onSuccess();

        Log.d("checking..", "onPostExecute: " + gpsTracker.getLatitude());

        progressDialog.dismiss();

        super.onPostExecute(s);
    }

}