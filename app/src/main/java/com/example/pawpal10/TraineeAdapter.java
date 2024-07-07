package com.example.pawpal10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TraineeAdapter extends RecyclerView.Adapter<TraineeAdapter.MyViewHolder> {

    private List<Trainee> traineeList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, abstractDetails;
        public ImageView profilePic;
        public Button viewMore;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.textTraineeName);
            abstractDetails = view.findViewById(R.id.textTraineeAbstract);
            profilePic = view.findViewById(R.id.imageTrainee);
            viewMore = view.findViewById(R.id.buttonViewMore);
        }
    }

    public TraineeAdapter(List<Trainee> traineeList) {
        this.traineeList = traineeList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trainee_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Trainee trainee = traineeList.get(position);
        holder.name.setText(trainee.getName());
        holder.abstractDetails.setText(trainee.getAbstractDetails());
        holder.profilePic.setImageResource(trainee.getProfilePic());

        // Implement OnClickListener for View More button
        holder.viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to TraineeDetailFragment
                TraineeDetailFragment fragment = new TraineeDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("traineeName", trainee.getName());
                bundle.putInt("traineeProfilePic", trainee.getProfilePic());
                bundle.putString("traineeAbstractDetails", trainee.getFullAbstractDetails());
                fragment.setArguments(bundle);

                FragmentTransaction transaction = ((FragmentActivity) holder.itemView.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, fragment); // Replace with your fragment container id
                transaction.addToBackStack(null); // Optional: Add fragment to back stack
                transaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return traineeList.size();
    }
}

