package com.restaurent.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.restaurent.R;
import com.restaurent.activity.Home;
import com.restaurent.activity.product.Product_Details;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.db_handler.DataBaseHandlerCart;
import com.restaurent.db_handler.DataBaseHandlerCartOptions;
import com.restaurent.interfaces.API_Result;
import com.restaurent.interfaces.Refresher;
import com.restaurent.mechanism.Methods;
import com.restaurent.shared_preferenc_estring.DataStorage;
import com.restaurent.utils.ProductDataSet;
import com.restaurent.utils.ProductImageDataSet;
import com.restaurent.utils.ProductOptionDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class Adapter_Row extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ProductDataSet> itemsData;
    Context mContext;
    Refresher refresher_refresher;
    private int PRODUCT = 0/*, FULL_VIEW = 1*/;

    public Adapter_Row(ArrayList<ProductDataSet> itemsData, Context context,Refresher refresher) {
        this.itemsData = itemsData;
        this.mContext = context;
        this.refresher_refresher=refresher;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        /*if (viewType == FULL_VIEW) {
            View v1 = inflater.inflate(R.layout.row_end_imageview, parent, false);
            viewHolder = new ViewHolderLastView(v1);
        } else {*/
            View v2 = inflater.inflate(R.layout.home_row, parent, false);
            viewHolder = new ViewHolder(v2);
        //}
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int type = viewHolder.getItemViewType();

        if (type == PRODUCT) {
            ViewHolder itemViewHolder = (ViewHolder) viewHolder;
            itemViewHolder.special_price_p.setVisibility(View.VISIBLE);
            itemViewHolder.special_price_sp.setVisibility(View.VISIBLE);
            //itemViewHolder.price.setVisibility(View.VISIBLE);

            itemViewHolder.title.setText(itemsData.get(position).getTitle());
            Methods.glide_image_loader_fixed_size(itemsData.get(position).getImage(), itemViewHolder.image);
            //Log.d("Image url", itemsData.get(position).getImage());

            if (itemsData.get(position).getSpecial_price().isEmpty()) {//Check the data
                //itemViewHolder.price.setText(itemsData.get(position).getPrice());
                //itemViewHolder.special_price_p.setVisibility(View.GONE);
                itemViewHolder.special_price_sp.setText(itemsData.get(position).getPrice());
            } else {
                //itemViewHolder.price.setVisibility(View.GONE);
                itemViewHolder.special_price_p.setText(itemsData.get(position).getPrice());
                itemViewHolder.special_price_p.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                itemViewHolder.special_price_sp.setText(itemsData.get(position).getSpecial_price());
            }
        }/* else {
            ViewHolderLastView viewHolderLastView = (ViewHolderLastView) viewHolder;
            viewHolderLastView.view_all_image.setImageResource(R.drawable.view_all);
        }*/
    }

    @Override
    public int getItemCount() {
        if (itemsData.size() > 20) {
            return 20;
        } else {
            return itemsData.size()/* + 1*/;
        }

    }
    public void refresh() {
        refresher_refresher.refresher();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView title;
        public ImageView image;
        int count;
        //public TextView price;
        Toolbar toolbar;
        EditText pincode;
        Button btn_product_add_to_cart, pincodecheck;
        ImageButton img_btn_add_wish_list;
        int int_cart_product_count=0;
        String str_cart_product_count;
        TextView cart_product_count, txt_product_price, txt_product_special_price, txt_product_child_image_count;
        TextView txt_product_color_title, txt_product_write_rating;
        ImageView img_product_parent_image, img_product_child_one, img_product_child_two, img_product_child_three, img_product_child_four;
        TextView txt_product_detail_details, txt_product_details_no_of_reviews, txt_product_description,
                txt_product_description_title, pincoderesponse;
        LinearLayout linear_add_to_cart, mReviewHolder;
        ImageButton cart_product_add,cart_product_minus;
        RatingBar rating_bar_product;
        RecyclerView option_holder;
        ArrayList<ProductImageDataSet> mImageList = new ArrayList<>();
        ArrayList<ProductOptionDataSet> mOptionList = new ArrayList<>();
        HashMap<Integer, ArrayList<ProductOptionDataSet>> mOptionListChild = new HashMap<>();
        ProductDataSet mProductDataSet = new ProductDataSet();
        ProductOptionAdapter productOptionAdapter;
        String mProduct_String, product_id;
        HashMap<String, Object> mDataSet = new HashMap<>();
        API_Result api_result;
        Spinner quantitySpinner;
        int  int_quantity;
        String quantity;
        TextView special_price_p;
        TextView special_price_sp;
TextView add_to_cart;
        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.column_title);
            image = (ImageView) view.findViewById(R.id.column_image);
            //price = (TextView) view.findViewById(R.id.column_price);
            special_price_p = (TextView) view.findViewById(R.id.column_special_price_p);
            special_price_sp = (TextView) view.findViewById(R.id.column_special_price_sp);
add_to_cart=(TextView)view.findViewById(R.id.column_add_to_cart);
            cart_product_add=(ImageButton)view.findViewById(R.id.cart_product_count_add);
            cart_product_minus=(ImageButton)view.findViewById(R.id.cart_product_count_remove);
            cart_product_count=(TextView)view.findViewById(R.id.cart_product_count);
            str_cart_product_count=cart_product_count.getText().toString();
            int_cart_product_count=Integer.parseInt(str_cart_product_count);
            linear_add_to_cart=(LinearLayout)view.findViewById(R.id.linear_add_to_cart);
            linear_add_to_cart.setVisibility(View.GONE);

            title.setOnClickListener(this);
            image.setOnClickListener(this);
            //price.setOnClickListener(this);
            special_price_p.setOnClickListener(this);
            special_price_sp.setOnClickListener(this);
            add_to_cart.setOnClickListener(this);
          //  quantity=itemsData.get(getAdapterPosition()).getQuantity();
           // int_quantity=Integer.parseInt(quantity);
            cart_product_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
int_cart_product_count++;

                    cart_product_count.setText(""+int_cart_product_count);
                    if(DataBaseHandlerCart.getInstance(mContext).checking_cart(itemsData.get(getAdapterPosition()).getProduct_id()))
                    {
                        ArrayList<Integer> list = DataBaseHandlerCart.getInstance(mContext).get_index(itemsData.get(getAdapterPosition()).getProduct_id());
                       for(int i=0;i<list.size();i++) {
                           count = list.get(i);
                       }
                        Log.e("Update",String.valueOf(count));
                        int reference=int_cart_product_count+count;
                        DataBaseHandlerCart.getInstance(mContext).update_product_count(count, 1);
                        Home.update_cart(itemsData.get(getAdapterPosition()).getImage());
                        Toast.makeText(mContext,mContext.getResources().getString(R.string.add_to_cart_success),Toast.LENGTH_SHORT).show();

                    }
                    else{
                        if( DataBaseHandlerCart.getInstance(mContext).get_Size_cart()!=0) {
                            ArrayList<Integer> list = DataBaseHandlerCart.getInstance(mContext).get_all_index();
                            for(int i=0;i<list.size();i++) {
                                count = list.get(i);
                            }

                            int refrence=count+1;
                            DataBaseHandlerCart.getInstance(mContext).add_to_cart(refrence, itemsData.get(getAdapterPosition()).getProduct_id(), int_cart_product_count, mProduct_String, 1000);
                            Home.update_cart(itemsData.get(getAdapterPosition()).getImage());


                        }
                        else{
                            ArrayList<Integer> list = DataBaseHandlerCart.getInstance(mContext).get_all_index();
                            for(int i=0;i<list.size();i++) {
                                count = list.get(i);
                            }

                            int refrence=count+1;
                            DataBaseHandlerCart.getInstance(mContext).add_to_cart(refrence, itemsData.get(getAdapterPosition()).getProduct_id(), int_cart_product_count, mProduct_String, 1000);
                            Home.update_cart(itemsData.get(getAdapterPosition()).getImage());
                            Toast.makeText(mContext,mContext.getResources().getString(R.string.add_to_cart_success),Toast.LENGTH_SHORT).show();
                        }
                    }
                  //  add_to_count();
                }
            });
            cart_product_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(int_cart_product_count==1){
                        linear_add_to_cart.setVisibility(View.GONE);
                        add_to_cart.setVisibility(View.VISIBLE);
                        ArrayList<Integer> list = DataBaseHandlerCart.getInstance(mContext).get_index(itemsData.get(getAdapterPosition()).getProduct_id());
                        for(int i=0;i<list.size();i++) {
                            count = list.get(i);
                        }
                        DataBaseHandlerCart.getInstance(mContext).update_product_count(count,-1);
                        Home.update_cart(itemsData.get(getAdapterPosition()).getImage());
                        Toast.makeText(mContext,"Remove from Cart Successfully",Toast.LENGTH_SHORT).show();

                      //  DataBaseHandlerCart.getInstance(mContext).remove_cart(itemsData.get(getAdapterPosition()).getProduct_id());
                    }
                    else {
                        int_cart_product_count--;
                      //
                        cart_product_count.setText("" + int_cart_product_count);
                        if(DataBaseHandlerCart.getInstance(mContext).checking_cart(itemsData.get(getAdapterPosition()).getProduct_id()))
                        {
                            ArrayList<Integer> list = DataBaseHandlerCart.getInstance(mContext).get_all_index();
                            for(int i=0;i<list.size();i++) {
                                count = list.get(i);
                            }
                            Log.e("Update",String.valueOf(count));
                            int reference=int_cart_product_count+count;
                            DataBaseHandlerCart.getInstance(mContext).update_product_count(count,-1);
                            Home.update_cart(itemsData.get(getAdapterPosition()).getImage());
                            Toast.makeText(mContext,"Remove from Cart Successfully",Toast.LENGTH_SHORT).show();



                        }
                       // add_to_count();
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.column_title:
                    transfer(getAdapterPosition());
                    break;
                case R.id.column_image:
                    transfer(getAdapterPosition());
                    break;
               /* case R.id.column_price:
                    transfer(getAdapterPosition());
                    break;*/
                case R.id.column_special_price_p:
                    transfer(getAdapterPosition());
                    break;
                case R.id.column_add_to_cart:

                    linear_add_to_cart.setVisibility(View.VISIBLE);
                    add_to_cart.setVisibility(View.GONE);
                    if(DataBaseHandlerCart.getInstance(mContext).checking_cart(itemsData.get(getAdapterPosition()).getProduct_id()))
                    {
                        ArrayList<Integer> list = DataBaseHandlerCart.getInstance(mContext).get_index(itemsData.get(getAdapterPosition()).getProduct_id());
                        for(int i=0;i<list.size();i++) {
                            count = list.get(i);
                        }
                        Log.e("Update",String.valueOf(count));
                        int reference=int_cart_product_count+count;
                        DataBaseHandlerCart.getInstance(mContext).update_product_count(count, 1);
                      // notifyDataSetChanged();
                      Home.update_cart(itemsData.get(getAdapterPosition()).getImage());
                        Toast.makeText(mContext,mContext.getResources().getString(R.string.add_to_cart_success),Toast.LENGTH_SHORT).show();

                       // refresh();

                    }
                    else{
                        if( DataBaseHandlerCart.getInstance(mContext).get_Size_cart()!=0) {
                            ArrayList<Integer> list = DataBaseHandlerCart.getInstance(mContext).get_all_index();
                            for(int i=0;i<list.size();i++) {
                                count = list.get(i);
                            }
                            int refrence=count+1;
                            DataBaseHandlerCart.getInstance(mContext).add_to_cart(refrence, itemsData.get(getAdapterPosition()).getProduct_id(), int_cart_product_count, mProduct_String, 1000);
                            Home.update_cart(itemsData.get(getAdapterPosition()).getImage());
                            Toast.makeText(mContext,mContext.getResources().getString(R.string.add_to_cart_success),Toast.LENGTH_SHORT).show();

                        }
                        else {
                            ArrayList<Integer> list = DataBaseHandlerCart.getInstance(mContext).get_all_index();
                            for(int i=0;i<list.size();i++) {
                                count = list.get(i);
                            }
                            int refrence=count+1;
                            DataBaseHandlerCart.getInstance(mContext).add_to_cart(refrence, itemsData.get(getAdapterPosition()).getProduct_id(), int_cart_product_count, mProduct_String, 1000);
                            Home.update_cart(itemsData.get(getAdapterPosition()).getImage());
                            Toast.makeText(mContext,mContext.getResources().getString(R.string.add_to_cart_success),Toast.LENGTH_SHORT).show();


                        }


                    }
//add_to_count();
            
                    break;
            }}
    public void add_to_count() {
        String dummy = "";
        int i = 0, j = 0;
       // quantity=itemsData.get(getAdapterPosition()).getQuantity();
        if (itemsData != null) {
            while (i < itemsData.size()) {
                //if (mOptionList.get(i).getProduct_option_required().equals(JSON_Names.KEY_REQUIRED_VALUE)) {
//                            String temp1 = DataStorage.mRetrieveSharedPreferenceString(mContext, JSON_Names.KEY_SECOND_ROW_OPTION_ID + mOptionList.get(i).getProduct_option_id());
                //          String temp2 = DataStorage.mRetrieveSharedPreferenceString(mContext, JSON_Names.KEY_SECOND_ROW_PRODUCT_OPTION_VALUE_ID + mOptionList.get(i).getProduct_option_id());
                          /*  if (temp1.equals(JSON_Names.KEY_NO_DATA) && temp2.equals(JSON_Names.KEY_NO_DATA)) {
                                if (j == 0) {
                                    dummy += mOptionList.get(i).getProduct_option_name();
                                    j++;
                                } else {
                                    dummy += " " + mContext.getResources().getString(R.string.and) + " " + mOptionList.get(i).getProduct_option_name();
                                }
                            }
                            i++;
                            //}
                        }*/

                if (dummy.length() != 0) {
                    Methods.toast(mContext.getResources().getString(R.string.please_select) + " " + dummy);
                } else {
                    quantity=String.valueOf(int_cart_product_count);
                    Integer minimum_value = Integer.valueOf(quantity);
                    //Check with option quantity for add to cart
                    int minimum_option_value = get_minimum_option_size();
                    if (minimum_value <= minimum_option_value || minimum_option_value == -1) {
                        if (itemsData != null) {
                            if (minimum_value < Integer.valueOf(itemsData.get(getAdapterPosition()).getMinimum())) {
                                String minimum = mContext.getResources().getString(R.string.quantity_check_1) + " " + itemsData.get(getAdapterPosition()).getTitle() + mContext.getResources().getString(R.string.quantity_check_2) +itemsData.get(getAdapterPosition()).getMinimum();
                                Methods.toast(minimum);
                            } else {
                                if (DataBaseHandlerCart.getInstance(mContext).get_Size_cart() == 0) {
                                    Methods.toast(mContext.getResources().getString(R.string.add_to_cart_success));
                                    DataBaseHandlerCart.getInstance(mContext).add_to_cart(1,itemsData.get(getAdapterPosition()).getProduct_id(),int_cart_product_count, mProduct_String, int_cart_product_count);
                                    if (mOptionList != null) {
                                        i = 0;
                                        while (i < mOptionList.size()) {
                                            //if (mOptionList.get(i).getProduct_option_required().equals(JSON_Names.KEY_REQUIRED_VALUE)) {
                                            String temp1 = DataStorage.mRetrieveSharedPreferenceString(mContext,
                                                    JSON_Names.KEY_SECOND_ROW_OPTION_ID + mOptionList.get(i).getProduct_option_id());
                                            String temp2 = DataStorage.mRetrieveSharedPreferenceString(mContext,
                                                    JSON_Names.KEY_SECOND_ROW_PRODUCT_OPTION_VALUE_ID + mOptionList.get(i).getProduct_option_id());
                                            DataBaseHandlerCartOptions.getInstance(mContext).add_options(1, temp1, temp2,
                                                    mProductDataSet.getProduct_id());
                                            i++;
                                            //}
                                        }
                                    }
                                } else {
                                    if (DataBaseHandlerCart.getInstance(mContext).checking_cart(itemsData.get(getAdapterPosition()).getProduct_id())) {
                                        ArrayList<Integer> list = DataBaseHandlerCart.getInstance(mContext).get_index(itemsData.get(getAdapterPosition()).getProduct_id());
                                        int count = DataBaseHandlerCart.getInstance(mContext).getProductCount(list);
                                        if (count <= Integer.valueOf(itemsData.get(getAdapterPosition()).getQuantity())) {
                                            if (list != null) {
                                                int check = DataBaseHandlerCart.getInstance(mContext).getLastIndex();
                                                if (check != 0) {
                                                    int index = check + 1;
                                                    ArrayList<Integer[]> list1 = new ArrayList<>();
                                                    if (mOptionList != null) {
                                                        i = 0;
                                                        while (i < mOptionList.size()) {
                                                            //if (mOptionList.get(i).getProduct_option_required().equals(JSON_Names.KEY_REQUIRED_VALUE)) {
                                                            String temp1 = DataStorage.mRetrieveSharedPreferenceString(mContext, JSON_Names.KEY_SECOND_ROW_OPTION_ID + mOptionList.get(i).getProduct_option_id());
                                                            String temp2 = DataStorage.mRetrieveSharedPreferenceString(mContext, JSON_Names.KEY_SECOND_ROW_PRODUCT_OPTION_VALUE_ID + mOptionList.get(i).getProduct_option_id());
                                                            Integer value[] = {Integer.valueOf(temp1), Integer.valueOf(temp2)};
                                                            list1.add(value);
                                                            i++;
                                                            //}
                                                        }
                                                    }
                                                    int reference = 0;
                                                    for (int k = 0; k < list.size(); k++) {
                                                        if (DataBaseHandlerCartOptions.getInstance(mContext).check_index(list.get(k), list1)) {
                                                            reference = list.get(k);
                                                        }
                                                    }
                                                    if (reference != 0) {
                                                        if (minimum_value + DataBaseHandlerCart.getInstance(mContext)
                                                                .get_product_count(reference) <= minimum_option_value) {
                                                            DataBaseHandlerCart.getInstance(mContext).update_product_count(reference, int_cart_product_count);
                                                            Methods.toast(mContext.getResources().getString(R.string.add_to_cart_success));
                                                        } else {
                                                            Methods.toast(mContext.getResources().getString(R.string.quantity_error_message));
                                                        }
                                                    } else {
                                                        DataBaseHandlerCart.getInstance(mContext).add_to_cart(index, itemsData.get(getAdapterPosition()).getProduct_id(),int_cart_product_count, mProduct_String, int_cart_product_count);
                                                        Methods.toast(mContext.getResources().getString(R.string.add_to_cart_success));
                                                        if (mOptionList != null) {
                                                            i = 0;
                                                            while (i < mOptionList.size()) {
                                                                //if (mOptionList.get(i).getProduct_option_required().equals(JSON_Names.KEY_REQUIRED_VALUE)) {
                                                                String temp1 = DataStorage.mRetrieveSharedPreferenceString(mContext, JSON_Names.KEY_SECOND_ROW_OPTION_ID + mOptionList.get(i).getProduct_option_id());
                                                                String temp2 = DataStorage.mRetrieveSharedPreferenceString(mContext, JSON_Names.KEY_SECOND_ROW_PRODUCT_OPTION_VALUE_ID + mOptionList.get(i).getProduct_option_id());
                                                                DataBaseHandlerCartOptions.getInstance(mContext).add_options(index, temp1, temp2, itemsData.get(getAdapterPosition()).getProduct_id());
                                                                i++;
                                                                // }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            Methods.toast(mContext.getResources().getString(R.string.quantity_error_message));
                                        }

                                    } else {
                                        int check = DataBaseHandlerCart.getInstance(mContext).getLastIndex();
                                        if (check != 0) {
                                            int index = check + 1;
                                            DataBaseHandlerCart.getInstance(mContext).add_to_cart(index, itemsData.get(getAdapterPosition()).getProduct_id(),int_cart_product_count, mProduct_String, int_cart_product_count);
                                            Methods.toast(mContext.getResources().getString(R.string.add_to_cart_success));
                                            if (mOptionList != null) {
                                                i = 0;
                                                while (i < mOptionList.size()) {
                                                    //if (mOptionList.get(i).getProduct_option_required().equals(JSON_Names.KEY_REQUIRED_VALUE)) {
                                                    String temp1 = DataStorage.mRetrieveSharedPreferenceString(mContext, JSON_Names.KEY_SECOND_ROW_OPTION_ID + mOptionList.get(i).getProduct_option_id());
                                                    String temp2 = DataStorage.mRetrieveSharedPreferenceString(mContext, JSON_Names.KEY_SECOND_ROW_PRODUCT_OPTION_VALUE_ID + mOptionList.get(i).getProduct_option_id());
                                                    DataBaseHandlerCartOptions.getInstance(mContext).add_options(index, temp1, temp2, itemsData.get(getAdapterPosition()).getProduct_id());
                                                    i++;
                                                    //}
                                                }
                                            }
                                        }
                                    }
                                }
                                option_reset(mOptionListChild);
                                if (mOptionList != null) {
                                    clearData(mOptionList);
                                }
//                                    productOptionAdapter.notifyDataSetChanged();
                                // ((Home)mContext.getApplicationContext()). invalidateOptionsMenu();
                            }
                        } else {
                            Methods.toast(mContext.getResources().getString(R.string.quantity_error_message));
                        }
                    } else {
                        String minimum = mContext.getResources().getString(R.string.quantity_check_1) + " "
                                + itemsData.get(getAdapterPosition()).getTitle() + mContext.getResources().getString(R.string.quantity_check_2) + " " + minimum_option_value;
                        Methods.toast(minimum);
                    }
                }
            }}}
        public void clearData(ArrayList<ProductOptionDataSet> mOptionList) {
            if (mOptionList != null) {
                for (int i = 0; i < mOptionList.size(); i++) {
                    DataStorage.mRemoveSharedPreferenceString(mContext, JSON_Names.KEY_SECOND_ROW_OPTION_ID
                            + mOptionList.get(i).getProduct_option_id());
                    DataStorage.mRemoveSharedPreferenceString(mContext, JSON_Names.KEY_SECOND_ROW_PRODUCT_OPTION_VALUE_ID
                            + mOptionList.get(i).getProduct_option_id());
                }
            }
        }
        public void option_reset(HashMap<Integer, ArrayList<ProductOptionDataSet>> mOptionListChild) {
            if (mOptionListChild != null) {
                for (int i = 0; i < mOptionListChild.size(); i++) {
                    for (int j = 0; j < mOptionListChild.get(i).size(); j++) {
                        mOptionListChild.get(i).get(j).setSelected(false);
                    }
                }
            }
        }
    /*class ViewHolderLastView extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView view_all_image;

        public ViewHolderLastView(View itemView) {
            super(itemView);
            view_all_image = (ImageView) itemView.findViewById(R.id.view_all_image);
            view_all_image.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.view_all_image:
                    Intent intent = new Intent(mContext, Category_Details.class);
                    intent.putExtra(JSON_Names.KEY_TITLE_SHARED_PREFERENCE, itemsData.get(0).getHeading());
                    if (itemsData.get(0).getHeading().equals(mContext.mContext.getResources().getString(R.string.featured))) {
                        intent.putExtra(JSON_Names.KEY_TYPE_SHARED_PREFERENCE, 11);
                    } else if (itemsData.get(0).getHeading().equals(mContext.mContext.getResources().getString(R.string.latest))) {
                        intent.putExtra(JSON_Names.KEY_TYPE_SHARED_PREFERENCE, 22);
                    } else if (itemsData.get(0).getHeading().equals(mContext.mContext.getResources().getString(R.string.special))) {
                        intent.putExtra(JSON_Names.KEY_TYPE_SHARED_PREFERENCE, 33);
                    }

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    break;
            }
        }
    }*/

    private void transfer(int position) {
        Intent intent = new Intent(mContext, Product_Details.class);
        intent.putExtra(JSON_Names.KEY_PRODUCT_STRING, itemsData.get(position).getProduct_id());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }


   /* @Override
    public int getItemViewType(int position) {
        /*if (itemsData.size() > 7) {
            if (position < 6) {
                return PRODUCT;
            } else {
                return FULL_VIEW;
            }
        } else {
            int size = itemsData.size();
            if (position < size) {
                return PRODUCT;
            } else {
                return FULL_VIEW;
            }
        }*/
    /*    return PRODUCT;
    }*/
       public int get_minimum_option_size() {
            int list[] = new int[mOptionList.size()];
            if (mOptionList != null && mOptionList.size() > 0) {
                for (int i = 0; i < mOptionList.size(); i++) {
                    //if (mOptionList.get(i).getProduct_option_required().equals(JSON_Names.KEY_REQUIRED_VALUE)) {
                    String option_id = DataStorage.mRetrieveSharedPreferenceString(mContext,
                            JSON_Names.KEY_SECOND_ROW_PRODUCT_OPTION_VALUE_ID + mOptionList.get(i).getProduct_option_id());

                    for (int j = 0; j < mOptionList.size(); j++) {
                        if (option_id.equals(mOptionListChild.get(i).get(j).getProduct_option_value_id())) {
                            list[i] = Integer.valueOf( itemsData.get(getAdapterPosition()).getQuantity());
                        }
                    }

                    // }
                }
                if (list.length != 0) {
                    Arrays.sort(list);
                }
                return list.length > 0 ? list[0] : -1;
            } else {
                return Integer.valueOf(itemsData.get(getAdapterPosition()).getQuantity());
            }
        }

    }}
