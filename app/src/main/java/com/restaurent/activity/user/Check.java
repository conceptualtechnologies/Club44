package com.restaurent.activity.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.restaurent.PayuActivity;
import com.restaurent.R;
import com.restaurent.activity.Cart;
import com.restaurent.api_call.API_Get;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.constant_class.URL_Class;
import com.restaurent.db_handler.DataBaseHandlerAccount;
import com.restaurent.db_handler.DataBaseHandlerAccountAddress;
import com.restaurent.db_handler.DataBaseHandlerCart;
import com.restaurent.db_handler.DataBaseHandlerCartOptions;
import com.restaurent.db_handler.DataBaseHandlerConfirmOrder;
import com.restaurent.fragments.check_out.CheckOut_Confirmation;
import com.restaurent.fragments.check_out.CheckOut_Success;
import com.restaurent.interfaces.API_Result;
import com.restaurent.interfaces.CheckOutAPIRequest;
import com.restaurent.interfaces.CheckOutTransaction;
import com.restaurent.json_mechanism.GetJSONData;
import com.restaurent.mechanism.Methods;
import com.restaurent.shared_preferenc_estring.DataStorage;
import com.restaurent.utils.AccountDataSet;
import com.restaurent.utils.ProductOptionDataSet;
import com.restaurent.utils.Response;
import com.restaurent.utils.ShippingAndPayment_DataSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

public class Check extends AppCompatActivity implements API_Result, CheckOutTransaction,CheckOutAPIRequest{
RadioGroup radioSexGroup;
    Button cod,pay;
    CheckOutAPIRequest checkOutAPIRequest;
    ProgressDialog progressDialog;
    API_Result api_result;
    FragmentManager fragmentManager;
    RelativeLayout checkout_holder;
   LinearLayout linear_holder;
    Toolbar toolbar;
    CircleImageView profilePictureView;
    Bitmap fb_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

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
        //db = new CartSQLiteHandler(getApplicationContext());

      //  assert getSupportActionBar() != null;

        checkout_holder= (RelativeLayout) findViewById(R.id.check_out_container);
        linear_holder= (LinearLayout) findViewById(R.id.linear_layout);


        addListenerOnButton();

    }

    public void addListenerOnButton() {

        radioSexGroup = (RadioGroup) findViewById(R.id.payment_type_chooser);
        cod=(Button)findViewById(R.id.cod);
        pay=(Button)findViewById(R.id.payu);
        api_result = this;
        fragmentManager = getSupportFragmentManager();
        progressDialog = new ProgressDialog(this);
        checkOutAPIRequest= this;
       cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkOutAPIRequest.checkout_confirmation();

            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PayuActivity.class);
                startActivity(intent);
            }
        });


    }


    @Override
    public void checkout_delivery_detail_request() {

    }

    @Override
    public void checkout_payment_type_request() {

    }

    @Override
    public void checkout_confirmation() {
        progressDialog.setCancelable(false);
        progressDialog.show();
        String temp = getCheckOutConfirmationPostData();

        String url[] = {URL_Class.mURL + URL_Class.mURL_Confirm_Order};
        new API_Get().get_method(url, api_result, temp, JSON_Names.KEY_POST_TYPE,
                true, getBaseContext(), "CheckoutConfirmation");
    }

    @Override
    public void checkout_place_order(String data) {
        progressDialog.setCancelable(false);
        progressDialog.show();
        String temp = getCheckOutPlaceOrder(data);
        if (temp != null) {
            String url[] = {URL_Class.mURL + URL_Class.mURL_Place_order};
            new API_Get().get_method(url, api_result, temp, JSON_Names.KEY_POST_TYPE,
                    true, getBaseContext(), "CheckoutPlaceOrder");
        }

    }
    public String getCheckOutPlaceOrder(String order_id) {
        try {
            JSONObject object = new JSONObject();
            if (!order_id.equals(JSON_Names.KEY_NO_DATA)) {
                object.put(JSON_Names.KEY_CONFIRMATION_ORDER_ID, order_id);
                object.put(JSON_Names.KEY_CONFIRMATION_ORDER_STATUS_ID, 1);
            } else {
                object.put(JSON_Names.KEY_CONFIRMATION_ORDER_ID, 0);
                object.put(JSON_Names.KEY_CONFIRMATION_ORDER_STATUS_ID, 0);
            }

            return object.toString();
        } catch (Exception e) {
            return null;
        }
    }
    @Override
    public void checkout_edit_address() {

    }

    @Override
    public void checkout_edit_address_post(String data) {

    }



    @Override
    public void checkout_address_edit(String data) {

    }

    @Override
    public void checkout_delivery_type(String data) {

    }

    @Override
    public void checkout_payment_type(String data) {

    }

    @Override
    public void checkout_cart_changer() {
        DataBaseHandlerConfirmOrder.getInstance(getApplicationContext()).delete_payment_type();
        DataBaseHandlerConfirmOrder.getInstance(getApplicationContext()).delete_shipping_type();
        DataBaseHandlerAccountAddress.getInstance(getApplicationContext()).delete_account_address();
        DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CART_DATA);
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        finish();
    }

    @Override
    public void checkout_confirmation(String data) {
        if(data==null){
            linear_holder.setVisibility(View.VISIBLE);
        }
        else {
            CheckOut_Confirmation checkOut_confirmation = CheckOut_Confirmation.getInstance(data);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.check_out_container, checkOut_confirmation, "Fourth");
            fragmentTransaction.addToBackStack("Fourth");
            fragmentTransaction.commit();
            linear_holder.setVisibility(View.GONE);
        }
    }

    @Override
    public void checkout_place_order() {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        DataBaseHandlerAccountAddress.getInstance(getApplicationContext()).delete_account_address();
        DataBaseHandlerConfirmOrder.getInstance(getApplicationContext()).delete_shipping_type();
        DataBaseHandlerConfirmOrder.getInstance(getApplicationContext()).delete_payment_type();
        DataBaseHandlerCart.getInstance(getApplicationContext()).delete_cart();
        DataBaseHandlerCartOptions.getInstance(getApplicationContext()).delete_cart_option();
        CheckOut_Success checkOut_success = new CheckOut_Success();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.check_out_container, checkOut_success);
        fragmentTransaction.commit();
    }
    @Override
    public void result(String[] data, String source) {
        progressDialog.dismiss();
        if (data != null) {
            if (data[0] != null) {
                switch (source) {

        case "CheckoutConfirmation":
        String CheckoutConfirmation = GetJSONData.getResponseStatus(data[0]);
            Log.e("Data",data[0]);
        if (CheckoutConfirmation != null) {
            if (CheckoutConfirmation.equals("success") || CheckoutConfirmation.equals("200")) {
                checkout_confirmation(data[0]);
            } else {
                Response error_response = GetJSONData.getResponse(data[0]);
                if (error_response != null) {
                    Methods.toast( error_response.getmMessage());
                    getSupportFragmentManager().popBackStack();
                }
            }
    }
            break;
                    case "CheckoutPlaceOrder":
                        Response response_data = GetJSONData.getResponse(data[0]);
                        if (response_data != null) {
                            if (response_data.getmStatus() == 200) {
                                checkout_place_order();
                            } else {
                                Methods.toast( response_data.getmMessage());
                            }
                        }
                        break;
                }
            } else {
                Methods.toast(getResources().getString( R.string.error));
            }
        } else {
            Methods.toast(getResources().getString( R.string.error));
        }}
    public String getCheckOutConfirmationPostData() {
        AccountDataSet accountDataSetShipping, accountDataSetPayment;
        ShippingAndPayment_DataSet shipping_dataSet, payment_dataSet;
        try {
            accountDataSetShipping = DataBaseHandlerAccountAddress.getInstance(getApplicationContext()).get_account_payment_address();
            accountDataSetPayment = DataBaseHandlerAccountAddress.getInstance(getApplicationContext()).get_account_shipping_address();
            shipping_dataSet = DataBaseHandlerConfirmOrder.getInstance(getApplicationContext()).get_shipping_type();
            payment_dataSet = DataBaseHandlerConfirmOrder.getInstance(getApplicationContext()).get_payment_type();
            JSONArray array = new JSONArray();
            ArrayList<Integer> list = DataBaseHandlerCart.getInstance(getApplicationContext()).get_all_index();

            for (int i = 0; i < DataBaseHandlerCart.getInstance(getApplicationContext()).get_Size_cart(); i++) {
                JSONObject product_object = new JSONObject();
                JSONObject object1 = new JSONObject();
                if (DataBaseHandlerCartOptions.getInstance(getApplicationContext()).check_option_index(list.get(i))) {
                    String id = DataBaseHandlerCart.getInstance(getApplicationContext()).getProductId(list.get(i));
                    int count = DataBaseHandlerCart.getInstance(getApplicationContext()).get_product_count(list.get(i));
                    product_object.put(JSON_Names.KEY_PRODUCT_ID, id);
                    product_object.put(JSON_Names.KEY_QUANTITY, count);
                    ArrayList<Integer[]> value = DataBaseHandlerCartOptions.getInstance(getApplicationContext()).option_checking(list.get(i));
                    for (int j = 0; j < value.size(); j++) {
                        if (check_choice(list.get(i), value.get(j))) {
                            JSONArray option_id = new JSONArray();
                            Integer data[] = value.get(j);
                            option_id.put(data[1]);
                            object1.put(String.valueOf(data[0]), option_id);
                        } else {
                            Integer data[] = value.get(j);
                            object1.put(String.valueOf(data[0]), data[1]);
                        }
                    }
                    product_object.put(JSON_Names.KEY_OPTION, object1);
                } else {
                    String id = DataBaseHandlerCart.getInstance(getApplicationContext()).getProductId(list.get(i));
                    int count = DataBaseHandlerCart.getInstance(getApplicationContext()).get_product_count(list.get(i));
                    product_object.put(JSON_Names.KEY_PRODUCT_ID, id);
                    product_object.put(JSON_Names.KEY_QUANTITY, count);
                }
                array.put(i, product_object);
            }
            JSONObject testHomeObject = new JSONObject();
            testHomeObject.put(JSON_Names.KEY_LANGUAGE_ID, 1);
            testHomeObject.put(JSON_Names.KEY_RESPONSE_COUPON, "");
            testHomeObject.put(JSON_Names.KEY_RESPONSE_VOUCHER, "");
            testHomeObject.put(JSON_Names.KEY_CUSTOMER_ID, DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_id());
            testHomeObject.put(JSON_Names.KEY_PRODUCTS, array);
            testHomeObject.put(JSON_Names.KEY_LANGUAGE_ID, 1);

            JSONObject payment = new JSONObject();
            payment.put(JSON_Names.KEY_ADDRESS_ID, DataBaseHandlerAccount.getInstance(getApplicationContext()).getAddressId());
            payment.put(JSON_Names.KEY_PAYMENT_FIRST_NAME, DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"firstname"));
            payment.put(JSON_Names.KEY_PAYMENT_LAST_NAME,DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"lastname"));
            payment.put(JSON_Names.KEY_PAYMENT_COMPANY, DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"company"));
            payment.put(JSON_Names.KEY_PAYMENT_ADDRESS_1, DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"address1"));
            payment.put(JSON_Names.KEY_PAYMENT_ADDRESS_2, "");
            payment.put(JSON_Names.KEY_PAYMENT_CITY, DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"city"));
            payment.put(JSON_Names.KEY_PAYMENT_PIN_CODE, DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"postcode"));
            payment.put(JSON_Names.KEY_PAYMENT_COUNTRY, DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"country"));
            payment.put(JSON_Names.KEY_PAYMENT_COUNTRY_ID, DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"countryid"));
            if (!DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"state").equals("0")) {
                payment.put(JSON_Names.KEY_PAYMENT_ZONE, DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"state"));
            } else {
                payment.put(JSON_Names.KEY_PAYMENT_ZONE, "");
            }
            payment.put(JSON_Names.KEY_PAYMENT_ZONE_ID, DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"zoneid"));
            payment.put(JSON_Names.KEY_PAYMENT_PHONE_NUMBER, DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_mobile_no());
            payment.put(JSON_Names.KEY_PAYMENT_EMAIL,DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_email_id());

            JSONObject shipping = new JSONObject();
            shipping.put(JSON_Names.KEY_ADDRESS_ID, DataBaseHandlerAccount.getInstance(getApplicationContext()).getAddressId());
            shipping.put(JSON_Names.KEY_SHIPPING_FIRST_NAME,  DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"firstname"));
            shipping.put(JSON_Names.KEY_SHIPPING_LAST_NAME, DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"lastname"));
            shipping.put(JSON_Names.KEY_SHIPPING_COMPANY, DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"company"));
            shipping.put(JSON_Names.KEY_SHIPPING_ADDRESS_1, DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"address1"));
            shipping.put(JSON_Names.KEY_SHIPPING_ADDRESS_2, "");
            shipping.put(JSON_Names.KEY_SHIPPING_CITY, DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"city"));
            shipping.put(JSON_Names.KEY_SHIPPING_PIN_CODE, DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"postcode"));
            shipping.put(JSON_Names.KEY_SHIPPING_COUNTRY, DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"country"));
            shipping.put(JSON_Names.KEY_SHIPPING_COUNTRY_ID,  DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"countryid"));
            if (!DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"state").equals("0")) {
                shipping.put(JSON_Names.KEY_SHIPPING_ZONE, DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"state"));
            } else {
                shipping.put(JSON_Names.KEY_SHIPPING_ZONE, "");
            }
            shipping.put(JSON_Names.KEY_SHIPPING_ZONE_ID, DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"zoneid"));
            shipping.put(JSON_Names.KEY_SHIPPING_PHONE_NUMBER,  DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_mobile_no());
            shipping.put(JSON_Names.KEY_SHIPPING_EMAIL, DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_email_id());

            JSONObject shipping_object = new JSONObject();
            JSONObject payment_object = new JSONObject();
            shipping_object.put(JSON_Names.KEY_RESPONSE_TITLE, shipping_dataSet.getmTitle());
            shipping_object.put(JSON_Names.KEY_RESPONSE_CODE, shipping_dataSet.getmCode());
            shipping_object.put(JSON_Names.KEY_RESPONSE_COST, shipping_dataSet.getmCost());
            shipping_object.put(JSON_Names.KEY_RESPONSE_TAX_CLASS_ID, shipping_dataSet.getmTaxClassId());
            shipping_object.put(JSON_Names.KEY_RESPONSE_SORT_ORDER, shipping_dataSet.getmSortOrder());
            payment_object.put(JSON_Names.KEY_RESPONSE_TITLE, payment_dataSet.getmTitle());
            payment_object.put(JSON_Names.KEY_RESPONSE_CODE, payment_dataSet.getmCode());
            payment_object.put(JSON_Names.KEY_RESPONSE_TERMS, payment_dataSet.getmTerms());
            payment_object.put(JSON_Names.KEY_RESPONSE_SORT_ORDER, payment_dataSet.getmSortOrder());

            testHomeObject.put(JSON_Names.KEY_PAYMENT_ADDRESS, payment);
            testHomeObject.put(JSON_Names.KEY_SHIPPING_ADDRESS, shipping);
            testHomeObject.put(JSON_Names.KEY_RESPONSE_SHIPPING_METHOD, shipping_object);
            testHomeObject.put(JSON_Names.KEY_RESPONSE_PAYMENT_METHOD, payment_object);


            return testHomeObject.toString();
        } catch (Exception e) {
            return null;
        }
    }
    @SuppressWarnings("unchecked")
    public boolean check_choice(int index, Integer data[]) {
        HashMap<Integer, ArrayList<ProductOptionDataSet>> mOptionListChildHolder = new HashMap<>();
        ArrayList<ProductOptionDataSet> temp;
        HashMap<String, Object> product_data;
        ArrayList<ProductOptionDataSet> mOptionList = new ArrayList<>();
        boolean result = false;
        String product_detail = DataBaseHandlerCart.getInstance(getApplicationContext()).getProductString(index);
        product_data = GetJSONData.getSeparateProductDetail(product_detail);
        if (product_data != null) {
            mOptionList = (ArrayList<ProductOptionDataSet>) product_data.get("Option");
            mOptionListChildHolder = (HashMap<Integer, ArrayList<ProductOptionDataSet>>) product_data.get("Option Child");
        }
        for (int i = 0; i < mOptionList.size(); i++) {
            if (mOptionList.get(i).getProduct_option_type().equals("checkbox")) {
                for (int j = 0; j < mOptionListChildHolder.get(i).size(); j++) {
                    temp = mOptionListChildHolder.get(i);
                    if (temp.get(j).getProduct_option_value_id().equals(String.valueOf(data[1])) &&
                            mOptionList.get(i).getProduct_option_id().equals(String.valueOf(data[0]))) {
                        result = true;
                    }
                }
            }
        }
        return result;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

