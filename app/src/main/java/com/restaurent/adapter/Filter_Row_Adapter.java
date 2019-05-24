package com.restaurent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.restaurent.R;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.constant_class.URL_Class;
import com.restaurent.shared_preferenc_estring.DataStorage;
import com.restaurent.utils.FilterDataSet;

import java.util.ArrayList;

public class Filter_Row_Adapter extends RecyclerView.Adapter<Filter_Row_Adapter.List_ViewHolder>{
    Context mContext;
    ArrayList<FilterDataSet> mChildList;

    public Filter_Row_Adapter(Context context, ArrayList<FilterDataSet> mChildList) {
        this.mContext=context;
        this.mChildList=mChildList;
    }


    public class List_ViewHolder extends RecyclerView.ViewHolder  implements CompoundButton.OnCheckedChangeListener{
        TextView title;
        CheckBox checkBox;

        public List_ViewHolder(View itemView) {
            super(itemView);
            title=(TextView) itemView.findViewById(R.id.filter_check_name);
            checkBox=(CheckBox) itemView.findViewById(R.id.filter_check_item);
            checkBox.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                String temp= DataStorage.mRetrieveSharedPreferenceString(mContext, JSON_Names.KEY_FILTER_ORDER);
                if(!temp.equals(JSON_Names.KEY_NO_DATA)){
                    temp=temp+ URL_Class.mURL_GetFilter_For_Filter_Separator+mChildList.get(getAdapterPosition()).getmFilterId();
                    DataStorage.mStoreSharedPreferenceString(mContext,JSON_Names.KEY_FILTER_ORDER,temp);
                }else{
                    temp=mChildList.get(getAdapterPosition()).getmFilterId();
                    DataStorage.mStoreSharedPreferenceString(mContext,JSON_Names.KEY_FILTER_ORDER,temp);
                }
            }
        }
    }
    @Override
    public int getItemCount() {
        return mChildList.size();
    }
    @Override
    public void onBindViewHolder(List_ViewHolder holder, int position) {
        holder.title.setText(mChildList.get(position).getmName());
    }

    @Override
    public List_ViewHolder onCreateViewHolder(ViewGroup root, int viewType) {
        View list = LayoutInflater.from(root.getContext()).inflate(R.layout.filter_inner_row, root, false);
        return new List_ViewHolder(list);
    }

}
