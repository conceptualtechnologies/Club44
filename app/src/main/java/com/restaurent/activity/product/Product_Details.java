package com.restaurent.activity.product;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.restaurent.Application_Context;
import com.restaurent.R;
import com.restaurent.activity.Cart;
import com.restaurent.activity.Home;
import com.restaurent.activity.Network_Error;
import com.restaurent.activity.NoInternetConnection;
import com.restaurent.activity.Search;
import com.restaurent.activity.Wish_List;
import com.restaurent.activity.account.MyAccountMainMenu;
import com.restaurent.activity.account.OrderHistory;
import com.restaurent.activity.user.CompanyProfile;
import com.restaurent.activity.user.Company_Aims;
import com.restaurent.activity.user.Customer_Care;
import com.restaurent.activity.user.Login;
import com.restaurent.activity.user.Offer;
import com.restaurent.activity.user.SignUp;
import com.restaurent.adapter.ProductOptionAdapter;
import com.restaurent.adapter.QuantityAdapter;
import com.restaurent.api_call.API_Get;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.constant_class.URL_Class;
import com.restaurent.db_handler.DataBaseHandlerAccount;
import com.restaurent.db_handler.DataBaseHandlerCart;
import com.restaurent.db_handler.DataBaseHandlerCartOptions;
import com.restaurent.db_handler.DataBaseHandlerWishList;
import com.restaurent.fragments.product.Product_Review_Post;
import com.restaurent.interfaces.API_Result;
import com.restaurent.interfaces.ReviewPost;
import com.restaurent.json_mechanism.GetJSONData;
import com.restaurent.mechanism.Methods;
import com.restaurent.network_checker.NetworkConnection;
import com.restaurent.shared_preferenc_estring.DataStorage;
import com.restaurent.utils.ProductDataSet;
import com.restaurent.utils.ProductImageDataSet;
import com.restaurent.utils.ProductOptionDataSet;
import com.restaurent.utils.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

public class Product_Details extends AppCompatActivity implements
        View.OnClickListener, API_Result, ReviewPost {

    Toolbar toolbar;
    EditText pincode;
    Button btn_product_add_to_cart, pincodecheck;
    ImageButton img_btn_add_wish_list;
    TextView txt_product_title, txt_product_price, txt_product_special_price, txt_product_child_image_count;
    TextView txt_product_color_title, txt_product_write_rating;
    ImageView img_product_parent_image, img_product_child_one, img_product_child_two, img_product_child_three, img_product_child_four;
    TextView txt_product_detail_details, txt_product_details_no_of_reviews, txt_product_description,
            txt_product_description_title, pincoderesponse;
    LinearLayout mImageLinearLayout, mReviewHolder;
    RatingBar rating_bar_product;
    RecyclerView option_holder;
    ArrayList<ProductImageDataSet> mImageList = new ArrayList<>();
    ArrayList<ProductOptionDataSet> mOptionList = new ArrayList<>();
    HashMap<Integer, ArrayList<ProductOptionDataSet>> mOptionListChild = new HashMap<>();
    ProductDataSet mProductDataSet = new ProductDataSet();
    ProductOptionAdapter productOptionAdapter;
    String mProduct_String, product_id;
    HashMap<String, Object> mDataSet = new HashMap<>();
    API_Result api_result;
    Spinner quantitySpinner;
    String quantity;
    CircleImageView profilePictureView;
    Bitmap fb_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product__details);

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
        api_result = this;

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setIcon(R.drawable.logo);
        btn_product_add_to_cart = (Button) findViewById(R.id.product_detail_add_to_cart);
        img_btn_add_wish_list = (ImageButton) findViewById(R.id.product_detail_add_to_fav);
        txt_product_title = (TextView) findViewById(R.id.product_detail_title);
        txt_product_price = (TextView) findViewById(R.id.product_detail_price);
        txt_product_special_price = (TextView) findViewById(R.id.product_detail_special_price);
        img_product_parent_image = (ImageView) findViewById(R.id.product_detail_image_view_parent);
        txt_product_color_title = (TextView) findViewById(R.id.product_detail_color_title);
        txt_product_child_image_count = (TextView) findViewById(R.id.product_detail_text_view_on_child_four);
        txt_product_description = (TextView) findViewById(R.id.product_description);
        txt_product_description_title = (TextView) findViewById(R.id.product_description_title);
        pincodecheck = (Button) findViewById(R.id.btncheckpin);
        pincodecheck.setVisibility(View.GONE);
        quantitySpinner = (Spinner) findViewById(R.id.product_detail_quantity_valueT);
        pincode = (EditText) findViewById(R.id.etcheckpin);
        pincode.setVisibility(View.GONE);
        txt_product_details_no_of_reviews = (TextView) findViewById(R.id.product_detail_no_of_rating_title);
        txt_product_detail_details = (TextView) findViewById(R.id.product_detail_details);
        txt_product_write_rating = (TextView) findViewById(R.id.product_detail_write_rating);
        pincoderesponse = (TextView) findViewById(R.id.pincoderesponse);
        option_holder = (RecyclerView) findViewById(R.id.product_detail_first_row);
        rating_bar_product = (RatingBar) findViewById(R.id.product_detail_rating_bar);

        img_product_child_one = (ImageView) findViewById(R.id.product_detail_image_view_child1);
        img_product_child_two = (ImageView) findViewById(R.id.product_detail_image_view_child2);
        img_product_child_three = (ImageView) findViewById(R.id.product_detail_image_view_child3);
        img_product_child_four = (ImageView) findViewById(R.id.product_detail_image_view_child4);

        mImageLinearLayout = (LinearLayout) findViewById(R.id.product_detail_child_image_Linear_holder);
        mReviewHolder = (LinearLayout) findViewById(R.id.rating_holder);

        product_id = getIntent().getExtras().getString(JSON_Names.KEY_PRODUCT_STRING);

        if (NetworkConnection.connectionChecking(getApplicationContext())) {
            if (NetworkConnection.connectionType(getApplicationContext())) {
                if (product_id != null) {
                    String url[] = {URL_Class.mURL + URL_Class.mProductDetail +
                            Methods.current_language() + URL_Class.mProduct_id + product_id};
                    new API_Get().get_method(url, api_result, "", JSON_Names.KEY_GET_TYPE, true,
                            getBaseContext(), "ProductDetail");
                }
            } else {
                Intent intent = new Intent(Product_Details.this, Network_Error.class);
                startActivity(intent);
                finish();
            }
        } else {
            Intent intent = new Intent(Product_Details.this, NoInternetConnection.class);
            startActivity(intent);
            finish();
        }
    }

    public void option_reset(HashMap<Integer, ArrayList<ProductOptionDataSet>> mOptionListChild) {
        if (mOptionListChild != null) {
            for (int i = 0; i < mOptionListChild.size(); i++) {
                for (int j = 0; j < mOptionListChild.get(i).size(); j++) {
                    mOptionListChild.get(i).get(j).setSelected(false);
                }
            }
        }
    }

    public void clearData(ArrayList<ProductOptionDataSet> mOptionList) {
        if (mOptionList != null) {
            for (int i = 0; i < mOptionList.size(); i++) {
                DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_SECOND_ROW_OPTION_ID
                        + mOptionList.get(i).getProduct_option_id());
                DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_SECOND_ROW_PRODUCT_OPTION_VALUE_ID
                        + mOptionList.get(i).getProduct_option_id());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        update(menu);

        if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
            menu.findItem(R.id.user_name).setVisible(false);
            menu.findItem(R.id.my_order).setVisible(false);
            menu.findItem(R.id.logout).setVisible(false);
        } else {

            String title = DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_name();
            menu.findItem(R.id.user_name).setTitle(title);
            menu.findItem(R.id.register).setVisible(false);
            menu.findItem(R.id.login).setVisible(false);
        }
        return true;
    }

    public void update(Menu menu) {
        View view = menu.findItem(R.id.cart_count).getActionView();
        ImageView t1 = (ImageView) view.findViewById(R.id.cart_image_view);
        TextView t2 = (TextView) view.findViewById(R.id.cart_count_value);
        String tempData = DataBaseHandlerCart.getInstance(getApplicationContext()).get_whole_list_count();
        if (!tempData.equals("0")) {
            t2.setText(tempData);
            t1.setOnClickListener(this);
            t2.setOnClickListener(this);
        } else {
            t2.setVisibility(View.GONE);
        }
    }

    public void wish_list_updater() {
        if (product_id != null)
            if (DataBaseHandlerWishList.getInstance(getApplicationContext()).checking_wish_list(product_id)) {
                img_btn_add_wish_list.setImageResource(R.drawable.ic_favorite_orange_500_24dp);
            } else {
                img_btn_add_wish_list.setImageResource(R.drawable.ic_favorite_border_grey_500_24dp);
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        update(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_PRODUCT_STRING);
            DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_PRODUCT_ID);
            if (mOptionList != null) {
                clearData(mOptionList);
            }
            onBackPressed();
            finish();
        } else if (id == R.id.search) {
            if (mOptionList != null) {
                clearData(mOptionList);
            }
            Intent intent = new Intent(Product_Details.this, Search.class);
            startActivity(intent);
        } else if (id == R.id.login) {
            if (mOptionList != null) {
                clearData(mOptionList);
            }
            Intent intent = new Intent(Product_Details.this, Login.class);
            startActivity(intent);
        } else if (id == R.id.register) {
            if (mOptionList != null) {
                clearData(mOptionList);
            }
            Intent intent = new Intent(Product_Details.this, SignUp.class);
            startActivity(intent);
        }
       else if (id == R.id.invite) {


            Intent sharingintent = new Intent(Intent.ACTION_SEND);
            sharingintent.setType("text/plain");
            sharingintent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share The App");
            sharingintent.putExtra(android.content.Intent.EXTRA_TEXT, "  ");
            startActivity(Intent.createChooser(sharingintent, "Share Via"));
        }else if (id == R.id.logout) {
            if (mOptionList != null) {
                clearData(mOptionList);
            }
            DataStorage.mRemoveSharedPreferenceString(getApplicationContext(),"profile_pic");
            DataBaseHandlerAccount.getInstance(getApplicationContext()).delete_account_detail();
            DataBaseHandlerWishList.getInstance(getApplicationContext()).delete_wish_list();
            DataBaseHandlerCart.getInstance(getApplicationContext()).delete_cart();
            DataBaseHandlerCartOptions.getInstance(getApplicationContext()).delete_cart_option();
            Intent intent = new Intent(Product_Details.this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.customer_care) {
            Intent intent = new Intent(getApplicationContext(), Customer_Care.class);
            startActivity(intent);
        }
        else if (id == R.id.offer) {
            Intent intent = new Intent(getApplicationContext(), Offer.class);
            startActivity(intent);
        }
        else if (id == R.id.menu_wish_list) {
            if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                Methods.toast(getResources().getString(R.string.must_login));
                Intent intent = new Intent(Product_Details.this, Login.class);
                startActivity(intent);
            } else {
                if (mOptionList != null) {
                    clearData(mOptionList);
                }
                Intent intent = new Intent(Product_Details.this, Wish_List.class);
                startActivity(intent);
            }
        } else if (id == R.id.user_name) {
            Intent intent = new Intent(Product_Details.this, MyAccountMainMenu.class);
            startActivity(intent);
        } else if (id == R.id.my_order) {
            if (mOptionList != null) {
                clearData(mOptionList);
            }
            Intent intent = new Intent(Product_Details.this, OrderHistory.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.product_detail_text_view_on_child_four) {
            intentTransfer("image");
        } else if (id == R.id.product_detail_add_to_fav) {
            if (NetworkConnection.connectionChecking(getApplicationContext())) {
                if (mProductDataSet != null) {
                    if (DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                        if (!DataBaseHandlerWishList.getInstance(getApplicationContext()).checking_wish_list(mProductDataSet.getProduct_id())) {
                            current_product_id();
                            DataBaseHandlerWishList.getInstance(getApplicationContext()).add_to_wish_list(mProductDataSet.getProduct_id(), mProductDataSet.getProduct_string());
                            if (get_wish_list_post_data() != null) {
                                String url[] = {URL_Class.mURL + URL_Class.mURL_Add_To_WishList};
                                new API_Get().get_method(url, api_result, get_wish_list_post_data(), JSON_Names.KEY_POST_TYPE, true,
                                        getBaseContext(), "ProductDetailWishListAdd");
                            }
                            img_btn_add_wish_list.setImageResource(R.drawable.ic_favorite_orange_500_24dp);
                            Methods.toast(getResources().getString(R.string.wish_list_success));
                        } else {
                            DataBaseHandlerWishList.getInstance(getApplicationContext()).remove_from_wish_list(mProductDataSet.getProduct_id());
                            current_product_id();
                            if (get_wish_list_post_data() != null) {
                                String url[] = {URL_Class.mURL + URL_Class.mURL_Remove_WishList};
                                new API_Get().get_method(url, api_result, get_wish_list_post_data(), JSON_Names.KEY_POST_TYPE, true,
                                        getBaseContext(), "ProductDetailWishListRemove");
                            }
                            img_btn_add_wish_list.setImageResource(R.drawable.ic_favorite_border_grey_500_24dp);
                            Methods.toast(getResources().getString(R.string.wish_list_removed));
                        }
                    } else {
                        Intent intent = new Intent(Product_Details.this, Login.class);
                        current_product_id();
                        DataStorage.mStoreSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN, JSON_Names.KEY_CURRENT_LOGIN_PRODUCT_DETAIL);
                        startActivity(intent);

                    }
                }
            } else {
                Intent intent = new Intent(Product_Details.this, NoInternetConnection.class);
                startActivity(intent);
                finish();
            }

        } else if (id == R.id.product_detail_details) {
            if (mOptionList != null) {
                clearData(mOptionList);
            }
            Intent intent = new Intent(Product_Details.this, Product_Detail_And_Description.class);
            if (mProduct_String != null)
                intent.putExtra(JSON_Names.KEY_PRODUCT_STRING, mProduct_String);
            startActivity(intent);
        } else if (id == R.id.cart_count_value) {
            if (mOptionList != null) {
                clearData(mOptionList);
            }
            Intent intent = new Intent(Product_Details.this, Cart.class);
            startActivity(intent);
        } else if (id == R.id.product_detail_write_rating) {
            if (DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    Product_Review_Post product_review_post = Product_Review_Post.getInstant(mProductDataSet.getProduct_id());
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.product_detail_review_post_holder, product_review_post, "Review");
                    fragmentTransaction.addToBackStack("Review");
                    fragmentTransaction.commit();
                }
            } else {
                Methods.toast(getResources().getString(R.string.must_login));
                Intent intent = new Intent(Product_Details.this, Login.class);
                startActivity(intent);
            }
        } else if (id == R.id.product_detail_image_view_child1) {
            if (mProductDataSet != null)
                image_view(img_product_parent_image, mProductDataSet.getImage());
        } else if (id == R.id.product_detail_image_view_child2) {
            if (mProductDataSet != null)
                image_view(img_product_parent_image, mImageList.get(0).getChildImage());
        } else if (id == R.id.product_detail_image_view_child3) {
            if (mProductDataSet != null)
                image_view(img_product_parent_image, mImageList.get(1).getChildImage());
        } else if (id == R.id.product_detail_image_view_child4) {
            if (mProductDataSet != null)
                image_view(img_product_parent_image, mImageList.get(2).getChildImage());
        } else if (id == R.id.product_detail_image_view_parent) {
            intentTransfer("image");
        } else if (id == R.id.cart_image_view) {
            if (mOptionList != null) {
                clearData(mOptionList);
            }
            Intent intent = new Intent(Product_Details.this, Cart.class);
            startActivity(intent);
        } else if (id == R.id.product_detail_no_of_rating_title) {
            intentTransfer("Review");
        }
    }

    public void current_product_id() {
        DataStorage.mStoreSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_PRODUCT_ID, mProductDataSet.getProduct_id());
    }

    public void image_view(ImageView imageView, String url) {
        Methods.glide_image_loader_fixed_size(url, imageView);
    }

    public void intentTransfer(String from) {
        if (mOptionList != null) {
            clearData(mOptionList);
        }
        Intent intent = new Intent(Product_Details.this, ImageFullView.class);
        intent.putExtra(JSON_Names.KEY_FROM, from);
        if (mProduct_String != null)
            intent.putExtra(JSON_Names.KEY_PRODUCT_STRING, mProduct_String);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_PRODUCT_STRING);
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        wish_list_updater();
        invalidateOptionsMenu();
        super.onResume();
    }

    @SuppressWarnings("unchecked")
    public void setting(String data) {
        if (data != null) {
            mDataSet = GetJSONData.getSeparateProductDetail(data);
        }
        if (mDataSet != null) {
            mProductDataSet = (ProductDataSet) mDataSet.get(JSON_Names.KEY_PD_PRODUCT);
            mImageList = (ArrayList<ProductImageDataSet>) mDataSet.get(JSON_Names.KEY_PD_IMAGE);
            mOptionList = (ArrayList<ProductOptionDataSet>) mDataSet.get(JSON_Names.KEY_PD_OPTION);
            mOptionListChild = (HashMap<Integer, ArrayList<ProductOptionDataSet>>) mDataSet.get(JSON_Names.KEY_PD_OPTION_CHILD);
        }
        //Any process or calculation should start after this method call
        if (mOptionList != null) {
            clearData(mOptionList);
        }

        if (mProductDataSet != null) {
            mProduct_String = mProductDataSet.getProduct_string();
            //DataStorage.mStoreSharedPreferenceString(this, JSON_Names.KEY_PRODUCT_STRING, mProductDataSet.getProduct_string());

            current_product_id();
            wish_list_updater();
            Integer minimum = Integer.valueOf(mProductDataSet.getMinimum());
            final String value[];

            if (Integer.valueOf(mProductDataSet.getQuantity()) > 30) {
                value = new String[30];
            } else {
                if (minimum == 1) {
                    value = new String[Integer.valueOf(mProductDataSet.getQuantity())];
                } else {
                    value = new String[(Integer.valueOf(mProductDataSet.getQuantity()) - minimum) + 1];
                }
            }

            for (int i = 0; i < value.length; i++) {
                if (i == 0) {
                    value[i] = "" + (minimum);
                } else {
                    value[i] = "" + (minimum + i);
                }
            }

            QuantityAdapter dataAdapter = new QuantityAdapter(getApplicationContext(), value);
            quantitySpinner.setAdapter(dataAdapter);

            quantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    quantity = value[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });



            img_btn_add_wish_list.setOnClickListener(this);
            image_view(img_product_parent_image, mProductDataSet.getImage());
            txt_product_title.setText(mProductDataSet.getTitle());

            if (mProductDataSet.getDescription() != null) {
                if (!mProductDataSet.getDescription().isEmpty()) {
                    String txtDescription = "     " + Html.fromHtml("<html><body>" + "<p align=\"justify\">" + mProductDataSet.getDescription() + "</p> " + "</body></html>");
                    txt_product_description.setText(txtDescription);
                } else {
                    txt_product_description_title.setVisibility(View.GONE);
                    txt_product_description.setVisibility(View.GONE);
                }
            } else {
                txt_product_description_title.setVisibility(View.GONE);
                txt_product_description.setVisibility(View.GONE);
            }

            if (mProductDataSet.getSpecial_price() != null) {
                if (!mProductDataSet.getSpecial_price().isEmpty()) {
                    txt_product_price.setText(mProductDataSet.getPrice());
                    txt_product_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    txt_product_special_price.setText(mProductDataSet.getSpecial_price());
                } else {
                    txt_product_special_price.setText(mProductDataSet.getPrice());
                    txt_product_price.setVisibility(View.GONE);
                }
            } else {
                txt_product_special_price.setText(mProductDataSet.getPrice());
                txt_product_price.setVisibility(View.GONE);
            }
        }
        if (mImageList != null) {
            if (mImageList.size() != 0) {

                if (mImageList.size() > 3) {
                    image_view(img_product_child_one, mProductDataSet.getImage());
                    image_view(img_product_child_two, mImageList.get(0).getChildImage());
                    image_view(img_product_child_three, mImageList.get(1).getChildImage());
                    image_view(img_product_child_four, mImageList.get(2).getChildImage());
                    String sample = "+" + (mImageList.size() - 3);
                    txt_product_child_image_count.setText(sample);
                } else if (mImageList.size() == 3) {
                    image_view(img_product_child_one, mProductDataSet.getImage());
                    image_view(img_product_child_two, mImageList.get(0).getChildImage());
                    image_view(img_product_child_three, mImageList.get(1).getChildImage());
                    image_view(img_product_child_four, mImageList.get(2).getChildImage());
                    txt_product_child_image_count.setVisibility(View.GONE);
                } else if (mImageList.size() == 2) {
                    image_view(img_product_child_one, mProductDataSet.getImage());
                    image_view(img_product_child_two, mImageList.get(0).getChildImage());
                    image_view(img_product_child_three, mImageList.get(1).getChildImage());
                    img_product_child_four.setVisibility(View.GONE);
                    txt_product_child_image_count.setVisibility(View.GONE);
                } else if (mImageList.size() == 1) {
                    image_view(img_product_child_one, mProductDataSet.getImage());
                    image_view(img_product_child_two, mImageList.get(0).getChildImage());
                    img_product_child_three.setVisibility(View.GONE);
                    img_product_child_four.setVisibility(View.GONE);
                    txt_product_child_image_count.setVisibility(View.GONE);
                }
            } else {
                mImageLinearLayout.setVisibility(View.GONE);
            }
        } else {
            mImageLinearLayout.setVisibility(View.GONE);
        }

        txt_product_detail_details.setOnClickListener(this);
        txt_product_child_image_count.setOnClickListener(this);
        img_product_parent_image.setOnClickListener(this);
        img_product_child_one.setOnClickListener(this);
        img_product_child_two.setOnClickListener(this);
        img_product_child_three.setOnClickListener(this);
        img_product_child_four.setOnClickListener(this);
        txt_product_write_rating.setOnClickListener(this);

        if (mProductDataSet != null) {
            if (mProductDataSet.getRating() != null && !mProductDataSet.getRating().isEmpty()) {//Null pointer exception possible
                rating_bar_product.setRating(Float.valueOf(mProductDataSet.getRating()));
            } else {
                rating_bar_product.setVisibility(View.GONE);
            }
            if (mProductDataSet.getRating() != null) {
                if (!mProductDataSet.getRating().isEmpty()) {
                    rating_bar_product.setRating(Float.valueOf(mProductDataSet.getRating()));
                    if (mProductDataSet.getNo_of_review() != null) {
                        if (!mProductDataSet.getNo_of_review().isEmpty()) {
                            String review = "( " + mProductDataSet.getNo_of_review() + " " + getResources().getString(R.string.reviews) + " )";
                            txt_product_details_no_of_reviews.setText(review);
                            txt_product_details_no_of_reviews.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                            txt_product_details_no_of_reviews.setOnClickListener(this);
                        }
                    } else {
                        mReviewHolder.setVisibility(View.GONE);
                    }
                } else {
                    mReviewHolder.setVisibility(View.GONE);
                }
            } else {
                mReviewHolder.setVisibility(View.GONE);
            }
            productOptionAdapter = new ProductOptionAdapter(getApplicationContext(), mOptionList, mOptionListChild, mProductDataSet.getProduct_id());
            option_holder.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, JSON_Names.KEY_FALSE_BOOLEAN));
            option_holder.setAdapter(productOptionAdapter);
            if (mOptionList != null) {
                if (mOptionList.size() > 0) {
                    int size = (int) getResources().getDisplayMetrics().density;
                    if (size >= 3) {
                        if (mOptionList.size() == 1) {
                            int dp = (int) (getResources().getDimension(R.dimen._92sdp) / size);
                            option_holder.getLayoutParams().height = (dp * (mOptionList.size() + 4));
                        } else {
                            int dp = (int) (getResources().getDimension(R.dimen._100sdp) / size);
                            option_holder.getLayoutParams().height = (dp * (mOptionList.size() + 6));
                        }
                    } else if (size == 2) {
                        int dp = (int) (getResources().getDimension(R.dimen._95sdp) / size);
                        option_holder.getLayoutParams().height = (dp * (mOptionList.size() + 2));
                    } else {
                        int dp = (int) (getResources().getDimension(R.dimen._95sdp) / size);
                        option_holder.getLayoutParams().height = (dp * (mOptionList.size()));
                    }
                }
            }
        }
        pincodecheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pincode_name = pincode.getText().toString().trim();

                if (!pincode_name.isEmpty()) {
                    // login user
                    checkLogin(pincode_name);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter Pin Code!", Toast.LENGTH_LONG)
                            .show();
                }
            }

            private void checkLogin(final String pincode_name) {
                // Tag used to cancel the request
                String tag_string_req = "req_login";

//        pDialog.setMessage("Logging in ...");
                // showDialog();

                StringRequest strReq = new StringRequest(Request.Method.GET,
                        URL_Class.mURL_pincheck, new com.android.volley.Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Login Response: ", response);
                        //hideDialog();

                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");
                            // Check for error node in json
                            if (!error) {
                                // Create login session

                                String pincoderesponse2 = jObj.getString("message");



                                    pincoderesponse.setText(pincoderesponse2.toString());



                            } else {
                                // Error in login. Get the error message
                                String errorMsg = jObj.getString("Coupon code not found");
                                Toast.makeText(getApplicationContext(),
                                        errorMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();

                            Toast.makeText(getApplicationContext(), "Connect to internet first!!", Toast.LENGTH_LONG).show();
                            //e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Login Error: ", error.getMessage());
                /*Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();*/
                        //hideDialog();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // Posting parameters to login url
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("pincode", pincode_name);

                        return params;
                    }
                };
                // Adding request to request queue
                Application_Context.getInstance().addToRequestQueue(strReq, tag_string_req);
            }


        });
        btn_product_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dummy = "";
                int i = 0, j = 0;
                if (mOptionList != null) {
                    while (i < mOptionList.size()) {
                        //if (mOptionList.get(i).getProduct_option_required().equals(JSON_Names.KEY_REQUIRED_VALUE)) {
                        String temp1 = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_SECOND_ROW_OPTION_ID + mOptionList.get(i).getProduct_option_id());
                        String temp2 = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_SECOND_ROW_PRODUCT_OPTION_VALUE_ID + mOptionList.get(i).getProduct_option_id());
                        if (temp1.equals(JSON_Names.KEY_NO_DATA) && temp2.equals(JSON_Names.KEY_NO_DATA)) {
                            if (j == 0) {
                                dummy += mOptionList.get(i).getProduct_option_name();
                                j++;
                            } else {
                                dummy += " " + getResources().getString(R.string.and) + " " + mOptionList.get(i).getProduct_option_name();
                            }
                        }
                        i++;
                        //}
                    }
                }
                if (dummy.length() != 0) {
                    Methods.toast(getResources().getString(R.string.please_select) + " " + dummy);
                } else {
                    Integer minimum_value = Integer.valueOf(quantity);
                    //Check with option quantity for add to cart
                    int minimum_option_value = get_minimum_option_size();
                    if (minimum_value <= minimum_option_value || minimum_option_value == -1) {
                        if (mProductDataSet != null) {
                            if (minimum_value < Integer.valueOf(mProductDataSet.getMinimum())) {
                                String minimum = getResources().getString(R.string.quantity_check_1) + " " + mProductDataSet.getTitle() + getResources().getString(R.string.quantity_check_2) + mProductDataSet.getMinimum();
                                Methods.toast(minimum);
                            } else {
                                if (DataBaseHandlerCart.getInstance(getApplicationContext()).get_Size_cart() == 0) {
                                    Methods.toast(getResources().getString(R.string.add_to_cart_success));
                                    DataBaseHandlerCart.getInstance(getApplicationContext()).add_to_cart(1, mProductDataSet.getProduct_id(), minimum_value, mProduct_String, minimum_option_value);
                                    if (mOptionList != null) {
                                        i = 0;
                                        while (i < mOptionList.size()) {
                                            //if (mOptionList.get(i).getProduct_option_required().equals(JSON_Names.KEY_REQUIRED_VALUE)) {
                                            String temp1 = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),
                                                    JSON_Names.KEY_SECOND_ROW_OPTION_ID + mOptionList.get(i).getProduct_option_id());
                                            String temp2 = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),
                                                    JSON_Names.KEY_SECOND_ROW_PRODUCT_OPTION_VALUE_ID + mOptionList.get(i).getProduct_option_id());
                                            DataBaseHandlerCartOptions.getInstance(getApplicationContext()).add_options(1, temp1, temp2,
                                                    mProductDataSet.getProduct_id());
                                            i++;
                                            //}
                                        }
                                    }
                                } else {
                                    if (DataBaseHandlerCart.getInstance(getApplicationContext()).checking_cart(mProductDataSet.getProduct_id())) {
                                        ArrayList<Integer> list = DataBaseHandlerCart.getInstance(getApplicationContext()).get_index(mProductDataSet.getProduct_id());
                                        int count = DataBaseHandlerCart.getInstance(getApplicationContext()).getProductCount(list);
                                        if (count <= Integer.valueOf(mProductDataSet.getQuantity())) {
                                            if (list != null) {
                                                int check = DataBaseHandlerCart.getInstance(getApplicationContext()).getLastIndex();
                                                if (check != 0) {
                                                    int index = check + 1;
                                                    ArrayList<Integer[]> list1 = new ArrayList<>();
                                                    if (mOptionList != null) {
                                                        i = 0;
                                                        while (i < mOptionList.size()) {
                                                            //if (mOptionList.get(i).getProduct_option_required().equals(JSON_Names.KEY_REQUIRED_VALUE)) {
                                                            String temp1 = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_SECOND_ROW_OPTION_ID + mOptionList.get(i).getProduct_option_id());
                                                            String temp2 = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_SECOND_ROW_PRODUCT_OPTION_VALUE_ID + mOptionList.get(i).getProduct_option_id());
                                                            Integer value[] = {Integer.valueOf(temp1), Integer.valueOf(temp2)};
                                                            list1.add(value);
                                                            i++;
                                                            //}
                                                        }
                                                    }
                                                    int reference = 0;
                                                    for (int k = 0; k < list.size(); k++) {
                                                        if (DataBaseHandlerCartOptions.getInstance(getApplicationContext()).check_index(list.get(k), list1)) {
                                                            reference = list.get(k);
                                                        }
                                                    }
                                                    if (reference != 0) {
                                                        if (minimum_value + DataBaseHandlerCart.getInstance(getApplicationContext())
                                                                .get_product_count(reference) <= minimum_option_value) {
                                                            DataBaseHandlerCart.getInstance(getApplicationContext()).update_product_count(reference, minimum_value);
                                                            Methods.toast(getResources().getString(R.string.add_to_cart_success));
                                                        } else {
                                                            Methods.toast(getResources().getString(R.string.quantity_error_message));
                                                        }
                                                    } else {
                                                        DataBaseHandlerCart.getInstance(getApplicationContext()).add_to_cart(index, mProductDataSet.getProduct_id(), minimum_value, mProduct_String, minimum_option_value);
                                                        Methods.toast(getResources().getString(R.string.add_to_cart_success));
                                                        if (mOptionList != null) {
                                                            i = 0;
                                                            while (i < mOptionList.size()) {
                                                                //if (mOptionList.get(i).getProduct_option_required().equals(JSON_Names.KEY_REQUIRED_VALUE)) {
                                                                String temp1 = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_SECOND_ROW_OPTION_ID + mOptionList.get(i).getProduct_option_id());
                                                                String temp2 = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_SECOND_ROW_PRODUCT_OPTION_VALUE_ID + mOptionList.get(i).getProduct_option_id());
                                                                DataBaseHandlerCartOptions.getInstance(getApplicationContext()).add_options(index, temp1, temp2, mProductDataSet.getProduct_id());
                                                                i++;
                                                                // }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            Methods.toast(getResources().getString(R.string.quantity_error_message));
                                        }

                                    } else {
                                        int check = DataBaseHandlerCart.getInstance(getApplicationContext()).getLastIndex();
                                        if (check != 0) {
                                            int index = check + 1;
                                            DataBaseHandlerCart.getInstance(getApplicationContext()).add_to_cart(index, mProductDataSet.getProduct_id(), minimum_value, mProduct_String, minimum_option_value);
                                            Methods.toast(getResources().getString(R.string.add_to_cart_success));
                                            if (mOptionList != null) {
                                                i = 0;
                                                while (i < mOptionList.size()) {
                                                    //if (mOptionList.get(i).getProduct_option_required().equals(JSON_Names.KEY_REQUIRED_VALUE)) {
                                                    String temp1 = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_SECOND_ROW_OPTION_ID + mOptionList.get(i).getProduct_option_id());
                                                    String temp2 = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_SECOND_ROW_PRODUCT_OPTION_VALUE_ID + mOptionList.get(i).getProduct_option_id());
                                                    DataBaseHandlerCartOptions.getInstance(getApplicationContext()).add_options(index, temp1, temp2, mProductDataSet.getProduct_id());
                                                    i++;
                                                    //}
                                                }
                                            }
                                        }
                                    }
                                }
                                option_reset(mOptionListChild);
                                if (mOptionList != null) {
                                    clearData(mOptionList);
                                }
                                productOptionAdapter.notifyDataSetChanged();
                                invalidateOptionsMenu();
                            }
                        } else {
                            Methods.toast(getResources().getString(R.string.quantity_error_message));
                        }
                    } else {
                        String minimum = getResources().getString(R.string.quantity_check_1) + " "
                                + mProductDataSet.getTitle() + getResources().getString(R.string.quantity_check_2) + " " + minimum_option_value;
                        Methods.toast(minimum);
                    }
                }
            }
        });


    }


    @Override
    public void result(String[] data, String source) {
        if (data != null) {
            if (data[0] != null) {
                switch (source) {
                    case "ProductDetail":
                        setting(data[0]);
                        break;
                    case "ProductDetailWishListAdd":
                    case "ProductDetailWishListRemove":
                        wish_list_setting(data[0]);
                        break;
                    case "ProductReviewPost":
                        Response response = GetJSONData.getResponse(data[0]);
                        if (response != null) {
                            if (response.getmStatus() == 200) {
                                toast_call(getResources().getString(R.string.success));
                                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            } else {
                                toast_call(getResources().getString(R.string.failure));
                                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            }
                        } else {
                            toast_call(getResources().getString(R.string.failure));
                            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }
                        break;
                }
            }
        }

    }

    public void toast_call(String id) {
        Methods.toast(id);
    }

    public String get_wish_list_post_data() {

        String mProduct_id;
        String mCustomer_id;
        try {
            mProduct_id = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_PRODUCT_ID);
            mCustomer_id = String.valueOf(DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_id());
            return URLEncoder.encode(JSON_Names.KEY_PRODUCT_ID, URL_Class.mConvertType) +
                    URL_Class.mEqual_Symbol +
                    URLEncoder.encode(mProduct_id, URL_Class.mConvertType) +
                    URL_Class.mAnd_Symbol +
                    URLEncoder.encode(JSON_Names.KEY_USER_ID, URL_Class.mConvertType) +
                    URL_Class.mEqual_Symbol +
                    URLEncoder.encode(mCustomer_id, URL_Class.mConvertType);
        } catch (Exception e) {
            return null;
        }
    }


    public void wish_list_setting(String data) {
        if (data != null) {
            Response response = GetJSONData.getResponse(data);
            if (response != null) {
                if (response.getmStatus() == 200) {
                    DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_PRODUCT_ID);
                } else {
                    Methods.toast(response.getmMessage());
                }
            }
        } else {
            Methods.toast(getResources().getString(R.string.error));
        }
    }

    @Override
    public void review_post(String data) {
        String url[] = {URL_Class.mURL + URL_Class.mURL_Review_Post};
        new API_Get().get_method(url, api_result, data, JSON_Names.KEY_POST_TYPE, true,
                getBaseContext(), "ProductReviewPost");
    }

    /*Get Minimum option quantity for add to cart*/

    public int get_minimum_option_size() {
        int list[] = new int[mOptionList.size()];
        if (mOptionList != null && mOptionList.size() > 0) {
            for (int i = 0; i < mOptionList.size(); i++) {
                //if (mOptionList.get(i).getProduct_option_required().equals(JSON_Names.KEY_REQUIRED_VALUE)) {
                String option_id = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),
                        JSON_Names.KEY_SECOND_ROW_PRODUCT_OPTION_VALUE_ID + mOptionList.get(i).getProduct_option_id());

                for (int j = 0; j < mOptionListChild.get(i).size(); j++) {
                    if (option_id.equals(mOptionListChild.get(i).get(j).getProduct_option_value_id())) {
                        list[i] = Integer.valueOf(mOptionListChild.get(i).get(j).getProduct_option_quantity());
                    }
                }

                // }
            }
            if (list.length != 0) {
                Arrays.sort(list);
            }
            return list.length > 0 ? list[0] : -1;
        } else {
            return Integer.valueOf(mProductDataSet.getQuantity());
        }
    }

}