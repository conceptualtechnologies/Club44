package com.restaurent.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.restaurent.Application_Context;
import com.restaurent.R;
import com.restaurent.fragments.product.Product_Details_Description_fragment;


public class Fragment_Page_Adapter extends FragmentStatePagerAdapter {

    String title[] = {Application_Context.getAppContext().getResources().getString(R.string.detail_specification),
            /*Application_Context.getAppContext().getResources().getString(R.string.description_specification)*/};
    String mProductString,mDescription;
    public Fragment_Page_Adapter(FragmentManager fm, String product_string/*,String description*/) {
        super(fm);
        this.mProductString=product_string;
        //this.mDescription=description;
    }

    @Override
    public Fragment getItem(int position) {
        //if(position==0){
            return new Product_Details_Description_fragment();
        /*}else {
            return new Product_Description_Fragment();
        }*/
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return title[position];
    }

    @Override
    public int getCount() {
        return title.length;
    }

}
