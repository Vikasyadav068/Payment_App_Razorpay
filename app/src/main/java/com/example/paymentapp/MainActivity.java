package com.example.paymentapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements PaymentResultListener {

    private TextView txt1;
    private Button btn;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        txt1=findViewById(R.id.paymentStatus);
        btn=findViewById(R.id.btn_pay);
        editText=findViewById(R.id.edit_amount);
        Checkout.preload(MainActivity.this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment(Integer.parseInt(editText.getText().toString()));
            }
        });

    }

public void startPayment(int Amount){
        Checkout checkout=new Checkout();
        checkout.setKeyID("enter your test  here");

        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("name", "Razor pay demo");
            jsonObject.put("description","buy a food for me");
            jsonObject.put("currency","INR");
            jsonObject.put("amount","Amount*100");

            JSONObject retryObj=new JSONObject();
            retryObj.put("enabled",true);
            retryObj.put("retry",retryObj);

            checkout.open(MainActivity.this,jsonObject);

        }
        catch (Exception e){

        }
}

    @Override
    public void onPaymentSuccess(String s) {
        txt1.setText(s);
    }

    @Override
    public void onPaymentError(int i, String s) {
txt1.setText("error :" + s );
    }
}