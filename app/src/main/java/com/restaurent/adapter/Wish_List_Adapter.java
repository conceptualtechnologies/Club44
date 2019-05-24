package com.restaurent.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.restaurent.R;
import com.restaurent.activity.product.Product_Details;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.constant_class.URL_Class;
import com.restaurent.db_handler.DataBaseHandlerAccount;
import com.restaurent.db_handler.DataBaseHandlerWishList;
import com.restaurent.interfaces.WishListAPIRequest;
import com.restaurent.mechanism.Methods;
import com.restaurent.shared_preferenc_estring.DataStorage;
import com.restaurent.utils.ProductDataSet;

import java.net.URLEncoder;
import java.util.ArrayList;

public class Wish_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    ArrayList<ProductDataSet> mDataSet;
    int EMPTY_VIEW = 0, CUSTOM_VIEW = 1;
    WishListAPIRequest wishListAPIRequest;

    public Wish_List_Adapter(Context context, ArrayList<ProductDataSet> dataSets, WishListAPIRequest wishListAPIRequest) {
        this.mContext = context;
        this.mDataSet = dataSets;
        this.wishListAPIRequest = wishListAPIRequest;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == CUSTOM_VIEW) {
            View view = inflater.inflate(R.layout.wish_list_row_view, parent, false);
            holder = new ViewHolder_WishList(view);
        } else {
            View view = inflater.inflate(R.layout.no_data, parent, false);
            holder = new ViewHolderEmpty_View(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == CUSTOM_VIEW) {
            ViewHolder_WishList holder_wishList = (ViewHolder_WishList) holder;
            Methods.glide_image_loader_fixed_size(mDataSet.get(position).getImage(), holder_wishList.img_wish_list_product_image);
            holder_wishList.txt_wish_list_product_price_quantity.setText(mDataSet.get(position).getPrice());
            holder_wishList.txt_wish_list_product_title.setText(mDataSet.get(position).getTitle());

        } else {
            ViewHolderEmpty_View empty_view = (ViewHolderEmpty_View) holder;
            empty_view.empty_view.setText(R.string.empty_wish_list);
        }
    }

    public void delete(int position) {
        mDataSet.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataSet != null) {
            if (mDataSet.size() > 0) {
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
        if (mDataSet != null) {
            if (mDataSet.size() > 0) {
                return mDataSet.size();
            } else {
                return 1;
            }

        } else {
            return 1;
        }
    }

    public void storeProductData(int position, View v) {
        Intent intent = new Intent(v.getContext(), Product_Details.class);
        intent.putExtra(JSON_Names.KEY_PRODUCT_STRING, mDataSet.get(position).getProduct_id());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        v.getContext().startActivity(intent);
    }

    public class ViewHolderEmpty_View extends RecyclerView.ViewHolder {
        TextView empty_view;

        public ViewHolderEmpty_View(View view) {
            super(view);
            empty_view = (TextView) view.findViewById(R.id.empty_view);
        }
    }

    public class ViewHolder_WishList extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {
        ImageView img_wish_list_product_image;
        TextView txt_wish_list_product_title;
        TextView txt_wish_list_product_price_quantity;
        ImageButton img_btn_wish_list_product_remove;

        public ViewHolder_WishList(View view) {
            super(view);
            img_wish_list_product_image = (ImageView) view.findViewById(R.id.wish_list_product_image);
            txt_wish_list_product_title = (TextView) view.findViewById(R.id.wish_list_product_title);
            txt_wish_list_product_price_quantity = (TextView) view.findViewById(R.id.wish_list_product_price);
            img_btn_wish_list_product_remove = (ImageButton) view.findViewById(R.id.wish_list_product_remove);
            img_wish_list_product_image.setOnTouchListener(this);
            txt_wish_list_product_title.setOnClickListener(this);
            txt_wish_list_product_price_quantity.setOnClickListener(this);
            img_btn_wish_list_product_remove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.wish_list_product_price:
                    storeProductData(getAdapterPosition(), v);
                    break;
                case R.id.wish_list_product_title:
                    storeProductData(getAdapterPosition(), v);
                    break;
                case R.id.wish_list_product_remove:
                    DataStorage.mStoreSharedPreferenceString(mContext.getApplicationContext(), JSON_Names.KEY_CURRENT_PRODUCT_ID, mDataSet.get(getAdapterPosition()).getProduct_id());
                    DataBaseHandlerWishList.getInstance(mContext.getApplicationContext()).remove_from_wish_list(mDataSet.get(getAdapterPosition()).getProduct_id());
                    String url[] = {URL_Class.mURL + URL_Class.mURL_Remove_WishList};
                    wishListAPIRequest.wish_list_api_request(get_wish_list_post_data(), url);
                    delete(getAdapterPosition());
                    break;
            }
        }

        public String get_wish_list_post_data() {

            String mProduct_id;
            String mCustomer_id;
            try {
                mProduct_id = DataStorage.mRetrieveSharedPreferenceString(mContext.getApplicationContext(), JSON_Names.KEY_CURRENT_PRODUCT_ID);
                mCustomer_id = String.valueOf(DataBaseHandlerAccount.getInstance(mContext.getApplicationContext()).get_customer_id());
                return URLEncoder.encode(JSON_Names.KEY_PRODUCT_ID, URL_Class.mConvertType) +
                        URL_Class.mEqual_Symbol +
                        URLEncoder.encode(mProduct_id, URL_Class.mConvertType) +
                        URL_Class.mAnd_Symbol +
                        URLEncoder.encode(JSON_Names.KEY_USER_ID, URL_Class.mConvertType) +
                        URL_Class.mEqual_Symbol +
                        URLEncoder.encode(mCustomer_id, URL_Class.mConvertType);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int id = v.getId();
            switch (id) {
                case R.id.wish_list_product_image:
                    storeProductData(getAdapterPosition(), v);
                    break;
            }
            return false;
        }
    }
}
