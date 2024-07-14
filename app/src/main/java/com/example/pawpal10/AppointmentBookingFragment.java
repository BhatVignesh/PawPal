package com.example.pawpal10;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

public class AppointmentBookingFragment extends Fragment {

    private LinearLayout timeSlotContainer;
    private CalendarView calendarView;
    private EditText purposeEditText;
    private Button bookButton;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String traineeName;
    private String selectedTimeSlot;
    private String selectedDate;
    private Button selectedSlotButton; // Track selected slot button

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

        timeSlotContainer = view.findViewById(R.id.timeSlotContainer);
        calendarView = view.findViewById(R.id.calendarView);
        purposeEditText = view.findViewById(R.id.purposeEditText);
        bookButton = view.findViewById(R.id.bookButton);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (getArguments() != null) {
            traineeName = getArguments().getString("traineeName");
        }

        // Set up time slots
        setupTimeSlots();

        // Update available slots when the date changes
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(year, month, dayOfMonth);

                // Check if selected date is within the next 7 days
                Calendar todayCalendar = Calendar.getInstance();
                todayCalendar.add(Calendar.DAY_OF_MONTH, 0); // Start from today
                Calendar maxDateCalendar = Calendar.getInstance();
                maxDateCalendar.add(Calendar.DAY_OF_MONTH, 7); // Up to 7 days from now

                if (selectedCalendar.before(todayCalendar) || selectedCalendar.after(maxDateCalendar)) {
                    Toast.makeText(getContext(), "Please select a date within the next 7 days.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Format selected date
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                selectedDate = sdf.format(selectedCalendar.getTime());

                // Update available slots
                updateAvailableSlots();
            }
        });

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookAppointment();
            }
        });

        // Set initial date to today
        Calendar initialCalendar = Calendar.getInstance();
        initialCalendar.add(Calendar.DAY_OF_MONTH, 0); // Start from today
        calendarView.setMinDate(initialCalendar.getTimeInMillis());
        initialCalendar.add(Calendar.DAY_OF_MONTH, 7); // Up to 7 days from now
        calendarView.setMaxDate(initialCalendar.getTimeInMillis());

        // Set initial date
        selectedDate = new SimpleDateFormat("ddMMyyyy", Locale.getDefault()).format(initialCalendar.getTime());
        updateAvailableSlots(); // Initial check for available slots
    }

    private void setupTimeSlots() {
        String[] timeSlots = getResources().getStringArray(R.array.time_slots_array);

        for (String slot : timeSlots) {
            Button slotButton = new Button(getContext());
            slotButton.setText(slot);
            slotButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Clear previous selection
                    if (selectedSlotButton != null) {
                        selectedSlotButton.setBackgroundResource(R.drawable.slot_background); // Set default background
                    }

                    // Highlight selected slot
                    selectedSlotButton = (Button) v;
                    selectedSlotButton.setBackgroundResource(R.drawable.selected_slot_background); // Set selected background

                    // Update selected time slot
                    selectedTimeSlot = slot;

                    // Optionally, you can also update any UI or perform actions based on the selected slot
                }
            });

            // Set default background
            slotButton.setBackgroundResource(R.drawable.slot_background);

            timeSlotContainer.addView(slotButton);
        }
    }

    private void updateAvailableSlots() {
        db.collection("appointments")
                .whereEqualTo("date", selectedDate)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<String> bookedSlots = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            bookedSlots.add(document.getString("timeSlot"));
                        }
                        updateSlotButtons(bookedSlots);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed to fetch booked slots.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateSlotButtons(ArrayList<String> bookedSlots) {
        int childCount = timeSlotContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = timeSlotContainer.getChildAt(i);
            if (child instanceof Button) {
                Button slotButton = (Button) child;
                String slot = slotButton.getText().toString();
                if (bookedSlots.contains(slot)) {
                    slotButton.setVisibility(View.GONE);
                } else {
                    slotButton.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void bookAppointment() {
        if (selectedTimeSlot == null) {
            Toast.makeText(getContext(), "Please select a time slot.", Toast.LENGTH_SHORT).show();
            return;
        }

        String purpose = purposeEditText.getText().toString().trim();

        if (purpose.isEmpty()) {
            purposeEditText.setError("Purpose is required");
            return;
        }

        if (user != null && traineeName != null) {
            String userEmail = user.getEmail();

            db.collection("users")
                    .whereEqualTo("email", userEmail)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                String username = queryDocumentSnapshots.getDocuments().get(0).getString("username");

                                db.collection("appointments")
                                        .whereEqualTo("date", selectedDate)
                                        .whereEqualTo("timeSlot", selectedTimeSlot)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                if (queryDocumentSnapshots.isEmpty()) {
                                                    Map<String, Object> appointment = new HashMap<>();
                                                    appointment.put("username", username);
                                                    appointment.put("userEmail", userEmail);
                                                    appointment.put("traineeName", traineeName);
                                                    appointment.put("date", selectedDate);
                                                    appointment.put("timeSlot", selectedTimeSlot);
                                                    appointment.put("purpose", purpose);

                                                    db.collection("appointments")
                                                            .add(appointment)
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                @Override
                                                                public void onSuccess(DocumentReference documentReference) {
                                                                    Toast.makeText(getContext(), "Appointment booked successfully!", Toast.LENGTH_SHORT).show();
                                                                    updateAvailableSlots(); // Update slots after booking
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(getContext(), "Failed to book appointment.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                } else {
                                                    Toast.makeText(getContext(), "This time slot is already booked for the selected date.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), "Failed to check appointment slot.", Toast.LENGTH_SHORT).show();
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
