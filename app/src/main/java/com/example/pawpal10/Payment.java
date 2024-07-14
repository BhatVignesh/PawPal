package com.example.pawpal10;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Payment extends AppCompatActivity {

    private static final String TAG = "Payment";
    private PaymentSheet paymentSheet;
    private String paymentClientSecret;
    private PaymentSheet.CustomerConfiguration customerConfig;
    private ProgressBar progressBar;
    Button pay;
    TextInputEditText editTextMbno, editTextAddress;
    FirebaseAuth auth;
    FirebaseFirestore db;
    ArrayList<String> cartItemList=new ArrayList<>();
    String currentDate;
    long totalAmount;
    TextView emptyCartTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        db=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        pay = findViewById(R.id.payment);
        progressBar = findViewById(R.id.payment_progress_bar);
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);
        progressBar.setVisibility(View.GONE);
        editTextMbno=findViewById(R.id.mbnoEdit);
        editTextAddress=findViewById(R.id.addressEdit);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = sdf.format(new Date());


        // Retrieve total amount from Intent
        totalAmount = getIntent().getLongExtra("TOTAL_AMOUNT", 0);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                makePaymentRequest(totalAmount);
            }
        });
    }

    private void makePaymentRequest(long amount) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.0.123:3000/payment-sheet"; // Replace with your actual backend endpoint

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("amount", amount*100);
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON request body", e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            customerConfig = new PaymentSheet.CustomerConfiguration(
                                    response.getString("customer"),
                                    response.getString("ephemeralKey")
                            );
                            paymentClientSecret = response.getString("paymentIntent");
                            PaymentConfiguration.init(getApplicationContext(), response.getString("publishableKey"));

                            // Once data is fetched, you can present the PaymentSheet
                            presentPaymentSheet();
                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing JSON response", e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Error fetching PaymentSheet data", error);
                // Handle error condition here
            }
        });

        queue.add(jsonObjectRequest);
    }

    private void presentPaymentSheet() {
        PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("Example, Inc.")
                .customer(customerConfig)
                .allowsDelayedPaymentMethods(true)
                .build();
        paymentSheet.presentWithPaymentIntent(
                paymentClientSecret,
                configuration
        );
    }

    private void onPaymentSheetResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.d(TAG, "PaymentSheet: Canceled");
            Toast.makeText(this,"Payment Cancelled",Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Log.e(TAG, "PaymentSheet: Failed", ((PaymentSheetResult.Failed) paymentSheetResult).getError());
            Toast.makeText(this,"Payment Failed",Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Log.d(TAG, "PaymentSheet: Completed");
            Toast.makeText(this,"Payment Complete",Toast.LENGTH_SHORT).show();
            db.collection("cartInfo")
                    .document(auth.getCurrentUser().getUid())
                    .collection("products")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            cartItemList.clear();
                            for (DocumentSnapshot document : task.getResult()) {
                                String productName = document.getString("Product_name");
                                cartItemList.add(productName);
                            }

                            // Fetch the existing orders
                            db.collection("orders")
                                    .document(auth.getCurrentUser().getUid())
                                    .get()
                                    .addOnCompleteListener(orderTask -> {
                                        if (orderTask.isSuccessful()) {
                                            DocumentSnapshot document = orderTask.getResult();
                                            Map<String, Object> order = new HashMap<>();
                                            if (document.exists()) {
                                                // Document exists, get the existing data
                                                order = document.getData();
                                            }

                                            // Determine the next order number
                                            int nextOrderNumber = 1;
                                            if (order != null && !order.isEmpty()) {
                                                nextOrderNumber = order.size() + 1;
                                            }

                                            // Create a new order map
                                            Map<String, Object> newOrder = new HashMap<>();
                                            newOrder.put("address", editTextAddress.getText().toString());
                                            newOrder.put("date", currentDate);
                                            newOrder.put("mbno", editTextMbno.getText().toString());
                                            newOrder.put("total_amount", totalAmount);
                                            newOrder.put("products", cartItemList);

                                            // Add the new order to the existing orders
                                            order.put(String.valueOf(nextOrderNumber), newOrder);

                                            // Update the document with the new orders
                                            db.collection("orders")
                                                    .document(auth.getCurrentUser().getUid())
                                                    .set(order)
                                                    .addOnCompleteListener(updateTask -> {
                                                        if (!updateTask.isSuccessful()) {
                                                            Toast.makeText(Payment.this, "Failed to add order: " + updateTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            Log.e(TAG, "Failed to add order", updateTask.getException());
                                                        } else {
                                                            deleteCartItems(); // Delete cart items after placing the order
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(Payment.this, "Failed to fetch previous orders: " + orderTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            Log.e(TAG, "Error fetching previous orders", orderTask.getException());
                                        }
                                    });
                        } else {
                            Toast.makeText(Payment.this, "Failed to fetch cart items: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Error fetching cart items", task.getException());
                        }
                    });
        }}

            private void deleteCartItems() {
                String userId = auth.getCurrentUser().getUid();
                db.collection("cartInfo")
                        .document(userId)
                        .collection("products")
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                WriteBatch batch = db.batch();
                                for (DocumentSnapshot document : task.getResult()) {
                                    batch.delete(document.getReference());
                                }
                                batch.commit().addOnCompleteListener(batchTask -> {
                                    if (batchTask.isSuccessful()) {
                                        db.collection("cartInfo")
                                                .document(userId)
                                                .delete()
                                                .addOnCompleteListener(deletetask -> {
                                                    if (deletetask.isSuccessful()) {
                                                        Intent intent = new Intent(Payment.this, MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(Payment.this, "Failed to delete cart items", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(Payment.this, "Failed to delete cart items", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Log.e(TAG, "Error fetching cart items", task.getException());
                                Toast.makeText(Payment.this, "Failed to fetch cart items", Toast.LENGTH_SHORT).show();
                            }
                        });
            }}

