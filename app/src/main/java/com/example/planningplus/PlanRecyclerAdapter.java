package com.example.planningplus;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class PlanRecyclerAdapter extends RecyclerView.Adapter<PlanRecyclerAdapter.ViewHolder>{

    String[] titles = {"Timed", "Proximity"};
    int[] images = {R.drawable.ic_clock_black_24dp, R.drawable.ic_place_black_24dp};

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.plans_card_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemTitle.setText(titles[position]);
        holder.itemImage.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemTitle;
        ImageView itemImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.plan_title);
            itemImage = itemView.findViewById(R.id.plan_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavController navController = Navigation.findNavController(v);
                    if(getAdapterPosition() == 0){
                        navController.navigate(R.id.action_plansFragment_to_planTimedFragment);
                    }
                    else if(getAdapterPosition() == 1){
                        navController.navigate(R.id.action_plansFragment_to_planProximityFragment);
                    }
                }
            });

        }
    }

}
