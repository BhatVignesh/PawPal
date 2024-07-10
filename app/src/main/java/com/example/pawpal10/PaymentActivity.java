package com.example.pawpal10;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pawpal10.databinding.PaymentActivityBinding;

public class PaymentActivity extends AppCompatActivity {
    public static final String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    private String amount;
    private String name = "Highbrow Director";
    private String upiId = "vismaypawar5@okhdfcbank";
    private String transactionNote = "pay test";
    private String status;
    private Uri uri;
    private PaymentActivityBinding binding;

    // ActivityResultLauncher for starting Google Pay
    private ActivityResultLauncher<Intent> googlePayLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int resultCode = result.getResultCode();
                Intent data = result.getData();
                handleGooglePayResult(resultCode, data);
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PaymentActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.googlePayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = binding.amountEditText.getText().toString();
                if (!amount.isEmpty()) {
                    uri = getUpiPaymentUri(name, upiId, transactionNote, amount);
                    payWithGPay();
                } else {
                    binding.amountEditText.setError("Amount is required!");
                    binding.amountEditText.requestFocus();
                }
            }
        });
    }

    private boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private Uri getUpiPaymentUri(String name, String upiId, String transactionNote, String amount) {
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", transactionNote)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();
    }

    private void payWithGPay() {
        if (isAppInstalled(this, GOOGLE_PAY_PACKAGE_NAME)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
            googlePayLauncher.launch(intent);
        } else {
            Toast.makeText(PaymentActivity.this, "Please Install Google Pay (GPay)", Toast.LENGTH_SHORT).show();
            // Optionally, direct the user to install Google Pay from the Play Store
            Intent installIntent = new Intent(Intent.ACTION_VIEW);
            installIntent.setData(Uri.parse("market://details?id=" + GOOGLE_PAY_PACKAGE_NAME));
            startActivity(installIntent);
        }
    }

    private void handleGooglePayResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            status = data.getStringExtra("Status");
            if (status != null && status.toLowerCase().equals("success")) {
                Toast.makeText(PaymentActivity.this, "Transaction Successful", Toast.LENGTH_SHORT).show();
                // Handle successful transaction
            } else {
                Toast.makeText(PaymentActivity.this, "Transaction Failed", Toast.LENGTH_SHORT).show();
                // Handle failed transaction
            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(PaymentActivity.this, "Transaction Cancelled", Toast.LENGTH_SHORT).show();
            // Handle transaction cancelled
        } else {
            Toast.makeText(PaymentActivity.this, "Transaction Error", Toast.LENGTH_SHORT).show();
            // Handle transaction error
        }
    }
}

