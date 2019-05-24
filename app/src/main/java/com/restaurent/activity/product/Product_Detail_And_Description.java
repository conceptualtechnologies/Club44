package com.restaurent.activity.product;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.restaurent.R;
import com.restaurent.adapter.Fragment_Page_Adapter;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.json_mechanism.GetJSONData;
import com.restaurent.utils.ProductDataSet;

import java.util.HashMap;

public class Product_Detail_And_Description extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    Fragment_Page_Adapter fragment_page_adapter;

    String mProduct_String;
    ProductDataSet mProductDataSet = new ProductDataSet();
    HashMap<String, Object> mDataSet = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product__detail__and__description);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tabLayout = (TabLayout) findViewById(R.id.product_detail_and_description_tab);
        viewPager = (ViewPager) findViewById(R.id.product_detail_and_description_viewpager);

        if (getIntent().getExtras().getString(JSON_Names.KEY_PRODUCT_STRING) != null) {
            mProduct_String = getIntent().getExtras().getString(JSON_Names.KEY_PRODUCT_STRING);
            //mProduct_String = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_PRODUCT_STRING);
            mDataSet = GetJSONData.getSeparateProductDetail(mProduct_String);
            if (mDataSet != null) {
                mProductDataSet = (ProductDataSet) mDataSet.get(JSON_Names.KEY_PD_PRODUCT);
            }

            fragment_page_adapter = new Fragment_Page_Adapter(this.getSupportFragmentManager(), mProduct_String/*, mProductDataSet.getDescription()*/);
            viewPager.setAdapter(fragment_page_adapter);
            tabLayout.setupWithViewPager(viewPager);
        }
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
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
