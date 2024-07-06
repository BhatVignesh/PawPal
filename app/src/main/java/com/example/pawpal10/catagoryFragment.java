package com.example.pawpal10;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class catagoryFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public catagoryFragment() {
        // Required empty public constructor
    }

    public static catagoryFragment newInstance(String param1, String param2) {
        catagoryFragment fragment = new catagoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catagory, container, false);

        LinearLayout dogFoodCategory = view.findViewById(R.id.dog_food_category2);
        dogFoodCategory.setOnClickListener(v -> navigateToFragment(new DogFoodFragment()));

        LinearLayout catFoodCategory = view.findViewById(R.id.cat_food_category2);
        catFoodCategory.setOnClickListener(v -> navigateToFragment(new CatFoodFragment()));

        LinearLayout pharmacyCategory = view.findViewById(R.id.pharmacy_category2);
        pharmacyCategory.setOnClickListener(v -> navigateToFragment(new PharmacyFragment()));

        LinearLayout littersSuppliesCategory = view.findViewById(R.id.litters_supplies_category2);
        littersSuppliesCategory.setOnClickListener(v -> navigateToFragment(new LittersSuppliesFragment()));

        LinearLayout groomingCategory = view.findViewById(R.id.grooming_category2);
        groomingCategory.setOnClickListener(v -> navigateToFragment(new GroomingFragment()));

        LinearLayout toysCategory = view.findViewById(R.id.toys_category2);
        toysCategory.setOnClickListener(v -> navigateToFragment(new ToysFragment()));

        return view;
    }

    private void navigateToFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}