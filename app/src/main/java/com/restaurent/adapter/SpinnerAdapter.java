package com.restaurent.adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.restaurent.R;
import com.restaurent.utils.SpinnerDataSet;

import java.util.ArrayList;


public class SpinnerAdapter extends ArrayAdapter<SpinnerDataSet> {
    ArrayList<SpinnerDataSet> data = null;
    private Activity context;

    public SpinnerAdapter(Activity context, int resource, ArrayList<SpinnerDataSet> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_item_country_spinner, parent, false);
        }
        SpinnerDataSet item = data.get(position);
        if (item != null) {

            TextView CountryName = (TextView) row.findViewById(R.id.spinner_country_value_spinner);

            if (CountryName != null) {
                if (item.get_id() == null && item.get_name() == null) {
                    CountryName.setText(item.getInitialText());
                    CountryName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_more_black_24dp, 0);
                } else {
                    CountryName.setText(item.get_name());
                    CountryName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_more_black_24dp, 0);
                }
            }

        }
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.country_spinner, parent, false);
        }
        SpinnerDataSet item = data.get(position);
        if (item != null) {
            TextView CountryName = (TextView) row.findViewById(R.id.spinner_country_value);
            int color = ContextCompat.getColor(context, R.color.white);
            CountryName.setBackgroundColor(color);

            if (item.get_id() == null && item.get_name() == null) {
                CountryName.setText(item.getInitialText());
            } else {
                CountryName.setText(item.get_name());
            }
        }

        return row;
    }
}

