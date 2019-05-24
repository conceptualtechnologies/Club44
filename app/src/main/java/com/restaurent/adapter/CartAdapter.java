package com.restaurent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.restaurent.Application_Context;
import com.restaurent.R;
import com.restaurent.db_handler.DataBaseHandlerCart;
import com.restaurent.db_handler.DataBaseHandlerCartOptions;
import com.restaurent.interfaces.Refresher;
import com.restaurent.json_mechanism.GetJSONData;
import com.restaurent.mechanism.Methods;
import com.restaurent.utils.ProductDataSet;

import java.util.ArrayList;
import java.util.HashMap;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    int EMPTY_VIEW = 0, CUSTOM_VIEW = 1;
    Refresher refresher_refresher;
    ArrayList<ProductDataSet> mResultDataSet;
    HashMap<String, ArrayList<String[]>> mCartOptionData;

    public CartAdapter(Context context, ArrayList<ProductDataSet> mDataSet, Refresher refresher, String result) {
        this.mContext = context;
        this.mResultDataSet = mDataSet;
        this.refresher_refresher = refresher;
        if (result != null)
            this.mCartOptionData = GetJSONData.get_cart_options(result);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == CUSTOM_VIEW) {
            View view = inflater.inflate(R.layout.cart_row, parent, false);
            viewHolder = new ViewHolder_Cart_List(view);
        } else {
            View view = inflater.inflate(R.layout.no_data, parent, false);
            viewHolder = new ViewHolderEmpty_View(view);
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (mResultDataSet != null) {
            if (mResultDataSet.size() > 0) {
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
        if (mResultDataSet != null) {
            if (mResultDataSet.size() > 0) {
                return mResultDataSet.size();
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
            ViewHolder_Cart_List holder_cart_list = (ViewHolder_Cart_List) holder;
            Methods.glide_image_loader(mResultDataSet.get(position).getImage(), holder_cart_list.mCartImage);
            holder_cart_list.mCartTitle.setText(mResultDataSet.get(position).getTitle());
            if(mResultDataSet.get(position).getQuantity()=="5"||mResultDataSet.get(position).getQuantity()=="10"||mResultDataSet.get(position).getQuantity()=="15"||mResultDataSet.get(position).getQuantity()=="20"||mResultDataSet.get(position).getQuantity()=="25"||mResultDataSet.get(position).getQuantity()=="30") {
                String price = "5 * " + mResultDataSet.get(position).getPrice();
                holder_cart_list.mCartPrice.setText(price);
            }
            else {
                String price = "1 * " + mResultDataSet.get(position).getPrice();
                holder_cart_list.mCartPrice.setText(price);
            }
            holder_cart_list.mCartProductCount.setText(mResultDataSet.get(position).getQuantity());
            holder_cart_list.mCartProductDiscount.setText(mResultDataSet.get(position).getProduct_discount());
           // Log.d("Discount",mResultDataSet.get(position).getProduct_discount());
            if (mCartOptionData.get(mResultDataSet.get(position).getProduct_id() + position) != null) {
                for (int i = 0; i < mCartOptionData.get(mResultDataSet.get(position).getProduct_id() + position).size(); i++) {
                    View view = LayoutInflater.from(mContext).inflate(R.layout.cart_option_holder, null, false);
                    TextView mOptionTitle = (TextView) view.findViewById(R.id.lblListItem);
                    String optionDataStringArray[] = mCartOptionData.get(mResultDataSet.get(position).getProduct_id() + position).get(i);
                    String optionDataString = optionDataStringArray[0] + " : " + optionDataStringArray[1];
                    mOptionTitle.setText(optionDataString);
                    holder_cart_list.mCartOptionHolder.addView(view);
                }
            }

        } else {
            ViewHolderEmpty_View empty_view = (ViewHolderEmpty_View) holder;
            empty_view.empty_view.setText(R.string.empty_cart);
        }
    }

    public void delete(int index, int position) {
        DataBaseHandlerCartOptions.getInstance(mContext.getApplicationContext()).remove_from_options(index);
        DataBaseHandlerCart.getInstance(mContext.getApplicationContext()).remove_cart(index);
        mResultDataSet.remove(position);
        notifyDataSetChanged();
    }

    public void refresher() {
        refresher_refresher.refresher();
    }

    public class ViewHolderEmpty_View extends RecyclerView.ViewHolder {
        TextView empty_view;

        public ViewHolderEmpty_View(View view) {
            super(view);
            empty_view = (TextView) view.findViewById(R.id.empty_view);
        }
    }

    public class ViewHolder_Cart_List extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mCartImage;
        TextView mCartTitle, mCartPrice, mCartProductCount,mCartProductDiscount;
        ImageButton mCartRemoveFromCart, mCartAddProductCount, mCartRemoveProductCount;
        LinearLayout mCartOptionHolder;

        public ViewHolder_Cart_List(View view) {
            super(view);
            mCartImage = (ImageView) view.findViewById(R.id.cart_product_image);
            mCartTitle = (TextView) view.findViewById(R.id.cart_product_title);
            mCartPrice = (TextView) view.findViewById(R.id.cart_product_price);
            mCartProductCount = (TextView) view.findViewById(R.id.cart_product_count);
            mCartProductDiscount = (TextView) view.findViewById(R.id.cart_product_discount);
            mCartRemoveFromCart = (ImageButton) view.findViewById(R.id.cart_product_remove_item);
            mCartAddProductCount = (ImageButton) view.findViewById(R.id.cart_product_count_add);
            mCartRemoveProductCount = (ImageButton) view.findViewById(R.id.cart_product_count_remove);
            mCartOptionHolder = (LinearLayout) view.findViewById(R.id.cart_option_holder);
            mCartRemoveFromCart.setOnClickListener(this);
            mCartAddProductCount.setOnClickListener(this);
            mCartRemoveProductCount.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.cart_product_remove_item:
                    delete(mResultDataSet.get(getAdapterPosition()).getIndex(), getAdapterPosition());
                    refresher();
                    break;
                case R.id.cart_product_count_add:
                    int value_add = mResultDataSet.get(getAdapterPosition()).getIndex();

                    if (mResultDataSet.get(getAdapterPosition()).getSubtract() < DataBaseHandlerCart.
                            getInstance(mContext.getApplicationContext()).get_maximum_count(value_add)) {
                        DataBaseHandlerCart.getInstance(mContext.getApplicationContext()).update_product_count(value_add, 1);
                        notifyDataSetChanged();
                        refresher();
                    } else {
                        String maximum = Application_Context.getAppContext().getResources().getString(R.string.quantity_check_3) + " "
                                + mResultDataSet.get(getAdapterPosition()).getTitle() + Application_Context.getAppContext().getResources()
                                .getString(R.string.quantity_check_2) + " "
                                + DataBaseHandlerCart.getInstance(mContext.getApplicationContext()).get_product_count(value_add);
                        Methods.toast(maximum);
                    }
                    break;
                case R.id.cart_product_count_remove:
                    int value_remove = mResultDataSet.get(getAdapterPosition()).getIndex();
                    if (DataBaseHandlerCart.getInstance(mContext.getApplicationContext()).get_product_count(value_remove) > Integer.valueOf(mResultDataSet.get(getAdapterPosition()).getMinimum())) {
                        DataBaseHandlerCart.getInstance(mContext.getApplicationContext()).update_product_count(value_remove, -1);
                        notifyDataSetChanged();
                        refresher();
                    } else {
                        String minimum = Application_Context.getAppContext().getResources().getString(R.string.select_minimum_1) + " " + mResultDataSet.get(getAdapterPosition()).getTitle() + Application_Context.getAppContext().getResources().getString(R.string.select_minimum_2) + " " + mResultDataSet.get(getAdapterPosition()).getMinimum();
                        Methods.toast(minimum);
                    }
                    break;
            }

        }
    }
}
