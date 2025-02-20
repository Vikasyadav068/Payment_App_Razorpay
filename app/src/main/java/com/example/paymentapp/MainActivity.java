package com.example.paymentapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements PaymentResultListener {

    private TextView txt1;
    private Button btn;
    private EditText editAmount, editName, editEmail, editPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI Elements
        txt1 = findViewById(R.id.paymentStatus);
        btn = findViewById(R.id.btn_pay);
        editAmount = findViewById(R.id.edit_amount);
        editName = findViewById(R.id.edit_name);
        editEmail = findViewById(R.id.edit_email);
        editPhone = findViewById(R.id.edit_phone);

        // Preload Razorpay Checkout
        Checkout.preload(MainActivity.this);

        // Payment Button Click Listener
        btn.setOnClickListener(v -> {
            String amountText = editAmount.getText().toString();
            String userName = editName.getText().toString();
            String userEmail = editEmail.getText().toString();
            String userPhone = editPhone.getText().toString();

            // ✅ Input Validation
            if (amountText.isEmpty() || userName.isEmpty() || userEmail.isEmpty() || userPhone.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please fill all details", Toast.LENGTH_SHORT).show();
                return;
            }

            int amount = Integer.parseInt(amountText);
            startPayment(amount, userName, userEmail, userPhone);
        });
    }

    public void startPayment(int amount, String userName, String userEmail, String userPhone) {
        Checkout checkout = new Checkout();
        checkout.setKeyID("Enter your test key here");

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", "Razorpay");  // Show "Razorpay" instead of "E-Commerce App"
            jsonObject.put("image", "https://razorpay.com/favicon.png"); // Razorpay official logo
            jsonObject.put("description", "Order Payment");
            jsonObject.put("currency", "INR");
            jsonObject.put("amount", amount * 100); // Amount should be in paise

            // ✅ Add Retry Options
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 2);
            jsonObject.put("retry", retryObj);

            // ✅ Dynamic User Details (Prefill)
            JSONObject prefill = new JSONObject();
            prefill.put("email", userEmail);
            prefill.put("contact", userPhone);
            prefill.put("name", userName);
            jsonObject.put("prefill", prefill);

            // ✅ Enable Payment Methods
            JSONObject method = new JSONObject();
            method.put("netbanking", true);
            method.put("card", true);
            method.put("upi", true);
            method.put("wallet", true);
            jsonObject.put("method", method);

            // ✅ Theme Customization
            JSONObject theme = new JSONObject();
            theme.put("color", "#4CAF50"); // Green Color for E-Commerce
            jsonObject.put("theme", theme);

            // ✅ Dynamic Order ID
            String orderId = UUID.randomUUID().toString(); // Generate unique Order ID
            JSONObject notes = new JSONObject();
            notes.put("order_id", orderId);
            notes.put("user_id", "USER123"); // Replace with actual user ID
            notes.put("product", "E-Commerce Purchase");
            jsonObject.put("notes", notes);

            // ✅ Payment Timeout
            jsonObject.put("timeout", 300); // 5-minute timeout

            // ✅ Start Payment Gateway
            checkout.open(MainActivity.this, jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        txt1.setText("✅ Payment Successful");
        Toast.makeText(MainActivity.this, "Payment Successful!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        txt1.setText("❌ Payment Failed");
        Toast.makeText(MainActivity.this, "Payment Failed: ", Toast.LENGTH_LONG).show();
    }
}
