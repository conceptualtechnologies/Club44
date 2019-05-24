package com.restaurent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.restaurent.R;
import com.restaurent.mechanism.Methods;
import com.restaurent.utils.ProductImageDataSet;

import java.util.ArrayList;

public class ImageFullViewAdapter extends RecyclerView.Adapter<ImageFullViewAdapter.ViewHolder> {
    Context mContext;
    ArrayList<ProductImageDataSet> mImageList;
    String mParentImage;

    public ImageFullViewAdapter(Context context, ArrayList<ProductImageDataSet> list, String parentImage) {
        this.mContext = context;
        this.mImageList = list;
        this.mParentImage = parentImage;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.image_row_view_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == 0) {
            image_caller(mParentImage, holder.mChildView);
        } else {
            image_caller(mImageList.get(position - 1).getChildImage(), holder.mChildView);
        }
    }

    public void image_caller(String url, ImageView imageView) {
        Methods.glide_image_loader_fixed_size(url, imageView);
    }

    @Override
    public int getItemCount() {
        return mImageList.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mChildView;

        public ViewHolder(View itemView) {
            super(itemView);
            mChildView = (ImageView) itemView.findViewById(R.id.image_full_view_row_image);
        }
    }
}
