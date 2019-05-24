package com.restaurent.db_handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.restaurent.constant_class.JSON_Names;
import com.restaurent.json_mechanism.GetJSONData;
import com.restaurent.utils.AccountDataSet;


public class DataBaseHandlerAccount extends SQLiteOpenHelper {

    final static String name = "db_account";
    final static int version = 1;
    final static String TABLE_NAME_ACCOUNT = "account";
    final static String CUSTOMER_ID = "customer_id";
    final static String CUSTOMER_DETAILS = "customer_account_string";
    final static String DROP_TABLE_ACCOUNT = "DROP TABLE IF EXISTS " + TABLE_NAME_ACCOUNT;
    final static String DELETE_TABLE_ACCOUNT = "DELETE FROM " + TABLE_NAME_ACCOUNT;
    final static String SELECT_VALUE_SELECT = "select ";
    final static String SELECT_VALUE_FROM = "from ";
    Cursor cursor;

    final static String CUSTOMER_FIRST_NAME = "customer_first_name";
    final static String CUSTOMER_LAST_NAME = "customer_last_name";
    final static String CUSTOMER_EMAIL_ID = "customer_e_mail";
    final static String CUSTOMER_TELEPHONE = "customer_phone_number";
    final static String CUSTOMER_FAX = "customer_fax";
    final static String CREATE_ACCOUNT_NEW = "create table " + TABLE_NAME_ACCOUNT + "(" + CUSTOMER_ID + " integer primary key," + CUSTOMER_FIRST_NAME + " text,"+ CUSTOMER_LAST_NAME + " text,"+ CUSTOMER_EMAIL_ID + " text,"+ CUSTOMER_TELEPHONE + " text,"+ CUSTOMER_FAX + " text," + CUSTOMER_DETAILS + " text);";

    private static DataBaseHandlerAccount sInstance;

    public static synchronized DataBaseHandlerAccount getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DataBaseHandlerAccount(context.getApplicationContext());
        }
        return sInstance;
    }

    private DataBaseHandlerAccount(Context context) {
        super(context, name, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ACCOUNT_NEW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_ACCOUNT);
        onCreate(db);
    }

    public Boolean insert_account_detail_new(AccountDataSet accountDataSets) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CUSTOMER_ID, accountDataSets.getmCustomerId());
        contentValues.put(CUSTOMER_FIRST_NAME, accountDataSets.getmFirstName());
        contentValues.put(CUSTOMER_LAST_NAME, accountDataSets.getmLastName());
        contentValues.put(CUSTOMER_EMAIL_ID, accountDataSets.getmEmailId());
        contentValues.put(CUSTOMER_TELEPHONE, accountDataSets.getmTelePhone());
        contentValues.put(CUSTOMER_FAX, accountDataSets.getmFax());
        contentValues.put(CUSTOMER_DETAILS, accountDataSets.getmAccountString());
        db.insert(TABLE_NAME_ACCOUNT, null, contentValues);
        return true;
    }

    public void delete_account_detail() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(DELETE_TABLE_ACCOUNT);
    }

    public boolean check_login() {
        String query = SELECT_VALUE_SELECT + CUSTOMER_ID + " " + SELECT_VALUE_FROM + TABLE_NAME_ACCOUNT;
        String result= JSON_Names.KEY_NO_DATA;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery(query, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                result = cursor.getString(cursor.getColumnIndex(CUSTOMER_ID));
            }
            cursor.close();
        }
        return (!result.equals(JSON_Names.KEY_NO_DATA));
    }

    public String get_account_detail() {
        String select = SELECT_VALUE_SELECT + CUSTOMER_DETAILS + " " + SELECT_VALUE_FROM + TABLE_NAME_ACCOUNT;
        String data=JSON_Names.KEY_NO_DATA;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery(select, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
              data= cursor.getString(cursor.getColumnIndex(CUSTOMER_DETAILS));
            }
            cursor.close();
        }
        if(!data.equals(JSON_Names.KEY_NO_DATA)) {
            return data;
        }else {
            return null;
        }

    }

    public String getAddressId(){
        String account_detail=get_account_detail();
        AccountDataSet dataSet=GetJSONData.getLoginData(account_detail);
        if(dataSet!=null){
            return dataSet.getmAddress_Id();
        }else {
            return "0";
        }
    }

    public int get_customer_id() {
        String query = SELECT_VALUE_SELECT + CUSTOMER_ID + " " + SELECT_VALUE_FROM + TABLE_NAME_ACCOUNT ;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery(query, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                int result = cursor.getInt(cursor.getColumnIndex(CUSTOMER_ID));
                if (result != 0) {
                    return result;
                }
            }
            cursor.close();
        }
        return 0;
    }

    public String get_customer_name(){
        String query = SELECT_VALUE_SELECT + CUSTOMER_FIRST_NAME + " " + SELECT_VALUE_FROM + TABLE_NAME_ACCOUNT ;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery(query, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String result = cursor.getString(cursor.getColumnIndex(CUSTOMER_FIRST_NAME));
                if (result!=null) {
                    return result;
                }
            }
            cursor.close();
        }
        return JSON_Names.KEY_NO_DATA;
    }
    public String get_customer_mobile_no(){
        String query = SELECT_VALUE_SELECT + CUSTOMER_TELEPHONE + " " + SELECT_VALUE_FROM + TABLE_NAME_ACCOUNT ;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery(query, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String result = cursor.getString(cursor.getColumnIndex(CUSTOMER_TELEPHONE));
                if (result!=null) {
                    return result;
                }
            }
            cursor.close();
        }
        return JSON_Names.KEY_NO_DATA;
    }
    public String get_customer_email_id(){
        String query = SELECT_VALUE_SELECT + CUSTOMER_EMAIL_ID + " " + SELECT_VALUE_FROM + TABLE_NAME_ACCOUNT ;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery(query, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String result = cursor.getString(cursor.getColumnIndex(CUSTOMER_EMAIL_ID));
                if (result!=null) {
                    return result;
                }
            }
            cursor.close();
        }
        return JSON_Names.KEY_NO_DATA;
    }
    public void update_account_detail(AccountDataSet accountDataSets){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CUSTOMER_FIRST_NAME, accountDataSets.getmFirstName());
        contentValues.put(CUSTOMER_LAST_NAME, accountDataSets.getmLastName());
        contentValues.put(CUSTOMER_EMAIL_ID, accountDataSets.getmEmailId());
        contentValues.put(CUSTOMER_TELEPHONE, accountDataSets.getmTelePhone());
        db.update(TABLE_NAME_ACCOUNT, contentValues, CUSTOMER_ID+"="+get_customer_id(),null);
    }

    public void update_account_detail_name(AccountDataSet accountDataSets){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CUSTOMER_FIRST_NAME, accountDataSets.getmFirstName());
        contentValues.put(CUSTOMER_LAST_NAME, accountDataSets.getmLastName());
        db.update(TABLE_NAME_ACCOUNT, contentValues, CUSTOMER_ID+"="+get_customer_id(),null);
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}
