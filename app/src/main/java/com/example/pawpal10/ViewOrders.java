package com.example.pawpal10;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Map;

public class ViewOrders extends Fragment {

    private RecyclerView recyclerViewOrders;
    private OrderAdapter orderAdapter;
    private ArrayList<OrderItem> orderList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private TextView noOrdersTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(getContext(), orderList);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_orders, container, false);

        noOrdersTextView = view.findViewById(R.id.No_orders_text);
        recyclerViewOrders = view.findViewById(R.id.orderRecyclerView);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewOrders.setAdapter(orderAdapter);

        fetchOrdersFromFirestore();

        return view;
    }

    private void fetchOrdersFromFirestore() {
        db.collection("orders").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> ordersMap = document.getData();
                        if (ordersMap != null && !ordersMap.isEmpty()) {
                            noOrdersTextView.setVisibility(View.GONE);
                            for (String key : ordersMap.keySet()) {
                                Map<String, Object> order = (Map<String, Object>) ordersMap.get(key);
                                String date = (String) order.get("date");
                                Long totalAmount = (Long) order.get("total_amount");
                                List<String> productList = (List<String>) order.get("products");
                                OrderItem orderItem = new OrderItem(date, productList, totalAmount);
                                orderList.add(orderItem);
                            }
                        } else {
                            noOrdersTextView.setVisibility(View.VISIBLE);
                        }
                        orderAdapter.notifyDataSetChanged();
                    } else {
                        noOrdersTextView.setVisibility(View.VISIBLE);
                        Log.d(TAG, "No such document");
                    }
                } else {
                    noOrdersTextView.setVisibility(View.VISIBLE);
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}


