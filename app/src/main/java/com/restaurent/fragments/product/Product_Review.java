package com.restaurent.fragments.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.restaurent.R;
import com.restaurent.adapter.Review_Adapter;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.json_mechanism.GetJSONData;
import com.restaurent.utils.ProductReviewListDataSet;

import java.util.ArrayList;
import java.util.HashMap;

public class Product_Review extends Fragment {
    View view;
    RecyclerView review_result;
    String product_string;
    ArrayList<ProductReviewListDataSet> mReviewList = new ArrayList<>();
    HashMap<String, Object> mDataSet = new HashMap<>();
    Button back_to_list;

    public Product_Review() {
    }

    public static Product_Review getInstance(String data) {
        Product_Review product_review = new Product_Review();
        Bundle bundle = new Bundle();
        bundle.putString(JSON_Names.KEY_PRODUCT_ID, data);
        product_review.setArguments(bundle);
        return product_review;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        product_string=getArguments().getString(JSON_Names.KEY_PRODUCT_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_review,container,false);
        setup();
        return view;
    }

    @SuppressWarnings("unchecked")
    public void setup(){
        review_result= (RecyclerView) view.findViewById(R.id.review_loader_recycler_view);
        back_to_list= (Button) view.findViewById(R.id.btn_back);
        review_result.setLayoutManager(new LinearLayoutManager(getActivity()));

        //product_string = DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(), JSON_Names.KEY_PRODUCT_STRING);
        mDataSet = GetJSONData.getSeparateProductDetail(product_string);
        if(mDataSet!=null) {
            mReviewList = (ArrayList<ProductReviewListDataSet>) mDataSet.get(JSON_Names.KEY_PD_REVIEW);
        }
        review_result.setAdapter(new Review_Adapter(getActivity(),mReviewList));

        back_to_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
                getActivity().finish();
            }
        });
    }

}
