package com.example.planningplus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SubTaskRecyclerAdapter extends RecyclerView.Adapter<SubTaskRecyclerAdapter.ViewHolder>{

    String[] titles;
    ArrayList<String> subTaskTitles;

    public SubTaskRecyclerAdapter(){
        subTaskTitles = new ArrayList<>();
        titles = subTaskTitles.toArray(new String[subTaskTitles.size()]);
    }

    public void setItems(ArrayList<String> subTasks){
        subTaskTitles.addAll(subTasks);
        onChange();
    }

    public void onChange(){
        titles = subTaskTitles.toArray(new String[subTaskTitles.size()]);
        notifyDataSetChanged();
    }

    public void addItem(String newString){
        subTaskTitles.add(newString);
        onChange();
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.textTitle);
            imageButton = itemView.findViewById(R.id.remove);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subTaskTitles.remove(getAdapterPosition());
                    onChange();
                }
            });

        }
    }

}
