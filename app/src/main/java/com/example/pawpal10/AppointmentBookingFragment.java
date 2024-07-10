package com.example.pawpal10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AppointmentBookingFragment extends Fragment {

    private Spinner timeSlotSpinner;
    private EditText purposeEditText; // New EditText for purpose
    private Button bookButton;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String traineeName;

    public AppointmentBookingFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appointment_booking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timeSlotSpinner = view.findViewById(R.id.timeSlotSpinner);
        purposeEditText = view.findViewById(R.id.purposeEditText); // Initialize EditText
        bookButton = view.findViewById(R.id.bookButton);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (getArguments() != null) {
            traineeName = getArguments().getString("traineeName");
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.time_slots_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSlotSpinner.setAdapter(adapter);

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookAppointment();
            }
        });
    }

    private void bookAppointment() {
        String timeSlot = timeSlotSpinner.getSelectedItem().toString();
        String purpose = purposeEditText.getText().toString().trim(); // Get purpose text and trim whitespace

        if (purpose.isEmpty()) {
            purposeEditText.setError("Purpose is required"); // Show error if purpose is empty
            return; // Return without booking appointment
        }

        if (user != null && traineeName != null) {
            String userEmail = user.getEmail();

            // Query Firestore to fetch the username based on the current user's email
            db.collection("users")
                    .whereEqualTo("email", userEmail)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                // Assuming there is only one document for each user email
                                String username = queryDocumentSnapshots.getDocuments().get(0).getString("username");

                                // Proceed to book the appointment with fetched username, purpose, and other details
                                Map<String, Object> appointment = new HashMap<>();
                                appointment.put("username", username);
                                appointment.put("userEmail", userEmail); // Store userEmail if needed
                                appointment.put("traineeName", traineeName);
                                appointment.put("timeSlot", timeSlot);
                                appointment.put("purpose", purpose); // Store purpose

                                db.collection("appointments")
                                        .add(appointment)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(getContext(), "Appointment booked successfully!", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), "Failed to book appointment.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(getContext(), "Username not found for current user.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Failed to fetch username.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getContext(), "User or trainee information missing.", Toast.LENGTH_SHORT).show();
        }
    }
}
