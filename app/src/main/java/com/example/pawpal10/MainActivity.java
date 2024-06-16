package com.example.pawpal10;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private Button logoutButton;
    private TextView textView;
    private BottomNavigationView bottomNavigationView;

    // Map to store menu item IDs and their corresponding fragments
    private Map<Integer, Fragment> fragmentMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Initialize UI elements
//        logoutButton = findViewById(R.id.logout_button); // Assuming your button ID is logout_button
//        textView = findViewById(R.id.user_details); // Assuming your TextView ID is user_details
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Populate fragmentMap with menu item IDs and their corresponding fragments
        fragmentMap.put(R.id.home_icon, new HomeFragment());
        fragmentMap.put(R.id.catagory_icon, new catagoryFragment());
        fragmentMap.put(R.id.paw_icon, new pawFragment());
        fragmentMap.put(R.id.medical_icon, new checkupFragment());
        fragmentMap.put(R.id.profile_icon, new profileFragment());

        // Set initial fragment
        replaceFragment(new HomeFragment());

        // Check if user is logged in
        user = auth.getCurrentUser();
        if (user == null) {
            // Redirect to login if not logged in
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            // Set user details if logged in
//            textView.setText(user.getEmail());
        }

        // Bottom navigation item selection listener
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Get the fragment corresponding to the selected menu item ID from fragmentMap
                Fragment selectedFragment = fragmentMap.get(item.getItemId());
                if (selectedFragment != null) {
                    replaceFragment(selectedFragment);
                    return true;
                }
                return false;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment); // Assuming your FrameLayout ID is frameLayout
        fragmentTransaction.commit();
    }
}










