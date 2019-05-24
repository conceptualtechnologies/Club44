package com.restaurent.activity.user;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.restaurent.R;
import com.restaurent.activity.Home;
import com.restaurent.api_call.API_Get;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.constant_class.URL_Class;
import com.restaurent.db_handler.DataBaseHandlerAccount;
import com.restaurent.fragments.check_out.CheckOut_Confirmation;
import com.restaurent.interfaces.API_Result;
import com.restaurent.mechanism.Methods;
import com.restaurent.network_checker.NetworkConnection;
import com.restaurent.shared_preferenc_estring.DataStorage;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by yeshu on 1/13/2018.
 */

public class Phone_Number extends AppCompatActivity   implements API_Result, View.OnClickListener {

    RequestQueue queue;
    public static Phone_Number activity = null;
    JsonObjectRequest jsonObjectRequest;
   String customer_id,otp1;
    API_Result api_result;
    String number,data;
    ProgressDialog progressDialog;
    EditText new_password,email,txt_code;
    Button change_password,email_submit,btn_code;
   Context context;
    TextView otp,enterphone;
     Dialog dialog;
    public void showDialog(Activity activity){
         dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.phone_number);
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        context=activity;
        api_result = (API_Result) this;
       // progressDialog = new ProgressDialog(this);
      customer_id= String.valueOf(DataBaseHandlerAccount.getInstance(activity.getApplicationContext()).get_customer_email_id());
        Log.e("Customer_id",customer_id);
       otp=(TextView) dialog.findViewById(R.id.tv_otp);
       enterphone =(TextView) dialog.findViewById(R.id.tv_show_pwd);
        email=(EditText)dialog.findViewById(R.id.et_email);
        email_submit=(Button)dialog.findViewById(R.id.btn_submit);
        txt_code=(EditText)dialog.findViewById(R.id.et_new_code);
        btn_code=(Button)dialog.findViewById(R.id.btn_code);
        queue = Volley.newRequestQueue(activity);

        otp.setVisibility(View.GONE);
        btn_code.setVisibility(View.GONE);
        txt_code.setVisibility(View.GONE);

        btn_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code_verification();
            }
        });
        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
       /* dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
*/

        //Customer_id = DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_id();



    email_submit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
mobile_verification();


        }

    });
}

    public void showpd() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Verifying......");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
    }

    public void hidedp() {

        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }


    }



    @Override
    public void result(String[] data, String source) {

    }

    @Override
    public void onClick(View v) {

    }


        public void mobile_verification() {

            showpd();
            number = email.getText().toString().trim();
            String url="http://club44.in/app_phone_verify.php" + "?email="+ DataStorage.mRetrieveSharedPreferenceString(context, "email_verify")+"&number="+number;
            url=url.replace("[^A-Za-z0-9;\\\\/:*?\\\"<>|&']","");
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,"", new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.e("response","http://club44.in/app_phone_verify.php" + "?customer_id12="+customer_id+"&number="+number);
                    //  underProcess_dataAdapter.clearAdapter();
                    hidedp();
                    try {
                        String response1 = response.getString("data");
                        if(response1.contentEquals("success")) {
                            Log.e("Customer_id1", response.toString());
                            Methods.toast("Enter Your OTP!!!");
                            btn_code.setVisibility(View.VISIBLE);
                            txt_code.setVisibility(View.VISIBLE);
                            enterphone.setVisibility(View.GONE);
                            otp.setVisibility(View.VISIBLE);
                            email_submit.setVisibility(View.GONE);
                        }
                        else{

                        }



                        // UnderProcess_DataAdapter.ListRowViewHolder.report_no.setBackgroundColor(getResources().getColor(R.color.blue_900));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //                    adapter.notifyDataSetChanged();
                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("FOrget Password", "Error Message" + error.getMessage());
                    hidedp();
                }


            });
            queue.add(jsonObjectRequest);

        }





             public void code_verification() {
                  showpd();
                 otp1 = txt_code.getText().toString().trim();
                 String url="http://club44.in/app_otp_verify.php" + "?email="+ DataStorage.mRetrieveSharedPreferenceString(context, "email_verify")+"&otp="+otp1;
                 url=url.replace("[^A-Za-z0-9;\\\\/:*?\\\"<>|&']","");
                  jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url ,"", new Response.Listener<JSONObject>() {
                      @Override
                      public void onResponse(JSONObject response) {
                          Log.e("response", response.toString());
                          //  underProcess_dataAdapter.clearAdapter();
                          hidedp();
                          try {
                              String response1 = response.getString("success");
                              if (response1.contentEquals("success")) {
                                  dialog.dismiss();
                                  CheckOut_Confirmation.mconfirm_phone_no.setVisibility(View.GONE);
                                  CheckOut_Confirmation.mPlaceOrder.setVisibility(View.VISIBLE);

                              } else {


                                  // UnderProcess_DataAdapter.ListRowViewHolder.report_no.setBackgroundColor(getResources().getColor(R.color.blue_900));
                              }
                              //                    adapter.notifyDataSetChanged();


                          } catch (JSONException e) {
                              e.printStackTrace();
                          }
                      }
                  }, new Response.ErrorListener() {

                      @Override
                      public void onErrorResponse(VolleyError error) {
                          VolleyLog.d("FOrget Password", "Error Message" + error.getMessage());
                          hidedp();
                      }


                  });
                  queue.add(jsonObjectRequest);

              }



}



