package com.restaurent.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.restaurent.R;
import com.restaurent.activity.Home;
import com.restaurent.activity.product.Product_Details;
import com.restaurent.activity.user.Login;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.constant_class.URL_Class;
import com.restaurent.db_handler.DataBaseHandlerAccount;
import com.restaurent.db_handler.DataBaseHandlerCart;
import com.restaurent.db_handler.DataBaseHandlerWishList;
import com.restaurent.interfaces.WishListAPIRequest;
import com.restaurent.mechanism.Methods;
import com.restaurent.shared_preferenc_estring.DataStorage;
import com.restaurent.utils.ProductDataSet;

import java.net.URLEncoder;
import java.util.ArrayList;

public class Product_Listing_Gird_And_List extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    ArrayList<ProductDataSet> mCategoryList;
    boolean mType;
    int GRID = 1, LIST = 2, EMPTY = 3;
    WishListAPIRequest wishListAPIRequest;
    String add[] = {URL_Class.mURL + URL_Class.mURL_Add_To_WishList}, remove[] = {URL_Class.mURL + URL_Class.mURL_Remove_WishList};

    OnLoadMoreListener onLoadMoreListener;
    final int VIEW_PROGRESS = 0;
    private int visibleThreshold = 6;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;


    public Product_Listing_Gird_And_List(Context context, ArrayList<ProductDataSet> list, boolean type,
                                         WishListAPIRequest wishListAPIRequest, RecyclerView recyclerView) {
        this.mContext = context;
        this.mCategoryList = list;
        this.mType = type;
        this.wishListAPIRequest = wishListAPIRequest;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder parentHolder;
        View view;
        if (viewType == GRID) {
            view = LayoutInflater.from(mContext).inflate(R.layout.category_grid_column, parent, false);
            parentHolder = new Grid_Holder(view);
        } else if (viewType == LIST) {
            view = LayoutInflater.from(mContext).inflate(R.layout.category_list_column, parent, false);
            parentHolder = new List_Holder(view);
        } else if (viewType == EMPTY) {
            view = LayoutInflater.from(mContext).inflate(R.layout.no_data, parent, false);
            parentHolder = new ViewHolderEmpty_View(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.progress_bar_layout, parent, false);
            parentHolder = new ProgressViewHolder(view);
        }
        return parentHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == LIST) {

            if (mCategoryList != null && mCategoryList.size() > 0) {
                List_Holder list = (List_Holder) holder;
                list.mCategory_special_p.setVisibility(View.VISIBLE);
                list.mCategory_special_sp.setVisibility(View.VISIBLE);
                list.mCategory_price.setVisibility(View.VISIBLE);

                if (mCategoryList.get(position) != null) {
                    image_caller(mCategoryList.get(position).getImage(), list.mCategory_image);
                    list.mCategory_title.setText(mCategoryList.get(position).getTitle());
                    if (mCategoryList.get(position).getSpecial_price().isEmpty()) {
                        list.mCategory_price.setText(mCategoryList.get(position).getPrice());
                        list.mCategory_special_p.setVisibility(View.GONE);
                        list.mCategory_special_sp.setVisibility(View.GONE);
                    } else {
                        list.mCategory_price.setVisibility(View.GONE);
                        list.mCategory_special_p.setText(mCategoryList.get(position).getPrice());
                        list.mCategory_special_p.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        list.mCategory_special_sp.setText(mCategoryList.get(position).getSpecial_price());
                    }
                    if (DataBaseHandlerWishList.getInstance(mContext.getApplicationContext()).checking_wish_list(mCategoryList.get(position).getProduct_id())) {
                        list.mCategory_fav.setImageResource(R.drawable.ic_favorite_orange_500_24dp);
                    } else {
                        list.mCategory_fav.setImageResource(R.drawable.ic_favorite_border_grey_500_24dp);
                    }
                }
            }
        } else if (holder.getItemViewType() == GRID) {
            if (mCategoryList != null && mCategoryList.size() > 0) {
                Grid_Holder grid = (Grid_Holder) holder;
                grid.mCategory_special_p.setVisibility(View.VISIBLE);
                grid.mCategory_special_sp.setVisibility(View.VISIBLE);
                grid.mCategory_price.setVisibility(View.VISIBLE);

                if (mCategoryList.get(position) != null) {
                    image_caller(mCategoryList.get(position).getImage(), grid.mCategory_image);
                    grid.mCategory_title.setText(mCategoryList.get(position).getTitle());

                    if (mCategoryList.get(position).getSpecial_price().isEmpty()) {
                        grid.mCategory_price.setText(mCategoryList.get(position).getPrice());
                        grid.mCategory_special_p.setVisibility(View.VISIBLE);
                        grid.mCategory_special_sp.setVisibility(View.GONE);

                    } else {
                        grid.mCategory_price.setVisibility(View.GONE);
                        grid.mCategory_special_p.setText(mCategoryList.get(position).getPrice());
                        grid.mCategory_special_p.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        grid.mCategory_special_sp.setText(mCategoryList.get(position).getSpecial_price());
                    }
                    if (DataBaseHandlerWishList.getInstance(mContext.getApplicationContext()).checking_wish_list(mCategoryList.get(position).getProduct_id())) {
                        grid.mCategory_fav.setImageResource(R.drawable.ic_favorite_orange_500_24dp);
                    } else {
                        grid.mCategory_fav.setImageResource(R.drawable.ic_favorite_border_grey_500_24dp);
                    }
                }
            }
        } else if (holder.getItemViewType() == EMPTY) {
            ViewHolderEmpty_View empty_view = (ViewHolderEmpty_View) holder;
            empty_view.empty_view.setText(R.string.empty_category);
        } else {
            ProgressViewHolder progressViewHolder = (ProgressViewHolder) holder;
            progressViewHolder.progressBar.setIndeterminate(true);
        }
    }

    public void image_caller(String url, ImageView imageView) {
        Methods.glide_image_loader_fixed_size(url, imageView);
    }

    @Override
    public int getItemCount() {
        if (mCategoryList != null) {
            if (mCategoryList.size() > 0) {
                return mCategoryList.size();
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mCategoryList != null && !mCategoryList.isEmpty()) {
            if(mCategoryList.get(position)!=null) {
                if (mType) {
                    return GRID;
                } else {
                    return LIST;
                }
            }else {
                return VIEW_PROGRESS;
            }
        } else {
            return EMPTY;
        }
    }

    public void test(View v, int position) {
        //DataStorage.mStoreSharedPreferenceString(mContext, JSON_Names.KEY_PRODUCT_STRING, mCategoryList.get(position).getProduct_string());
        Intent intent = new Intent(v.getContext(), Product_Details.class);
        intent.putExtra(JSON_Names.KEY_PRODUCT_STRING, mCategoryList.get(position).getProduct_id());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        v.getContext().startActivity(intent);
    }

    public void addToWishList(ImageButton mCategory_fav, View v, int position) {
        if (DataBaseHandlerAccount.getInstance(mContext.getApplicationContext()).check_login()) {
            if (!DataBaseHandlerWishList.getInstance(mContext.getApplicationContext()).checking_wish_list(mCategoryList.get(position).getProduct_id())) {
                DataStorage.mStoreSharedPreferenceString(mContext.getApplicationContext(), JSON_Names.KEY_CURRENT_PRODUCT_ID, mCategoryList.get(position).getProduct_id());
                DataBaseHandlerWishList.getInstance(mContext.getApplicationContext()).add_to_wish_list(mCategoryList.get(position).getProduct_id(), mCategoryList.get(position).getProduct_string());
                if (get_wish_list_post_data() != null) {
                    wishListAPIRequest.wish_list_api_request(get_wish_list_post_data(), add);
                }
                mCategory_fav.setImageResource(R.drawable.ic_favorite_orange_500_24dp);
            } else {
                DataStorage.mStoreSharedPreferenceString(mContext.getApplicationContext(), JSON_Names.KEY_CURRENT_PRODUCT_ID, mCategoryList.get(position).getProduct_id());
                DataBaseHandlerWishList.getInstance(mContext.getApplicationContext()).remove_from_wish_list(mCategoryList.get(position).getProduct_id());
                mCategory_fav.setImageResource(R.drawable.ic_favorite_border_grey_500_24dp);
                if (get_wish_list_post_data() != null) {
                    wishListAPIRequest.wish_list_api_request(get_wish_list_post_data(), remove);
                }
            }
        } else {
            Intent intent = new Intent(v.getContext(), Login.class);
            DataStorage.mStoreSharedPreferenceString(mContext.getApplicationContext(), JSON_Names.KEY_CURRENT_PRODUCT_ID, mCategoryList.get(position).getProduct_id());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            v.getContext().startActivity(intent);
        }
    }

    public class Grid_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mCategory_image;
        TextView mCategory_title;
        TextView mCategory_price;
        TextView mCategory_special_p;
        TextView mCategory_special_sp;
        ImageButton mCategory_fav;
        String mProduct_String;
        int count;
        int int_cart_product_count=0;
        String str_cart_product_count;
        TextView cart_product_count;
        TextView add_to_cart;
        LinearLayout linear_add_to_cart;
        ImageButton cart_product_add,cart_product_minus;
        Grid_Holder(View view) {
            super(view);
            mCategory_image = (ImageView) view.findViewById(R.id.category_image);
            mCategory_title = (TextView) view.findViewById(R.id.category_title);
            mCategory_price = (TextView) view.findViewById(R.id.category_price);
            mCategory_special_p = (TextView) view.findViewById(R.id.category_special_price_p);
            mCategory_special_sp = (TextView) view.findViewById(R.id.category_special_price_sp);
            mCategory_fav = (ImageButton) view.findViewById(R.id.category_wishList);
            add_to_cart=(TextView)view.findViewById(R.id.column_add_to_cart);
            cart_product_add=(ImageButton)view.findViewById(R.id.cart_product_count_add);
            cart_product_minus=(ImageButton)view.findViewById(R.id.cart_product_count_remove);
            cart_product_count=(TextView)view.findViewById(R.id.cart_product_count);
            str_cart_product_count=cart_product_count.getText().toString();
            int_cart_product_count=Integer.parseInt(str_cart_product_count);
            linear_add_to_cart=(LinearLayout)view.findViewById(R.id.linear_add_to_cart);
            linear_add_to_cart.setVisibility(View.GONE);
            mCategory_fav.setOnClickListener(this);
            mCategory_image.setOnClickListener(this);
            mCategory_title.setOnClickListener(this);
            mCategory_price.setOnClickListener(this);
            mCategory_special_p.setOnClickListener(this);
            mCategory_special_sp.setOnClickListener(this);
            add_to_cart.setOnClickListener(this);
            //  quantity=mCategoryList.get(getAdapterPosition()).getQuantity();
            // int_quantity=Integer.parseInt(quantity);
            cart_product_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int_cart_product_count++;

                    cart_product_count.setText(""+int_cart_product_count);
                    if(DataBaseHandlerCart.getInstance(mContext).checking_cart(mCategoryList.get(getAdapterPosition()).getProduct_id()))
                    {
                        ArrayList<Integer> list = DataBaseHandlerCart.getInstance(mContext).get_index(mCategoryList.get(getAdapterPosition()).getProduct_id());
                        for(int i=0;i<list.size();i++) {
                            count = list.get(i);
                        }
                        Log.e("Update",String.valueOf(count));
                        int reference=int_cart_product_count+count;
                        DataBaseHandlerCart.getInstance(mContext).update_product_count(count, 1);
                        Home.update_cart(mCategoryList.get(getAdapterPosition()).getImage());
                        Toast.makeText(mContext,mContext.getResources().getString(R.string.add_to_cart_success),Toast.LENGTH_SHORT).show();

                    }
                    else{
                        if( DataBaseHandlerCart.getInstance(mContext).get_Size_cart()!=0) {
                            ArrayList<Integer> list = DataBaseHandlerCart.getInstance(mContext).get_all_index();
                            for(int i=0;i<list.size();i++) {
                                count = list.get(i);
                            }

                            int refrence=count+1;
                            DataBaseHandlerCart.getInstance(mContext).add_to_cart(refrence, mCategoryList.get(getAdapterPosition()).getProduct_id(), int_cart_product_count, mProduct_String, 1000);
                            Home.update_cart(mCategoryList.get(getAdapterPosition()).getImage());


                        }
                        else{
                            ArrayList<Integer> list = DataBaseHandlerCart.getInstance(mContext).get_all_index();
                            for(int i=0;i<list.size();i++) {
                                count = list.get(i);
                            }

                            int refrence=count+1;
                            DataBaseHandlerCart.getInstance(mContext).add_to_cart(refrence, mCategoryList.get(getAdapterPosition()).getProduct_id(), int_cart_product_count, mProduct_String, 1000);
                            Home.update_cart(mCategoryList.get(getAdapterPosition()).getImage());
                            Toast.makeText(mContext,mContext.getResources().getString(R.string.add_to_cart_success),Toast.LENGTH_SHORT).show();
                        }
                    }
                    //  add_to_count();

                    //  add_to_count();
                }
            });
            cart_product_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(int_cart_product_count==1){
                        linear_add_to_cart.setVisibility(View.GONE);
                        add_to_cart.setVisibility(View.VISIBLE);
                        ArrayList<Integer> list = DataBaseHandlerCart.getInstance(mContext).get_index(mCategoryList.get(getAdapterPosition()).getProduct_id());
                        for(int i=0;i<list.size();i++) {
                            count = list.get(i);
                        }
                        DataBaseHandlerCart.getInstance(mContext).update_product_count(count,-1);
                        Home.update_cart(mCategoryList.get(getAdapterPosition()).getImage());
                        Toast.makeText(mContext,"Remove from Cart Successfully",Toast.LENGTH_SHORT).show();

                        //  DataBaseHandlerCart.getInstance(mContext).remove_cart(mCategoryList.get(getAdapterPosition()).getProduct_id());
                    }
                    else {
                        int_cart_product_count--;
                        //
                        cart_product_count.setText("" + int_cart_product_count);
                        if(DataBaseHandlerCart.getInstance(mContext).checking_cart(mCategoryList.get(getAdapterPosition()).getProduct_id()))
                        {
                            ArrayList<Integer> list = DataBaseHandlerCart.getInstance(mContext).get_all_index();
                            for(int i=0;i<list.size();i++) {
                                count = list.get(i);
                            }
                            Log.e("Update",String.valueOf(count));
                            int reference=int_cart_product_count+count;
                            DataBaseHandlerCart.getInstance(mContext).update_product_count(count,-1);
                            Home.update_cart(mCategoryList.get(getAdapterPosition()).getImage());
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
                case R.id.category_wishList:
                    addToWishList(mCategory_fav, v, getAdapterPosition());
                    break;
                case R.id.category_image:
                    test(v, getAdapterPosition());
                    break;
                case R.id.category_title:
                    test(v, getAdapterPosition());
                    break;
                case R.id.category_price:
                    test(v, getAdapterPosition());
                    break;
                case R.id.category_special_price_p:
                    test(v, getAdapterPosition());
                    break;
                case R.id.category_special_price_sp:
                    test(v, getAdapterPosition());
                    break;
                case R.id.column_add_to_cart:
                    linear_add_to_cart.setVisibility(View.VISIBLE);
                    add_to_cart.setVisibility(View.GONE);
                    if (DataBaseHandlerCart.getInstance(mContext).checking_cart(mCategoryList.get(getAdapterPosition()).getProduct_id())) {
                        ArrayList<Integer> list = DataBaseHandlerCart.getInstance(mContext).get_index(mCategoryList.get(getAdapterPosition()).getProduct_id());
                        for (int i = 0; i < list.size(); i++) {
                            count = list.get(i);
                        }
                        Log.e("Update", String.valueOf(count));
                        int reference = int_cart_product_count + count;
                        DataBaseHandlerCart.getInstance(mContext).update_product_count(count, 1);
                        // notifyDataSetChanged();
                        Home.update_cart(mCategoryList.get(getAdapterPosition()).getImage());
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.add_to_cart_success), Toast.LENGTH_SHORT).show();

                        // refresh();

                    } else {
                        if (DataBaseHandlerCart.getInstance(mContext).get_Size_cart() != 0) {
                            ArrayList<Integer> list = DataBaseHandlerCart.getInstance(mContext).get_all_index();
                            for (int i = 0; i < list.size(); i++) {
                                count = list.get(i);
                            }
                            int refrence = count + 1;
                            DataBaseHandlerCart.getInstance(mContext).add_to_cart(refrence, mCategoryList.get(getAdapterPosition()).getProduct_id(), int_cart_product_count, mProduct_String, 1000);
                            Home.update_cart(mCategoryList.get(getAdapterPosition()).getImage());
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.add_to_cart_success), Toast.LENGTH_SHORT).show();

                        } else {
                            ArrayList<Integer> list = DataBaseHandlerCart.getInstance(mContext).get_all_index();
                            for (int i = 0; i < list.size(); i++) {
                                count = list.get(i);
                            }
                            int refrence = count + 1;
                            DataBaseHandlerCart.getInstance(mContext).add_to_cart(refrence, mCategoryList.get(getAdapterPosition()).getProduct_id(), int_cart_product_count, mProduct_String, 1000);
                            Home.update_cart(mCategoryList.get(getAdapterPosition()).getImage());
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.add_to_cart_success), Toast.LENGTH_SHORT).show();


                        }


                    }
                    break;
            }}

    }

    public class List_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mCategory_image;
        TextView mCategory_title;
        TextView mCategory_price;
        TextView mCategory_special_p;
        TextView mCategory_special_sp;
        ImageButton mCategory_fav;
        String mProduct_String;
        int count;
        int int_cart_product_count=0;
        String str_cart_product_count;
        TextView cart_product_count;
        TextView add_to_cart;
        LinearLayout linear_add_to_cart;
        ImageButton cart_product_add,cart_product_minus;
        List_Holder(View view) {
            super(view);
            mCategory_image = (ImageView) view.findViewById(R.id.category_image);
            mCategory_title = (TextView) view.findViewById(R.id.category_title);
            mCategory_price = (TextView) view.findViewById(R.id.category_price);
            mCategory_special_p = (TextView) view.findViewById(R.id.category_special_price_p);
            mCategory_special_sp = (TextView) view.findViewById(R.id.category_special_price_sp);
            mCategory_fav = (ImageButton) view.findViewById(R.id.category_wishList);
            add_to_cart=(TextView)view.findViewById(R.id.column_add_to_cart);
            cart_product_add=(ImageButton)view.findViewById(R.id.cart_product_count_add);
            cart_product_minus=(ImageButton)view.findViewById(R.id.cart_product_count_remove);
            cart_product_count=(TextView)view.findViewById(R.id.cart_product_count);
            str_cart_product_count=cart_product_count.getText().toString();
            int_cart_product_count=Integer.parseInt(str_cart_product_count);
            linear_add_to_cart=(LinearLayout)view.findViewById(R.id.linear_add_to_cart);
            linear_add_to_cart.setVisibility(View.GONE);
            mCategory_fav.setOnClickListener(this);
            mCategory_image.setOnClickListener(this);
            mCategory_title.setOnClickListener(this);
            mCategory_price.setOnClickListener(this);
            mCategory_special_p.setOnClickListener(this);
            mCategory_special_sp.setOnClickListener(this);

            add_to_cart.setOnClickListener(this);
            //  quantity=mCategoryList.get(getAdapterPosition()).getQuantity();
            // int_quantity=Integer.parseInt(quantity);
            cart_product_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int_cart_product_count++;

                    cart_product_count.setText(""+int_cart_product_count);
                    if(DataBaseHandlerCart.getInstance(mContext).checking_cart(mCategoryList.get(getAdapterPosition()).getProduct_id()))
                    {
                        ArrayList<Integer> list = DataBaseHandlerCart.getInstance(mContext).get_index(mCategoryList.get(getAdapterPosition()).getProduct_id());
                        for(int i=0;i<list.size();i++) {
                            count = list.get(i);
                        }
                        Log.e("Update",String.valueOf(count));
                        int reference=int_cart_product_count+count;
                        DataBaseHandlerCart.getInstance(mContext).update_product_count(count, 1);
                        Home.update_cart(mCategoryList.get(getAdapterPosition()).getImage());
                        Toast.makeText(mContext,mContext.getResources().getString(R.string.add_to_cart_success),Toast.LENGTH_SHORT).show();

                    }
                    else{
                        if( DataBaseHandlerCart.getInstance(mContext).get_Size_cart()!=0) {
                            ArrayList<Integer> list = DataBaseHandlerCart.getInstance(mContext).get_all_index();
                            for(int i=0;i<list.size();i++) {
                                count = list.get(i);
                            }

                            int refrence=count+1;
                            DataBaseHandlerCart.getInstance(mContext).add_to_cart(refrence, mCategoryList.get(getAdapterPosition()).getProduct_id(), int_cart_product_count, mProduct_String, 1000);
                            Home.update_cart(mCategoryList.get(getAdapterPosition()).getImage());


                        }
                        else{
                            ArrayList<Integer> list = DataBaseHandlerCart.getInstance(mContext).get_all_index();
                            for(int i=0;i<list.size();i++) {
                                count = list.get(i);
                            }

                            int refrence=count+1;
                            DataBaseHandlerCart.getInstance(mContext).add_to_cart(refrence, mCategoryList.get(getAdapterPosition()).getProduct_id(), int_cart_product_count, mProduct_String, 1000);
                            Home.update_cart(mCategoryList.get(getAdapterPosition()).getImage());
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
                        ArrayList<Integer> list = DataBaseHandlerCart.getInstance(mContext).get_index(mCategoryList.get(getAdapterPosition()).getProduct_id());
                        for(int i=0;i<list.size();i++) {
                            count = list.get(i);
                        }
                        DataBaseHandlerCart.getInstance(mContext).update_product_count(count,-1);
                        Home.update_cart(mCategoryList.get(getAdapterPosition()).getImage());
                        Toast.makeText(mContext,"Remove from Cart Successfully",Toast.LENGTH_SHORT).show();

                        //  DataBaseHandlerCart.getInstance(mContext).remove_cart(mCategoryList.get(getAdapterPosition()).getProduct_id());
                    }
                    else {
                        int_cart_product_count--;
                        //
                        cart_product_count.setText("" + int_cart_product_count);
                        if(DataBaseHandlerCart.getInstance(mContext).checking_cart(mCategoryList.get(getAdapterPosition()).getProduct_id()))
                        {
                            ArrayList<Integer> list = DataBaseHandlerCart.getInstance(mContext).get_all_index();
                            for(int i=0;i<list.size();i++) {
                                count = list.get(i);
                            }
                            Log.e("Update",String.valueOf(count));
                            int reference=int_cart_product_count+count;
                            DataBaseHandlerCart.getInstance(mContext).update_product_count(count,-1);
                            Home.update_cart(mCategoryList.get(getAdapterPosition()).getImage());
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
                case R.id.category_wishList:
                    addToWishList(mCategory_fav, v, getAdapterPosition());
                    break;
                case R.id.category_image:
                    test(v, getAdapterPosition());
                    break;
                case R.id.category_title:
                    test(v, getAdapterPosition());
                    break;
                case R.id.category_price:
                    test(v, getAdapterPosition());
                    break;
                case R.id.category_special_price_p:
                    test(v, getAdapterPosition());
                    break;
                case R.id.category_special_price_sp:
                    test(v, getAdapterPosition());
                    break;
                case R.id.column_add_to_cart:
                    linear_add_to_cart.setVisibility(View.VISIBLE);
                    add_to_cart.setVisibility(View.GONE);
                    if (DataBaseHandlerCart.getInstance(mContext).checking_cart(mCategoryList.get(getAdapterPosition()).getProduct_id())) {
                        ArrayList<Integer> list = DataBaseHandlerCart.getInstance(mContext).get_index(mCategoryList.get(getAdapterPosition()).getProduct_id());
                        for (int i = 0; i < list.size(); i++) {
                            count = list.get(i);
                        }
                        Log.e("Update", String.valueOf(count));
                        int reference = int_cart_product_count + count;
                        DataBaseHandlerCart.getInstance(mContext).update_product_count(count, 1);
                        // notifyDataSetChanged();
                        Home.update_cart(mCategoryList.get(getAdapterPosition()).getImage());
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.add_to_cart_success), Toast.LENGTH_SHORT).show();

                        // refresh();

                    } else {
                        if (DataBaseHandlerCart.getInstance(mContext).get_Size_cart() != 0) {
                            ArrayList<Integer> list = DataBaseHandlerCart.getInstance(mContext).get_all_index();
                            for (int i = 0; i < list.size(); i++) {
                                count = list.get(i);
                            }
                            int refrence = count + 1;
                            DataBaseHandlerCart.getInstance(mContext).add_to_cart(refrence, mCategoryList.get(getAdapterPosition()).getProduct_id(), int_cart_product_count, mProduct_String, 1000);
                            Home.update_cart(mCategoryList.get(getAdapterPosition()).getImage());
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.add_to_cart_success), Toast.LENGTH_SHORT).show();

                        } else {
                            ArrayList<Integer> list = DataBaseHandlerCart.getInstance(mContext).get_all_index();
                            for (int i = 0; i < list.size(); i++) {
                                count = list.get(i);
                            }
                            int refrence = count + 1;
                            DataBaseHandlerCart.getInstance(mContext).add_to_cart(refrence, mCategoryList.get(getAdapterPosition()).getProduct_id(), int_cart_product_count, mProduct_String, 1000);
                            Home.update_cart(mCategoryList.get(getAdapterPosition()).getImage());
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.add_to_cart_success), Toast.LENGTH_SHORT).show();


                        }


                    }
//add_to_count();

                    break;

            }
        }
    }

    public class ViewHolderEmpty_View extends RecyclerView.ViewHolder {
        TextView empty_view;

        public ViewHolderEmpty_View(View view) {
            super(view);
            empty_view = (TextView) view.findViewById(R.id.empty_view);
        }
    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
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

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoaded() {
        loading = false;
    }
}
