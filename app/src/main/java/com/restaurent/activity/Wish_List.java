package com.restaurent.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.restaurent.R;
import com.restaurent.activity.user.Login;
import com.restaurent.adapter.Wish_List_Adapter;
import com.restaurent.api_call.API_Get;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.constant_class.URL_Class;
import com.restaurent.db_handler.DataBaseHandlerAccount;
import com.restaurent.db_handler.DataBaseHandlerWishList;
import com.restaurent.interfaces.API_Result;
import com.restaurent.interfaces.WishListAPIRequest;
import com.restaurent.json_mechanism.GetJSONData;
import com.restaurent.mechanism.Methods;
import com.restaurent.network_checker.NetworkConnection;
import com.restaurent.shared_preferenc_estring.DataStorage;
import com.restaurent.utils.ProductDataSet;
import com.restaurent.utils.Response;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;


public class Wish_List extends AppCompatActivity implements API_Result, WishListAPIRequest {
    Toolbar toolbar;
    RecyclerView mWish_List_recycler_view;
    ArrayList<ProductDataSet> mDataSet = new ArrayList<>();
    ProgressDialog progressDialog;
    API_Result api_result;
    WishListAPIRequest wishListAPIRequest;
    CircleImageView profilePictureView;
    Bitmap fb_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish__list);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        api_result = this;
        wishListAPIRequest = this;
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        profilePictureView=(CircleImageView) toolbar.findViewById(R.id.image);
        URL fb_url = null;//small | noraml | large
        try {
            fb_url = new URL(DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"profile_pic"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpsURLConnection conn1 = null;
        try {
            if(fb_url==null){

            }
            else {
                conn1 = (HttpsURLConnection) fb_url.openConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setFollowRedirects(true);
//        conn1.setInstanceFollowRedirects(true);
        try {
            if(conn1==null){

            }
            else {
                fb_img = BitmapFactory.decodeStream(conn1.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(fb_url==null){

        }else {
            profilePictureView.setImageBitmap(fb_img);
        }
        progressDialog = new ProgressDialog(this);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mWish_List_recycler_view = (RecyclerView) findViewById(R.id.wish_list_recycler_view);
        mWish_List_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        if (NetworkConnection.connectionChecking(getApplicationContext())) {
            if (DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                String temp_url[] = {URL_Class.mURL + URL_Class.mURL_Get_WishList + Methods.current_language() +
                        URL_Class.mUser_Id + DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_id()};
                progressDialog.show();
                new API_Get().get_method(temp_url, api_result, "", JSON_Names.KEY_GET_TYPE, true, getBaseContext(), "WishList");
            } else {
                Intent intent = new Intent(Wish_List.this, Login.class);
                startActivity(intent);
                finish();
            }
        } else {
            Intent intent = new Intent(Wish_List.this, NoInternetConnection.class);
            startActivity(intent);
            finish();
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void result(String[] data, String source) {
        progressDialog.cancel();
        if (data != null) {
            if (data[0] != null) {
                if (source.equals("WishList")) {
                    setting(data[0]);
                }else if(source.equals("WishListAPIRequest")){
                    Response response = GetJSONData.getResponse(data[0]);
                    if (response != null) {
                        if (response.getmStatus() == 200) {
                            Methods.toast(response.getmMessage());
                            DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_PRODUCT_ID);
                        } else {
                            Methods.toast(response.getmMessage());
                        }
                    }
                }
            }else {
                Methods.toast(getResources().getString(R.string.error));
            }
        }else {
            Methods.toast(getResources().getString(R.string.error));
        }
    }

    @Override
    public void wish_list_api_request(String data, String[] url) {
        new API_Get().get_method(url, api_result, data, JSON_Names.KEY_POST_TYPE,
                true, getBaseContext(), "WishListAPIRequest");
    }

    public void setting(String data) {
        ArrayList<ProductDataSet> mDataSetTemp;
        if (!data.equals(JSON_Names.KEY_NO_DATA)) {
            mDataSetTemp = GetJSONData.getProductDetails(data);
            if (mDataSetTemp != null) {
                DataBaseHandlerWishList.getInstance(getApplicationContext()).delete_wish_list();
                for (int i = 0; i < mDataSetTemp.size(); i++) {
                    if (!DataBaseHandlerWishList.getInstance(getApplicationContext()).
                            checking_wish_list(mDataSetTemp.get(i).getProduct_id())) {
                        DataBaseHandlerWishList.getInstance(getApplicationContext()).add_to_wish_list
                                (mDataSetTemp.get(i).getProduct_id(), mDataSetTemp.get(i).getProduct_string());
                    }
                }
                mDataSet = mDataSetTemp;
            }
        }
        mWish_List_recycler_view.setAdapter(new Wish_List_Adapter(getApplicationContext(), mDataSet, wishListAPIRequest));
    }
}
