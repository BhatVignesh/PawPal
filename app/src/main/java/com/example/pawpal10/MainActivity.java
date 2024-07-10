package com.example.pawpal10;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private BottomNavigationView bottomNavigationView;
    private Map<Integer, Fragment> fragmentMap;
    private Map<String, Fragment> searchFragmentMap;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private SearchView searchView;
    private ArrayList<String> arrayList;
    private ImageView cartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeFirebaseAuth();
        initializeSearchView();  // Initialize SearchView first
        initializeBottomNavigationView();
        initializeFragments();
        initializeCartButton();
        checkUserAuthentication();
    }

    private void initializeFirebaseAuth() {
        auth = FirebaseAuth.getInstance();
    }

    private void initializeBottomNavigationView() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = fragmentMap.get(item.getItemId());
                if (selectedFragment != null) {
                    replaceFragment(selectedFragment);
                    return true;
                }
                return false;
            }
        });
    }

    private void initializeFragments() {
        fragmentMap = new HashMap<>();
        fragmentMap.put(R.id.home_icon, new HomeFragment());
        fragmentMap.put(R.id.catagory_icon, new catagoryFragment());
        fragmentMap.put(R.id.paw_icon, new pawFragment());
        fragmentMap.put(R.id.profile_icon, new profileFragment());

        // Initialize searchFragmentMap for search view items
        searchFragmentMap = new HashMap<>();
        searchFragmentMap.put("Home", new HomeFragment());
        searchFragmentMap.put("Category", new catagoryFragment());
        searchFragmentMap.put("Paw", new pawFragment());
        searchFragmentMap.put("Profile", new profileFragment());

        // Set initial fragment
        replaceFragment(new HomeFragment());
    }

    private void checkUserAuthentication() {
        user = auth.getCurrentUser();
        if (user == null) {
            redirectToLogin();
        }
    }

    private void redirectToLogin() {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();
    }

    private void initializeSearchView() {
        searchView = findViewById(R.id.searchView);
        listView = findViewById(R.id.searchListView);
        listView.setVisibility(View.GONE);

        arrayList = new ArrayList<>();
        arrayList.add("Home");
        arrayList.add("Category");
        arrayList.add("Paw");
        arrayList.add("Profile");

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        // Set query hint programmatically
        searchView.setQueryHint("Search");

        // Handle clicks on SearchView to expand and focus
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the SearchView with one tap
                if (!searchView.isIconified()) {
                    return;
                }
                searchView.setIconified(false);
                searchView.requestFocus();
            }
        });

        // Handle query text change for filtering ListView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listView.setVisibility(View.VISIBLE);
                arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });

        // Handle closing SearchView to hide ListView
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                listView.setVisibility(View.GONE);
                return false;
            }
        });

        // Handle clicks on ListView items
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = arrayAdapter.getItem(position);
            Fragment selectedFragment = searchFragmentMap.get(selectedItem);
            if (selectedFragment != null) {
                replaceFragment(selectedFragment);
                searchView.setQuery("", false);
                searchView.clearFocus();
                listView.setVisibility(View.GONE);
            }
        });
    }

    private void initializeCartButton() {
        cartButton = findViewById(R.id.cartButton);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Load animation
                Animation scaleAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.click_scale);
                // Start animation
                v.startAnimation(scaleAnimation);
                replaceFragment(new Cart());
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();

        // Handle SearchView visibility
        if (fragment instanceof profileFragment) {
            searchView.setVisibility(View.GONE);
        } else {
            searchView.setVisibility(View.VISIBLE);
        }
    }
}

