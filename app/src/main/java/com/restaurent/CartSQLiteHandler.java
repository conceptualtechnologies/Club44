package com.restaurent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class CartSQLiteHandler extends SQLiteOpenHelper {

	private static final String TAG = CartSQLiteHandler.class.getSimpleName();

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "android_cart";

	// Login table name
	private static final String TABLE_CART = "cart";

	// Login Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_TOTAL = "total";
	//private static final String KEY_DISCOUNT = "discount";


	public CartSQLiteHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_CART + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_TOTAL + " TEXT" + ")";
		db.execSQL(CREATE_LOGIN_TABLE);

		Log.d(TAG, "Database tables created");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);

		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */
	public void addCart(String total) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TOTAL, total); // Name
		//values.put(KEY_DISCOUNT,discount);//UserID


		// Inserting Row
		long id = db.insert(TABLE_CART, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New cart value inserted into sqlite: " + id);
	}

	/*public void updateCart(String total) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TOTAL, total); // Name
		//values.put(KEY_DISCOUNT,discount);//UserID


		// Inserting Row
		long id = db.update(TABLE_CART,values,"set total=",total);
		db.close(); // Closing database connection

		Log.d(TAG, "New cart value inserted into sqlite: " + id);

			}*/

	/**
	 * Getting user data from database
	 * */
	public HashMap<String, String> getCartDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_CART;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			user.put("total", cursor.getString(1));
			//user.put("discount", cursor.getString(2));

							}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Fetching cart values from Sqlite: " + user.toString());

		return user;
	}

	/**
	 * Re crate database Delete all tables and create them again
	 * */
	public void deleteCart() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_CART, null, null);
		db.close();

		Log.d(TAG, "Deleted all CART value from sqlite");
	}

}
