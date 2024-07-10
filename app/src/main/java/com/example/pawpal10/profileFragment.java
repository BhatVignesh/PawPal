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
import com.google.firebase.firestore.FirebaseFirestore;

public class profileFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private TextView profileTextView;
    private Button logoutButton, viewCartButton, payButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        profileTextView = view.findViewById(R.id.profileTextView);
        logoutButton = view.findViewById(R.id.logoutButton);
        viewCartButton = view.findViewById(R.id.view_cart_button);
        payButton = view.findViewById(R.id.payButton); // Assuming you have added a payButton in your layout XML

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

            viewCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToFragment(new Cart());
                }
            });

            payButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PaymentActivity.class);
                    startActivity(intent);
                }
            });
        }

        return view;
    }

    private void navigateToFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
