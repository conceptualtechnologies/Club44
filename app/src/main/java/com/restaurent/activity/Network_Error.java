package com.restaurent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.restaurent.R;
import com.restaurent.network_checker.NetworkConnection;


public class Network_Error extends AppCompatActivity {
    ImageView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two__g__network__error);
        error = (ImageView) findViewById(R.id.two_g_error);
        error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkConnection.connectionChecking(getApplicationContext())) {
                    Intent intent = new Intent(Network_Error.this, SplashScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(Network_Error.this, NoInternetConnection.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
