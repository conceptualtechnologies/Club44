package com.restaurent.activity.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.restaurent.R;
import com.restaurent.activity.NoInternetConnection;
import com.restaurent.api_call.API_Get;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.constant_class.URL_Class;
import com.restaurent.interfaces.API_Result;
import com.restaurent.json_mechanism.GetJSONData;
import com.restaurent.mechanism.Methods;
import com.restaurent.network_checker.NetworkConnection;
import com.restaurent.utils.Response;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class ForgetPassword extends AppCompatActivity implements API_Result {
    Button btn_send_email, btn_back_to_login;
    Toolbar toolbar;
    EditText get_email;
    API_Result api_result;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        api_result=this;

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        dialog = new ProgressDialog(this);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        get_email = (EditText) findViewById(R.id.et_enter_email_id_for_link);
        btn_back_to_login = (Button) findViewById(R.id.btn_back_to_login);
        btn_send_email = (Button) findViewById(R.id.btn_send_email);
        btn_send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!get_email.getText().toString().equals("")) {

                    switch (Boolean.toString(Methods.isEmailValidator(get_email.getText().toString()))) {

                        case "true": {
                            if (NetworkConnection.connectionChecking(getApplicationContext())) {
                                HashMap<String, String> postDataParams = new HashMap<>();
                                postDataParams.put(JSON_Names.KEY_MAIL_ID, get_email.getText().toString());
                                String url[]={URL_Class.mURL + URL_Class.mURL_ForgetPassword};
                                if(getPostDataString(postDataParams)!=null)
                                    new API_Get().get_method(url,api_result,getPostDataString(postDataParams),JSON_Names.KEY_POST_TYPE,true,getBaseContext(),"ForgetCountry");
                                dialog.setMessage("Loading Please Wait ...");
                                dialog.setTitle("Connecting to server ...");
                                dialog.show();
                                dialog.setCancelable(false);
                            } else {
                                Intent intent = new Intent(ForgetPassword.this, NoInternetConnection.class);
                                startActivity(intent);
                                finish();
                            }
                            break;

                        }
                        case "false": {
                            Methods.toast(getResources().getString(R.string.please_enter_valid_email));

                        }

                    }
                } else {
                    Methods.toast(getResources().getString(R.string.please_enter_your_email_id));
                }
            }

        });

        btn_back_to_login.setOnClickListener(new View.OnClickListener()

                                             {
                                                 @Override
                                                 public void onClick(View v) {
                                                     onBackPressed();
                                                     finish();
                                                 }
                                             }

        );
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
        if (id == R.id.login) {
            Intent intent_open_login = new Intent(ForgetPassword.this, Login.class);
            startActivity(intent_open_login);
            finish();
            return true;
        }
        else if (id == android.R.id.home) {
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void result(String[] data, String source) {
        if(source.equals("ForgetCountry")) {
            if (data != null) {
                if (data[0] != null) {
                    dialog.cancel();
                    if (data[0] != null) {
                        Response response = GetJSONData.getResponse(data[0]);
                        if (response != null) {
                            if (response.getmStatus() == 200) {
                                Methods.toast(response.getmMessage());
                                onBackPressed();
                                finish();
                            } else {
                                Methods.toast(response.getmMessage());
                            }
                        }
                    } else {
                        Methods.toast(getResources().getString( R.string.error));
                    }
                }
            }
        }
    }

    private String getPostDataString(HashMap<String, String> params) {
        try {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append(URL_Class.mAnd_Symbol);

                result.append(URLEncoder.encode(entry.getKey(), URL_Class.mConvertType));
                result.append(URL_Class.mEqual_Symbol);
                result.append(URLEncoder.encode(entry.getValue(), URL_Class.mConvertType));
            }

            return result.toString();
        }catch (Exception e){
            return null;
        }
    }

}