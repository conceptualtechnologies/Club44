package com.restaurent.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.restaurent.R;
import com.restaurent.activity.category.Category_Details;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.mechanism.Methods;
import com.restaurent.utils.CategoryDataSet;

import java.util.ArrayList;

public class HomeCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    ArrayList<CategoryDataSet> mHomeCategoryList = new ArrayList<>();

    public HomeCategoryAdapter(Context context, ArrayList<CategoryDataSet> list) {
        this.mContext = context;
        this.mHomeCategoryList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.home_category_row, parent, false);
        viewHolder = new CategoryViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
        categoryViewHolder.mCategoryTitle.setText(mHomeCategoryList.get(position).getName());
        Methods.glide_image_loader_fixed_size(mHomeCategoryList.get(position).getImage(), categoryViewHolder.mCategoryImageView);
        //categoryViewHolder.mCategoryImageView.setImageResource(R.drawable.screen);
    }

    @Override
    public int getItemCount() {
        //return mHomeCategoryList.size();
        if (mHomeCategoryList.size() > 4) {
            return 4;
        } else {
            return mHomeCategoryList.size();
        }
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mCategoryImageView;
        TextView mCategoryTitle;

        public CategoryViewHolder(View view) {
            super(view);
            mCategoryImageView = (ImageView) view.findViewById(R.id.home_category_image);
            mCategoryTitle = (TextView) view.findViewById(R.id.home_category_title);

            mCategoryImageView.setOnClickListener(this);
            mCategoryTitle.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.home_category_image:
                    intent_transfer(getAdapterPosition());
                    break;
                case R.id.home_category_title:
                    intent_transfer(getAdapterPosition());
                    break;
            }
        }
    }

    public void intent_transfer(int position) {
        Intent intent = new Intent(mContext, Category_Details.class);
        intent.putExtra(JSON_Names.KEY_TITLE_SHARED_PREFERENCE, mHomeCategoryList.get(position).getName());
        intent.putExtra(JSON_Names.KEY_IMAGE_URL_SHARED_PREFERENCE, mHomeCategoryList.get(position).getImage());
        intent.putExtra(JSON_Names.KEY_CATEGORY_ID_SHARED_PREFERENCE, mHomeCategoryList.get(position).getCategory_id());
        intent.putExtra(JSON_Names.KEY_TYPE_SHARED_PREFERENCE, R.string.sort_category);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}
