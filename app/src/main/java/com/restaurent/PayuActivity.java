package com.restaurent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.restaurent.db_handler.DataBaseHandlerAccount;
import com.restaurent.shared_preferenc_estring.DataStorage;

public class PayuActivity extends AppCompatActivity {

    TextView  amt;
    Button pay = null;

    public static final String TAG = "PayUMoneySDK Sample";

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static final String MY_PREFS_NAME2 = "MyPrefsFile2";
    private CartSQLiteHandler db;

    String f_total, f1_total, first_name, mob_no, e_mail, total2;
    float total;
    Double amount, get2, total3, total4;

    EditText fname, pnumber, emailAddress,rechargeAmt;
    Button Paynow;
    String data = "Data";
    String f_total2="f_total";
    String customer_name,customer_email,customer_no;
    FragmentManager fragmentManager;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payu);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        //db = new CartSQLiteHandler(getApplicationContext());

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fname = (EditText) findViewById(R.id.fname);
        pnumber = (EditText) findViewById(R.id.pnumber);
        emailAddress = (EditText) findViewById(R.id.emailAddress);
        rechargeAmt = (EditText) findViewById(R.id.rechargeAmt);
        Paynow = (Button) findViewById(R.id.Paynow);
        fragmentManager = getSupportFragmentManager();
        customer_name= DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_name();
        customer_no= DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_mobile_no();
        fname.setText(customer_name);
        customer_email= DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_email_id();
        emailAddress.setText(customer_email);
        pnumber.setText(customer_no);
        //       SharedPreferences pref = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
//        first_name = pref.getString("first_name", "first_name");
//        mob_no = pref.getString("phone_number", "phone_number");
//        e_mail = pref.getString("email_id", "email_id");

        String restoredText = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"data");
        String restoredText2 = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"total");
       // int restoredText3 = prefs.getInt("f_total1", 0);
        /*if (restoredText3!=0)
        {
            double total6=Double.valueOf(restoredText3);
            double total7=total6+amount;
            rechargeAmt.setText(String.valueOf(total7));
        }*/
        Log.d("value",restoredText);
        get2= Double.parseDouble(restoredText2);
        //Float restoredText3 = prefs.getFloat("f_total", 0);
        if(restoredText.contentEquals("No Data")) {
            String shipping="0";
            amount = Double.parseDouble(shipping);
        }
        else {
            amount = Double.parseDouble(restoredText);
        }
        if (get2 !=0) {

           // total = prefs.getFloat("f_total", 0);
            total4 = get2 + amount;
            //"No name defined" is the default value.
            get2 = total4;
           // total3 = get2.replaceAll("[-/]", "");
            rechargeAmt.setText(get2.toString());
        }
//DataStorage.mRemoveSharedPreferenceString(getApplicationContext());


      /*  if (get2 != 0) {



            total3 = get2 + amount;


            // String ff_total = cart.get("total");
            String f2 = Double.toString(total3);
            Log.d("total", f2);
            f1_total = f2;

            f_total = f1_total.replaceAll("[-/]", "");
            rechargeAmt.setText(f_total);



    }*/



        /*fname.setText(first_name);
        pnumber.setText(mob_no);
        emailAddress.setText(e_mail);*/


        Paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //if
                if (TextUtils.isEmpty(fname.getText().toString()) || TextUtils.isEmpty(pnumber.getText().toString()) || TextUtils.isEmpty(emailAddress.getText().toString())) {

                    Toast.makeText(getApplicationContext(), "Please Enter Required Details!!", Toast.LENGTH_LONG).show();

                    /*fname.setError("Please Enter Name");
                    pnumber.setError("Please Enter Phone Number");
                    emailAddress.setError("Please Enter EmailID");
                    return;*/

                } else {

                    String getFname = fname.getText().toString().trim();
                    String getPhone = pnumber.getText().toString().trim();
                    String getEmail = emailAddress.getText().toString().trim();
                    String getAmt = rechargeAmt.getText().toString().trim();


                    Intent intent = new Intent(getApplicationContext(), PayMentGateWay.class);
                    intent.putExtra("FIRST_NAME", getFname);
                    intent.putExtra("PHONE_NUMBER", getPhone);
                    intent.putExtra("EMAIL_ADDRESS", getEmail);
                    intent.putExtra("RECHARGE_AMT", getAmt);
                    startActivity(intent);
                }

            }
        });


        //f_total = "1";

        //Toast.makeText(getApplicationContext(),f_total,Toast.LENGTH_LONG).show();

        //amt.setText(f_total);

    }

  /*  @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
/*private boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String getTxnId() {
        return ("0nf7" + System.currentTimeMillis());
    }

    private double getAmount() {


        amount = null;


        amount = Double.parseDouble(f_total);

        *//*if (isDouble(amt.getText().toString())) {
            amount = Double.parseDouble(amt.getText().toString());
            return amount;
        } else {
            Toast.makeText(getApplicationContext(), "Paying Default Amount ₹10", Toast.LENGTH_LONG).show();
            return amount;
        }*//*
        return amount;
    }


    public void makePayment(View view) {

        PayUmoneySdkInitilizer.PaymentParam.Builder builder = new PayUmoneySdkInitilizer.PaymentParam.Builder();

        builder.setAmount(getAmount())
                *//*.setTnxId(getTxnId())
                .setPhone("8802961733")
                .setProductName("product_name")
                .setFirstName("ashish")
                .setEmail("ashish-singh@live.in")
                .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")
                .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")
                .setUdf1("")
                .setUdf2("")
                .setUdf3("")
                .setUdf4("")
                .setUdf5("")
                .setIsDebug(true)
                .setKey("0OX81G")
                .setMerchantId("5269658");// Debug Merchant ID*//*
                .setTnxId(getTxnId())
                .setPhone("8882434664")
                .setProductName("product_name")
                .setFirstName("piyush")
                .setEmail("piyush.jain@payu.in")
                .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")
                .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")
                .setUdf1("")
                .setUdf2("")
                .setUdf3("")
                .setUdf4("")
                .setUdf5("")
                .setIsDebug(true)
                .setKey("dRQuiA")
                .setMerchantId("4928174");// Debug Merchant ID

        PayUmoneySdkInitilizer.PaymentParam paymentParam = builder.build();

            *//*
             server side call required to calculate hash with the help of <salt>
             <salt> is already shared along with merchant <key>
             serverCalculatedHash =sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|<salt>)

             (e.g.)

             sha512(FCstqb|0nf7|10.0|product_name|piyush|piyush.jain@payu.in||||||MBgjYaFG)

             9f1ce50ba8995e970a23c33e665a990e648df8de3baf64a33e19815acd402275617a16041e421cfa10b7532369f5f12725c7fcf69e8d10da64c59087008590fc

            *//*

        // Recommended
        calculateServerSideHashAndInitiatePayment(paymentParam);

           *//*
            testing purpose

            String serverCalculatedHash="9f1ce50ba8995e970a23c33e665a990e648df8de3baf64a33e19815acd402275617a16041e421cfa10b7532369f5f12725c7fcf69e8d10da64c59087008590fc";
            paymentParam.setMerchantHash(serverCalculatedHash);
            PayUmoneySdkInitilizer.startPaymentActivityForResult(this, paymentParam);
            *//*
    }

    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }

    private void calculateServerSideHashAndInitiatePayment(final PayUmoneySdkInitilizer.PaymentParam paymentParam) {

        // Replace your server side hash generator API URL
        String url = "https://test.payumoney.com/payment/op/calculateHashForTest";

        Toast.makeText(this, "Please wait... Generating hash from server ... ", Toast.LENGTH_LONG).show();
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has(SdkConstants.STATUS)) {
                        String status = jsonObject.optString(SdkConstants.STATUS);
                        if (status != null || status.equals("1")) {

                            String hash = jsonObject.getString(SdkConstants.RESULT);
                            Log.i("app_activity", "Server calculated Hash :  " + hash);

                            paymentParam.setMerchantHash(hash);

                            PayUmoneySdkInitilizer.startPaymentActivityForResult(PayuActivity.this, paymentParam);
                        } else {
                            Toast.makeText(PayuActivity.this,
                                    jsonObject.getString(SdkConstants.RESULT),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {
                    Toast.makeText(PayuActivity.this,
                            PayuActivity.this.getString(R.string.connect_to_internet),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PayuActivity.this,
                            error.getMessage(),
                            Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return paymentParam.getParams();
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PayUmoneySdkInitilizer.PAYU_SDK_PAYMENT_REQUEST_CODE) {

            *//*if(data != null && data.hasExtra("result")){
              String responsePayUmoney = data.getStringExtra("result");
                if(SdkHelper.checkForValidString(responsePayUmoney))
                    showDialogMessage(responsePayUmoney);
            } else {
                showDialogMessage("Unable to get Status of Payment");
            }*//*


            if (resultCode == RESULT_OK) {
                Log.i(TAG, "Success - Payment ID : " + data.getStringExtra(SdkConstants.PAYMENT_ID));
                String paymentId = data.getStringExtra(SdkConstants.PAYMENT_ID);
                showDialogMessage("Payment Success Id : " + paymentId);
            } else if (resultCode == RESULT_CANCELED) {
                Log.i(TAG, "failure");
                showDialogMessage("cancelled");
            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_FAILED) {
                Log.i("app_activity", "failure");

                if (data != null) {
                    if (data.getStringExtra(SdkConstants.RESULT).equals("cancel")) {

                    } else {
                        showDialogMessage("failure");
                    }
                }
                //Write your code if there's no result
            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_BACK) {
                Log.i(TAG, "User returned without login");
                showDialogMessage("User returned without login");
            }
        }
    }

    private void showDialogMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(TAG);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }*/

}
