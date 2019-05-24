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

public class Itemdetail extends AppCompatActivity  implements  API_Result {
    private String[] arraySpinner;
    Button submit1;
    EditText get_start, get_close ;
    ProgressDialog progressDialog;
    Spinner s,s1,s2,s3;
    String as,as1,as2,as3;
    API_Result api_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemdetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        api_result = this;
        this.arraySpinner = new String[]{
                "Tea/Coffee", "Lunch/Dinner", "Dal Bafle/Bati", "Jain Thali","Gujarati Thali","Cake/Sweets"
        };
        s = (Spinner) findViewById(R.id.spinner5);
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
                "Samosa", "Kachori", "Poha", "Jalebi" ,"Alu Bada","Bread Bada","Paratha"

        };
        s1 = (Spinner) findViewById(R.id.spinner6);
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
                "Dosa", "Idli", "Bada", "Manchurian"
        };
        s2 = (Spinner) findViewById(R.id.spinner7);
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
        this.arraySpinner = new String[]{
                "1", "2", "3", "4" ,"5","6"
        };
        s3 = (Spinner) findViewById(R.id.spinner8);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        s3.setAdapter(adapter3);
        s3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                as3 = s3.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        progressDialog = new ProgressDialog(this);
        submit1= (Button) findViewById(R.id.btn_submit_itemdetails);
        submit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkConnection.connectionChecking(getApplicationContext())) {
                    if (getPostDataString() != null) {
                        progressDialog.setMessage("Loading Please Wait ...");
                        progressDialog.setTitle("deliverydetails...");
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        String url[] = {URL_Class.mURL + URL_Class.mURL_itemdetails};
                        new API_Get().get_method(url, api_result, getPostDataString(), JSON_Names.KEY_POST_TYPE, true, getBaseContext(), "fso_itemdetails");
                    }
                }
            }
        });
    }
    private String getPostDataString() {
        try {
            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("item_available", as);
            postDataParams.put("break_fast", as1);
            postDataParams.put("southindian_chinese", as2);
            postDataParams.put("services_area_covered", as3);

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
        if (source.equals("fso_itemdetails")) {
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