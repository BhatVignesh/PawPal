package com.example.pawpal10;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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

public class Cart extends Fragment implements CartDetailsAdapter.OnItemClickListener {

    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    RecyclerView recyclerView;
    CartDetailsAdapter cartDetailsAdapter;
    List<CartDetailsModel> cartModelList;
    TextView totalAmountTextView;
    TextView emptyCartTextView;
    Button proceedToPaymentBtn; // Added Pay button

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
        proceedToPaymentBtn=root.findViewById(R.id.proceed_to_payment_btn);

        totalAmountTextView = root.findViewById(R.id.totalAmountTextView);
        emptyCartTextView = root.findViewById(R.id.emptyCartTextView);
        cartModelList = new ArrayList<>();
        cartDetailsAdapter = new CartDetailsAdapter(getActivity(), cartModelList);
        cartDetailsAdapter.setOnItemClickListener(this); // Set listener
        recyclerView.setAdapter(cartDetailsAdapter);

        // Fetch cart data from Firestore
        fetchCartItems();
        proceedToPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long totalAmount = getTotalAmount();
                Intent intent = new Intent(getActivity(), Payment.class);
                intent.putExtra("TOTAL_AMOUNT", totalAmount);
                startActivity(intent);
            }
        });
        return root;
    }

    private void fetchCartItems() {
        db.collection("cartInfo")
                .document(auth.getCurrentUser().getUid())
                .collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            long totalAmount = 0; // Reset total amount
                            cartModelList.clear(); // Clear the list before adding new items
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

                            // Log the size of cartModelList for debugging
                            Log.d("CartFragment", "Cart items fetched, size: " + cartModelList.size());

                            if (cartModelList.isEmpty()) {
                                emptyCartTextView.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                totalAmountTextView.setVisibility(View.GONE);
                                proceedToPaymentBtn.setVisibility(View.GONE);
                            } else {
                                emptyCartTextView.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                totalAmountTextView.setVisibility(View.VISIBLE);
                                proceedToPaymentBtn.setVisibility(View.VISIBLE);
                            }

                            totalAmountTextView.setText("Total Amount: ₹" + totalAmount);
                            cartDetailsAdapter.notifyDataSetChanged(); // Notify adapter of data change
                        } else {
                            Log.d("CartFragment", "Error getting documents: ", task.getException());
                        }
                    }
                });
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
                            fetchCartItems(); // Refresh cart items
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
                                fetchCartItems(); // Refresh cart items
                            } else {
                                // If quantity is 1 (or 0), delete the item from Firestore
                                document.getReference().delete()
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(getContext(), "Item removed from cart", Toast.LENGTH_SHORT).show();
                                            cartModelList.remove(item); // Remove from local list
                                            fetchCartItems(); // Refresh cart items
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

    // Method to get total amount from TextView (example implementation)
    private long getTotalAmount() {
        String totalAmountText = totalAmountTextView.getText().toString();
        // Assuming format is "Total Amount: ₹XXX"
        String[] parts = totalAmountText.split("₹");
        if (parts.length > 1) {
            String amountString = parts[1].trim();
            return Long.parseLong(amountString);
        }
        return 0;
    }
}
