package com.restaurent.activity.account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.restaurent.utils.AccountDataSet;
import com.restaurent.utils.Response;

import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAccountInfo extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, API_Result {

    android.support.v7.widget.Toolbar toolbar;
    ProgressDialog progressDialog;
    EditText edt_myac_fname, edt_myac_lname, edt_myac_email, edt_myac_teleph/*, edt_myac_fx*/;
    Button btnmyac_continue, btnmyac_back;
    TextView error_myac_fname, error_myac_lname, error_myac_email1, error_myac_email2, error_myac_teleph/*, error_myac_fx*/;
    ArrayList<AccountDataSet> profileList = new ArrayList<>();
    AccountDataSet accountDataSet;
    API_Result api_result;
    ProgressDialog progressD;

    CircleImageView profilePictureView;
    Bitmap fb_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account_info);

        progressD = new ProgressDialog(this);
        api_result = this;

        if (NetworkConnection.connectionChecking(getApplicationContext())) {
            progressD.show();
            progressD.setCancelable(false);
            String[] url = {URL_Class.mURL + URL_Class.mURL_Get_Customer_Profile
                    + URL_Class.mCustomer_id + String.valueOf(DataBaseHandlerAccount.getInstance(getApplicationContext())
                    .get_customer_id())};
            new API_Get().get_method(url, api_result, "", JSON_Names.KEY_GET_TYPE, true, getBaseContext(), "CustomerProfile");
        } else {
            Intent intent = new Intent(MyAccountInfo.this, NoInternetConnection.class);
            startActivity(intent);
            finish();
        }

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.actionbar);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);
        accountDataSet = new AccountDataSet();

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edt_myac_fname = (EditText) findViewById(R.id.et_my_ac_first_name);
        edt_myac_lname = (EditText) findViewById(R.id.et_my_ac_last_name);
        edt_myac_email = (EditText) findViewById(R.id.et_my_ac_email);
        edt_myac_teleph = (EditText) findViewById(R.id.et_my_ac_telephone);
        //edt_myac_fx = (EditText) findViewById(R.id.et_my_ac_fax);
        edt_myac_email = (EditText) findViewById(R.id.et_my_ac_email);

        error_myac_fname = (TextView) findViewById(R.id.tv_error_edit_pro_first_name);
        error_myac_lname = (TextView) findViewById(R.id.tv_error_edit_pro_last_name);
        error_myac_email1 = (TextView) findViewById(R.id.tv_error_email1);
        error_myac_email2 = (TextView) findViewById(R.id.tv_error_email2);
        error_myac_teleph = (TextView) findViewById(R.id.tv_error_telephone);
        //error_myac_fx = (TextView) findViewById(R.id.tv_error_fax);

        btnmyac_continue = (Button) findViewById(R.id.btn_my_ac_continue);
        btnmyac_back = (Button) findViewById(R.id.btn_my_ac_back);

        btnmyac_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        btnmyac_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edt_myac_fname.getText().toString().equals("")
                        && (edt_myac_fname.getText().toString().length() >= 1)
                        && (edt_myac_fname.getText().toString().length() <= 32)

                        && !edt_myac_lname.getText().toString().equals("")
                        && (edt_myac_lname.getText().toString().length() >= 1)
                        && (edt_myac_lname.getText().toString().length() <= 32)

                        && !edt_myac_email.getText().toString().equals("")

                        && !edt_myac_teleph.getText().toString().equals("")
                        && (edt_myac_teleph.getText().toString().length() >= 3)
                        && (edt_myac_teleph.getText().toString().length() <= 32)) {


                    switch (Boolean.toString(Methods.isEmailValidator(edt_myac_email.getText().toString()))) {

                        case "true": {
                            error_myac_email2.setVisibility(View.GONE);
                            if (NetworkConnection.connectionChecking(getApplicationContext())) {
                                if (eDitAccountInfo() != null) {
                                    String[] url = {URL_Class.mURL + URL_Class.mUpdateAccountDetail};
                                    new API_Get().get_method(url, api_result, eDitAccountInfo(),
                                            JSON_Names.KEY_POST_TYPE, true, getBaseContext(), "UpdateAccount");
                                }
                            } else {
                                Intent intent = new Intent(MyAccountInfo.this, NoInternetConnection.class);
                                startActivity(intent);
                                finish();
                            }
                            break;
                        }
                        case "false": {
                            error_myac_email2.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    Methods.toast(getResources().getString(R.string.fill_required_field));
                }
                if (edt_myac_fname.getText().toString().equals("")) {
                    error_myac_fname.setVisibility(View.VISIBLE);
                } else {
                    error_myac_fname.setVisibility(View.GONE);
                }
                if ((edt_myac_fname.getText().toString().length() >= 1) && (edt_myac_fname.getText().toString().length() <= 32)) {
                    error_myac_fname.setVisibility(View.GONE);
                } else {
                    error_myac_fname.setVisibility(View.VISIBLE);
                }
                if (edt_myac_lname.getText().toString().equals("")) {
                    error_myac_lname.setVisibility(View.VISIBLE);
                } else {
                    error_myac_lname.setVisibility(View.GONE);
                }
                if ((edt_myac_lname.getText().toString().length() >= 1) && (edt_myac_lname.getText().toString().length() <= 32)) {
                    error_myac_lname.setVisibility(View.GONE);
                } else {
                    error_myac_lname.setVisibility(View.VISIBLE);
                }
                if (edt_myac_email.getText().toString().equals("")) {
                    error_myac_email1.setVisibility(View.VISIBLE);
                } else {
                    error_myac_email1.setVisibility(View.GONE);
                }
                if (edt_myac_teleph.getText().toString().equals("")) {
                    error_myac_teleph.setVisibility(View.VISIBLE);
                } else {
                    error_myac_teleph.setVisibility(View.GONE);
                }
                if ((edt_myac_teleph.getText().toString().length() >= 3) && (edt_myac_teleph.getText().toString().length() <= 32)) {
                    error_myac_teleph.setVisibility(View.GONE);
                } else {
                    error_myac_teleph.setVisibility(View.VISIBLE);
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
            Intent intent = new Intent(MyAccountInfo.this, Cart.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        invalidateOptionsMenu();
        super.onResume();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (id == R.id.cart_image_view) {
            Intent intent = new Intent(MyAccountInfo.this, Cart.class);
            startActivity(intent);
        }
        return false;
    }

    public String eDitAccountInfo() {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(JSON_Names.KEY_FIRST_NAME, edt_myac_fname.getText().toString());
            jsonObject.put(JSON_Names.KEY_LAST_NAME, edt_myac_lname.getText().toString());
            jsonObject.put(JSON_Names.KEY_EMAIL, edt_myac_email.getText().toString());
            jsonObject.put(JSON_Names.KEY_PHONE, edt_myac_teleph.getText().toString());
            jsonObject.put(JSON_Names.KEY_CUSTOMER_ID, String.valueOf(DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_id()));

            accountDataSet.setmFirstName(edt_myac_fname.getText().toString());
            accountDataSet.setmLastName(edt_myac_lname.getText().toString());
            accountDataSet.setmEmailId(edt_myac_email.getText().toString());
            accountDataSet.setmTelePhone(edt_myac_teleph.getText().toString());

            return jsonObject.toString();

        } catch (Exception e) {
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
        } else if (id == R.id.login) {
            Intent intent = new Intent(MyAccountInfo.this, Login.class);
            startActivity(intent);
        }/*else if (id == R.id.company_profile) {
            Intent intent = new Intent(getApplicationContext(),CompanyProfile.class);
            startActivity(intent);
        }
        else if (id == R.id.company_aims) {
            Intent intent = new Intent(getApplicationContext(), Company_Aims.class);
            startActivity(intent);
        }*/
        else if (id == R.id.search) {
            Intent intent = new Intent(MyAccountInfo.this, Search.class);
            startActivity(intent);
        } else if (id == R.id.register) {
            Intent intent = new Intent(MyAccountInfo.this, SignUp.class);
            startActivity(intent);
        }  else if (id == R.id.invite) {


            Intent sharingintent = new Intent(Intent.ACTION_SEND);
            sharingintent.setType("text/plain");
            sharingintent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share The App");
            sharingintent.putExtra(android.content.Intent.EXTRA_TEXT,"Download Tasty Town Foods App From Play Store - https://play.google.com/store/apps/details?id=com.pooja_prasad ");
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
       /* else if (id == R.id.availability_of_products) {
            Intent intent = new Intent(getApplicationContext(), Availability_of_products.class);
            startActivity(intent);
        }*/
        else if (id == R.id.logout) {
            DataStorage.mRemoveSharedPreferenceString(getApplicationContext(),"profile_pic");
            DataBaseHandlerAccount.getInstance(getApplicationContext()).delete_account_detail();
            DataBaseHandlerWishList.getInstance(getApplicationContext()).delete_wish_list();
            DataBaseHandlerCart.getInstance(getApplicationContext()).delete_cart();
            DataBaseHandlerCartOptions.getInstance(getApplicationContext()).delete_cart_option();
            Intent intent = new Intent(MyAccountInfo.this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.user_name) {
            onBackPressed();
            finish();
        } else if (id == R.id.menu_wish_list) {
            if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                Methods.toast(getResources().getString(R.string.must_login));
                Intent intent = new Intent(MyAccountInfo.this, Login.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MyAccountInfo.this, Wish_List.class);
                startActivity(intent);
            }
        } else if (id == R.id.my_order) {
            Intent intent = new Intent(MyAccountInfo.this, OrderHistory.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void result(String[] data, String source) {
        progressD.cancel();
        if (data != null) {
            if (data[0] != null) {
                if (source.equals("CustomerProfile")) {
                    profileList = GetJSONData.getCustomerAddress(data[0]);
                    if (profileList != null) {
                        edt_myac_fname.setText(profileList.get(0).getmDefaultFirstName());
                        edt_myac_lname.setText(profileList.get(0).getmDefaultLastName());
                        edt_myac_email.setText(profileList.get(0).getmEmailId());
                        edt_myac_teleph.setText(profileList.get(0).getmTelePhone());
                    }
                } else if (source.equals("UpdateAccount")) {
                    Response response = GetJSONData.getResponse(data[0]);
                    if (response != null) {
                        if (response.getmStatus() == 200) {
                            Methods.toast(getResources().getString(R.string.account_edit_success));
                            DataBaseHandlerAccount.getInstance(getApplicationContext()).update_account_detail(accountDataSet);
                            success();
                        } else {
                            Methods.toast(response.getmMessage());
                        }
                    }
                }
            } else {
                Methods.toast(getResources().getString(R.string.error));
            }
        } else {
            Methods.toast(getResources().getString(R.string.error));
        }
    }
}
