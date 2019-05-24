package com.restaurent.activity.category;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import com.restaurent.R;
import com.restaurent.activity.NoInternetConnection;
import com.restaurent.activity.user.CompanyProfile;
import com.restaurent.activity.user.Company_Aims;
import com.restaurent.adapter.Filter_Home_Adapter;
import com.restaurent.api_call.API_Get;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.constant_class.URL_Class;
import com.restaurent.interfaces.API_Result;
import com.restaurent.json_mechanism.GetJSONData;
import com.restaurent.mechanism.Methods;
import com.restaurent.network_checker.NetworkConnection;
import com.restaurent.utils.WholeFilterDataSet;

import java.util.ArrayList;

public class Filter_Activity extends AppCompatActivity implements View.OnClickListener,API_Result {

    public static int mCategory_Id;
    ArrayList<WholeFilterDataSet> mList = new ArrayList<>();
    RecyclerView mHomeRecyclerView;
    Button mFilterButton, mFilterCancelButton;
    Toolbar toolbar;
    API_Result api_result;
    ProgressDialog progressDialog;
    String title,image_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        api_result=this;
        progressDialog = new ProgressDialog(this);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(JSON_Names.KEY_TRUE_BOOLEAN);

        mFilterButton = (Button) findViewById(R.id.filter_recycler_view_btn);
        mFilterCancelButton = (Button) findViewById(R.id.filter_recycler_view_btn_cancel);
        mHomeRecyclerView = (RecyclerView) findViewById(R.id.filter_recycler_view);
        mHomeRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mFilterCancelButton.setOnClickListener(this);
        mFilterButton.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getInt(JSON_Names.KEY_CATEGORY_ID_SHARED_PREFERENCE) != 0) {
                mCategory_Id = bundle.getInt(JSON_Names.KEY_CATEGORY_ID_SHARED_PREFERENCE);
            }
            if (bundle.getString(JSON_Names.KEY_TITLE_SHARED_PREFERENCE) != null) {
                title = bundle.getString(JSON_Names.KEY_TITLE_SHARED_PREFERENCE);
            }
            if (bundle.getString(JSON_Names.KEY_IMAGE_URL_SHARED_PREFERENCE) != null) {
                image_url = bundle.getString(JSON_Names.KEY_IMAGE_URL_SHARED_PREFERENCE);
            }
        }
        if (NetworkConnection.connectionChecking(getApplicationContext())) {
            progressDialog.show();
            progressDialog.setCancelable(false);
            String url[] = {URL_Class.mURL + URL_Class.mURL_GetFilter_For_Category + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id};
            new API_Get().get_method(url,api_result,"",JSON_Names.KEY_GET_TYPE,true,getBaseContext(),"Filter");
        } else {
            Intent intent = new Intent(Filter_Activity.this, NoInternetConnection.class);
            startActivity(intent);
            finish();
        }
    }

    public void setting(String data) {
        if (!data.equals(JSON_Names.KEY_NO_DATA)) {
            mList = GetJSONData.getFilterData(data);
            if (mList != null) {
                mHomeRecyclerView.setAdapter(new Filter_Home_Adapter(getApplicationContext(), mList));
            } else {
                mHomeRecyclerView.setAdapter(new Filter_Home_Adapter(getApplicationContext(), null));
            }
        } else {
            mHomeRecyclerView.setAdapter(new Filter_Home_Adapter(getApplicationContext(), null));
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.filter_recycler_view_btn) {
            if (mList == null) {
                Methods.toast(getResources().getString(  R.string.empty_filter));
            }
            intentTransfer();
        } else if (v.getId() == R.id.filter_recycler_view_btn_cancel) {
            intentTransfer();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return JSON_Names.KEY_TRUE_BOOLEAN;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            intentTransfer();
        }

        return super.onOptionsItemSelected(item);
    }

    public void intentTransfer() {
        Intent intent = new Intent(Filter_Activity.this, Category_Details.class);
        intent.putExtra(JSON_Names.KEY_CATEGORY_ID_SHARED_PREFERENCE,mCategory_Id);
        intent.putExtra(JSON_Names.KEY_TYPE_SHARED_PREFERENCE, R.string.filter);
        intent.putExtra(JSON_Names.KEY_IMAGE_URL_SHARED_PREFERENCE, image_url);
        intent.putExtra(JSON_Names.KEY_TITLE_SHARED_PREFERENCE, title);
        startActivity(intent);
        finish();
    }

    @Override
    public void result(String[] data, String source) {
        progressDialog.cancel();
        if(source.equals("Filter")) {
            if (data != null) {
                setting(data[0]);
            }
        }
    }
}
