package com.restaurent.fragments.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.restaurent.R;
import com.restaurent.adapter.Product_Specification_Adapter;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.json_mechanism.GetJSONData;
import com.restaurent.shared_preferenc_estring.DataStorage;

import java.util.ArrayList;

public class Product_Details_Description_fragment extends Fragment {

    String product_string;
    RecyclerView mSpecification_holder;
    View view;
    ArrayList<Object> mResultDataSet=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_product_details, container, false);
        mSpecification_holder= (RecyclerView) view.findViewById(R.id.product_specification_recycler_view);
        product_string = DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(), JSON_Names.KEY_PRODUCT_STRING);
        mResultDataSet= GetJSONData.getProductSpecificationForSpecification(product_string);
        if(mResultDataSet!=null) {
            mSpecification_holder.setLayoutManager(new LinearLayoutManager(getActivity()));
            mSpecification_holder.setAdapter(new Product_Specification_Adapter(getActivity(), mResultDataSet));
        }
        return view;
    }
}
