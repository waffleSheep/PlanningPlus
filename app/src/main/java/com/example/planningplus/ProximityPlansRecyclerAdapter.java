package com.example.planningplus;

import android.app.assist.AssistContent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class ProximityPlansRecyclerAdapter extends RecyclerView.Adapter<ProximityPlansRecyclerAdapter.ViewHolder>{

    String[] titles;
    String[] deadlines;

    public ProximityPlansRecyclerAdapter(){
        titles = new String[0];
        deadlines = new String[0];
    }

    public void setItems(ArrayList<Plan> plans){
        ArrayList<Plan> plansOnSelectedDay = new ArrayList<>();
        for(Plan i : plans){
            if(i.hasProximityAlert){
                plansOnSelectedDay.add(i);
            }
        }
        ArrayList<String> titlesTemp = new ArrayList<>();
        ArrayList<String> deadlinesTemp = new ArrayList<>();
        for(Plan i : plansOnSelectedDay){
            titlesTemp.add(i.planTitle);
            deadlinesTemp.add(i.latitude + " " + i.longitude);
        }
        titles = titlesTemp.toArray(new String[titlesTemp.size()]);
        deadlines = deadlinesTemp.toArray(new String[deadlinesTemp.size()]);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.proximity_plan_card_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemTitle.setText(titles[position]);
        holder.itemDescription.setText(deadlines[position]);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemDescription;
        TextView itemTitle;
        private final Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemDescription = itemView.findViewById(R.id.plan_time);
            itemTitle = itemView.findViewById(R.id.plan_name);
            context = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

}
