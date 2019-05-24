package com.restaurent.activity.account;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.restaurent.R;
import com.restaurent.activity.Cart;
import com.restaurent.activity.Home;
import com.restaurent.activity.NoInternetConnection;
import com.restaurent.activity.Search;
import com.restaurent.activity.Wish_List;
import com.restaurent.activity.user.Availability_of_products;
import com.restaurent.activity.user.CompanyProfile;
import com.restaurent.activity.user.Company_Aims;
import com.restaurent.activity.user.Customer_Care;
import com.restaurent.activity.user.Login;

import com.restaurent.activity.user.Offer;
import com.restaurent.activity.user.SignUp;
import com.restaurent.api_call.API_Get;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.constant_class.URL_Class;
import com.restaurent.db_handler.DataBaseHandlerAccount;
import com.restaurent.db_handler.DataBaseHandlerCart;
import com.restaurent.db_handler.DataBaseHandlerCartOptions;
import com.restaurent.db_handler.DataBaseHandlerWishList;
import com.restaurent.interfaces.API_Result;
import com.restaurent.json_mechanism.GetJSONData;
import com.restaurent.mechanism.Methods;
import com.restaurent.network_checker.NetworkConnection;
import com.restaurent.shared_preferenc_estring.DataStorage;
import com.restaurent.utils.Response;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChangePwd extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener,API_Result {

    android.support.v7.widget.Toolbar toolbar;
    EditText edt_c_pwd_password, edt_c_pwd_c_password;
    Button btn_c_pwd_back, btn_c_pwd_continue;
    TextView error_c_pwd_password, error_c_pwd_c_password1, error_c_pwd_c_password2;
    API_Result api_result;
    CircleImageView profilePictureView;
    Bitmap fb_img;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pwd);

        api_result=this;
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.actionbar);
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

        edt_c_pwd_password = (EditText) findViewById(R.id.et_change_password);
        edt_c_pwd_c_password = (EditText) findViewById(R.id.et_change_confirm_password);

        btn_c_pwd_back = (Button) findViewById(R.id.btn_change_pwd_back);
        btn_c_pwd_continue = (Button) findViewById(R.id.btn_change_pwd_continue);

        error_c_pwd_password = (TextView) findViewById(R.id.tv_cpwd_error_pwd);
        error_c_pwd_c_password1 = (TextView) findViewById(R.id.tv_cpwd_error_confirm_pwd1);
        error_c_pwd_c_password2 = (TextView) findViewById(R.id.tv_cpwd_error_confirm_pwd2);

        btn_c_pwd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        btn_c_pwd_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!edt_c_pwd_password.getText().toString().isEmpty())
                        && (!edt_c_pwd_c_password.getText().toString().isEmpty())
                        && (edt_c_pwd_password.getText().toString().equals(edt_c_pwd_c_password.getText().toString()))
                        && (edt_c_pwd_password.getText().toString().length() >= 5) && (edt_c_pwd_password.getText().toString().length() <= 20)
                        && (edt_c_pwd_c_password.getText().toString().length() >= 5) && (edt_c_pwd_c_password.getText().toString().length() <= 20)
                        ) {
                    if (NetworkConnection.connectionChecking(getApplicationContext())) {
                        if(writeOut()!=null) {
                            String[] url = {URL_Class.mURL + URL_Class.mURL_Change_Password};
                            new API_Get().get_method(url, api_result, writeOut(), JSON_Names.KEY_POST_TYPE,
                                    true, getBaseContext(),"ChangePassword");
                        }
                    } else {
                        Intent intent = new Intent(ChangePwd.this, NoInternetConnection.class);
                        startActivity(intent);
                        finish();
                    }
                }
                if (edt_c_pwd_password.getText().toString().equals("")) {
                    error_c_pwd_password.setVisibility(View.VISIBLE);
                } else {
                    error_c_pwd_password.setVisibility(View.GONE);
                }
                if ((edt_c_pwd_password.getText().toString().length() >= 5) && (edt_c_pwd_password.getText().toString().length() <= 20)) {
                    error_c_pwd_password.setVisibility(View.GONE);
                } else {
                    error_c_pwd_password.setVisibility(View.VISIBLE);
                }
                if (edt_c_pwd_c_password.getText().toString().isEmpty()) {
                    error_c_pwd_c_password1.setVisibility(View.VISIBLE);
                } else {
                    error_c_pwd_c_password1.setVisibility(View.GONE);
                }
                if ((edt_c_pwd_c_password.getText().toString().length() >= 5) && (edt_c_pwd_c_password.getText().toString().length() <= 20)) {
                    error_c_pwd_c_password1.setVisibility(View.GONE);
                } else {
                    error_c_pwd_c_password1.setVisibility(View.VISIBLE);
                }
                if (!edt_c_pwd_password.getText().toString().equals(edt_c_pwd_c_password.getText().toString()) && !edt_c_pwd_c_password.getText().toString().equals("") && !edt_c_pwd_password.getText().toString().equals("")) {
                    error_c_pwd_c_password2.setVisibility(View.VISIBLE);
                } else {
                    error_c_pwd_c_password2.setVisibility(View.GONE);
                }
                if (!edt_c_pwd_password.getText().toString().equals(edt_c_pwd_c_password.getText().toString())
                        && (edt_c_pwd_password.getText().toString().length() >= 5) && (edt_c_pwd_password.getText().toString().length() <= 20)
                        && (edt_c_pwd_c_password.getText().toString().length() >= 5) && (edt_c_pwd_c_password.getText().toString().length() <= 20)) {
                    error_c_pwd_c_password2.setVisibility(View.VISIBLE);
                } else {
                    error_c_pwd_c_password2.setVisibility(View.GONE);
                }
            }
        });
    }

    public void success() {
        onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cart_count_value) {
            Intent intent = new Intent(ChangePwd.this, Cart.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (id == R.id.cart_image_view) {
            Intent intent = new Intent(ChangePwd.this, Cart.class);
            startActivity(intent);
        }
        return false;
    }

    @Override
    protected void onResume() {
        invalidateOptionsMenu();
        super.onResume();
    }

    @Override
    public void result(String[] data, String source) {
        if(source.equals("ChangePassword")) {
            if (data != null) {
                if (data[0] != null) {
                    Response response = GetJSONData.getResponse(data[0]);
                    if (response != null) {
                        if (response.getmStatus() == 200) {
                            Methods.toast(getResources().getString( R.string.change_password_success));
                            success();
                        } else {
                            Methods.toast(response.getmMessage());
                        }
                    }
                } else {
                    Methods.toast(getResources().getString(R.string.error));
                }
            }
        }
    }

    public String writeOut()  {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put(JSON_Names.KEY_PASSWORD, edt_c_pwd_password.getText().toString());
            params.put(JSON_Names.KEY_CONFIRM, edt_c_pwd_c_password.getText().toString());
            params.put(JSON_Names.KEY_CUSTOMER_ID, String.valueOf(DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_id()));

            StringBuilder s = new StringBuilder();
            Boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first) {
                    first = false;
                } else {
                    s.append(URL_Class.mAnd_Symbol);
                }
                s.append(URLEncoder.encode(entry.getKey(), URL_Class.mConvertType));
                s.append(URL_Class.mEqual_Symbol);
                s.append(URLEncoder.encode(entry.getValue(), URL_Class.mConvertType));
            }
            return s.toString();
        }catch (Exception e){
            return null;
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
            t2.setOnClickListener(this);
            t1.setOnTouchListener(this);
        } else {
            t2.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            finish();
        }
     /*   else if (id == R.id.company_profile) {
            Intent intent = new Intent(getApplicationContext(),CompanyProfile.class);
            startActivity(intent);
        }
        else if (id == R.id.company_aims) {
            Intent intent = new Intent(getApplicationContext(), Company_Aims.class);
            startActivity(intent);
        }*/
        else if (id == R.id.login) {
            Intent intent = new Intent(ChangePwd.this, Login.class);
            startActivity(intent);
        } else if (id == R.id.register) {
            Intent intent = new Intent(ChangePwd.this, SignUp.class);
            startActivity(intent);
        } else if (id == R.id.search) {
            Intent intent = new Intent(ChangePwd.this, Search.class);
            startActivity(intent);
        } else if (id == R.id.invite) {


            Intent sharingintent = new Intent(Intent.ACTION_SEND);
            sharingintent.setType("text/plain");
            sharingintent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share The App");
            sharingintent.putExtra(android.content.Intent.EXTRA_TEXT, "Download Tasty Town Foods App From Play Store - https://play.google.com/store/apps/details?id=com.pooja_prasad ");
            startActivity(Intent.createChooser(sharingintent, "Share Via"));
        }
        else if (id == R.id.customer_care) {
            Intent intent = new Intent(getApplicationContext(), Customer_Care.class);
            startActivity(intent);
        }
        else if (id == R.id.offer) {
            Intent intent = new Intent(getApplicationContext(), Offer.class);
            startActivity(intent);
        }
      /*  else if (id == R.id.availability_of_products) {
            Intent intent = new Intent(getApplicationContext(), Availability_of_products.class);
            startActivity(intent);
        }*/
        else if (id == R.id.logout) {
            DataStorage.mRemoveSharedPreferenceString(getApplicationContext(),"profile_pic");
            DataBaseHandlerAccount.getInstance(getApplicationContext()).delete_account_detail();
            DataBaseHandlerWishList.getInstance(getApplicationContext()).delete_wish_list();
            DataBaseHandlerCart.getInstance(getApplicationContext()).delete_cart();
            DataBaseHandlerCartOptions.getInstance(getApplicationContext()).delete_cart_option();
            Intent intent = new Intent(ChangePwd.this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_wish_list) {
            if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                Methods.toast(getResources().getString(  R.string.must_login));
                Intent intent = new Intent(ChangePwd.this, Login.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(ChangePwd.this, Wish_List.class);
                startActivity(intent);
            }
        }else if(id==R.id.user_name){
            Intent intent = new Intent(ChangePwd.this, MyAccountMainMenu.class);
            startActivity(intent);
        } else if (id == R.id.my_order) {
            Intent intent = new Intent(ChangePwd.this, OrderHistory.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
