package com.restaurent.api_call;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.restaurent.constant_class.JSON_Names;
import com.restaurent.constant_class.URL_Class;
import com.restaurent.interfaces.API_Result;
import com.restaurent.mechanism.Methods;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class API_Get {
    private API_Result mResult;
    private String post_data;
    private Boolean progress_bar;
    private Context context;
    private String source;

    //Determine the request whether get or post
    public void get_method(String[] url, API_Result result, String data, String type, Boolean show, Context context, String source) {

        this.mResult = result;
        if(data!=null) {
            this.post_data = data;
        }
        this.context=context;
        this.progress_bar=show;
        this.source=source;
        if (type.equals(JSON_Names.KEY_GET_TYPE)) {
            new AsyncTaskGet().execute(url);
        } else {
            new AsyncTaskPost().execute(url);
        }
    }

    private class AsyncTaskGet extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String[] doInBackground(String... urls) {
            String mData[] = new String[urls.length];
            try {
                for (int i = 0; i < urls.length; i++) {
                    URL url = new URL(urls[i]);
                    Log.e("Url post",urls[i]);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod(JSON_Names.KEY_GET_TYPE);
                    urlConnection.setReadTimeout(15000);
                    if (urlConnection.getInputStream() != null) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        mData[i] = Methods.convertInputStreamToString(br);
                        Log.e("Response",mData[i]);
                    }
                }
                return mData;
            } catch (Exception e) {
                Log.e("Error",e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] data) {

            if (data != null) {
                mResult.result(data,source);
            }else{
                mResult.result(null,source);
            }
        }
    }

    private class AsyncTaskPost extends AsyncTask<String, Void, String[]> {


        @Override
        protected String[] doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                //Log.e("Url post",params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(JSON_Names.KEY_POST_TYPE);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setReadTimeout(15000);
                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, URL_Class.mConvertType), post_data.length());
                writer.write(post_data);
                //Log.e("Request",post_data);
                writer.close();
                outputStream.close();

                String response[] = new String[1];
                BufferedReader br;
                if (connection.getResponseCode() == 200) {
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                }
                response[0] = Methods.convertInputStreamToString(br);
                //Log.e("Response",response[0]);
                return response;
            } catch (Exception e) {
                //Log.e("Error",e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] s) {
            if (s != null) {
                mResult.result(s,source);
            }else{
                mResult.result(null,source);
            }
        }
    }

}
