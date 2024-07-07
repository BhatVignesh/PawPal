package com.example.pawpal10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DogFoodFragment extends Fragment {

    private RecyclerView recyclerView;
    private DogFoodAdapter adapter;
    private List<DogFoodItem> dogFoodItemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dog_food, container, false);

        recyclerView = view.findViewById(R.id.popularItemsRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        recyclerView.addItemDecoration(new SpacingItemDecoration(spacingInPixels));

        dogFoodItemList = new ArrayList<>();
        // Populate the list with sample data
        dogFoodItemList.add(new DogFoodItem(R.drawable.popitem1, "Dog Food 1", "$19.99", 4.5f, 120));
        dogFoodItemList.add(new DogFoodItem(R.drawable.popitem2, "Dog Food 2", "$29.99", 4.2f, 80));
        dogFoodItemList.add(new DogFoodItem(R.drawable.popitem3, "Dog Food 3", "$33.99", 4.6f, 50));
        dogFoodItemList.add(new DogFoodItem(R.drawable.popitem4, "Dog Food 4", "$44.99", 4.4f, 60));
        dogFoodItemList.add(new DogFoodItem(R.drawable.popitem5, "Dog Food 5", "$55.99", 4.2f, 70));
        dogFoodItemList.add(new DogFoodItem(R.drawable.popitem6, "Dog Food 6", "$66.99", 4.0f, 90));

        adapter = new DogFoodAdapter(getContext(), dogFoodItemList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
