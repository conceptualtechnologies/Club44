package com.restaurent.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;


import com.restaurent.R;
import com.restaurent.activity.account.MyAccountMainMenu;
import com.restaurent.activity.account.OrderHistory;
import com.restaurent.activity.user.Login;
import com.restaurent.activity.user.SignUp;
import com.restaurent.adapter.Product_Listing_Gird_And_List;
import com.restaurent.api_call.API_Get;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.constant_class.URL_Class;
import com.restaurent.db_handler.DataBaseHandlerAccount;
import com.restaurent.db_handler.DataBaseHandlerCart;
import com.restaurent.db_handler.DataBaseHandlerCartOptions;
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

public class Search extends AppCompatActivity implements View.OnClickListener, API_Result, WishListAPIRequest {
    public static ArrayList<ProductDataSet> mSearch_Details;
    Toolbar toolbar;
    RecyclerView search_result_recycler_view;
    SearchView search;
    TextView search_not_found_result;
    String mQuery;
    int i = 0;
    boolean condition = false;
    Product_Listing_Gird_And_List adapter;
    ProgressDialog progressDialog;
    FloatingActionButton view_changer_seller;
    API_Result api_result;
    WishListAPIRequest wishListAPIRequest;
    int mPageCount = 1;
    CircleImageView profilePictureView;
    Bitmap fb_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__list);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
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
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        api_result = this;
        wishListAPIRequest = this;

        progressDialog = new ProgressDialog(this);

        search_result_recycler_view = (RecyclerView) findViewById(R.id.search_result_recycler_view);
        search = (SearchView) findViewById(R.id.test_search_view);
        search_not_found_result = (TextView) findViewById(R.id.search_result_not_found);
        view_changer_seller = (FloatingActionButton) findViewById(R.id.fab);
        search_result_recycler_view.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        search_not_found_result.setText(R.string.empty_text);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search.clearFocus();
                String url[] = {URL_Class.mURL + URL_Class.mURL_Search_Product + Methods.current_language()
                        + URL_Class.mURL_Search + query + URL_Class.mURL_Limit + URL_Class.mURL_Page + mPageCount};
                if (NetworkConnection.connectionChecking(getApplicationContext())) {
                    new API_Get().get_method(url, api_result, "", JSON_Names.KEY_GET_TYPE, true, getBaseContext(), "Search");
                } else {
                    Intent intent = new Intent(Search.this, NoInternetConnection.class);
                    startActivity(intent);
                    finish();
                }
                mQuery = query;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mPageCount = 1;
                return false;
            }
        });

        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                sendEmpty();
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        update(menu);

        if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
            menu.findItem(R.id.user_name).setVisible(false);
            menu.findItem(R.id.my_order).setVisible(false);
            menu.findItem(R.id.logout).setVisible(false);
            menu.findItem(R.id.search).setVisible(false);
        } else {

            String title = DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_name();
            menu.findItem(R.id.user_name).setTitle(title);
            menu.findItem(R.id.register).setVisible(false);
            menu.findItem(R.id.login).setVisible(false);
            menu.findItem(R.id.search).setVisible(false);
        }
        return true;
    }

    public void update(Menu menu) {
        TextView textView;
        ImageView imageView;
        View view = menu.findItem(R.id.cart_count).getActionView();
        textView = (TextView) view.findViewById(R.id.cart_count_value);
        imageView = (ImageView) view.findViewById(R.id.cart_image_view);
        final String tempData = DataBaseHandlerCart.getInstance(getApplicationContext()).get_whole_list_count();
        if (!tempData.equals("0")) {
            textView.setText(String.valueOf(tempData));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentTransfer();
                }
            });
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    intentTransfer();
                    return false;
                }
            });
        } else {
            textView.setVisibility(View.INVISIBLE);
        }

    }

    public void intentTransfer() {
        Intent intent = new Intent(Search.this, Cart.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_PRODUCT_STRING);
            onBackPressed();
            finish();
        } else if (id == R.id.user_name) {
            Intent intent = new Intent(Search.this, MyAccountMainMenu.class);
            startActivity(intent);
        } else if (id == R.id.login) {
            Intent intent = new Intent(Search.this, Login.class);
            startActivity(intent);
        } else if (id == R.id.register) {
            Intent intent = new Intent(Search.this, SignUp.class);
            startActivity(intent);
        } else if (id == R.id.logout) {
            DataStorage.mRemoveSharedPreferenceString(getApplicationContext(),"profile_pic");
            DataBaseHandlerAccount.getInstance(getApplicationContext()).delete_account_detail();
            DataBaseHandlerWishList.getInstance(getApplicationContext()).delete_wish_list();
            DataBaseHandlerCart.getInstance(getApplicationContext()).delete_cart();
            DataBaseHandlerCartOptions.getInstance(getApplicationContext()).delete_cart_option();
            Intent intent = new Intent(Search.this, Home.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_wish_list) {
            if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                Methods.toast(getResources().getString(R.string.must_login));
                Intent intent = new Intent(Search.this, Login.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(Search.this, Wish_List.class);
                startActivity(intent);
            }
        } else if (id == R.id.my_order) {
            Intent intent = new Intent(Search.this, OrderHistory.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void gridRecyclerView() {
        search_result_recycler_view.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        sendType(true);
    }

    public void listRecyclerView() {
        search_result_recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        sendType(false);
    }

    public void sendEmpty(){
        search_result_recycler_view.setAdapter(null);
    }

    public void sendType(boolean type) {
        search_not_found_result.setVisibility(View.GONE);
        view_changer_seller.setVisibility(View.VISIBLE);
        view_changer_seller.setOnClickListener(this);
        fromSearch();
        adapter = new Product_Listing_Gird_And_List(getApplicationContext(), mSearch_Details, type,
                wishListAPIRequest, search_result_recycler_view);
        search_result_recycler_view.setAdapter(adapter);

        adapter.setOnLoadMoreListener(new Product_Listing_Gird_And_List.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPageCount++;
                String current_count = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_COUNT);
                if (mPageCount <= ((Integer.valueOf(current_count) / 6) + 1)) {
                    String url[] = new String[1];

                    url[0] = URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mURL_Search
                            + mQuery + URL_Class.mURL_Limit + URL_Class.mURL_Page + mPageCount;

                    mSearch_Details.add(null);
                    adapter.notifyItemInserted(mSearch_Details.size() - 1);

                    new API_Get().get_method(url, api_result, "", JSON_Names.KEY_GET_TYPE, true, getBaseContext(), "PageCalling");
                }
            }
        });
    }

    public void fromSearch() {
        DataStorage.mStoreSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_FROM_SEARCH, JSON_Names.KEY_TRUE_STRING);
    }

    public void check() {
        condition = true;
    }

    @Override
    protected void onResume() {
        invalidateOptionsMenu();
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                if (condition) {
                    if (i == 0) {
                        view_changer_seller.setImageResource(R.drawable.ic_view_list_white_18dp);
                        listRecyclerView();
                        i = 1;
                    } else {
                        view_changer_seller.setImageResource(R.drawable.ic_view_module_white_18dp);
                        gridRecyclerView();
                        i = 0;
                    }
                }
                break;
        }
    }

    @Override
    public void result(String[] data, String source) {
        progressDialog.cancel();
        if (data != null) {
            if (data[0] != null) {
                switch (source) {
                    case "Search":
                        mSearch_Details = GetJSONData.getProductDetails(data[0]);
                        check();
                        if (i == 0) {
                            sendType(true);
                        } else {
                            sendType(false);
                        }
                        break;
                    case "PageCalling":
                        if (!mSearch_Details.isEmpty()) {
                            mSearch_Details.remove(mSearch_Details.size() - 1);
                            adapter.notifyItemRemoved(mSearch_Details.size());
                            if (GetJSONData.getProductDetails(data[0]) != null) {
                                ArrayList<ProductDataSet> mTemp = GetJSONData.getProductDetails(data[0]);
                                if (mTemp != null)
                                    for (int i = 0; i < mTemp.size(); i++) {
                                        mSearch_Details.add(mTemp.get(i));
                                    }
                            }
                            adapter.notifyItemInserted(mSearch_Details.size());
                            adapter.setLoaded();
                        }
                        break;
                    case "WishListPost":
                        Response response = GetJSONData.getResponse(data[0]);
                        if (response != null) {
                            if (response.getmStatus() == 200) {
                                Methods.toast(response.getmMessage());
                            } else {
                                Methods.toast(response.getmMessage());
                            }
                        }
                        break;
                }
            } else {
                sendType(false);
                search_result_recycler_view.setVisibility(View.INVISIBLE);
                String result = "Result not found!!!";
                search_not_found_result.setText(result);
            }
        }
    }

    @Override
    public void wish_list_api_request(String data, String[] url) {
        new API_Get().get_method(url, api_result, data, JSON_Names.KEY_POST_TYPE, true,
                getBaseContext(), "WishListPost");
    }
}
