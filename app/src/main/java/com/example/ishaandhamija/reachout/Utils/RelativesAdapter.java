package com.example.ishaandhamija.reachout.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ishaandhamija.reachout.Models.Relative;
import com.example.ishaandhamija.reachout.R;

import java.util.ArrayList;

/**
 * Created by HP on 20-Sep-17.
 */

public class RelativesAdapter extends RecyclerView.Adapter<RelativesAdapter.ListItemHolder> {
    Context context;
    ArrayList<Relative> relatives;

    public RelativesAdapter(Context context, ArrayList<Relative> relatives) {
        this.context = context;
        this.relatives = relatives;
    }

    @Override
    public RelativesAdapter.ListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.relative_list_item,parent,false);
        return new ListItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RelativesAdapter.ListItemHolder holder, int position) {
        Relative item = relatives.get(position);
        holder.name.setText(item.getName());
        holder.age.setText(item.getAge());
        holder.bloodgroup.setText(item.getbGroup());

    }

    @Override
    public int getItemCount() {
        return relatives.size();
    }

    public class ListItemHolder extends RecyclerView.ViewHolder{
        View mainView;
        ImageView image;
        TextView name;
        TextView age;
        TextView bloodgroup;
        public ListItemHolder(View itemView) {
            super(itemView);
            mainView = itemView;
            image = (ImageView) itemView.findViewById(R.id.userImg);
            name = (TextView) itemView.findViewById(R.id.name);
            age = (TextView) itemView.findViewById(R.id.age);
            bloodgroup = (TextView) itemView.findViewById(R.id.blood);
        }
    }
}
