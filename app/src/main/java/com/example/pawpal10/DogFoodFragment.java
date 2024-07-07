package com.example.pawpal10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DogFoodFragment extends Fragment implements DogFoodAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private DogFoodAdapter adapter;
    private List<DogFoodItem> dogFoodItemList;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dog_food, container, false);

        recyclerView = view.findViewById(R.id.popularItemsRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        recyclerView.addItemDecoration(new SpacingItemDecoration(spacingInPixels));

        dogFoodItemList = new ArrayList<>();
        dogFoodItemList.add(new DogFoodItem(R.drawable.popitem1, "Pedigree", "$19.99", 4.5f, 120));
        dogFoodItemList.add(new DogFoodItem(R.drawable.popitem2, "DesiNayiFood", "$29.99", 4.2f, 80));
        dogFoodItemList.add(new DogFoodItem(R.drawable.popitem3, "VegDogFood", "$33.99", 4.6f, 50));
        dogFoodItemList.add(new DogFoodItem(R.drawable.popitem4, "VeganDogFood", "$44.99", 4.4f, 60));
        dogFoodItemList.add(new DogFoodItem(R.drawable.popitem5, "Wiskers", "$55.99", 4.2f, 70));
        dogFoodItemList.add(new DogFoodItem(R.drawable.popitem6, "Bone", "$66.99", 4.0f, 90));

        adapter = new DogFoodAdapter(getContext(), dogFoodItemList);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        // Initialize Firebase Firestore and get current user
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        return view;
    }

    @Override
    public void onAddToCartClick(DogFoodItem item) {
        addToCart(item);
    }

    private void addToCart(DogFoodItem item) {
        if (currentUser == null) {
            Toast.makeText(getContext(), "User not signed in", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("cartInfo").document(currentUser.getUid())
                .collection("products")
                .whereEqualTo("Product_name", item.getName())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // If product exists, increment the quantity
                            DocumentReference docRef = querySnapshot.getDocuments().get(0).getReference();
                            docRef.update("quantity", querySnapshot.getDocuments().get(0).getLong("quantity") + 1)
                                    .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Quantity updated", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to update quantity", Toast.LENGTH_SHORT).show());
                        } else {
                            // If product does not exist, add a new document
                            Map<String, Object> cartItem = new HashMap<>();
                            cartItem.put("Product_name", item.getName());
                            cartItem.put("Product_price", Double.parseDouble(item.getPrice().replace("$", ""))); // Parse price to double
                            cartItem.put("quantity", 1);

                            db.collection("cartInfo").document(currentUser.getUid())
                                    .collection("products")
                                    .add(cartItem)
                                    .addOnSuccessListener(documentReference -> Toast.makeText(getContext(), item.getName() + " added to cart", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to add to cart", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        Toast.makeText(getContext(), "Error checking cart", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}



