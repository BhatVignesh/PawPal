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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Cart extends Fragment {

    FirebaseFirestore db;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    CartDetailsAdapter cartDetailsAdapter;
    List<CartDetailsModel> cartModelList;

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

        cartModelList = new ArrayList<>();
        cartDetailsAdapter = new CartDetailsAdapter(getActivity(), cartModelList);
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
                            for (DocumentSnapshot document : task.getResult()) {
                                String productName = document.getString("Product_name");

                                // Retrieve product_price as Long and convert to int
                                Long productPrice = document.getLong("Product_price");

                                // Retrieve quantity as Long and convert to int
                                Long quantityLong = document.getLong("quantity");
                                int quantity = (quantityLong != null) ? quantityLong.intValue() : 0;

                                CartDetailsModel cartDetailsModel = new CartDetailsModel(productName, productPrice, quantity);
                                cartModelList.add(cartDetailsModel);
                            }
                            cartDetailsAdapter.notifyDataSetChanged(); // Notify adapter of data change
                        } else {
                            Log.d("CartFragment", "Error getting documents: ", task.getException());
                        }
                    }
                });



        return root;
    }
}