package com.example.pawpal10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookedAppointmentsFragment extends Fragment {

    private RecyclerView recyclerView;
    private AppointmentsAdapter adapter;
    private List<Appointment> appointmentList;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private TextView noAppointmentTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booked_appointments, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        noAppointmentTextView = view.findViewById(R.id.noAppointmentTextView);

        appointmentList = new ArrayList<>();
        adapter = new AppointmentsAdapter(appointmentList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            fetchAppointments();
        }

        return view;
    }

    private void fetchAppointments() {
        db.collection("appointments")
                .whereEqualTo("userEmail", user.getEmail())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        appointmentList.clear(); // Clear existing appointments
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Appointment appointment = document.toObject(Appointment.class);
                            appointmentList.add(appointment);
                        }
                        adapter.notifyDataSetChanged();

                        // Show/hide noAppointmentTextView based on appointmentList size
                        if (appointmentList.isEmpty()) {
                            noAppointmentTextView.setVisibility(View.VISIBLE);
                        } else {
                            noAppointmentTextView.setVisibility(View.GONE);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                    }
                });
    }
}
