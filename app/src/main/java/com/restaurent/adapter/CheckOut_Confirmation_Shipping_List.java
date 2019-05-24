package com.restaurent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.restaurent.R;
import com.restaurent.utils.ConfirmResponseDataSet;

import java.util.ArrayList;

public class CheckOut_Confirmation_Shipping_List  extends RecyclerView.Adapter<CheckOut_Confirmation_Shipping_List.ViewHolder>{
    Context mContext;
    ArrayList<ConfirmResponseDataSet> mList=new ArrayList<>();

    public CheckOut_Confirmation_Shipping_List(Context context, ArrayList<ConfirmResponseDataSet> list){
        this.mContext=context;
        this.mList=list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.confirm_order_total_list_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mList!=null){
            holder.title.setText(mList.get(position).getmTotalsTitle());
            holder.value.setText(mList.get(position).getmTotalsValue());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title,value;

        public ViewHolder(View view) {
            super(view);
            title= (TextView) view.findViewById(R.id.confirmation_shipping_title);
            value= (TextView) view.findViewById(R.id.confirmation_shipping_value);
        }
    }
}
