package com.restaurent.activity.account;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.restaurent.R;
import com.restaurent.activity.Cart;
import com.restaurent.activity.Home;
import com.restaurent.activity.NoInternetConnection;
import com.restaurent.activity.Wish_List;
import com.restaurent.activity.user.Availability_of_products;
import com.restaurent.activity.user.CompanyProfile;
import com.restaurent.activity.user.Company_Aims;
import com.restaurent.activity.user.Customer_Care;
import com.restaurent.activity.user.Login;

import com.restaurent.activity.user.Offer;
import com.restaurent.activity.user.SignUp;
import com.restaurent.adapter.SpinnerAdapter;
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
import com.restaurent.utils.SpinnerDataSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddsBook_EditAdds extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, API_Result {

    public static Integer iCountryId;
    public static Integer iStateId;
    public static String tempCString;
    public static String tempSString;
    public static Integer tempInt;
    public static String tempCountryInitialText;
    public static String tempStateInitialText;
    public static String currentState;
    public static String tempSStateCheck;
    public ArrayList<SpinnerDataSet> countryArrayList = new ArrayList<>();
    public HashMap<Integer, ArrayList<SpinnerDataSet>> stateArrayList = new HashMap<>();
    public String mAddress_id, mCustomer_id;
    public String CountryName_cc, ZoneNameSS;
    public Integer CountryId_cc, ZoneId_ss;
    //public int zero = 0;
    public Bundle extras;
    android.support.v7.widget.Toolbar toolbar;
    ProgressDialog progressDialog;
    Button btn_edit_pro_back, btn_edit_pro_continue;
    EditText edt_first_name, edt_last_name, edt_company, edt_adds, edt_city, edt_postcode;
    TextView error_edit_pro_first_name, error_edit_pro_last_name, error_edit_pro_adds, error_edit_pro_city, error_edit_pro_postcode,
            error_edit_pro_country, error_edit_pro_state;
    Spinner spinner_edit_pro_country, Spinner_edit_pro_state;
    Boolean default_id=false;
    AccountDataSet accountDataSet;
    API_Result api_result;
    CircleImageView profilePictureView;
    Bitmap fb_img;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adds_book_edit_adds);

        api_result = this;

        progressDialog = new ProgressDialog(this);
        if (NetworkConnection.connectionChecking(getApplicationContext())) {
            progressDialog.show();
            progressDialog.setCancelable(false);
            String url[] = {URL_Class.mURL + URL_Class.mURL_Country};
            new API_Get().get_method(url, api_result, "", JSON_Names.KEY_GET_TYPE, true, getBaseContext(), "AddressBookCountry");
        } else {
            no_connection_transfer();
        }

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

        accountDataSet = new AccountDataSet();

        edt_first_name = (EditText) findViewById(R.id.et_edit_pro_first_name);
        edt_last_name = (EditText) findViewById(R.id.et_edit_pro_last_name);
        edt_company = (EditText) findViewById(R.id.et_edit_pro_company);
        edt_adds = (EditText) findViewById(R.id.et_edit_pro_address);
        edt_city = (EditText) findViewById(R.id.et_edit_pro_city);
        edt_postcode = (EditText) findViewById(R.id.et_edit_pro_postcode);

        error_edit_pro_first_name = (TextView) findViewById(R.id.tv_error_edit_pro_first_name);
        error_edit_pro_last_name = (TextView) findViewById(R.id.tv_error_edit_pro_last_name);
        error_edit_pro_adds = (TextView) findViewById(R.id.tv_error_edit_pro_address);
        error_edit_pro_city = (TextView) findViewById(R.id.tv_error_edit_pro_city);
        error_edit_pro_postcode = (TextView) findViewById(R.id.tv_error_edit_pro_postcode);
        error_edit_pro_country = (TextView) findViewById(R.id.tv_error_edit_pro_country);
        error_edit_pro_state = (TextView) findViewById(R.id.tv_error_edit_pro_region_state);

        btn_edit_pro_back = (Button) findViewById(R.id.btn_edit_pro_back);
        btn_edit_pro_continue = (Button) findViewById(R.id.btn_edit_pro_continue);

        spinner_edit_pro_country = (Spinner) findViewById(R.id.spinner_edit_pro_country);
        Spinner_edit_pro_state = (Spinner) findViewById(R.id.spinner_edit_pro_region_or_state);


        extras = getIntent().getExtras();
        if (extras != null) {
            mAddress_id = extras.getString(JSON_Names.KEY_ADDRESS_ID);
            mCustomer_id = extras.getString(JSON_Names.KEY_CUSTOMER_ID);
            edt_first_name.setText(extras.getString(JSON_Names.KEY_FIRST_NAME));
            edt_last_name.setText(extras.getString(JSON_Names.KEY_LAST_NAME));
            edt_company.setText(extras.getString(JSON_Names.KEY_COMPANY));
            edt_adds.setText(extras.getString(JSON_Names.KEY_ADDRESS_1));
            edt_city.setText(extras.getString(JSON_Names.KEY_CITY));
            edt_postcode.setText(extras.getString(JSON_Names.KEY_POSTCODE));
            CountryName_cc = extras.getString(JSON_Names.KEY_COUNTRY);
            ZoneNameSS = extras.getString(JSON_Names.KEY_STATE);
            CountryId_cc = extras.getInt(JSON_Names.KEY_COUNTRY_ID);
            ZoneId_ss = extras.getInt(JSON_Names.KEY_ZONE_ID);
            default_id = extras.getBoolean(JSON_Names.KEY_DEFAULT_ADDRESS_ID);
        }
        btn_edit_pro_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        btn_edit_pro_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!edt_first_name.getText().toString().equals("")
                        && (edt_first_name.getText().toString().length() >= 1)
                        && (edt_first_name.getText().toString().length() <= 32)

                        && !edt_last_name.getText().toString().equals("")
                        && (edt_last_name.getText().toString().length() >= 1)
                        && (edt_last_name.getText().toString().length() <= 32)

                        && !edt_adds.getText().toString().equals("")
                        && (edt_adds.getText().toString().length() >= 3)
                        && (edt_adds.getText().toString().length() <= 128)

                        && !edt_city.getText().toString().equals("")
                        && (edt_city.getText().toString().length() >= 2)
                        && (edt_city.getText().toString().length() <= 128)

                        && ((!(tempCString.equals("0")) && (tempSStateCheck.equals("STATE_CONTAIN_DATA")) && !(currentState.equals("STATE_INDEX_INITIAL")))
                        || (!tempCString.equals("0") && (tempSStateCheck.equals("EMPTY_STATE_LIST"))))
                        ) {
                    if (NetworkConnection.connectionChecking(getApplicationContext())) {
                        if (extras != null) {
                            if (writeOut() != null) {
                                String url[] = {URL_Class.mURL + URL_Class.mURL_Edit_Address};
                                new API_Get().get_method(url, api_result, writeOut(),
                                        JSON_Names.KEY_POST_TYPE, true, getBaseContext(), "AddressBookPostEdit");
                            }
                            accountDataSet.setmFirstName(edt_first_name.getText().toString());
                            accountDataSet.setmLastName(edt_last_name.getText().toString());
                        } else {
                            if (writeOut() != null) {
                                String url[] = {URL_Class.mURL + URL_Class.mURL_New_Address};
                                new API_Get().get_method(url, api_result, writeOut(), JSON_Names.KEY_POST_TYPE,
                                        true, getBaseContext(), "AddressBookPostNew");
                            }
                        }
                    } else {
                        no_connection_transfer();
                    }
                }
                if (edt_first_name.getText().toString().equals("")) {
                    error_edit_pro_first_name.setVisibility(View.VISIBLE);
                } else {
                    error_edit_pro_first_name.setVisibility(View.GONE);
                }
                if ((edt_first_name.getText().toString().length() >= 1) && (edt_first_name.getText().toString().length() <= 32)) {
                    error_edit_pro_first_name.setVisibility(View.GONE);
                } else {
                    error_edit_pro_first_name.setVisibility(View.VISIBLE);
                }
                if (edt_last_name.getText().toString().equals("")) {
                    error_edit_pro_last_name.setVisibility(View.VISIBLE);
                } else {
                    error_edit_pro_last_name.setVisibility(View.GONE);
                }
                if ((edt_last_name.getText().toString().length() >= 1) && (edt_last_name.getText().toString().length() <= 32)) {
                    error_edit_pro_last_name.setVisibility(View.GONE);
                } else {
                    error_edit_pro_last_name.setVisibility(View.VISIBLE);
                }


                if (edt_adds.getText().toString().equals("")) {
                    error_edit_pro_adds.setVisibility(View.VISIBLE);
                } else {
                    error_edit_pro_adds.setVisibility(View.GONE);
                }
                if ((edt_adds.getText().toString().length() >= 3) && (edt_adds.getText().toString().length() <= 128)) {
                    error_edit_pro_adds.setVisibility(View.GONE);
                } else {
                    error_edit_pro_adds.setVisibility(View.VISIBLE);
                }
                if (edt_city.getText().toString().equals("")) {
                    error_edit_pro_city.setVisibility(View.VISIBLE);
                } else {
                    error_edit_pro_city.setVisibility(View.GONE);
                }
                if ((edt_city.getText().toString().length() >= 2) && (edt_city.getText().toString().length() <= 128)) {
                    error_edit_pro_city.setVisibility(View.GONE);
                } else {
                    error_edit_pro_city.setVisibility(View.VISIBLE);
                }
                if (tempCString.equals("0")) {
                    error_edit_pro_country.setVisibility(View.VISIBLE);
                } else {
                    error_edit_pro_country.setVisibility(View.GONE);
                }

                if (!(tempCString.equals("0")) && (tempSStateCheck.equals("STATE_CONTAIN_DATA")) && (currentState.equals("STATE_INDEX_INITIAL"))) {
                    error_edit_pro_state.setVisibility(View.VISIBLE);
                } else {
                    error_edit_pro_state.setVisibility(View.GONE);
                }
            }
        });
    }

    public void no_connection_transfer() {
        Intent intent = new Intent(AddsBook_EditAdds.this, NoInternetConnection.class);
        startActivity(intent);
        finish();
    }

    public void success() {
        onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cart_count_value) {
            Intent intent = new Intent(AddsBook_EditAdds.this, Cart.class);
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
            Intent intent = new Intent(AddsBook_EditAdds.this, Cart.class);
            startActivity(intent);
        }
        return false;
    }

    public String writeOut() {
        try {
            HashMap<String, String> params = new HashMap<>();

            if (extras != null) {
                params.put(JSON_Names.KEY_ADDRESS_ID, mAddress_id);
                params.put(JSON_Names.KEY_CUSTOMER_ID, mCustomer_id);
            } else {
                params.put(JSON_Names.KEY_CUSTOMER_ID,
                        String.valueOf(DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_id()));
            }
            params.put(JSON_Names.KEY_FIRST_NAME, edt_first_name.getText().toString());
            params.put(JSON_Names.KEY_LAST_NAME, edt_last_name.getText().toString());
            params.put(JSON_Names.KEY_COMPANY, edt_company.getText().toString());
            params.put(JSON_Names.KEY_ADDRESS_1, edt_adds.getText().toString());
            params.put(JSON_Names.KEY_CITY, edt_city.getText().toString());
            params.put(JSON_Names.KEY_POSTCODE, edt_postcode.getText().toString());
            params.put(JSON_Names.KEY_COUNTRY, tempCString);
            params.put(JSON_Names.KEY_STATE, tempSString);

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
        } catch (Exception e) {
            return null;
        }
    }

    private int getCountryIndex(ArrayList<SpinnerDataSet> countryAL, Integer countryid) {
        Integer index = 0;
        for (int i = 1; i < countryAL.size(); i++) {
            SpinnerDataSet Ccc = countryAL.get(i);
            if (Ccc.get_id().equals(countryid)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private int getStateIndex(HashMap<Integer, ArrayList<SpinnerDataSet>> stateAL, Integer countryid, Integer stateid) {
        Integer index = 0;
        ArrayList<ArrayList<SpinnerDataSet>> ssState = new ArrayList<>();
        ssState.add(stateAL.get(countryid));
        ArrayList<SpinnerDataSet> sState = ssState.get(0);
        for (int j = 1; j < sState.size(); j++) {
            SpinnerDataSet Sss = sState.get(j);
            if (Sss.get_id().equals(stateid)) {
                index = j;
                break;
            }
        }
        return index;
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
        } else if (id == R.id.user_name) {
            Intent intent = new Intent(AddsBook_EditAdds.this, MyAccountMainMenu.class);
            startActivity(intent);
        } else if (id == R.id.login) {
            Intent intent = new Intent(AddsBook_EditAdds.this, Login.class);
            startActivity(intent);
        } else if (id == R.id.register) {
            Intent intent = new Intent(AddsBook_EditAdds.this, SignUp.class);
            startActivity(intent);
        }else if (id == R.id.invite) {


            Intent sharingintent = new Intent(Intent.ACTION_SEND);
            sharingintent.setType("text/plain");
            sharingintent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share The App");
            sharingintent.putExtra(android.content.Intent.EXTRA_TEXT, "Download Tasty Town Foods App From Play Store - https://play.google.com/store/apps/details?id=com.pooja_prasad ");
            startActivity(Intent.createChooser(sharingintent, "Share Via"));
        }

      /*  else if (id == R.id.customer_care) {
            Intent intent = new Intent(getApplicationContext(), Customer_Care.class);
            startActivity(intent);
        }*/
        else if (id == R.id.offer) {
            Intent intent = new Intent(getApplicationContext(), Offer.class);
            startActivity(intent);
        }

       /* else if (id == R.id.availability_of_products) {
            Intent intent = new Intent(getApplicationContext(), Availability_of_products.class);
            startActivity(intent);
        }
        else if (id == R.id.company_aims) {
            Intent intent = new Intent(getApplicationContext(), Company_Aims.class);
            startActivity(intent);
        }*/
        else if (id == R.id.logout) {
            DataStorage.mRemoveSharedPreferenceString(getApplicationContext(),"profile_pic");
            DataBaseHandlerAccount.getInstance(getApplicationContext()).delete_account_detail();
            DataBaseHandlerWishList.getInstance(getApplicationContext()).delete_wish_list();
            DataBaseHandlerCart.getInstance(getApplicationContext()).delete_cart();
            DataBaseHandlerCartOptions.getInstance(getApplicationContext()).delete_cart_option();
            Intent intent = new Intent(AddsBook_EditAdds.this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_wish_list) {
            if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                Methods.toast(getResources().getString(R.string.must_login));
                Intent intent = new Intent(AddsBook_EditAdds.this, Login.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(AddsBook_EditAdds.this, Wish_List.class);
                startActivity(intent);
            }
        } else if (id == R.id.my_order) {
            Intent intent = new Intent(AddsBook_EditAdds.this, OrderHistory.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void result(String[] data, String source) {
        progressDialog.cancel();
        if (source.equals("AddressBookCountry")) {
            if (data != null) {
                try {
                    JSONArray jarray = new JSONArray(data[0]);
                    countryArrayList.add(new SpinnerDataSet());

                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jobject_country = jarray.getJSONObject(i);
                        int countryId = jobject_country.getInt(JSON_Names.KEY_COUNTRY_ID);
                        String countryName = jobject_country.getString(JSON_Names.KEY_FILTER_NAME);
                        countryArrayList.add(new SpinnerDataSet(countryName, countryId));

                        JSONArray jsonArray = jobject_country.getJSONArray(JSON_Names.KEY_STATE);
                        ArrayList<SpinnerDataSet> tempStateArray = new ArrayList<>();
                        ArrayList<SpinnerDataSet> tempInitialText = new ArrayList<>();
                        tempInitialText.add(new SpinnerDataSet());
                        tempStateArray.add(tempInitialText.get(0));

                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject jobject_state = jsonArray.getJSONObject(j);
                            int stateId = jobject_state.getInt(JSON_Names.KEY_STATE_ID);
                            String stateName = jobject_state.getString(JSON_Names.KEY_FILTER_NAME);
                            tempStateArray.add(new SpinnerDataSet(stateName, stateId));
                        }

                        ++i;
                        SpinnerDataSet getCountryIdTemp = countryArrayList.get(i);
                        --i;
                        tempInt = getCountryIdTemp.get_id();
                        stateArrayList.put(tempInt, tempStateArray);
                        tempInt = null;
                    }
                } catch (Exception e) {
                    countryArrayList = null;
                }

                if (countryArrayList != null) {
                    spinner_edit_pro_country = (Spinner) findViewById(R.id.spinner_edit_pro_country);
                    Spinner_edit_pro_state = (Spinner) findViewById(R.id.spinner_edit_pro_region_or_state);


                    SpinnerAdapter adapter_Country = new SpinnerAdapter(AddsBook_EditAdds.this, android.R.layout.simple_spinner_item, countryArrayList);
                    adapter_Country.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_edit_pro_country.setAdapter(adapter_Country);

                    if (CountryId_cc != null) {
                        spinner_edit_pro_country.setSelection(getCountryIndex(countryArrayList, CountryId_cc));
                    }

                    spinner_edit_pro_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                           public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                                                               SpinnerDataSet selectedCountry = countryArrayList.get(pos);

                                                                               tempCountryInitialText = selectedCountry.getInitialText();
                                                                               if (!(tempCountryInitialText == null)) {

                                                                                   List<String> noStates = new ArrayList<>();
                                                                                   noStates.add("");
                                                                                   noStates.add("--- None ---");

                                                                                   ArrayAdapter<String> adapter_StateN = new ArrayAdapter<>(AddsBook_EditAdds.this, R.layout.spinner_item_nodata, R.id.spinner_state_no_data, noStates);
                                                                                   Spinner_edit_pro_state.setAdapter(adapter_StateN);

                                                                               }

                                                                               if ((iCountryId = selectedCountry.get_id()) != null) {
                                                                                   tempCString = iCountryId.toString();

                                                                                   ArrayList<ArrayList<SpinnerDataSet>> firstArray = new ArrayList<>();
                                                                                   firstArray.add(stateArrayList.get(iCountryId));
                                                                                   final ArrayList<SpinnerDataSet> secondArray = firstArray.get(0);

                                                                                   if (firstArray.get(0).size() == 1) {
                                                                                       tempSStateCheck = "EMPTY_STATE_LIST";
                                                                                       tempSString = "0";
                                                                                   } else {
                                                                                       tempSStateCheck = "STATE_CONTAIN_DATA";
                                                                                   }

                                                                                   if (!(firstArray.get(0).size() == 1)) {

                                                                                       SpinnerAdapter adapter_State = new SpinnerAdapter(AddsBook_EditAdds.this, android.R.layout.simple_spinner_item, secondArray);
                                                                                       adapter_State.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                                                       Spinner_edit_pro_state.getBaseline();
                                                                                       Spinner_edit_pro_state.setAdapter(adapter_State);
                                                                                       if (ZoneId_ss != null && tempCString.equals(CountryId_cc.toString())) {
                                                                                           Spinner_edit_pro_state.setSelection(getStateIndex(stateArrayList, CountryId_cc, ZoneId_ss));
                                                                                       }
                                                                                       Spinner_edit_pro_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                                                                           public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                                                                               SpinnerDataSet selectedState = secondArray.get(pos);

                                                                                               tempStateInitialText = selectedState.getInitialText();
                                                                                               if (!(tempStateInitialText == null)) {
                                                                                                   currentState = "STATE_INDEX_INITIAL";
                                                                                               } else {
                                                                                                   currentState = "STATE_INDEX_MORE";
                                                                                               }

                                                                                               if ((iStateId = selectedState.get_id()) != null) {
                                                                                                   String tt;
                                                                                                   tt = (iStateId.toString());
                                                                                                   tempSString = tt;
                                                                                               }
                                                                                           }

                                                                                           @Override
                                                                                           public void onNothingSelected(AdapterView<?> arg0) {
                                                                                           }

                                                                                       });
                                                                                   } else {
                                                                                       List<String> noStates = new ArrayList<>();
                                                                                       noStates.add("");
                                                                                       noStates.add("--- None ---");

                                                                                       ArrayAdapter<String> adapter_StateN = new ArrayAdapter<>(AddsBook_EditAdds.this, R.layout.spinner_item_nodata, R.id.spinner_state_no_data, noStates);
                                                                                       Spinner_edit_pro_state.setAdapter(adapter_StateN);
                                                                                   }

                                                                               } else {
                                                                                   tempCString = "0";
                                                                               }

                                                                           }

                                                                           @Override
                                                                           public void onNothingSelected(AdapterView<?> arg0) {
                                                                           }
                                                                       }
                    );
                }
            }
        } else if (source.equals("AddressBookPostNew") || source.equals("AddressBookPostEdit")) {
            if (data != null) {
                if (data[0] != null) {
                    Response response = GetJSONData.getResponse(data[0]);
                    if (response != null) {
                        if (response.getmStatus() == 200) {
                            Methods.toast(getResources().getString(R.string.address_update));
                            if (default_id) {
                                DataBaseHandlerAccount.getInstance(getApplicationContext()).update_account_detail_name(accountDataSet);
                            }
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

}