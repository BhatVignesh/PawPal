package com.example.pawpal10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.denzcoskun.imageslider.constants.ScaleTypes;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ListView listView;
    private String[] name = {"Home", "Category", "Paw", "Medical", "Profile"};
    private ArrayAdapter<String> arrayAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize ListView and ArrayAdapter
        listView = view.findViewById(R.id.listview); // Corrected ID here
        arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, name);
        listView.setAdapter(arrayAdapter);

        // Set up ImageSlider
        ArrayList<SlideModel> imageList = new ArrayList<>();
        imageList.add(new SlideModel(R.drawable.dog1, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.dog2, ScaleTypes.CENTER_CROP));
        ImageSlider imageSlider = view.findViewById(R.id.image_slider);
        imageSlider.setImageList(imageList);

        return view;
    }

}
