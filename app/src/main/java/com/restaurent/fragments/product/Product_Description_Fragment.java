package com.restaurent.fragments.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.restaurent.R;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.json_mechanism.GetJSONData;
import com.restaurent.shared_preferenc_estring.DataStorage;
import com.restaurent.utils.ProductDataSet;

import java.util.HashMap;

public class Product_Description_Fragment extends Fragment {
    TextView product_description;
    String mProduct_String;
    ProductDataSet mProductDataSet = new ProductDataSet();
    HashMap<String, Object> mDataSet = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_product_description, container, false);
        product_description= (TextView) view.findViewById(R.id.product_description);
        mProduct_String = DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(), JSON_Names.KEY_PRODUCT_STRING);
        mDataSet = GetJSONData.getSeparateProductDetail(mProduct_String);
        if(mDataSet!=null) {
            mProductDataSet = (ProductDataSet) mDataSet.get(JSON_Names.KEY_PD_PRODUCT);
            if (mProductDataSet.getDescription() != null) {
                String txtDescription = "     " + Html.fromHtml("<html><body>" + "<p align=\"justify\">" + mProductDataSet.getDescription() + "</p> " + "</body></html>");
                product_description.setText(txtDescription);
            }
        }
        return view;
    }
}
