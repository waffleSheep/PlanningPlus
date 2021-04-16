package com.example.planningplus;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class AssigneeRecyclerAdapter extends RecyclerView.Adapter<AssigneeRecyclerAdapter.ViewHolder>{

    String[] titles;
    Integer[] backgroundColours;
    ArrayList<String> subTaskTitles;
    ArrayList<Integer> backgroundColoursList;

    public AssigneeRecyclerAdapter(){
        subTaskTitles = new ArrayList<>();
        backgroundColoursList = new ArrayList<>();
        titles = subTaskTitles.toArray(new String[subTaskTitles.size()]);
        backgroundColours = backgroundColoursList.toArray(new Integer[backgroundColoursList.size()]);
    }

    public void setItems(ArrayList<String> subTasks, ArrayList<Boolean> bgColours){
        for(int i = 0; i < bgColours.size(); ++i){
            if(bgColours.get(i).equals(Boolean.TRUE)){
                backgroundColoursList.add(Color.GREEN);
            }
            else{
                backgroundColoursList.add(Color.WHITE);
            }
        }
        subTaskTitles.addAll(subTasks);
        onChange();
    }

    public void onChange(){
        titles = subTaskTitles.toArray(new String[subTaskTitles.size()]);
        backgroundColours = backgroundColoursList.toArray(new Integer[backgroundColoursList.size()]);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_task_card_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemTitle.setText(titles[position]);
        holder.imageButton.setVisibility(View.INVISIBLE);
        holder.cardView.setBackgroundColor(backgroundColours[position]);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public ArrayList<String> getSubTaskTitles() {
        return subTaskTitles;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemTitle;
        ImageButton imageButton;
        MaterialCardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.textTitle);
            imageButton = itemView.findViewById(R.id.remove);
            cardView = itemView.findViewById(R.id.card_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

        }
    }

}
