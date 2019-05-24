package com.restaurent.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import com.restaurent.R;
import com.restaurent.activity.user.Login;
import com.restaurent.api_call.API_Get;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.constant_class.URL_Class;
import com.restaurent.interfaces.API_Result;
import com.restaurent.json_mechanism.GetJSONData;
import com.restaurent.mechanism.Methods;
import com.restaurent.network_checker.NetworkConnection;
import com.restaurent.shared_preferenc_estring.DataStorage;
import com.restaurent.utils.Response;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Create_Product_Detail extends AppCompatActivity implements API_Result,View.OnClickListener {
    public static Integer iStateId;
    public static String tempCString;

    public static String currentState;
    public static String tempSStateCheck;
    public String mEmailValidCheck;
String image;
    private Boolean upflag = false;
    private Uri selectedImage = null;
    private Bitmap bitmap, bitmapRotate;
    private ProgressDialog pDialog;
    String imagepath = "";
    String fname;
    File file;
    int int2;
    public Integer iCountryId;
    TextView go_login_page;
    Button submit, camera;
    ImageView iv_name, iv_pwd, iv_email, iv_address, iv_mobile, iv_department,iv_document_image;
    EditText get_name, get_title, get_product, get_adhar,get_mobile,get_email;
    EditText get_city;
    int report_no_addition;

    ProgressDialog progressDialog;
    TextView error_name, error_title, error_product, error_adhar;
    TextView error_city,get_created_by;
    TextView error_pwd;
    Spinner departments;
    String dpt_items;
private  int count=0;
    API_Result api_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__product__detail);
        get_city = (EditText) findViewById(R.id.et_city);
        get_name = (EditText) findViewById(R.id.et_name);
        get_title = (EditText) findViewById(R.id.et_title);

        get_product = (EditText) findViewById(R.id.et_product);
        get_adhar = (EditText) findViewById(R.id.et_adhar);

        get_mobile = (EditText) findViewById(R.id.et_mobile);
        get_email = (EditText) findViewById(R.id.et_email);
        //get_department = (EditText) findViewById(R.id.et_problem_department);

        iv_name = (ImageView) findViewById(R.id.iv_signup_name);
        iv_email = (ImageView) findViewById(R.id.iv_signup_email);
        iv_pwd = (ImageView) findViewById(R.id.iv_signup_password);

        iv_address = (ImageView) findViewById(R.id.iv_signup_address);
        iv_mobile = (ImageView) findViewById(R.id.iv_signup_phone);
        iv_document_image = (ImageView) findViewById(R.id.iv_document);
       /* Picasso.with(this).load(R.drawable.name).into(iv_name);
        Picasso.with(this).load(R.drawable.email).into(iv_email);
        Picasso.with(this).load(R.drawable.password).into(iv_pwd);
        Picasso.with(this).load(R.drawable.mobile).into(iv_mobile);
//        Picasso.with(this).load(R.drawable.departments).into(iv_department);
        Picasso.with(this).load(R.drawable.address).into(iv_address);*/


        error_name = (TextView) findViewById(R.id.tv_error_s1_name);
        error_name.setVisibility(View.GONE);


        error_title = (TextView) findViewById(R.id.tv_error_s1_title);
        error_title.setVisibility(View.GONE);

        error_product = (TextView) findViewById(R.id.tv_error_s1_product);
        error_product.setVisibility(View.GONE);
        error_adhar = (TextView) findViewById(R.id.tv_error_s1_adhar);
        error_adhar.setVisibility(View.GONE);
        error_city = (TextView) findViewById(R.id.tv_error_s2_problem_department);
        error_city.setVisibility(View.GONE);


        submit = (Button) findViewById(R.id.btn_create_report_file);
        camera = (Button) findViewById(R.id.btn_document);
        camera.setOnClickListener(this);

        // add click listener to Button "POST"
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!get_name.getText().toString().equals("")
                        && (get_name.getText().toString().length() >= 1)
                        && (get_name.getText().toString().length() <= 32)


                        && !get_title.getText().toString().equals("")
                        && (get_title.getText().toString().length() >= 3)
                        && (get_title.getText().toString().length() <= 32)

                        && !get_product.getText().toString().equals("")
                        && (get_product.getText().toString().length() >= 2)
                        && (get_product.getText().toString().length() <= 128)

                        && !get_mobile.getText().toString().equals("")
                        && (get_mobile.getText().toString().length() >= 2)
                        && (get_mobile.getText().toString().length() <= 128))

                    /*    && !get_department.getText().toString().equals("")
                        && (get_department.getText().toString().length() >= 2)
                        && (get_department.getText().toString().length() <= 128))*/


                {
                    if (NetworkConnection.connectionChecking(getApplicationContext())) {
                        if (getPostDataString() != null) {
                            progressDialog.setMessage("Loading Please Wait ...");
                            progressDialog.setTitle("Creating File...");
                            progressDialog.show();
                            progressDialog.setCanceledOnTouchOutside(true);
                            if (get_city.getText().toString().equals("Police")) {
                                String url[] = {URL_Class.mURL + URL_Class.mURL_Country};
                                new API_Get().get_method(url, api_result, getPostDataString(), JSON_Names.KEY_POST_TYPE, true, getBaseContext(), "CreatePolicePost");
                            }
                        }
                    } else {
                        no_connection_transfer();
                    }

                } else {
                    Methods.toast(getResources().getString(R.string.fill_required_field));
                }
                if (get_name.getText().toString().equals("")) {
                    error_name.setVisibility(View.VISIBLE);
                } else {
                    error_name.setVisibility(View.GONE);
                }
                if ((get_name.getText().toString().length() >= 1) && (get_name.getText().toString().length() <= 32)) {
                    error_name.setVisibility(View.GONE);
                } else {
                    error_name.setVisibility(View.VISIBLE);
                }


                if (get_title.getText().toString().equals("")) {
                    error_title.setVisibility(View.VISIBLE);
                } else {
                    error_title.setVisibility(View.GONE);
                }
                if ((get_title.getText().toString().length() >= 3) && (get_title.getText().toString().length() <= 32)) {
                    error_title.setVisibility(View.GONE);
                } else {
                    error_title.setVisibility(View.VISIBLE);
                }

                if (get_product.getText().toString().equals("")) {
                    error_product.setVisibility(View.VISIBLE);
                } else {
                    error_product.setVisibility(View.GONE);
                }
                if ((get_product.getText().toString().length() >= 3) && (get_product.getText().toString().length() <= 32)) {
                    error_product.setVisibility(View.GONE);
                } else {
                    error_product.setVisibility(View.VISIBLE);
                }
                if (get_mobile.getText().toString().equals("")) {
                    error_product.setVisibility(View.VISIBLE);
                } else {
                    error_product.setVisibility(View.GONE);
                }
                if ((get_mobile.getText().toString().length() >= 3) && (get_mobile.getText().toString().length() <= 128)) {
                    error_product.setVisibility(View.GONE);
                } else {
                    error_product.setVisibility(View.VISIBLE);
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
            postDataParams.put("reportno", get_name.getText().toString());
            postDataParams.put("title", get_title.getText().toString());
            postDataParams.put("reference", get_product.getText().toString());
            postDataParams.put("status", get_mobile.getText().toString());
            postDataParams.put("department", get_city.getText().toString());
            postDataParams.put("created_by", get_created_by.getText().toString());
            postDataParams.put("phone", get_mobile.getText().toString());
            postDataParams.put("email", get_email.getText().toString());
            image=getStringImage(bitmap);
            postDataParams.put("image", image);
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
        if (source.equals("CreatePolicePost")) {
            progressDialog.cancel();


            if (data != null) {
                Response response = GetJSONData.getResponse(data[0]);
                if (response != null) {
                    if (response.getmStatus() == 200) {
                        Methods.toast("Report is Created");
                        get_name.setText("");
                        get_title.setText("");
                        get_product.setText("");
                        get_mobile.setText("");
                        get_email.setText("");

                        if (DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN).equals(JSON_Names.KEY_CURRENT_LOGIN_SIGN_UP)) {
                            DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN);

                        } else {
                            Methods.toast(getResources().getString(R.string.reg_error));
                        }
                    } else {
                        Methods.toast(response.getmMessage());
                    }
                }
            } else {
                Methods.toast(getResources().getString(R.string.reg_error));
            }

        }

    }
    @Override
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
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

    //    In some mobiles image will get rotate so to correting that this code will help us
    private int getImageOrientation() {
        final String[] imageColumns = {MediaStore.Images.Media._ID, MediaStore.Images.ImageColumns.ORIENTATION};
        final String imageOrderBy = MediaStore.Images.Media._ID + " DESC";
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                imageColumns, null, null, imageOrderBy);

        if (cursor.moveToFirst()) {
            int orientation = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));
            System.out.println("orientation===" + orientation);
            cursor.close();
            return orientation;
        } else {
            return 0;
        }
    }

    //    Saving file to the mobile internal memory
    private void saveFile(Bitmap sourceUri, File destination) {
        if (destination.exists()) destination.delete();
        try {
            FileOutputStream out = new FileOutputStream(destination);
            sourceUri.compress(Bitmap.CompressFormat.JPEG, 200, out);
            out.flush();
            out.close();
          /*  if (cd.isConnectingToInternet()) {
                new DoFileUpload().execute();
            } else {
                Toast.makeText(MainActivity.this, "No Internet Connection..", Toast.LENGTH_LONG).show();
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_document:
                Intent cameraintent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraintent, 101);

        }
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}

