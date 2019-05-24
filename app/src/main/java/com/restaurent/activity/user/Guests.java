package com.restaurent.activity.user;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.restaurent.R;
import com.restaurent.interfaces.API_Result;
import com.restaurent.mechanism.Methods;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Guests extends AppCompatActivity implements View.OnClickListener {

    private List<style_list> listItemsList = new ArrayList<style_list>();
    private RecyclerView mRecyclerView;
    private String[] arraySpinner;
    SharedPreferences pref;
    SharedPreferences.Editor edt;
    GridLayoutManager linearLayoutManager;
    RequestQueue queue;
    Button register;
    Button take_photo;
    EditText tv_follow_up, tv_follow_up_date;
    String image;
    private Boolean upflag = false;
    private Uri selectedImage = null;
    private Bitmap bitmap, bitmapRotate;
    private ProgressDialog pDialog;
    String imagepath = "";
    String fname;
    private static final int SELECT_PICTURE = 100;
    private Uri filePath;
    File file;
    EditText get_names, get_emails, get_p_no;
TextView get_dates, get_time,get_table_number,get_tia;
    ImageView iv_document_image;
    String Name, Email, P_no, Time, Tia, Table_number, Date, Saloon, Photo;
    Spinner Tablenumbers, ss,sss;
    String data;
    String as,ass;
    private int mYear, mMonth, mDay;
    private TimePicker timePicker1;
    ProgressDialog progressDialog;
    private static int RESULT_LOAD = 1;
    String img_Decodable_Str;
    Button gallery, submit;
    ImageView image1;
    public static TextView tvDisplayDate;
    private int myear;
    private int mmonth;
    private int mday;
    DatePickerDialog _date;
    static final int DATE_DIALOG_ID = 999;
    String s,table_id,date,time,tia;
    int j;
    API_Result api_result;
    private int daysOfMonth = 31;
    LayoutInflater inflater;
    private DatePickerDialog toDatePickerDialog;
    private NumberPicker monthPicker;
    private NumberPicker yearPicker;
    private NumberPicker dayPicker;

    private Calendar cal = Calendar.getInstance();

    public static final String MONTH_KEY = "monthValue";
    public static final String DAY_KEY = "dayValue";
    public static final String YEAR_KEY = "yearValue";

    int monthVal = -1, dayVal = -1, yearVal = -1;
    private DatePickerDialog.OnDateSetListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_guests);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if(bundle.getString("table_number")!=null){
                s=bundle.getString("table_number");
            }
            if(bundle.getString("table_id")!=null){
                table_id=bundle.getString("table_id");
            }

            }

//        Log.d("table_number", s);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Guests.this);


        get_names = (EditText) findViewById(R.id.et_name);
        get_emails = (EditText) findViewById(R.id.et_email);
        get_p_no = (EditText) findViewById(R.id.et_phone);



        get_table_number = (TextView) findViewById(R.id.tv_tablenumber);
        get_table_number.setText(s);
        register = (Button) findViewById(R.id.btn_confirm);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(v);
            }
        });

        // submit = (Button) findViewById(R.id.btn_confirm);
        //  occation = (Spinner) findViewById(R.id.occation);

        get_dates = (TextView) findViewById(R.id.tvdate);
        get_dates.setText(DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"date"));
        get_tia = (TextView) findViewById(R.id.tvtia);
        get_tia.setText(DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"from"));
        get_time = (TextView) findViewById(R.id.tvtime);
    get_time.setText(DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"to"));}






public void showpd() {
        if (progressDialog == null) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering Please Wait......");
            progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
        }
        }

public void hidedp() {

        if (progressDialog != null) {
        progressDialog.dismiss();
        progressDialog = null;
        }


        }

public void register(View v) {


        Name = get_names.getText().toString().trim();
        Email = get_emails.getText().toString().trim();
        P_no = get_p_no.getText().toString().trim();
   s = get_table_number.getText().toString().trim();
    Date = get_dates.getText().toString().trim();
    Time = get_time.getText().toString().trim();
    Tia = get_tia.getText().toString().trim();







    new Background().execute(Name, Email,table_id ,P_no,s,Date,Time,Tia);
        }

    @Override
    public void onClick(View v) {

    }


    class Background extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... params) {
        String name = params[0];
        String email = params[1];

        String p_no = params[2];
        String date = params[3];
        String table_id2 = params[4];
        String table_number = params[5];


        String time = params[6];
        String tia = params[7];




        data = null;
        int tmp;


        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://club44.in/booktables.php?");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("name", Name));
            nameValuePairs.add(new BasicNameValuePair("email", Email));
            nameValuePairs.add(new BasicNameValuePair("p_no", P_no));
            nameValuePairs.add(new BasicNameValuePair("date",Date));
            nameValuePairs.add(new BasicNameValuePair("table_id",table_id));
            nameValuePairs.add(new BasicNameValuePair("table_number", s));
            nameValuePairs.add(new BasicNameValuePair("time", Time));
            nameValuePairs.add(new BasicNameValuePair("tia", Tia));






            //  nameValuePairs.add(new BasicNameValuePair("mobile",Mobile));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse httpRes = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpRes.getEntity();
            data = EntityUtils.toString(httpEntity);

            Log.d("Data", data);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
          //  e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;

    }

    @Override
    protected void onPostExecute(String s) {
        hidedp();
        Log.d("Response", s);
        try {
            JSONObject jsonObject=new JSONObject(s);
            String response=jsonObject.getString("data");
            if(response.contentEquals("Table  Booked")){
                Methods.toast("Your Table Booked");
            }
            if(response.contentEquals("Table Already Booked")){
                Methods.toast("Table Already Booked");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(Guests.this, "You Register", Toast.LENGTH_LONG).show();


    }


}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }}


  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            switch (requestCode) {
                case 101:
                    if (resultCode == Activity.RESULT_OK) {
                        if (data != null) {
                            selectedImage = data.getData();// the uri of the image taken

                            if (String.valueOf((Bitmap) data.getExtras().get("data")).equals("null")) {
                                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                            } else {
                                bitmap = (Bitmap) data.getExtras().get("data");
                            }
                            if (Float.valueOf(getImageOrientation()) >= 0) {
                                bitmapRotate = rotateImage(bitmap, Float.valueOf(getImageOrientation()));
                            } else {
                                bitmapRotate = bitmap;
                                bitmap.recycle();
                            }

                            iv_document_image.setVisibility(View.VISIBLE);
                            iv_document_image.setImageBitmap(bitmapRotate);

//                            Saving image to mobile internal memory for sometime
                            String root = getApplicationContext().getFilesDir().toString();
                            File myDir = new File(root + "/androidlift");
                            myDir.mkdirs();

                            Random generator = new Random();
                            int n = 10000;
                            n = generator.nextInt(n);

//                            Give the file name that u want
                            fname = "null" + n + ".jpg";

                            imagepath = root + "/androidlift/" + fname;
                            file = new File(myDir, fname);
                            upflag = true;
                        }
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

