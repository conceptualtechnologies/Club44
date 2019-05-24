package com.restaurent.activity.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import com.android.volley.RequestQueue;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.payu.magicretry.MainActivity;

import com.restaurent.R;
import com.restaurent.activity.Home;
import com.restaurent.activity.NoInternetConnection;
import com.restaurent.api_call.API_Get;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.constant_class.URL_Class;
import com.restaurent.db_handler.DataBaseHandlerAccount;
import com.restaurent.db_handler.DataBaseHandlerWishList;
import com.restaurent.interfaces.API_Result;
import com.restaurent.json_mechanism.GetJSONData;
import com.restaurent.mechanism.Methods;
import com.restaurent.network_checker.NetworkConnection;
import com.restaurent.shared_preferenc_estring.DataStorage;
import com.restaurent.utils.AccountDataSet;
import com.restaurent.utils.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Login extends AppCompatActivity implements API_Result ,View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {
    private String[] arraySpinner;
    Toolbar toolbar;
    Button login;
    TextView forget_pwd, sign_up,direct_order_number;
    EditText get_email_id, get_password;
    CheckBox chk_box_show_pwd;
    API_Result api_result;
    ProgressDialog dialog;
    Spinner s;
    String as;
    String data,name,email;

    SignInButton signInButton;
    private int RC_SIGN_IN=100;
    GoogleApiClient mGoogleApiClient;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        api_result = this;
        dialog = new ProgressDialog(this);


    dialog = new ProgressDialog(this);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.e("LoginActivity", object.toString());

                                // Application code
                                try {

                                    //Methods.toast(object.getString("email"));
                                    Methods.toast("Successfully Logged in with Facebook");
                                    AccountDataSet accountDataSet=new AccountDataSet();
                                    accountDataSet.setmFirstName(object.getString("name"));

                                    name=object.getString("name");
                                    //email=object.getString("email");
                                    email = object.getString("name");
                                    String profilePicUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                    DataStorage.mStoreSharedPreferenceString(getApplicationContext(), "profile_pic",profilePicUrl);


                                    email=email.substring(0,5);
                                    accountDataSet.setmEmailId(email);
                                   // accountDataSet.setmCustomerId(object.getString("id"));
                                    DataStorage.mStoreSharedPreferenceString(getApplicationContext(), "email_verify",email);
                                    DataBaseHandlerAccount.getInstance(getApplicationContext()).insert_account_detail_new(accountDataSet);

                                    Log.e("mail id",email);
                                    String url[] = {URL_Class.mURL + URL_Class.mURL_registration};
                                    new API_Get().get_method(url, api_result, getPostDataString(), JSON_Names.KEY_POST_TYPE, true, getBaseContext(), "fbSignUpPost");


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                // 01/31/1980 format
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday,picture");
                request.setParameters(parameters);
                request.executeAsync();


               // Methods.toast( acct.getDisplayName());

            }

            @Override
            public void onCancel() {
                Methods.toast("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                Methods.toast("Login attempt failed.");
            }
        });

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    //setSupportActionBar(toolbar);

        //assert getSupportActionBar() != null;
        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);



                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

        get_email_id = (EditText) findViewById(R.id.et_login_enter_email_id);
        get_password = (EditText) findViewById(R.id.et_login_enter_password);
        login = (Button) findViewById(R.id.btn_login_pg_login);
        forget_pwd = (TextView) findViewById(R.id.tv_login_pg_forget_pwd);
        sign_up = (TextView) findViewById(R.id.tv_login_pg_sign_up);
        chk_box_show_pwd = (CheckBox) findViewById(R.id.c_box_tick_login);

        chk_box_show_pwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    get_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    get_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        forget_pwd.setPaintFlags(forget_pwd.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkConnection.connectionChecking(getApplicationContext())) {
                    if (!get_email_id.getText().toString().equals("") && !get_password.getText().toString().equals("")) {

                        switch (Boolean.toString(Methods.isEmailValidator(get_email_id.getText().toString()))) {
                            case "true": {
                                dialog.setMessage("Loading Please Wait ...");
                                dialog.setTitle("Logging in...");
                                dialog.show();
                                dialog.setCancelable(false);
                                HashMap<String, String> postDataParams = new HashMap<>();
                                postDataParams.put(URL_Class.mLogin_UserName, get_email_id.getText().toString());
                                postDataParams.put(URL_Class.mLogin_Password, get_password.getText().toString());


                                String[] data = {URL_Class.mURL + URL_Class.mURL_Login};
                                new API_Get().get_method(data, api_result, getPostDataString(postDataParams), JSON_Names.KEY_POST_TYPE, true, getBaseContext(), "LoginPost");

break;


                            }
                            case "false": {
                                Methods.toast(getResources().getString(R.string.please_enter_valid_email));
                                break;
                            }
                        }

                    } else if (get_email_id.getText().toString().equals("") && get_password.getText().toString().equals("")) {

                        Methods.toast(getResources().getString(R.string.enter_email_id_and_pwd));

                    } else if (!get_email_id.getText().toString().equals("") && get_password.getText().toString().equals("")) {

                        Methods.toast(getResources().getString( R.string.please_enter_your_pwd));

                    } else if (get_email_id.getText().toString().equals("") && !get_password.getText().toString().equals("")) {

                        Methods.toast(getResources().getString(R.string.please_enter_your_email_id));
                    }
                } else {
                    NoConnectionIntentTransfer();
                }

            }
        });
        forget_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkConnection.connectionChecking(getApplicationContext())) {


                    Intent intent_forget_pwd = new Intent(Login.this, ForgetPassword.class);
                    startActivity(intent_forget_pwd);
                } else {
                    NoConnectionIntentTransfer();
                }

            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkConnection.connectionChecking(getApplicationContext())) {
                    if (DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN).equals(JSON_Names.KEY_CURRENT_LOGIN_FROM_SIGN_UP)) {
                        DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN);
                        onBackPressed();
                        finish();
                    } else {

                        DataStorage.mStoreSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN, JSON_Names.KEY_CURRENT_LOGIN_SIGN_UP);
                        Intent intent = new Intent(getApplicationContext(), SignUp.class);
                        startActivity(intent);
                    }
                } else {
                    NoConnectionIntentTransfer();
                }

            }
        });
    }

    public void NoConnectionIntentTransfer() {
        Intent intent = new Intent(Login.this, NoInternetConnection.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    public static Bitmap getFacebookProfilePicture(String userID) throws IOException {
        URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
        Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

        return bitmap;
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
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void result(String[] data, String source) {
        dialog.cancel();
        if(source.equals("LoginPost")) {
            if (data != null) {
                if (data[0] != null) {
                    Response response = GetJSONData.getResponse(data[0]);
                    if (response != null) {
                        if (response.getmStatus() == 200) {
                            Methods.toast(getResources().getString(R.string.login_success));
                            AccountDataSet accountDataSets = GetJSONData.getLoginData(data[0]);
                            if (accountDataSets != null) {
                                DataBaseHandlerAccount.getInstance(getApplicationContext()).insert_account_detail_new(accountDataSets);
                                DataStorage.mStoreSharedPreferenceString(getApplicationContext(), "firstname", accountDataSets.getmFirstName());
                                DataStorage.mStoreSharedPreferenceString(getApplicationContext(), "lastname", accountDataSets.getmLastName());
                                DataStorage.mStoreSharedPreferenceString(getApplicationContext(), "email_verify", accountDataSets.getmEmailId());

                                DataStorage.mStoreSharedPreferenceString(getApplicationContext(), "telephone", accountDataSets.getmCity());
                                DataStorage.mStoreSharedPreferenceString(getApplicationContext(), "city", accountDataSets.getmState());
                                DataStorage.mStoreSharedPreferenceString(getApplicationContext(), "password", accountDataSets.getmPostcode());
                                DataStorage.mStoreSharedPreferenceString(getApplicationContext(), "address", accountDataSets.getmZone_id());
                                onBackPressed();

                                finish();





                                    String url[] = {URL_Class.mURL + URL_Class.mURL_Add_To_WishList};
                                    if (DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN).equals(JSON_Names.KEY_CURRENT_LOGIN_PRODUCT_DETAIL)) {
                                        if (wish_list_builder() != null) {
                                            new API_Get().get_method(url, api_result, wish_list_builder(),
                                                    JSON_Names.KEY_POST_TYPE, true, getBaseContext(), "LoginWishListPostProductList");
                                        }
                                        DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN);
                                    } else if (DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN).equals(JSON_Names.KEY_CURRENT_LOGIN_CATEGORY_LISTING)) {
                                        new API_Get().get_method(url, api_result, wish_list_builder(),
                                                JSON_Names.KEY_POST_TYPE, true, getBaseContext(), "LoginWishListPostCategoryList");
                                        DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN);


                                    }
                                }
                            } else {
                                Methods.toast(response.getmMessage());
                            }
                        }
                    } else {
                        Methods.toast(getResources().getString(R.string.error));
                    }
                }
            } else if(source.equals("SignUpPost")){
                if (data != null) {
                    Response response = GetJSONData.getResponse(data[0]);
                    if (response != null) {
                        if (response.getmStatus() == 200) {
                            HashMap<String, String> postDataParams = new HashMap<>();
                            postDataParams.put(URL_Class.mURL_Email,email);


                            String[] data2 = {URL_Class.mURL + URL_Class.mURL_Google_Login};
                            new API_Get().get_method(data2, api_result, getPostDataString(postDataParams), JSON_Names.KEY_POST_TYPE, true, getBaseContext(), "googleLoginPost");




                            //Methods.toast(getResources().getString(R.string.wish_list_success));
                        } else {
                            HashMap<String, String> postDataParams = new HashMap<>();
                            postDataParams.put(URL_Class.mURL_Email, email);
                           // Methods.toast(response.getmMessage());
                            String[] data2 = {URL_Class.mURL + URL_Class.mURL_Google_Login};

                            new API_Get().get_method(data2, api_result, getPostDataString(postDataParams), JSON_Names.KEY_POST_TYPE, true, getBaseContext(), "googleLoginPost");
                        }
                    } else {
                        Methods.toast(getResources().getString(R.string.error));
                    }
                } else {
                    Methods.toast(getResources().getString(R.string.error));
                }
            }
        else if(source.equals("googleLoginPost")){
            if (data != null) {
                //Log.e("Login Customer Id",data.toString());
                Response response = GetJSONData.getResponse(data[0]);
                if (response != null) {
                    if (response.getmStatus() == 200) {
                        Methods.toast(getResources().getString(R.string.login_success));

                        AccountDataSet accountDataSets = GetJSONData.getLoginData(data[0]);

                        if (accountDataSets != null) {
                            DataBaseHandlerAccount.getInstance(getApplicationContext()).insert_account_detail_new(accountDataSets);
                            Intent intent = new Intent(Login.this, Home.class);
                            startActivity(intent);



                        } //Methods.toast(getResources().getString(R.string.wish_list_success));
                    } else {
                        Methods.toast(response.getmMessage());
                    }
                } else {
                    Methods.toast("Error");
                }
            } else {
                Methods.toast("Error");
            }
        }
        else if(source.equals("fbSignUpPost")){
            if (data != null) {
                Response response = GetJSONData.getResponse(data[0]);
                if (response != null) {
                    if (response.getmStatus() == 200) {
                        HashMap<String, String> postDataParams = new HashMap<>();
                        postDataParams.put(URL_Class.mURL_Email,DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_email_id());


                        String[] data2 = {URL_Class.mURL + URL_Class.mURL_Google_Login};
                        new API_Get().get_method(data2, api_result, getPostDataString(postDataParams), JSON_Names.KEY_POST_TYPE, true, getBaseContext(), "googleLoginPost");




                        //Methods.toast(getResources().getString(R.string.wish_list_success));
                    } else {
                        HashMap<String, String> postDataParams = new HashMap<>();
                        postDataParams.put(URL_Class.mURL_Email,DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_email_id());
                        // Methods.toast(response.getmMessage());
                        String[] data2 = {URL_Class.mURL + URL_Class.mURL_Google_Login};

                        new API_Get().get_method(data2, api_result, getPostDataString(postDataParams), JSON_Names.KEY_POST_TYPE, true, getBaseContext(), "googleLoginPost");
                    }
                } else {
                    Methods.toast(getResources().getString(R.string.error));
                }
            } else {
                Methods.toast(getResources().getString(R.string.error));
            }
        }
    }

    public String wish_list_builder() {
        String mProduct_id;
        String mCustomer_id;
        try {
            if (DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                mProduct_id = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_PRODUCT_ID);
                mCustomer_id = String.valueOf(DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_id());
                DataBaseHandlerWishList.getInstance(getApplicationContext()).add_to_wish_list(mProduct_id, DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_PRODUCT_STRING));

                return URLEncoder.encode(JSON_Names.KEY_PRODUCT_ID, URL_Class.mConvertType)
                        + URL_Class.mEqual_Symbol
                        + URLEncoder.encode(mProduct_id, URL_Class.mConvertType)
                        + URL_Class.mAnd_Symbol
                        + URLEncoder.encode(JSON_Names.KEY_USER_ID, URL_Class.mConvertType)
                        + URL_Class.mEqual_Symbol
                        + URLEncoder.encode(mCustomer_id, URL_Class.mConvertType);
            }
            return null;

        } catch (Exception e) {
            return null;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Methods.toast("Successfully Logged in with Google");
            DataStorage.mStoreSharedPreferenceString(getApplicationContext(), "name", acct.getDisplayName());
            AccountDataSet accountDataSet=new AccountDataSet();
            accountDataSet.setmFirstName(acct.getDisplayName());
            accountDataSet.setmEmailId(acct.getEmail());
            accountDataSet.setmCustomerId(acct.getId());
            String googlepic=(String.valueOf(acct.getPhotoUrl()));
            DataStorage.mStoreSharedPreferenceString(getApplicationContext(), "profile_pic",googlepic);
            name=acct.getDisplayName();
            email=acct.getEmail();
            DataStorage.mStoreSharedPreferenceString(getApplicationContext(), "email_verify",email);
            DataBaseHandlerAccount.getInstance(getApplicationContext()).insert_account_detail_new(accountDataSet);
            String url[] = {URL_Class.mURL + URL_Class.mURL_registration};
            new API_Get().get_method(url, api_result, getPostDataString(), JSON_Names.KEY_POST_TYPE, true, getBaseContext(), "SignUpPost");

            //Methods.toast( acct.getDisplayName());



        } else {
            // Signed out, show unauthenticated UI.

        }
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.sign_in_button:
        signIn();

        break;
    }
}
    private String getPostDataString() {
        try {
            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("first_name", name);
            postDataParams.put("email", email);





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
}
