package com.example.pawpal10;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class Cart extends Fragment implements CartDetailsAdapter.OnItemClickListener {

    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    RecyclerView recyclerView;
    CartDetailsAdapter cartDetailsAdapter;
    List<CartDetailsModel> cartModelList;
    TextView totalAmountTextView;
    TextView emptyCartTextView;
    long totalAmount;

    public Cart() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cart, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        recyclerView = root.findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        totalAmountTextView = root.findViewById(R.id.totalAmountTextView);
        emptyCartTextView = root.findViewById(R.id.emptyCartTextView);
        cartModelList = new ArrayList<>();
        cartDetailsAdapter = new CartDetailsAdapter(getActivity(), cartModelList);
        cartDetailsAdapter.setOnItemClickListener(this); // Set listener
        recyclerView.setAdapter(cartDetailsAdapter);

        // Fetch cart data from Firestore
        db.collection("cartInfo")
                .document(auth.getCurrentUser().getUid())
                .collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            totalAmount = 0; // Reset total amount
                            cartModelList.clear();
                            for (DocumentSnapshot document : task.getResult()) {
                                String productName = document.getString("Product_name");

                                // Retrieve product_price as Long and convert to int
                                Long productPrice = document.getLong("Product_price");

                                // Retrieve quantity as Long and convert to int
                                Long quantityLong = document.getLong("quantity");
                                int quantity = (quantityLong != null) ? quantityLong.intValue() : 0;
                                String imageUrl = (String) document.get("imageUrl");
                                String size = (String) document.get("size");
                                Long totalPrice = document.getLong("Total_price");

                                CartDetailsModel cartDetailsModel = new CartDetailsModel(productName, productPrice, quantity, imageUrl, size, totalPrice);
                                cartModelList.add(cartDetailsModel);

                                // Add to total amount
                                if (totalPrice != null) {
                                    totalAmount += totalPrice;
                                }
                            }
                            totalAmountTextView.setText("Total Amount: ₹" + totalAmount);
                            if (cartModelList.isEmpty()) {
                                emptyCartTextView.setVisibility(View.VISIBLE);
                                totalAmountTextView.setVisibility(View.GONE);
                            } else {
                                emptyCartTextView.setVisibility(View.GONE);
                                totalAmountTextView.setVisibility(View.VISIBLE);
                            }
                            cartDetailsAdapter.notifyDataSetChanged(); // Notify adapter of data change
                        } else {
                            Log.d("CartFragment", "Error getting documents: ", task.getException());
                        }
                    }
                });

        return root;
    }

    @Override
    public void onAdd1ToCartClick(CartDetailsModel item) {
        // Handle add1 button click logic here
        db.collection("cartInfo").document(currentUser.getUid())
                .collection("products")
                .whereEqualTo("Product_name", item.getProduct_name())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // If product exists, increment the quantity and update the total price
                            DocumentReference docRef = querySnapshot.getDocuments().get(0).getReference();
                            long currentQuantity = querySnapshot.getDocuments().get(0).getLong("quantity");
                            long newQuantity = currentQuantity + 1;
                            long productPrice = querySnapshot.getDocuments().get(0).getLong("Product_price");
                            long newTotalPrice = newQuantity * productPrice;

                            docRef.update("quantity", newQuantity, "Total_price", newTotalPrice)
                                    .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Quantity and total price updated", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to update quantity and total price", Toast.LENGTH_SHORT).show());

                            item.setQuantity((int) newQuantity);
                            item.setTotal_price(newTotalPrice);
                            totalAmount += productPrice; // Update total amount
                            totalAmountTextView.setText("Total Amount: ₹" + totalAmount);
                            cartDetailsAdapter.notifyDataSetChanged();
                            checkIfCartIsEmpty(); // Check if the cart is empty
                        }
                    } else {
                        Toast.makeText(getContext(), "Error checking cart", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDelete1FromCartClick(CartDetailsModel item) {
        // Handle delete1 button click logic here
        db.collection("cartInfo").document(currentUser.getUid())
                .collection("products")
                .whereEqualTo("Product_name", item.getProduct_name())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            long currentQuantity = document.getLong("quantity");
                            long productPrice = document.getLong("Product_price");

                            // Update quantity or delete item based on current quantity
                            if (currentQuantity > 1) {
                                // If quantity is more than 1, decrement the quantity and update the total price
                                long newQuantity = currentQuantity - 1;
                                long newTotalPrice = newQuantity * productPrice;

                                document.getReference().update("quantity", newQuantity, "Total_price", newTotalPrice)
                                        .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Quantity and total price updated", Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to update quantity and total price", Toast.LENGTH_SHORT).show());

                                item.setQuantity((int) newQuantity);
                                item.setTotal_price(newTotalPrice);
                                totalAmount -= productPrice; // Update total amount
                                totalAmountTextView.setText("Total Amount: ₹" + totalAmount);
                                cartDetailsAdapter.notifyDataSetChanged();
                                checkIfCartIsEmpty(); // Check if the cart is empty
                            } else {
                                // If quantity is 1 (or 0), delete the item from Firestore
                                document.getReference().delete()
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(getContext(), "Item removed from cart", Toast.LENGTH_SHORT).show();
                                            cartModelList.remove(item); // Remove from local list
                                            totalAmount -= productPrice; // Update total amount
                                            totalAmountTextView.setText("Total Amount: ₹" + totalAmount);
                                            cartDetailsAdapter.notifyDataSetChanged(); // Refresh RecyclerView
                                            checkIfCartIsEmpty(); // Check if the cart is empty
                                        })
                                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to remove item", Toast.LENGTH_SHORT).show());
                            }
                        } else {
                            // Handle case where item not found (should not occur in normal flow)
                            Toast.makeText(getContext(), "Item not found in cart", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Error checking cart", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkIfCartIsEmpty() {
        if (cartModelList.isEmpty()) {
            emptyCartTextView.setVisibility(View.VISIBLE);
            totalAmountTextView.setVisibility(View.GONE);
        } else {
            emptyCartTextView.setVisibility(View.GONE);
            totalAmountTextView.setVisibility(View.VISIBLE);
        }
    }
}

