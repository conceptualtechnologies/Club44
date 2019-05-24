package com.restaurent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

//import com.payUMoney.sdk.SdkConstants;

//import com.payUMoney.sdk.SdkConstants;

public class paymentSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        TextView textView = (TextView) findViewById(R.id.status);
        /*String status = getIntent().getStringExtra(SdkS.RESULT);
        Toast.makeText(getApplicationContext(),status, Toast.LENGTH_LONG).show();
        if(status.equals("success"))
            textView.setText("Congratz!!Your payment is successful");
        else
            textView.setText("Payment failed");

    }*/

    }}
