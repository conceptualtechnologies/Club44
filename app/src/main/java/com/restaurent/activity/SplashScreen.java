package com.restaurent.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.restaurent.R;
import com.restaurent.activity.user.Open;
import com.restaurent.api_call.API_Get;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.constant_class.URL_Class;
import com.restaurent.interfaces.API_Result;
import com.restaurent.mechanism.Methods;
import com.restaurent.network_checker.NetworkConnection;
import com.restaurent.shared_preferenc_estring.DataStorage;


public class SplashScreen extends Activity implements API_Result {

    static int SPLASH_TIME_OUT = 500;
    ProgressBar progressBar;
    API_Result api_resultGet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);
        clear();
        api_resultGet=this;

//        DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_NAVIGATION_DATA);
       // DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_FROM_SEARCH);
        if (NetworkConnection.connectionChecking(getApplicationContext())) {
            if (NetworkConnection.connectionType(getApplicationContext())) { //This statement will detect network type and it does not allow 2G network.
                String list[] = {URL_Class.mURL + URL_Class.mURL_MainCategory +
                        Methods.current_language(), URL_Class.mURL + URL_Class.mBanner + Methods.current_language()};
                progressBar.setVisibility(View.VISIBLE);
                new API_Get().get_method(list, api_resultGet, "", JSON_Names.KEY_GET_TYPE,true,getBaseContext(),"SplashScreen");
            } else {
                two_g_transfer();
            }
        } else {
            Intent intent = new Intent(SplashScreen.this, NoInternetConnection.class);
            startActivity(intent);
            finish();
        }
    }

    public void clear() {
        //DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_PRODUCT_STRING);
       // DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN);
    }

    public void two_g_transfer() {
        Intent intent = new Intent(SplashScreen.this, Network_Error.class);
        startActivity(intent);
        finish();
    }

    public void success() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, Open.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void result(String[] data, String source) {
        progressBar.setVisibility(View.INVISIBLE);
        if(source.equals("SplashScreen")) {
            if (data != null) {
                if (data.length == 2) {
                    DataStorage.mStoreSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_NAVIGATION_DATA, data[0]);
                    DataStorage.mStoreSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_IMAGE, data[1]);
                    success();
                } else {
                    two_g_transfer();
                }
            } else {
                two_g_transfer();
            }
        }
    }
}
