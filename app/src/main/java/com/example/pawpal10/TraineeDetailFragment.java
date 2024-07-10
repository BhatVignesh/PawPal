package com.example.pawpal10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class TraineeDetailFragment extends Fragment {

    public TraineeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trainee_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView imageTraineeDetail = view.findViewById(R.id.imageTraineeDetail);
        TextView textTraineeNameDetail = view.findViewById(R.id.textTraineeNameDetail);
        TextView textTraineeDetails = view.findViewById(R.id.textTraineeDetails);
        Button buttonBookAppointment = view.findViewById(R.id.buttonBookAppointment);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String traineeName = bundle.getString("traineeName");
            int traineeProfilePic = bundle.getInt("traineeProfilePic");
            String traineeAbstractDetails = bundle.getString("traineeAbstractDetails");

            imageTraineeDetail.setImageResource(traineeProfilePic);
            textTraineeNameDetail.setText(traineeName);
            textTraineeDetails.setText(traineeAbstractDetails);

            buttonBookAppointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppointmentBookingFragment fragment = new AppointmentBookingFragment();
                    Bundle args = new Bundle();
                    args.putString("traineeName", traineeName);
                    fragment.setArguments(args);

                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.frameLayout, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        }
    }
}
