package com.example.pawpal10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class pawFragment extends Fragment {

    private RecyclerView recyclerView;
    private TraineeAdapter adapter;
    private List<Trainee> traineeList = new ArrayList<>();

    public pawFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_paw, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewTrainees);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TraineeAdapter(traineeList);
        recyclerView.setAdapter(adapter);
        prepareTraineeData(); // Method to populate traineeList with dummy data
        return view;
    }

    private void prepareTraineeData() {
        if (traineeList.isEmpty()) {
            // Populate traineeList with dummy data (replace with your actual data)
            traineeList.add(new Trainee("Mukesh Gutka", "Lorem ipsum...", R.drawable.person1));
            traineeList.add(new Trainee("Saritha", "Lorem ipsum...", R.drawable.person2));
            traineeList.add(new Trainee("KVN", "Lorem ipsum...", R.drawable.person3));
            // Add more trainees as needed
            adapter.notifyDataSetChanged();
        }
    }
}
