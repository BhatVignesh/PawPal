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
            traineeList.add(new Trainee(
                    "John Doe",
                    "John Doe is a passionate animal lover with over 5 years of experience in dog training and pet care. He specializes in positive reinforcement techniques and has worked with various breeds to improve behavior and obedience.",
                    R.drawable.person1
            ));

            // Trainee 3
            traineeList.add(new Trainee(
                    "Suzan Johnson",
                    "Suzan Johnson is a certified dog trainer with a passion for canine behavior and rehabilitation. She specializes in fear and aggression management, using positive reinforcement techniques to build trust and confidence in her clients' pets.",
                    R.drawable.person2
            ));

            // Trainee 4
            traineeList.add(new Trainee(
                    "Josh Thompson",
                    "Josh Thompson is an animal behaviorist known for his work with rescue animals. He focuses on rehabilitation and socialization, helping traumatized pets integrate into loving homes through patient training and care.",
                    R.drawable.person3
            ));

            // Add more trainees as needed
            adapter.notifyDataSetChanged();
        }
    }
}
