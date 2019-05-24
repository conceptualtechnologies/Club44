package com.restaurent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.restaurent.R;
import com.restaurent.utils.CategoryDataSet;

import java.util.ArrayList;


public class CategoryAdapter extends BaseExpandableListAdapter {
    Context context;
    ArrayList<CategoryDataSet> mCAParentList_L;
    ArrayList<ArrayList<CategoryDataSet>> mCAChildList_L;

    public CategoryAdapter(Context context, ArrayList<CategoryDataSet> mCAParentList, ArrayList<ArrayList<CategoryDataSet>> mCAChildList) {
        this.context = context;
        this.mCAParentList_L = mCAParentList;
        this.mCAChildList_L = mCAChildList;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }


    @Override
    public CategoryDataSet getChild(int groupPosition, int childPosition) {
        return this.mCAChildList_L.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        CategoryDataSet categoryDataSet = getChild(groupPosition, childPosition);
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_category, parent, false);
            holder = new ViewHolder();
            holder.name1 = (TextView) convertView.findViewById(R.id.lblListItem);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name1.setText(categoryDataSet.getName());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mCAChildList_L.get(groupPosition).size();
    }

    @Override
    public CategoryDataSet getGroup(int groupPosition) {
        return this.mCAParentList_L.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.mCAParentList_L.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        CategoryDataSet parentCategory = getGroup(groupPosition);
        ViewHolder holder ;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, parent, false);
            holder = new ViewHolder();
            holder.name2 = (TextView) convertView.findViewById(R.id.lblListHeader);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name2.setText(parentCategory.getName());
        ImageView iconExpand = (ImageView) convertView.findViewById(R.id.icon_expand);
        ImageView iconCollapse = (ImageView) convertView
                .findViewById(R.id.icon_collapse);

        if (isExpanded) {
            iconExpand.setVisibility(View.GONE);
            iconCollapse.setVisibility(View.VISIBLE);
        } else {
            iconExpand.setVisibility(View.VISIBLE);
            iconCollapse.setVisibility(View.GONE);
        }

        if (getChildrenCount(groupPosition) == 0) {
            iconExpand.setVisibility(View.GONE);
            iconCollapse.setVisibility(View.GONE);
        }


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }


    static class ViewHolder {
        TextView name1;
        TextView name2;

    }

}