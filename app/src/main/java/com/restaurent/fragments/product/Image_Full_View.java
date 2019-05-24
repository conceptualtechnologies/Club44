package com.restaurent.fragments.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.restaurent.R;
import com.restaurent.adapter.ImageFullViewAdapter;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.json_mechanism.GetJSONData;
import com.restaurent.mechanism.Methods;
import com.restaurent.recycler_view_click_event.RecyclerViewClickEventHandler;
import com.restaurent.utils.ProductDataSet;
import com.restaurent.utils.ProductImageDataSet;

import java.util.ArrayList;
import java.util.HashMap;

public class Image_Full_View extends Fragment {
    ImageView mParent;
    RecyclerView mImageRecyclerView;
    String mImageData;
    HashMap<String, Object> mFullList;
    ArrayList<ProductImageDataSet> mImageList;
    ProductDataSet mParentImage;

    public Image_Full_View() {
    }

    public static Image_Full_View getInstance(String data) {
        Image_Full_View image_full_view = new Image_Full_View();
        Bundle bundle = new Bundle();
        bundle.putString(JSON_Names.KEY_PRODUCT_ID, data);
        image_full_view.setArguments(bundle);
        return image_full_view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageData = getArguments().getString(JSON_Names.KEY_PRODUCT_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_full_view, container, false);
        setting(view);
        return view;
    }

    @SuppressWarnings("unchecked")
    public void setting(View view) {
        //mImageData = DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(), JSON_Names.KEY_PRODUCT_STRING);

        mFullList = GetJSONData.getSeparateProductDetail(mImageData);
        if (mFullList != null) {
            mParentImage = (ProductDataSet) mFullList.get(JSON_Names.KEY_PRODUCT_IMAGE_FULL_VIEW);
            mImageList = (ArrayList<ProductImageDataSet>) mFullList.get(JSON_Names.KEY_IMAGE_IMAGE_FULL_VIEW);
        }

        mParent = (ImageView) view.findViewById(R.id.image_full_view_parent_image);

        image_caller(mParent, mParentImage.getImage());

        mImageRecyclerView = (RecyclerView) view.findViewById(R.id.image_full_view_recycler_view);

        mImageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, JSON_Names.KEY_FALSE_BOOLEAN));
        mImageRecyclerView.setAdapter(new ImageFullViewAdapter(getActivity().getApplicationContext(), mImageList, mParentImage.getImage()));
        mImageRecyclerView.addOnItemTouchListener(new RecyclerViewClickEventHandler(getActivity().getApplicationContext(), new RecyclerViewClickEventHandler.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == 0) {
                    image_caller(mParent, mParentImage.getImage());
                } else {
                    image_caller(mParent, mImageList.get(position - 1).getChildImage());
                }
            }
        }));
    }

    public void image_caller(ImageView imageView, String url) {
        Methods.glide_image_loader(url, imageView);
    }
}
