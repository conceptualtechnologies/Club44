package com.restaurent.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.restaurent.Application_Context;
import com.restaurent.R;
import com.restaurent.activity.user.Check;
import com.restaurent.activity.user.CompanyProfile;
import com.restaurent.activity.user.Company_Aims;
import com.restaurent.activity.user.Login;
import com.restaurent.adapter.CartAdapter;
import com.restaurent.api_call.API_Get;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.constant_class.URL_Class;
import com.restaurent.db_handler.DataBaseHandlerAccount;
import com.restaurent.db_handler.DataBaseHandlerCart;
import com.restaurent.db_handler.DataBaseHandlerCartOptions;
import com.restaurent.interfaces.API_Result;
import com.restaurent.interfaces.Refresher;
import com.restaurent.json_mechanism.GetJSONData;
import com.restaurent.mechanism.Methods;
import com.restaurent.network_checker.NetworkConnection;
import com.restaurent.shared_preferenc_estring.DataStorage;
import com.restaurent.utils.ProductDataSet;
import com.restaurent.utils.ProductOptionDataSet;
import com.restaurent.utils.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

public class Cart extends AppCompatActivity implements Refresher, API_Result {
    Toolbar toolbar;
    RecyclerView mCartProduct;
    Button mBtnCheckOut, btn_apply_coupon,book;
    TextView mSubTotal, mTotal;
    EditText text_cpn;
    ArrayList<ProductDataSet> mDataSet = null;
    ProgressDialog progressDialog;
    HashMap<String, Object> product_data = new HashMap<>();
    ArrayList<ProductOptionDataSet> mOptionList = new ArrayList<>();
    HashMap<Integer, ArrayList<ProductOptionDataSet>> mOptionListChildHolder = new HashMap<>();
    ArrayList<ProductOptionDataSet> temp = new ArrayList<>();
    ArrayList<Integer> list;
    Refresher refresher;
    Boolean checker = true;
    API_Result api_result;
    String hour=null,minutes=null;
    SharedPreferences.Editor editor;

    SharedPreferences myPrefs;


    String total;

    private ProgressDialog pDialog;

    private static final String TAG = Cart.class.getSimpleName();
    private int mYear,mMonth,mDay;
    EditText tv_follow_up,tv_follow_up_date;

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    CircleImageView profilePictureView;
    Bitmap fb_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart__handler);

        api_result = this;

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

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        pDialog = new ProgressDialog(this);

        progressDialog = new ProgressDialog(this);
        refresher = this;

        list = DataBaseHandlerCart.getInstance(getApplicationContext()).get_all_index();
        if (NetworkConnection.connectionChecking(getApplicationContext())) {
            if (list != null) {
                post_calling();
            }
        } else {
            Intent intent = new Intent(Cart.this, NoInternetConnection.class);
            startActivity(intent);
            finish();
        }

        mCartProduct = (RecyclerView) findViewById(R.id.cart_product_items);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mCartProduct.setLayoutManager(linearLayoutManager);
        mBtnCheckOut = (Button) findViewById(R.id.cart_check_out);
        btn_apply_coupon = (Button) findViewById(R.id.btn_cpn);
        text_cpn = (EditText) findViewById(R.id.cpn_txt);
        mSubTotal = (TextView) findViewById(R.id.cart_subtotal_value);


        myPrefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        editor = myPrefs.edit();

       // db.deleteCart();


        tv_follow_up_date=(EditText)findViewById(R.id.followup_detail_date);
        tv_follow_up_date.setInputType(InputType.TYPE_NULL);

                Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date

// Create the DatePickerDialog instance
                DatePickerDialog datePicker = new DatePickerDialog(Cart.this,
                        datePickerListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                datePicker.setCancelable(false);
                datePicker.setTitle("Select the date");
                datePicker.show();

        Calendar c=Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        mYear=c.get(Calendar.YEAR);
        mMonth=c.get(Calendar.MONTH);
        mDay=c.get(Calendar.DAY_OF_MONTH);



     /*   tv_follow_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // backup the input type
                // tv_follow_up.setInputType(InputType.TYPE_NULL); // disable soft input
                // call native handler

                //  LinearLayout linearLayout=new LinearLayout(getApplicationContext());
                Calendar mcurrentTime = Calendar.getInstance();
                int hours = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Cart.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        hour= String.valueOf(selectedHour);
                        minutes= String.valueOf(selectedMinute);

                        if(hour.length()==1){
                            hour="0"+hour;

                        }
                        else{
                            hour=""+hour;

                        }
                        if(minutes.length()==1){

                            minutes="0"+minutes;
                        }
                        else{

                            minutes=""+minutes;
                        }

                        tv_follow_up.setText( hour+ ":" + minutes);
                        //tv_follow_up.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hours, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
//                linearLayout.addView(v);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

            }

        });
*/


        mBtnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mDataSet != null) {



                    if (DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {


                        Intent intent = new Intent(Cart.this, Check.class);
                        startActivity(intent);
                    }


                    else {
                        Intent intent = new Intent(Cart.this, Login.class);
                        startActivity(intent);
                    }
                } else {
                    onBackPressed();
                    finish();
                }
            }
        });

        btn_apply_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pincode_name = text_cpn.getText().toString().trim();

                if (!pincode_name.isEmpty()) {
                    // login user
                    checkLogin(pincode_name);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter Coupon code to redeem!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

    }

    public void post_calling() {
        progressDialog.show();
        progressDialog.setCancelable(false);
        String[] data = {URL_Class.mURL + URL_Class.mURL_Get_Cart_Product};
        new API_Get().get_method(data, api_result, getCartPostData(), JSON_Names.KEY_POST_TYPE, true, getBaseContext(), "Cart");
    }

    public void dataSetting(String result) {
        DataStorage.mStoreSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CART_DATA, result);
        mDataSet = GetJSONData.get_cart_detail(result);
        if (mDataSet != null) {
            mCartProduct.setLayoutManager(new LinearLayoutManager(this));
            mCartProduct.setAdapter(new CartAdapter(getBaseContext(), mDataSet, refresher, result));
        }
        total = GetJSONData.get_cart_total(result);

        String value2=total.toString();
        String f1_total = value2.substring(2);
        String replace=f1_total.replaceAll("[-/]","");
        //int value3=Integer.valueOf(value2);
        //editor = myPrefs.edit();
        editor.putString("f_total",replace);
        editor.apply();

        //editor.putString("f_total", total);
       // editor.commit();

        if (total != null) {
            mSubTotal.setText(total);
//            mTotal.setText(total);
            String f2_total=mSubTotal.getText().toString();
            String f_total = f2_total.replaceAll("₹","");
            DataStorage.mStoreSharedPreferenceString(getApplicationContext(),"total",f_total);
        }



    }



    private void checkLogin(final String pincode_name) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

//        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_Class.mURL_GetCoupan, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Coupon Response: " + response.toString());
                //hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        // Create login session

                       String discount = jObj.getString("discount");

                      Double disc_value = Double.parseDouble(discount);

                      String m_total =  mTotal.getText().toString();

                        String f1_total = m_total.substring(3);

                        String f_total = f1_total.replaceAll("[-/]","");

                        total = f_total;

                        Double value = Double.parseDouble(f_total);

                        Double final_value = value-disc_value;

                        mTotal.setText(final_value.toString());

                          if(mTotal.getText().equals(mSubTotal.getText())){

                        }
                          else{

                              editor = myPrefs.edit();

                              editor.apply();
                          }

hideDialog();
                        text_cpn.setText("Coupon Applied Successfully!!");

                        btn_apply_coupon.setVisibility(View.GONE);




                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("Coupon code not found");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();

                    Toast.makeText(getApplicationContext(), "Connect to internet first!!" , Toast.LENGTH_LONG).show();
                    //e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                /*Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();*/
                //hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String,String>();
                params.put("pincode_name", pincode_name);

                return params;
            }
        };
        // Adding request to request queue
        Application_Context.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CART_DATA);
            onBackPressed();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("unchecked")
    public boolean check_choice(int index, Integer data[]) {
        boolean result = false;
        String product_detail = DataBaseHandlerCart.getInstance(getApplicationContext()).getProductString(index);
        product_data = GetJSONData.getSeparateProductDetail(product_detail);
        if (product_data != null) {
            mOptionList = (ArrayList<ProductOptionDataSet>) product_data.get("Option");
            mOptionListChildHolder = (HashMap<Integer, ArrayList<ProductOptionDataSet>>) product_data.get("Option Child");
            for (int i = 0; i < mOptionList.size(); i++) {
                if (mOptionList.get(i).getProduct_option_type().equals("checkbox")) {
                    for (int j = 0; j < mOptionListChildHolder.get(i).size(); j++) {
                        temp = mOptionListChildHolder.get(i);
                        if (temp.get(j).getProduct_option_value_id().equals(String.valueOf(data[1])) && mOptionList.get(i).getProduct_option_id().equals(String.valueOf(data[0]))) {
                            result = true;
                        }
                    }
                }
            }
        }
        return result;
    }

    public void refresh() {
        button_text_changer();
        list = DataBaseHandlerCart.getInstance(getApplicationContext()).get_all_index();
        if (list != null) {

            if (list.size() != 0) {
                post_calling();
            } else {
                mSubTotal.setText("");
//                mTotal.setText("");
                mCartProduct.setAdapter(new CartAdapter(getBaseContext(), null, refresher, null));
                mDataSet = null;
                checker = false;
                button_text_changer();
            }
        } else {
            mSubTotal.setText("");
          //  mTotal.setText("");
            mCartProduct.setAdapter(new CartAdapter(getBaseContext(), null, refresher, null));
            mDataSet = null;
            checker = false;
            button_text_changer();
        }
    }

    public void button_text_changer() {
        if (!checker) {
            mBtnCheckOut.setText(R.string._continue);
        } else {
            mBtnCheckOut.setText(R.string.checkout);
        }
    }

    @Override
    public void refresher() {
        refresh();
    }

    @Override
    public void result(String[] data, String source) {
        progressDialog.cancel();
        if (source.equals("Cart")) {
            if (data != null) {
                if (data[0] != null) {
                    String response = GetJSONData.getResponseStatus(data[0]);
                    if (response != null) {
                        if (response.equals("success") || response.equals("200")) {
                            dataSetting(data[0]);
                        } else {
                            Response error_response = GetJSONData.getResponse(data[0]);
                            if (error_response != null) {
                                Methods.toast(error_response.getmMessage());
                                mSubTotal.setText("");
                                mTotal.setText("");
                                mCartProduct.setAdapter(new CartAdapter(getBaseContext(), null, refresher, null));
                                mDataSet = null;
                                checker = false;
                                button_text_changer();
                            }
                        }
                    }
                } else {
                    mCartProduct.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                    mCartProduct.setAdapter(new CartAdapter(getBaseContext(), mDataSet, refresher, null));
                }
            }
        }
    }

    public String getCartPostData() {
        try {
            JSONArray array = new JSONArray();
            list = DataBaseHandlerCart.getInstance(getApplicationContext()).get_all_index();
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
            testHomeObject.put(JSON_Names.KEY_PRODUCTS, array);
            testHomeObject.put(JSON_Names.KEY_LANGUAGE_ID, 1);
            return testHomeObject.toString();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refresher();
    }
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            String year1 = String.valueOf(selectedYear);
            String month1 = String.valueOf(selectedMonth + 1);
            String day1 = String.valueOf(selectedDay);

            tv_follow_up_date.setText(day1 + "/" + month1 + "/" + year1);

        }
    };
}


