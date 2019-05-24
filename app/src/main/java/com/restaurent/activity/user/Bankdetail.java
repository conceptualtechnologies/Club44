package com.restaurent.activity.user;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.restaurent.R;
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

public class Bankdetail extends AppCompatActivity  implements API_Result {
    private String[] arraySpinner;
    Button submit;
    EditText get_account_holder, get_account_number ;
    EditText get_bank,get_branch,get_ifsc,get_payment_condition;
    EditText get_remark, get_city ;
    EditText get_password, get_confirm_password;
    ProgressDialog progressDialog;
    String data;
    String Subject, Branch, Firstname, Lastname,Email,Mobile,City,Password,Confirmpassword;
    Spinner s;
    String as;
    API_Result api_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankdetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.arraySpinner = new String[]{
                "Advance Pay", "Dailycash Pay", "Daily Pay in A/c", "Monthly Pay in A/c"
        };
        s = (Spinner) findViewById(R.id.spinner_payment);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                as = s.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        progressDialog = new ProgressDialog(this);
        get_account_holder = (EditText) findViewById(R.id.et_account_holder);
        get_account_number = (EditText) findViewById(R.id.et_account_number);
        get_bank = (EditText) findViewById(R.id.et_bank);
        get_remark = (EditText) findViewById(R.id.et_remark);
        get_branch = (EditText) findViewById(R.id.et_branch);
        get_ifsc = (EditText) findViewById(R.id.et_ifsc);

        submit = (Button) findViewById(R.id.btn_submit_bank);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (NetworkConnection.connectionChecking(getApplicationContext())) {
                    if (getPostDataString() != null) {
                        progressDialog.setMessage("Loading Please Wait ...");
                        progressDialog.setTitle("bankdatails ...");
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        String url[] = {URL_Class.mURL + URL_Class.mURL_bank_datails};
                        new API_Get().get_method(url, api_result, getPostDataString(), JSON_Names.KEY_POST_TYPE, true, getBaseContext(), "fsobankdatails");
                    }
                }
            }
        });
    }

    private String getPostDataString() {
        try {
            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("account_holder_name", get_account_holder.getText().toString());
            postDataParams.put("account_number", get_account_number.getText().toString());
            postDataParams.put("bank_name", get_bank.getText().toString());
            postDataParams.put("branch_name", get_branch.getText().toString());
            postDataParams.put("ifsc_code", get_ifsc.getText().toString());
            postDataParams.put("payment_condition", as);
            postDataParams.put("remark", get_remark.getText().toString());
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
        if (source.equals("fsobankdatails")) {
            progressDialog.cancel();
            Methods.toast("DONE");


            if (data != null) {
                Response response = GetJSONData.getResponse(data[0]);
                if (response != null) {
                    if (response.getmStatus() == 200) {

                      /*  Intent intent=new Intent(Bankdetails.this,Home.class);
                        startActivity(intent);*/


                    } else {
                        Methods.toast(response.getmMessage());
                    }
                }
            } else {
                Methods.toast(getResources().getString(R.string.error));
            }
            finish();
        }
    }
}