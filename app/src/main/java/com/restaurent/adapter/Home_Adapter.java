package com.restaurent.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.restaurent.R;
import com.restaurent.activity.Search;
import com.restaurent.activity.category.Category_Details;
import com.restaurent.activity.user.Special_product;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.interfaces.Refresher;
import com.restaurent.json_mechanism.GetJSONData;
import com.restaurent.mechanism.Methods;
import com.restaurent.shared_preferenc_estring.DataStorage;
import com.restaurent.utils.CategoryDataSet;
import com.restaurent.utils.ProductDataSet;

import java.util.ArrayList;
import java.util.logging.Handler;




public class Home_Adapter extends RecyclerView.Adapter<ViewHolder> implements View.OnTouchListener {
    final int KEY_SLIDE = 4, KEY_LIST = 3, KEY_BANNER = 1, KEY_CATEGORY = 2;
    Context mContext;
    ArrayList<Object> mWholeList = new ArrayList<>();
    Refresher refresher;
    int pos;
    String[] banner_url;
    int slide_btn_fix_size = 0;
    Boolean isScrolling = false;

    public Home_Adapter(Context context, ArrayList<Object> mHomeViewOrder) {
        this.mContext = context;
        this.mWholeList = mHomeViewOrder;

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        switch (id) {
            case R.id.test_search_view:
                Intent intent = new Intent(v.getContext(), Search.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
                break;
        }
        return false;
    }

    @Override
    public int getItemViewType(int position) {
        if (mWholeList.get(position) instanceof String[]) {
            return KEY_SLIDE;
        } else if (mWholeList.get(position) instanceof String) {
            return KEY_BANNER;
        } else if (mWholeList.get(position) instanceof Boolean) {
            return KEY_CATEGORY;
        } else {
            return KEY_LIST;
        }

    }

    @Override
    public int getItemCount() {

        return mWholeList.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case KEY_SLIDE:
                banner_url = (String[]) mWholeList.get(position);
                if (banner_url != null) {
                    if (banner_url.length > 0) {
                        if (slide_btn_fix_size == 0) {
                            final ViewHolder_SlideView holderSlide = (ViewHolder_SlideView) holder;
                            holderSlide.slide_view.setAdapter(new Adapter_SlideView(mContext, banner_url));
                            pos = holderSlide.slide_view.getCurrentItem();
                            for (int i = 0; i < banner_url.length; i++) {
                                View view = LayoutInflater.from(mContext).inflate(R.layout.slide_button, null, false);
                                final Button button = (Button) view.findViewById(R.id.btn_one);
                                button.setPadding(100, 100, 100, 100);
                                int size = (int) mContext.getResources().getDisplayMetrics().density;
                                if (size == 3) {
                                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(20, 20);
                                    button.setLayoutParams(layoutParams);
                                } else if (size == 2) {
                                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(15, 15);
                                    button.setLayoutParams(layoutParams);
                                } else {
                                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(12, 12);
                                    button.setLayoutParams(layoutParams);
                                }
                                if (pos == i) {
                                    button.setBackgroundResource(R.drawable.btn_slider_bg);
                                } else {
                                    button.setBackgroundResource(R.drawable.round_button);
                                }
                                button.setId(i);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        holderSlide.slide_view.setCurrentItem(button.getId());
                                    }
                                });
                                holderSlide.button_holder.addView(button);
                            }


                            holderSlide.slide_view.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                }

                                @Override
                                public void onPageSelected(int position) {
                                    holderSlide.button_holder.removeAllViews();
                                    if (banner_url != null) {
                                        if (banner_url.length > 0) {
                                            for (int i = 0; i < banner_url.length; i++) {
                                                View view = LayoutInflater.from(mContext).inflate(R.layout.slide_button, null, false);
                                                final Button button = (Button) view.findViewById(R.id.btn_one);
                                                int size = (int) mContext.getResources().getDisplayMetrics().density;
                                                if (size == 3) {
                                                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(20, 20);
                                                    button.setLayoutParams(layoutParams);
                                                } else if (size == 2) {
                                                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(15, 15);
                                                    button.setLayoutParams(layoutParams);
                                                } else {
                                                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(12, 12);
                                                    button.setLayoutParams(layoutParams);
                                                }
                                                if (position == i) {
                                                    button.setBackgroundResource(R.drawable.btn_slider_bg);
                                                } else {
                                                    button.setBackgroundResource(R.drawable.round_button);
                                                }
                                                button.setId(i);
                                                button.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        holderSlide.slide_view.setCurrentItem(button.getId());
                                                    }
                                                });
                                                holderSlide.button_holder.addView(button);
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {
                                    if (state == ViewPager.SCROLL_STATE_IDLE) {//Its too fast
                                        if (banner_url.length > 0) {
                                            if (banner_url.length - 1 == holderSlide.slide_view.getCurrentItem()) {
                                                holderSlide.slide_view.setCurrentItem(0);
                                            } else {
                                                holderSlide.slide_view.setCurrentItem(holderSlide.slide_view.getCurrentItem() + 1);
                                            }
                                        }
                                        //Log.e("Testing 1", "Checking 1");
                                    } else if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                                        isScrolling = true;
                                        //Log.e("Testing 2", "Checking 2");
                                    } else if (state == ViewPager.SCROLL_STATE_SETTLING) {
                                        //Log.e("Testing 3", "Checking 3");
                                    }
                                }
                            });
                            slide_btn_fix_size++;
                        }
                    }
                }

                break;
            case KEY_BANNER:
                ViewHolder_Image_view holderImage = (ViewHolder_Image_view) holder;
                String url = (String) mWholeList.get(position);
                Methods.glide_image_loader(url, holderImage.testImage);
                break;
            case KEY_CATEGORY:
                ArrayList<CategoryDataSet> mHomeCategoryList;
                mHomeCategoryList = GetJSONData.getHomeCategoryData(
                        DataStorage.mRetrieveSharedPreferenceString(mContext.getApplicationContext(), JSON_Names.KEY_NAVIGATION_DATA));
                ViewHolder_Category_Recycler_view viewHolder_category_recycler_view = (ViewHolder_Category_Recycler_view) holder;
                viewHolder_category_recycler_view.child_Category_Recycler_view.setLayoutManager(new GridLayoutManager(mContext, 2));
                /*viewHolder_category_recycler_view.child_Category_Recycler_view.setLayoutManager(new LinearLayoutManager(mContext,
                        LinearLayoutManager.HORIZONTAL, false));*/
                if (mHomeCategoryList != null) {
                    if (mHomeCategoryList.size() != 0)
                        viewHolder_category_recycler_view.child_Category_Recycler_view.setAdapter(new HomeCategoryAdapter(mContext, mHomeCategoryList));
                }
                break;
            default:
                ViewHolder_Recycler_view holderRecycler_view = (ViewHolder_Recycler_view) holder;
                final ArrayList<ProductDataSet> list = (ArrayList<ProductDataSet>) mWholeList.get(position);
                if (list != null) {
                    holderRecycler_view.title.setActivated(true);
                    holderRecycler_view.title.setText(list.get(0).getHeading());
                    holderRecycler_view.view_all.getParent().requestDisallowInterceptTouchEvent(true);
                    holderRecycler_view.childRecycler_view.setNestedScrollingEnabled(false);
                    holderRecycler_view.childRecycler_view.setLayoutManager(new GridLayoutManager(mContext,2,GridLayoutManager.VERTICAL,false));
                    holderRecycler_view.childRecycler_view.setAdapter(new Adapter_Row(list, mContext,refresher));
                    /*holderRecycler_view.childRecycler_view.addOnItemTouchListener(new RecyclerViewClickEventHandler(mContext, new RecyclerViewClickEventHandler.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent = new Intent(mContext, Product_Details.class);
                            intent.putExtra(JSON_Names.KEY_PRODUCT_STRING, list.get(position).getProduct_id());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);
                        }
                    }));*/
                    holderRecycler_view.view_all.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, Category_Details.class);
                            intent.putExtra(JSON_Names.KEY_TITLE_SHARED_PREFERENCE, list.get(0).getHeading());
                            if (list.get(0).getHeading().equals(mContext.getResources().getString(R.string.featured))) {
                                intent.putExtra(JSON_Names.KEY_TYPE_SHARED_PREFERENCE, 11);
                            } else if (list.get(0).getHeading().equals(mContext.getResources().getString(R.string.latest))) {
                                intent.putExtra(JSON_Names.KEY_TYPE_SHARED_PREFERENCE, 22);
                            } else if (list.get(0).getHeading().equals(mContext.getResources().getString(R.string.special))) {
                                intent.putExtra(JSON_Names.KEY_TYPE_SHARED_PREFERENCE, 33);
                            }

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);
                        }
                    });
                }
                break;
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        switch (viewType) {
            case KEY_CATEGORY:
                View v1 = inflater.inflate(R.layout.home_category_holder, viewGroup, JSON_Names.KEY_FALSE_BOOLEAN);
                viewHolder = new ViewHolder_Category_Recycler_view(v1);
                break;
            case KEY_BANNER:
                View v3 = inflater.inflate(R.layout.home_image_view, viewGroup, JSON_Names.KEY_FALSE_BOOLEAN);
                viewHolder = new ViewHolder_Image_view(v3);
                break;
            case KEY_SLIDE:

            View v2 = inflater.inflate(R.layout.home_sliderview, viewGroup, JSON_Names.KEY_FALSE_BOOLEAN);
            viewHolder = new ViewHolder_SlideView(v2);
            break;
            default:
                View v4 = inflater.inflate(R.layout.home_inner_recycler_view, viewGroup, JSON_Names.KEY_FALSE_BOOLEAN);
                viewHolder = new ViewHolder_Recycler_view(v4);
                break;
        }
        return viewHolder;
    }

    public class ViewHolder_SlideView extends ViewHolder {
        ViewPager slide_view;
        LinearLayout button_holder;
        ViewFlipper viewFlipper;
        public ViewHolder_SlideView(View view) {
            super(view);
            slide_view = (ViewPager) view.findViewById(R.id.home_slider_view_view_pager);
            button_holder = (LinearLayout) view.findViewById(R.id.home_slider_view_view_pager_button_holder);
            button_holder.setVisibility(View.GONE);
            slide_view.setVisibility(View.GONE);

        }
    }

    public class ViewHolder_Recycler_view extends ViewHolder {
        RecyclerView childRecycler_view;
        TextView title,view_all;

        public ViewHolder_Recycler_view(View itemView) {
            super(itemView);
            childRecycler_view = (RecyclerView) itemView.findViewById(R.id.test_home_recycler_view);
            title = (TextView) itemView.findViewById(R.id.test_cat_title);
            view_all = (TextView) itemView.findViewById(R.id.home_view_all);

        }
    }

    public class ViewHolder_Image_view extends ViewHolder {

        ImageView testImage;

        public ViewHolder_Image_view(View itemView) {
            super(itemView);
            testImage = (ImageView) itemView.findViewById(R.id.test_imageView_recycler_view);

        }
    }

    public class ViewHolder_Category_Recycler_view extends ViewHolder {
        RecyclerView child_Category_Recycler_view;

        public ViewHolder_Category_Recycler_view(View itemView) {
            super(itemView);
            LinearLayout special_product = (LinearLayout) itemView.findViewById(R.id.layout_hot);
            special_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent_transfer();

                }
            });

            child_Category_Recycler_view = (RecyclerView) itemView.findViewById(R.id.home_category_recycler_view);

        }

        public void intent_transfer() {
            Intent intent = new Intent(mContext, Special_product.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);

        }
    }
}
