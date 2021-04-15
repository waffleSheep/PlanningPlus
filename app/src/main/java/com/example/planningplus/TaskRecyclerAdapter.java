package com.example.planningplus;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.ViewHolder>{

    String[] titles;
    String[] deadlines;
    Long[] ids;
    Integer[] colours;

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
    public void setItems(ArrayList<Task> tasks){
        Collections.sort(tasks);
        ArrayList<String> titlesTemp = new ArrayList<>();
        ArrayList<String> deadlinesTemp = new ArrayList<>();
        ArrayList<Integer> coloursTemp = new ArrayList<>();
        ArrayList<Long> idsTemp = new ArrayList<>();
        for(Task i : tasks){
            titlesTemp.add(i.taskTitle);
            if(i.taskDeadline.equals(""))
                deadlinesTemp.add("No set deadline");
            else
                deadlinesTemp.add(i.taskDeadline);
            idsTemp.add(i.id);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            try{
                Date taskDate = simpleDateFormat.parse(i.taskDeadline);
                Date currDate = Calendar.getInstance().getTime();
                if(currDate.after(taskDate)){
                    coloursTemp.add(Color.RED);
                }
                else{
                    coloursTemp.add(Color.GREEN);
                }

            }
            catch (ParseException ex){
                coloursTemp.add(Color.GREEN);
            }
        }
        titles = titlesTemp.toArray(new String[titlesTemp.size()]);
        deadlines = deadlinesTemp.toArray(new String[deadlinesTemp.size()]);
        ids = idsTemp.toArray(new Long[idsTemp.size()]);
        colours = coloursTemp.toArray(new Integer[coloursTemp.size()]);
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
        holder.itemTitle.setTextColor(colours[position]);
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
                    Database.id = ids[getAdapterPosition()];
                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.action_tasksFragment_to_taskViewFragment);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Long id = ids[getAdapterPosition()];
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference documentReference = db.collection("users").document(Database.username);
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User user = documentSnapshot.toObject(User.class);
                            for(int i = 0; i < user.tasks.size(); ++i){
                                if(user.tasks.get(i).id.equals(id)){
                                    if(user.tasks.get(i).isAssigned){

                                        return;
                                    }
                                    user.tasks.remove(i);
                                    break;
                                }
                            }
                            documentReference.set(user);
                        }
                    });
                    return false;
                }
            });
        }
    }

}
