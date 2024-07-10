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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatFoodFragment extends Fragment implements DogFoodAdapter.OnItemClickListener {

    FirebaseAuth auth;
    FirebaseUser currentUser;
    private RecyclerView recyclerView;
    private DogFoodAdapter adapter;
    private List<DogFoodItem> dogFoodItemList;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cat_food, container, false);

        recyclerView = view.findViewById(R.id.CatFoodRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        recyclerView.addItemDecoration(new SpacingItemDecoration(spacingInPixels));

        dogFoodItemList = new ArrayList<>();
        adapter = new DogFoodAdapter(getContext(), dogFoodItemList);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        // Initialize Firebase Firestore and get current user
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("product").document("catFood").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> dogFoodMap = (Map<String, Object>) document.getData();
                        for (String key : dogFoodMap.keySet()) {
                            Map<String, Object> product = (Map<String, Object>) dogFoodMap.get(key);

                            String description = (String) product.get("description");
                            String name = (String) product.get("name");
                            String price = (String) product.get("price");
                            String productType = (String) product.get("product_type");
                            double rating = (Double) product.get("rating");
                            int reviewCount = ((Long) product.get("reviewCount")).intValue();
                            int pid = Integer.parseInt(key);
                            String imageUrl = (String) product.get("imageUrl");
                            String size=(String) product.get("size");

                            DogFoodItem item = new DogFoodItem(imageUrl, name, price, (float) rating, reviewCount, pid, productType, description,size);
                            dogFoodItemList.add(item);
                        }
                        // Notify the adapter that data has changed
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "No such document", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error getting document.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onAddToCartClick(DogFoodItem item) {
        addToCart(item);
    }

    private void addToCart(DogFoodItem item) {
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
                            long currentQuantity = querySnapshot.getDocuments().get(0).getLong("quantity");
                            long newQuantity = currentQuantity + 1;
                            long productPrice = querySnapshot.getDocuments().get(0).getLong("Product_price");
                            long newTotalPrice = newQuantity * productPrice;

                            docRef.update("quantity", newQuantity, "Total_price", newTotalPrice)
                                    .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Quantity and total price updated", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to update quantity and total price", Toast.LENGTH_SHORT).show());
                        } else {
                            // If product does not exist, add a new document
                            Map<String, Object> cartItem = new HashMap<>();
                            cartItem.put("Product_name", item.getName());
                            cartItem.put("Product_price", Double.parseDouble(item.getPrice())); // Parse price to double
                            cartItem.put("quantity", 1);
                            cartItem.put("imageUrl",item.getImageUrl());
                            cartItem.put("size",item.getSize());
                            cartItem.put("Total_price",Double.parseDouble(item.getPrice()));

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
