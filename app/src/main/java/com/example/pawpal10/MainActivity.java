package com.example.pawpal10;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    BottomNavigationView bottomNavigationView;

    // Map to store menu item IDs and their corresponding fragments
    Map<Integer, Fragment> fragmentMap = new HashMap<>();
    ListView listView;
    String[] name = {"Home", "Category", "Paw", "Medical", "Profile"};
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

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
        }
//        listView = view.findViewById(R.id.listview); // Corrected ID here
//        arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, name);
//        listView.setAdapter(arrayAdapter);

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

//Toolbar toolbar = findViewById(R.id.search_toolbar);
//setSupportActionBar(toolbar);
//
//// Initialize the adapter
//String[] name = {"Home", "Category", "Paw", "Medical", "Profile"};
//arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, name);

//MenuItem searchItem = menu.findItem(R.id.action_search);
//SearchView searchView = (SearchView) searchItem.getActionView();
//
//// Configure SearchView
//        searchView.setQueryHint("Type here to search");
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        // Handle search query submit
//        return false;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
//        // Filter data based on newText
//        arrayAdapter.getFilter().filter(newText);
//        return true; // Return true to indicate the query has been handled
//    }
//});











