package com.example.pawpal10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.util.Log;
import android.widget.Toast;

public class HomeFragment extends Fragment {

    Button pbtn1,pbtn2,pbtn3,pbtn4;
    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser currentUser;
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
        pbtn1=view.findViewById(R.id.pi_btn1);
        pbtn2=view.findViewById(R.id.pi_btn2);
        pbtn3=view.findViewById(R.id.pi_btn3);
        pbtn4=view.findViewById(R.id.pi_btn4);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser= auth.getCurrentUser();
        pbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = "Pedigree";
                long productPrice=1500;

                db.collection("cartInfo").document(currentUser.getUid())
                        .collection("products")
                        .whereEqualTo("Product_name", productName)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    boolean productExists = false;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        productExists = true;
                                        // Get the current quantity and total price
                                        long currentQuantity = document.getLong("quantity");
                                        // Increment the quantity by 1
                                        long newQuantity = currentQuantity + 1;
                                        long newTotalPrice = newQuantity * productPrice;

                                        db.collection("cartInfo").document(currentUser.getUid())
                                                .collection("products").document(document.getId())
                                                .update("quantity", newQuantity, "Total_price", newTotalPrice)
                                                .addOnSuccessListener(aVoid -> {
                                                    // Show Toast message for updated product
                                                    Toast.makeText(v.getContext(), "Pedigree Added.", Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                    if (!productExists) {
                                        // Product doesn't exist, so create it
                                        Map<String, Object> newProduct = new HashMap<>();
                                        newProduct.put("Product_name", productName);
                                        newProduct.put("Product_price", productPrice); // Set the price accordingly
                                        newProduct.put("Total_price", productPrice); // Initial total price
                                        newProduct.put("imageUrl", "https://firebasestorage.googleapis.com/v0/b/pawpal-7c02f.appspot.com/o/popitem1.png?alt=media&token=b43537f6-7f92-40cd-ba23-28a09a192a84");
                                        newProduct.put("quantity", 1);
                                        newProduct.put("size", "10kg");

                                        db.collection("cartInfo").document(currentUser.getUid())
                                                .collection("products")
                                                .add(newProduct)
                                                .addOnSuccessListener(documentReference -> {
                                                    // Show Toast message for added product
                                                    Toast.makeText(v.getContext(), "Product added", Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                } else {
                                    Log.d("Firestore", "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });
        pbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String productName = "Whiskas";
                long productPrice = 1999;

                db.collection("cartInfo").document(currentUser.getUid())
                        .collection("products")
                        .whereEqualTo("Product_name", productName)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    boolean productExists = false;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        productExists = true;
                                        // Get the current quantity and total price
                                        long currentQuantity = document.getLong("quantity");
                                        // Increment the quantity by 1
                                        long newQuantity = currentQuantity + 1;
                                        long newTotalPrice = newQuantity * productPrice;

                                        db.collection("cartInfo").document(currentUser.getUid())
                                                .collection("products").document(document.getId())
                                                .update("quantity", newQuantity, "Total_price", newTotalPrice)
                                                .addOnSuccessListener(aVoid -> {
                                                    // Show Toast message for updated product
                                                    Toast.makeText(v.getContext(), "Whiskas Added.", Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                    if (!productExists) {
                                        // Product doesn't exist, so create it
                                        Map<String, Object> newProduct = new HashMap<>();
                                        newProduct.put("Product_name", productName);
                                        newProduct.put("Product_price", productPrice); // Set the price accordingly
                                        newProduct.put("Total_price", productPrice); // Initial total price
                                        newProduct.put("imageUrl", "https://www.whiskas.in/cdn-cgi/image/format=auto,q=90/sites/g/files/fnmzdf2051/files/2022-09/118853301007853-product-image-1.png");
                                        newProduct.put("quantity", 1);
                                        newProduct.put("size", "10kg");

                                        db.collection("cartInfo").document(currentUser.getUid())
                                                .collection("products")
                                                .add(newProduct)
                                                .addOnSuccessListener(documentReference -> {
                                                    // Show Toast message for added product
                                                    Toast.makeText(v.getContext(), "Product added", Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                } else {
                                    Log.d("Firestore", "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });
        pbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String productName = "Vetplus Nutraceutical Supplement Coatex Blister for Dog & Cat";
                long productPrice = 5000;

                db.collection("cartInfo").document(currentUser.getUid())
                        .collection("products")
                        .whereEqualTo("Product_name", productName)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    boolean productExists = false;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        productExists = true;
                                        // Get the current quantity and total price
                                        long currentQuantity = document.getLong("quantity");
                                        // Increment the quantity by 1
                                        long newQuantity = currentQuantity + 1;
                                        long newTotalPrice = newQuantity * productPrice;

                                        db.collection("cartInfo").document(currentUser.getUid())
                                                .collection("products").document(document.getId())
                                                .update("quantity", newQuantity, "Total_price", newTotalPrice)
                                                .addOnSuccessListener(aVoid -> {
                                                    // Show Toast message for updated product
                                                    Toast.makeText(v.getContext(), "Vetplus Coatex added.", Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                    if (!productExists) {
                                        // Product doesn't exist, so create it
                                        Map<String, Object> newProduct = new HashMap<>();
                                        newProduct.put("Product_name", productName);
                                        newProduct.put("Product_price", productPrice); // Set the price accordingly
                                        newProduct.put("Total_price", productPrice); // Initial total price
                                        newProduct.put("imageUrl", "https://www.orangepet.in/cdn/shop/products/Coatex-caps_900x.png?v=1664182736");
                                        newProduct.put("quantity", 1);
                                        newProduct.put("size", "800mg");

                                        db.collection("cartInfo").document(currentUser.getUid())
                                                .collection("products")
                                                .add(newProduct)
                                                .addOnSuccessListener(documentReference -> {
                                                    // Show Toast message for added product
                                                    Toast.makeText(v.getContext(),  "Vetplus Coatex added.", Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                } else {
                                    Log.d("Firestore", "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });
        pbtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String productName = "Meowgic Paws Tofu Cat Litter – Meowgic Paws Pet Supplies";
                long productPrice = 800;

                db.collection("cartInfo").document(currentUser.getUid())
                        .collection("products")
                        .whereEqualTo("Product_name", productName)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    boolean productExists = false;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        productExists = true;
                                        // Get the current quantity and total price
                                        long currentQuantity = document.getLong("quantity");
                                        // Increment the quantity by 1
                                        long newQuantity = currentQuantity + 1;
                                        long newTotalPrice = newQuantity * productPrice;

                                        db.collection("cartInfo").document(currentUser.getUid())
                                                .collection("products").document(document.getId())
                                                .update("quantity", newQuantity, "Total_price", newTotalPrice)
                                                .addOnSuccessListener(aVoid -> {
                                                    // Show Toast message for updated product
                                                    Toast.makeText(v.getContext(), productName + " added.", Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                    if (!productExists) {
                                        // Product doesn't exist, so create it
                                        Map<String, Object> newProduct = new HashMap<>();
                                        newProduct.put("Product_name", productName);
                                        newProduct.put("Product_price", productPrice); // Set the price accordingly
                                        newProduct.put("Total_price", productPrice); // Initial total price
                                        newProduct.put("imageUrl", "https://headsupfortails.com/cdn/shop/files/DSC_2566.jpg?v=1713440062&width=1426");
                                        newProduct.put("quantity", 1);
                                        newProduct.put("size", "1kg");

                                        db.collection("cartInfo").document(currentUser.getUid())
                                                .collection("products")
                                                .add(newProduct)
                                                .addOnSuccessListener(documentReference -> {
                                                    // Show Toast message for added product
                                                    Toast.makeText(v.getContext(), productName + " added.", Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                } else {
                                    Log.d("Firestore", "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });

        return view;
    }

    private void navigateToFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}

