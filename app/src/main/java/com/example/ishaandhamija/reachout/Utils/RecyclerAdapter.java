package com.example.ishaandhamija.reachout.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ishaandhamija.reachout.R;

import java.util.ArrayList;


/**
 * Created by ishaandhamija on 26/09/17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerHolder> {

    Context ctx;
    ArrayList<String> serviceList;

    public RecyclerAdapter(Context context, ArrayList<String> list) {
        this.ctx = context;
        this.serviceList = list;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = li.inflate(R.layout.sample_service, parent, false);

        return new RecyclerHolder(itemView, ctx);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        final String service = serviceList.get(position);
        holder.serviceName.setText(service);
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

}