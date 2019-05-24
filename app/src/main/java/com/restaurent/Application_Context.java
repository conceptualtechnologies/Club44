package com.restaurent;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Application_Context extends Application {
    private static Application_Context mInstance;
    private RequestQueue mRequestQueue;

    public static final String TAG = Application_Context.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
    }

    public static synchronized Application_Context getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
    public static Context getAppContext(){
        return mInstance.getApplicationContext();
    }
}
