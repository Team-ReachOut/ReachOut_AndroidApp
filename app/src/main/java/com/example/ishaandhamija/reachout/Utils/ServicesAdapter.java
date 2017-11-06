package com.example.ishaandhamija.reachout.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ishaandhamija.reachout.Activities.DashboardActivity;
import com.example.ishaandhamija.reachout.R;

import java.util.ArrayList;

/**
 * Created by HP on 30-Oct-17.
 */

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ListItemHolder> {

    Context context;
    ArrayList<String> services;

    public ServicesAdapter(Context context, ArrayList<String> services) {
        this.context = context;
        this.services = services;
    }

    @Override
    public ServicesAdapter.ListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.list_item_services,parent,false);
        return new ListItemHolder(view);
    }

    @Override
    public void onBindViewHolder(final ServicesAdapter.ListItemHolder holder, int position) {
        String item = services.get(position);
        holder.textView.setText(item);
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,holder.textView.getText(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, DashboardActivity.class);
                String service = holder.textView.getText().toString();
                i.putExtra("Service",service);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ListItemHolder extends RecyclerView.ViewHolder{
        View mainView;
        TextView textView;
        public ListItemHolder(View itemView) {
            super(itemView);
            mainView = itemView;
            textView = (TextView) itemView.findViewById(R.id.service);
        }
    }
}
