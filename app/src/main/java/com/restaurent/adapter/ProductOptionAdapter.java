package com.restaurent.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.restaurent.R;
import com.restaurent.utils.ProductOptionDataSet;

import java.util.ArrayList;
import java.util.HashMap;


public class ProductOptionAdapter extends RecyclerView.Adapter<ProductOptionAdapter.ViewHolder> {
    ArrayList<ProductOptionDataSet> mList;
    HashMap<Integer,ArrayList<ProductOptionDataSet>> mChildList;
    Context mContext;
    String mID;

    public ProductOptionAdapter(Context context, ArrayList<ProductOptionDataSet> list,
                                HashMap<Integer,ArrayList<ProductOptionDataSet>> childList, String product_id) {
        this.mContext = context;
        this.mList = list;
        this.mChildList = childList;
        this.mID=product_id;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView mRowView;
        TextView mRowTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            mRowTitle= (TextView) itemView.findViewById(R.id.product_detail_color_title);
            mRowView= (RecyclerView) itemView.findViewById(R.id.product_detail_second_row);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.product_row_holder,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ProductRowAdapter productRowAdapter=new ProductRowAdapter(mContext, mChildList.get(position),
                mList.get(position).getProduct_option_id(), mID);
        holder.mRowTitle.setText(mList.get(position).getProduct_option_name());
        holder.mRowView.setLayoutManager(new LinearLayoutManager(mContext,
                LinearLayoutManager.HORIZONTAL, false));
        holder.mRowView.setAdapter(productRowAdapter);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


}
