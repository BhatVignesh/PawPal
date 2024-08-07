package com.example.pawpal10;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class profileFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private TextView profileTextView;
    private Button logoutButton;
    private Button bookedAppointmentsButton,viewOrdersButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        profileTextView = view.findViewById(R.id.profileTextView);
        logoutButton = view.findViewById(R.id.logoutButton);
        bookedAppointmentsButton = view.findViewById(R.id.bookedAppointmentsButton);
        viewOrdersButton=view.findViewById(R.id.viewOrdersbtn);

        if (user == null) {
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish();
            }
        } else {
            String userEmail = user.getEmail();
            profileTextView.setText(userEmail);

            logoutButton.setVisibility(View.VISIBLE);
            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    auth.signOut();
                    Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getActivity(), Login.class);
                    startActivity(intent);

                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                }
            });

            bookedAppointmentsButton.setVisibility(View.VISIBLE);
            bookedAppointmentsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToFragment(new BookedAppointmentsFragment());
                }
            });
            viewOrdersButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToFragment(new ViewOrders());
                }
            });
        }

        return view;
    }

    private void navigateToFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }
}
