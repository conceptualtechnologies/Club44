package com.restaurent.activity.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.restaurent.R;
import com.restaurent.activity.Home;
import com.restaurent.activity.Home_Page;

public class Open extends AppCompatActivity {
    private  TextView Book,Events,Homes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);


        Book  = (TextView) findViewById(R.id.tv_booktables);

        Book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Open.this, Tabl.class);
                startActivity(intent);
            }
        });


      Events = (TextView) findViewById(R.id.tv_even);

        Events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Open.this, Even.class);
                startActivity(intent);
            }
        });
        Homes = (TextView) findViewById(R.id.tv_home);

        Homes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Open.this, Home.class);
                startActivity(intent);
            }
        });


    }
}
