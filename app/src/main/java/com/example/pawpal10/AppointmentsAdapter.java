package com.example.pawpal10;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.AppointmentViewHolder> {

    private List<Appointment> appointmentList;

    public AppointmentsAdapter(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);
        holder.dateTextView.setText(appointment.getDate());
        holder.traineeNameTextView.setText(appointment.getTraineeName());
        holder.timeSlotTextView.setText(appointment.getTimeSlot());
        holder.purposeTextView.setText(appointment.getPurpose());
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView traineeNameTextView;
        TextView timeSlotTextView;
        TextView purposeTextView;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView); // Assuming R.id.usernameTextView is used for date
            traineeNameTextView = itemView.findViewById(R.id.traineeNameTextView);
            timeSlotTextView = itemView.findViewById(R.id.timeSlotTextView);
            purposeTextView = itemView.findViewById(R.id.purposeTextView);
        }
    }
}
