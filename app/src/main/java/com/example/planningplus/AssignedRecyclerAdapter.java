package com.example.planningplus;

import android.app.assist.AssistContent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AssignedRecyclerAdapter extends RecyclerView.Adapter<AssignedRecyclerAdapter.ViewHolder>{

    String[] titles;
    String[] deadlines;
    Long[] ids;

    /*
        public TaskRecyclerAdapter(ArrayList<Task> tasks){
            ArrayList<String> titlesTemp = new ArrayList<>();
            ArrayList<String> deadlinesTemp = new ArrayList<>();
            ArrayList<Long> idsTemp = new ArrayList<>();
            for(Task i : tasks){
                titlesTemp.add(i.taskTitle);
                if(i.taskDeadline.equals(""))
                    deadlinesTemp.add("No set deadline");
                else
                    deadlinesTemp.add(i.taskDeadline);
                idsTemp.add(i.id);
            }
            titles = titlesTemp.toArray(new String[titlesTemp.size()]);
            deadlines = deadlinesTemp.toArray(new String[deadlinesTemp.size()]);
            ids = idsTemp.toArray(new Long[idsTemp.size()]);
        }
        */
    public void setItems(ArrayList<AssignedTask> tasks){
        ArrayList<String> titlesTemp = new ArrayList<>();
        ArrayList<String> deadlinesTemp = new ArrayList<>();
        ArrayList<Long> idsTemp = new ArrayList<>();
        for(AssignedTask i : tasks){
            titlesTemp.add(i.task.taskTitle);
            if(i.task.taskDeadline.equals(""))
                deadlinesTemp.add("No set deadline");
            else
                deadlinesTemp.add(i.task.taskDeadline);
            idsTemp.add(i.task.id);
        }
        titles = titlesTemp.toArray(new String[titlesTemp.size()]);
        deadlines = deadlinesTemp.toArray(new String[deadlinesTemp.size()]);
        ids = idsTemp.toArray(new Long[idsTemp.size()]);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card_layout,parent,false);
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
            itemDescription = itemView.findViewById(R.id.task_deadline);
            itemTitle = itemView.findViewById(R.id.task_title);
            context = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }

}
