package com.restaurent.activity;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Lincoln on 07/01/16.
 */
public class MyPreferenceManager {

    private String TAG = MyPreferenceManager.class.getSimpleName();



    // Shared Preferences
    SharedPreferences pref;

    //SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences("IDvalue",0);

    // Editor for Shared preferences
    SharedPreferences.Editor editor;


    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE1 = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "rg_hospital";

    // All Shared Preferences Keys
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_NOTIFICATIONS = "notifications";

    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE1);
        editor = pref.edit();
    }




    public void addNotification(String notification) {

        // get old notifications
        String oldNotifications = getNotifications();

        if (oldNotifications != null) {
           // oldNotifications += "|" + notification;
            oldNotifications = notification;
        } else {
            oldNotifications = notification;

        }

        editor.putString(KEY_NOTIFICATIONS, oldNotifications);
        editor.commit();
    }

    public String getNotifications() {
        return pref.getString(KEY_NOTIFICATIONS, null);
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }
}
