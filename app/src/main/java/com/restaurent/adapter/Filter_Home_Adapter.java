package com.restaurent.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.restaurent.R;
import com.restaurent.utils.FilterDataSet;
import com.restaurent.utils.WholeFilterDataSet;

import java.util.ArrayList;

public class Filter_Home_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    ArrayList<WholeFilterDataSet> mReturnValue = new ArrayList<>();
    ArrayList<FilterDataSet> mTitleList = new ArrayList<>();
    ArrayList<ArrayList<FilterDataSet>> mChildList = new ArrayList<>();
    WholeFilterDataSet set;
    int EMPTY_VIEW = 0, CUSTOM_VIEW = 1;

    public Filter_Home_Adapter(Context context, ArrayList<WholeFilterDataSet> mReturnValue) {
        this.mContext = context;
        this.mReturnValue = mReturnValue;
    }

    @Override
    public int getItemViewType(int position) {
        if (mReturnValue != null) {
            if (mReturnValue.size() > 0) {
                return CUSTOM_VIEW;
            } else {
                return EMPTY_VIEW;
            }
        } else {
            return EMPTY_VIEW;
        }
    }

    @Override
    public int getItemCount() {
        if (mReturnValue != null) {
            if (mReturnValue.size() > 0) {
                return mReturnValue.size();
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == CUSTOM_VIEW) {
            List_ViewHolder list_viewHolder = (List_ViewHolder) holder;
            set = mReturnValue.get(0);
            mTitleList = set.mTitleList;
            mChildList = set.mChildList;
            list_viewHolder.row_recycler_view.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            list_viewHolder.row_recycler_view.setAdapter(new Filter_Row_Adapter(mContext, mChildList.get(position)));
            list_viewHolder.title.setText(mTitleList.get(position).getmName());
        } else {
            ViewHolderEmpty_View empty_view = (ViewHolderEmpty_View) holder;
            empty_view.empty_view.setText(R.string.empty_filter);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup root, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == EMPTY_VIEW) {
            view = inflater.inflate(R.layout.no_data, root, false);
            viewHolder = new ViewHolderEmpty_View(view);
        } else {
            view = inflater.inflate(R.layout.filter_home_row, root, false);
            viewHolder = new List_ViewHolder(view);
        }
        return viewHolder;
    }

    public class List_ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        RecyclerView row_recycler_view;

        public List_ViewHolder(View itemView) {
            super(itemView);
            row_recycler_view = (RecyclerView) itemView.findViewById(R.id.filter_child_recycler_view);
            title = (TextView) itemView.findViewById(R.id.filter_row_title);
        }
    }

    public class ViewHolderEmpty_View extends RecyclerView.ViewHolder {
        TextView empty_view;
        public ViewHolderEmpty_View(View view) {
            super(view);
            empty_view = (TextView) view.findViewById(R.id.empty_view);
        }
    }
}
