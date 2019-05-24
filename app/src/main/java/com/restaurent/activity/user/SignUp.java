package com.restaurent.activity.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import com.restaurent.R;
import com.restaurent.api_call.API_Get;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.constant_class.URL_Class;
import com.restaurent.interfaces.API_Result;
import com.restaurent.json_mechanism.GetJSONData;
import com.restaurent.mechanism.Methods;
import com.restaurent.shared_preferenc_estring.DataStorage;
import com.restaurent.utils.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity implements API_Result {
    Spinner selection;
    String selected_option;
    String query;
    InputStream is = null;
    String result = "";
    String stock_list, advisor_name;
    JSONObject jsonobject, jArray2;
    JSONArray jsonarray;
    public static String currentState;
    public static String tempSStateCheck;
    public String mEmailValidCheck;
    Button payment;
    public Integer iCountryId;
    TextView go_login_page;


    Button submit;
    ImageView iv_name, iv_pwd, iv_email, iv_address, iv_mobile, iv_department;
    EditText get_user_name, get_last_name, get_pwd, get_website, get_project, get_adhar, get_email, get_city, get_mobile, get_confirm_password, get_address, get_pincode;
    Spinner get_stock_list, mySpinner;

    ImageView name, email, address, password, iv_stock_list, phone_no, advisor;
    ProgressDialog progressDialog;
    TextView error_first_name, error_last_name, error_email, error_password, error_mobile, error_address;
    API_Result api_result;
    Spinner s;
    String data;
    String as;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        api_result = this;
        progressDialog = new ProgressDialog(this);



        get_user_name = (EditText) findViewById(R.id.et_user_name);

        get_last_name = (EditText) findViewById(R.id.et_last_name);
        get_email = (EditText) findViewById(R.id.et_email);
        get_mobile = (EditText) findViewById(R.id.et_mobile);
        get_address = (EditText) findViewById(R.id.et_address);
        get_pwd = (EditText) findViewById(R.id.et_pass);
        get_city = (EditText) findViewById(R.id.et_city);


        submit = (Button) findViewById(R.id.btn_submit);

        // go_login_page = (TextView) findViewById(R.id.tv_return_to_login_pg);
//        go_login_page.setPaintFlags(go_login_page.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

      /*  submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transfer();
            }
        });*/

        // add click listener to Button "POST"
        submit.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {

                if (getPostDataString() != null) {
                    progressDialog.setMessage("Loading Please Wait ...");
                    progressDialog.setTitle("Signing up ...");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(true);



    String url[] = {URL_Class.mURL + URL_Class.mURL_registration};
    new API_Get().get_method(url, api_result, getPostDataString(), JSON_Names.KEY_POST_TYPE, true, getBaseContext(), "SignUpPost");


                } else {
                    Methods.toast(getResources().getString(R.string.fill_required_field));
                }

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

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN);
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public void transfer() {
        if (DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN).equals(JSON_Names.KEY_CURRENT_LOGIN_SIGN_UP)) {
            DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN);
            onBackPressed();
            finish();
        } else {
            DataStorage.mStoreSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN, JSON_Names.KEY_CURRENT_LOGIN_FROM_SIGN_UP);
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        }
    }

    public void no_connection_transfer() {

    }

    private String getPostDataString() {
        try {
            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("first_name", get_user_name.getText().toString());
            postDataParams.put("last_name", get_last_name.getText().toString());

            postDataParams.put("email", get_email.getText().toString());
            postDataParams.put("telephone", get_mobile.getText().toString());



            postDataParams.put("city", get_address.getText().toString());
            postDataParams.put("password", get_pwd.getText().toString());
            postDataParams.put("address", get_address.getText().toString());



            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> entry : postDataParams.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append(URL_Class.mAnd_Symbol);

                result.append(URLEncoder.encode(entry.getKey(), URL_Class.mConvertType));
                result.append(URL_Class.mEqual_Symbol);
                result.append(URLEncoder.encode(entry.getValue(), URL_Class.mConvertType));
            }
            return result.toString();
        } catch (Exception e) {
            return null;
        }

    }


    @Override
    public void result(String[] data, String source) {
        progressDialog.cancel();
        if (source.equals("SignUpPost")) {
            progressDialog.cancel();
            Log.d("SignUp", data.toString());

            if (data != null) {
                Response response = GetJSONData.getResponse(data[0]);
                if (response != null) {
                    if (response.getmStatus() == 200) {
                        Methods.toast(getResources().getString(R.string.registration_successful_sign_up));
                        Intent intent = new Intent(SignUp.this, Login.class);
                        startActivity(intent);


                    } else {
                        Methods.toast(response.getmMessage());
                    }
                }
            } else {
                Methods.toast(getResources().getString(R.string.reg_error));
            }

        }
    }
}

