package com.restaurent.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.restaurent.R;


public class RecyclerView_fragment extends Fragment {
	RecyclerView homeRecycler;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.recyclerview_list, container);
		homeRecycler=(RecyclerView) view.findViewById(R.id.home_list_recycler_view);
		return view;
	}

}
