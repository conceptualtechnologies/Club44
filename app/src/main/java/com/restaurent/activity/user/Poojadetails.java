package com.restaurent.activity.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.restaurent.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Poojadetails extends AppCompatActivity  {
    Spinner s;
    private TextView tvvendordetail;
    private TextView tvitemdetail;
    private TextView tvbankdetaill;
    private TextView tvdelivery;
    ImageView iv_document_image;
    private Boolean upflag = false;
    private Uri selectedImage = null;
    private Bitmap bitmap, bitmapRotate;
    private ProgressDialog pDialog;
    String imagepath = "";
    String fname;
    private static final int SELECT_PICTURE = 100;
    private Uri filePath;
    File file;
    int Size;
    List<Boolean> blist = new ArrayList<Boolean>(Size);
    ArrayAdapter<String> adapter;
    int index;
    ImageView image1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poojadetails);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvbankdetaill = (TextView) findViewById(R.id.tv_bank_detail);
        tvbankdetaill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Poojadetails.this, Bankdetail.class);
                startActivity(intent);
            }
        });
        tvitemdetail = (TextView) findViewById(R.id.tv_item_detail);
        tvitemdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Poojadetails.this,Itemdetail.class);
                startActivity(intent);
            }
        });

        tvdelivery= (TextView) findViewById(R.id.tv_delivery);
        tvdelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Poojadetails.this, Delivery.class);
                startActivity(intent);
            }
        });



    }


}
