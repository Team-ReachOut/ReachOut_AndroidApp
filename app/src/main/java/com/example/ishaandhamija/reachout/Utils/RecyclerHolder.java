package com.example.ishaandhamija.reachout.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.ishaandhamija.reachout.R;

/**
 * Created by ishaandhamija on 26/09/17.
 */

public class RecyclerHolder extends RecyclerView.ViewHolder {

    TextView serviceName;
    View view;

    public RecyclerHolder(View itemView, final Context ctx) {
        super(itemView);

        this.serviceName = (TextView) itemView.findViewById(R.id.serviceName);

        this.view = itemView;
    }
}