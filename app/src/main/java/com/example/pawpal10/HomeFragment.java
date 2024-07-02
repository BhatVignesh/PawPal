package com.example.pawpal10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.denzcoskun.imageslider.constants.ScaleTypes;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Set up ImageSlider
        ArrayList<SlideModel> imageList = new ArrayList<>();
        imageList.add(new SlideModel(R.drawable.dog1, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.dog2, ScaleTypes.CENTER_CROP));
        ImageSlider imageSlider = view.findViewById(R.id.image_slider);
        imageSlider.setImageList(imageList);

        // Set up click listeners for each category block
        LinearLayout dogFoodCategory = view.findViewById(R.id.dog_food_category);
        dogFoodCategory.setOnClickListener(v -> navigateToFragment(new DogFoodFragment()));

        LinearLayout catFoodCategory = view.findViewById(R.id.cat_food_category);
        catFoodCategory.setOnClickListener(v -> navigateToFragment(new CatFoodFragment()));

        LinearLayout pharmacyCategory = view.findViewById(R.id.pharmacy_category);
        pharmacyCategory.setOnClickListener(v -> navigateToFragment(new PharmacyFragment()));

        LinearLayout littersSuppliesCategory = view.findViewById(R.id.litters_supplies_category);
        littersSuppliesCategory.setOnClickListener(v -> navigateToFragment(new LittersSuppliesFragment()));

        LinearLayout groomingCategory = view.findViewById(R.id.grooming_category);
        groomingCategory.setOnClickListener(v -> navigateToFragment(new GroomingFragment()));

        LinearLayout toysCategory = view.findViewById(R.id.toys_category);
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

