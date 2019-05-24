package com.restaurent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.restaurent.R;


public class QuantityAdapter extends ArrayAdapter<String> {
    Context mContext;
    String[]data;
    public QuantityAdapter(Context context, String[] value) {
        super(context, android.R.layout.simple_spinner_item, value);
        this.mContext=context;
        this.data=value;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return custom(parent,position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return custom(parent,position);
    }

    public View custom(ViewGroup viewGroup, int position){
        LayoutInflater layoutInflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.quantity_spinner, viewGroup, false);

        TextView quantity_home = (TextView) view.findViewById(R.id.quantity_row);
        quantity_home.setText(data[position]);
        return view;
    }
}
