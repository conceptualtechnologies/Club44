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

public class Delivery extends AppCompatActivity  implements API_Result {
    private String[] arraySpinner;
    Button submit1;
    EditText get_start, get_close ;
    EditText get_bank,get_branch,get_ifsc,get_payment_condition;
    EditText get_remark, get_city ;
    EditText get_password, get_confirm_password;
    ProgressDialog progressDialog;
    String data;
    String Subject, Branch, Firstname, Lastname,Email,Mobile,City,Password,Confirmpassword;
    Spinner s,s1,s2;
    String as,as1,as2;
    API_Result api_result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        api_result = this;
        this.arraySpinner = new String[]{
                "YES", "NO"
        };
        s = (Spinner) findViewById(R.id.spinner1);
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
        this.arraySpinner = new String[]{
                "30Min", "45Min", "60Min", "1.30Min" ,"2Hours","1Day"

        };
        s1 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        s1.setAdapter(adapter1);
        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                as1 = s1.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.arraySpinner = new String[]{
                "30Min", "45Min", "60Min", "1.30Min" ,"2Hours","1Day"
        };
        s2 = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        s2.setAdapter(adapter2);
        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                as2 = s2.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        progressDialog = new ProgressDialog(this);
        get_start = (EditText) findViewById(R.id.et_start);
        get_close = (EditText) findViewById(R.id.et_close);
        submit1= (Button) findViewById(R.id.btn_submit_delivery);
        submit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkConnection.connectionChecking(getApplicationContext())) {
                    if (getPostDataString() != null) {
                        progressDialog.setMessage("Loading Please Wait ...");
                        progressDialog.setTitle("delivery details...");
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        String url[] = {URL_Class.mURL + URL_Class.mURL_delivery};
                        new API_Get().get_method(url, api_result, getPostDataString(), JSON_Names.KEY_POST_TYPE, true, getBaseContext(), "fso_delivery");
                    }
                }
            }
        });
    }




    private String getPostDataString() {
        try {
            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("delivery_boy_available", as);
            postDataParams.put("order_before_time", as1);
            postDataParams.put("delivery_time_period", as2);
            postDataParams.put("start_time", get_start.getText().toString());
            postDataParams.put("close_time", get_close.getText().toString());
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
        if (source.equals("fso_delivery")) {
            progressDialog.cancel();
            Methods.toast("DONE");

            if (data != null) {
                Response response = GetJSONData.getResponse(data[0]);
                if (response != null) {
                    if (response.getmStatus() == 200) {

                   /*     Intent intent=new Intent(Delivery.this,Home.class);
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
