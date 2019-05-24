package com.restaurent.activity.category;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.restaurent.R;
import com.restaurent.activity.Cart;

import com.restaurent.activity.Home;
import com.restaurent.activity.NoInternetConnection;
import com.restaurent.activity.Search;
import com.restaurent.activity.Wish_List;
import com.restaurent.activity.account.MyAccountMainMenu;
import com.restaurent.activity.account.OrderHistory;
import com.restaurent.activity.user.Availability_of_products;
import com.restaurent.activity.user.CompanyProfile;
import com.restaurent.activity.user.Company_Aims;
import com.restaurent.activity.user.Customer_Care;
import com.restaurent.activity.user.Login;
import com.restaurent.activity.user.Offer;
import com.restaurent.activity.user.SignUp;
import com.restaurent.adapter.Product_Listing_Gird_And_List;
import com.restaurent.api_call.API_Get;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.constant_class.URL_Class;
import com.restaurent.db_handler.DataBaseHandlerAccount;
import com.restaurent.db_handler.DataBaseHandlerCart;
import com.restaurent.db_handler.DataBaseHandlerCartOptions;
import com.restaurent.db_handler.DataBaseHandlerWishList;
import com.restaurent.interfaces.API_Result;
import com.restaurent.interfaces.WishListAPIRequest;
import com.restaurent.json_mechanism.GetJSONData;
import com.restaurent.mechanism.Methods;
import com.restaurent.network_checker.NetworkConnection;
import com.restaurent.shared_preferenc_estring.DataStorage;
import com.restaurent.utils.CategoryDataSet;
import com.restaurent.utils.ProductDataSet;
import com.restaurent.utils.Response;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;


public class Category_Details extends AppCompatActivity implements
        View.OnClickListener, API_Result, WishListAPIRequest {
    public static ArrayList<ProductDataSet> mCategory_Details;
    public static AppCompatActivity activity = null;
    RecyclerView mCategory_recycler;
    TextView mCategory_filter;
    TextView mCategory_sort_by;
    ImageView mCategory_viewType, sub_category_drawer;
    ImageView mCategory_banner;
    ListView mListView;
    int i = 0;
    Context mContext;
    ArrayList<CategoryDataSet> mList;
    Product_Listing_Gird_And_List adapter;
    int mType;
    int mCategory_Id;
    Toolbar toolbar;
    String title, image_url;
    CollapsingToolbarLayout collapsingToolbar;
    ProgressBar progressBar;
    Boolean current_value = true, mFeatured = false;
    PopupWindow mSortingList;
    TextView sort_default, sort_name_AZ, sort_name_ZA, sort_price_LH, sort_price_HL,
    /*sort_rating_H, sort_rating_L,*/ sort_model_AZ, sort_model_ZA;
    API_Result api_resultGet;
    WishListAPIRequest wishListAPIRequest;
    LinearLayout filter_and_sorting_holder;
    int mPageCount = 1;
Button chat;
    CircleImageView profilePictureView;
    Bitmap fb_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_coordinator_layout);
        mContext = getApplicationContext();
        api_resultGet = this;
        wishListAPIRequest = this;
activity=this;
//        chat.setOnClickListener(this);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        profilePictureView=(CircleImageView) toolbar.findViewById(R.id.image);
        URL fb_url = null;//small | noraml | large
        try {
            fb_url = new URL(DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"profile_pic"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpsURLConnection conn1 = null;
        try {
            if(fb_url==null){

            }
            else {
                conn1 = (HttpsURLConnection) fb_url.openConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setFollowRedirects(true);
//        conn1.setInstanceFollowRedirects(true);
        try {
            if(conn1==null){

            }
            else {
                fb_img = BitmapFactory.decodeStream(conn1.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(fb_url==null){

        }else {
            profilePictureView.setImageBitmap(fb_img);
        }
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mListView = (ListView) findViewById(R.id.sub_category_list_view);
        progressBar = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCategory_recycler = (RecyclerView) findViewById(R.id.category_recycler_view);
        mCategory_recycler.setLayoutManager(new GridLayoutManager(mContext, 2));
        mCategory_filter = (TextView) findViewById(R.id.category_filter);
        mCategory_sort_by = (TextView) findViewById(R.id.category_sorting);
        mCategory_viewType = (ImageView) findViewById(R.id.category_type_of_view);
        mCategory_banner = (ImageView) findViewById(R.id.category_banner);
        sub_category_drawer = (ImageView) findViewById(R.id.sub_category_drawer);
        filter_and_sorting_holder = (LinearLayout) findViewById(R.id.filter_and_sorting_holder);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getInt(JSON_Names.KEY_CATEGORY_ID_SHARED_PREFERENCE) != 0) {
                mCategory_Id = bundle.getInt(JSON_Names.KEY_CATEGORY_ID_SHARED_PREFERENCE);
            }
            if (bundle.getInt(JSON_Names.KEY_TYPE_SHARED_PREFERENCE) != 0) {
                mType = bundle.getInt(JSON_Names.KEY_TYPE_SHARED_PREFERENCE);
            }
            if (bundle.getString(JSON_Names.KEY_TITLE_SHARED_PREFERENCE) != null) {
                title = bundle.getString(JSON_Names.KEY_TITLE_SHARED_PREFERENCE);
            }
            if (bundle.getString(JSON_Names.KEY_IMAGE_URL_SHARED_PREFERENCE) != null) {
                image_url = bundle.getString(JSON_Names.KEY_IMAGE_URL_SHARED_PREFERENCE);
            }
        }

        /*String url[] = new String[2];
        if (mType == (R.string.sort_category)) {
            url[0] = URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id;
            url[1] = URL_Class.mURL + URL_Class.mURL_MainCategory + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id;
            storeCurrentUrl(url[0]);
            loadData();
        } else if (mType == (R.string.sort_default)) {
            url[0] = URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id + URL_Class.mURL_GetCategory_SortList_Default;
            url[1] = URL_Class.mURL + URL_Class.mURL_MainCategory + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id;
            storeCurrentUrl(url[0]);
            loadData();
        } else if (mType == (R.string.sort_name_AZ)) {
            url[0] = URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id + URL_Class.mURL_GetCategory_SortList_ByName_ASC;
            url[1] = URL_Class.mURL + URL_Class.mURL_MainCategory + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id;
            storeCurrentUrl(url[0]);
            loadData();
        } else if (mType == (R.string.sort_name_ZA)) {
            url[0] = URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id + URL_Class.mURL_GetCategory_SortList_ByName_DESC;
            url[1] = URL_Class.mURL + URL_Class.mURL_MainCategory + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id;
            storeCurrentUrl(url[0]);
            loadData();
        } else if (mType == (R.string.sort_price_LH)) {
            url[0] = URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id + URL_Class.mURL_GetCategory_SortList_ByPrice_ASC;
            url[1] = URL_Class.mURL + URL_Class.mURL_MainCategory + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id;
            storeCurrentUrl(url[0]);
            loadData();
        } else if (mType == (R.string.sort_price_HL)) {
            url[0] = URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id + URL_Class.mURL_GetCategory_SortList_ByPrice_DESC;
            url[1] = URL_Class.mURL + URL_Class.mURL_MainCategory + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id;
            storeCurrentUrl(url[0]);
            loadData();
        }*//* else if (mType == (R.string.sort_rating_H)) {
            url[0] = URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id  + URL_Class.mURL_GetCategory_SortList_ByRating_DESC;
            url[1] = URL_Class.mURL + URL_Class.mURL_MainCategory + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id;
            storeCurrentUrl(url[0]);
            loadData();
        } else if (mType == (R.string.sort_rating_L)) {
            url[0] = URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id  + URL_Class.mURL_GetCategory_SortList_ByRating_ASC;
            url[1] = URL_Class.mURL + URL_Class.mURL_MainCategory + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id;
            storeCurrentUrl(url[0]);
            loadData();
        }*//* else if (mType == (R.string.sort_model_AZ)) {
            url[0] = URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id + URL_Class.mURL_GetCategory_SortList_ByModel_ASC;
            url[1] = URL_Class.mURL + URL_Class.mURL_MainCategory + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id;
            storeCurrentUrl(url[0]);
            loadData();
        } else if (mType == (R.string.sort_model_ZA)) {
            url[0] = URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id + URL_Class.mURL_GetCategory_SortList_ByModel_DESC;
            url[1] = URL_Class.mURL + URL_Class.mURL_MainCategory + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id;
            storeCurrentUrl(url[0]);
            loadData();
        } else if (mType == (R.string.filter)) {
            // Add a statement to get the data from filter activity through shared preference
            if (!DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_FILTER_ORDER).equals(JSON_Names.KEY_NO_DATA)) {
                url[0] = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_CATEGORY_URL) + URL_Class.mURL_GetFilter_For_Filter_Id + DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_FILTER_ORDER);
            } else {
                url[0] = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_CATEGORY_URL);
            }
            url[1] = URL_Class.mURL + URL_Class.mURL_MainCategory + Methods.current_language() + URL_Class.mCategory_id
                    + mCategory_Id;
            DataStorage.mStoreSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_FILTER_SHARED_PREFERENCE, JSON_Names.KEY_TRUE_STRING);
            storeCurrentUrl(url[0]);
            loadData();
        } */
        String url[];
        if (mType == (R.string.sort_category)) {
            url = new String[2];
            url[0] = URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() +
                    URL_Class.mCategory_id + mCategory_Id + URL_Class.mURL_Limit + URL_Class.mURL_Page + mPageCount;
            url[1] = URL_Class.mURL + URL_Class.mURL_MainCategory + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id;
            storeCurrentUrl(URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() +
                    URL_Class.mCategory_id + mCategory_Id + URL_Class.mURL_Limit + URL_Class.mURL_Page);
            loadData();
        } else if (mType == (R.string.sort_default)) {
            url = new String[2];
            mPageCount = 1;
            url[0] = URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() +
                    URL_Class.mCategory_id + mCategory_Id + URL_Class.mURL_GetCategory_SortList_Default + URL_Class.mURL_Limit +
                    URL_Class.mURL_Page + mPageCount;
            url[1] = URL_Class.mURL + URL_Class.mURL_MainCategory + Methods.current_language() +
                    URL_Class.mCategory_id + mCategory_Id;
            storeCurrentUrl(URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() +
                    URL_Class.mCategory_id + mCategory_Id + URL_Class.mURL_GetCategory_SortList_Default + URL_Class.mURL_Limit +
                    URL_Class.mURL_Page);
            loadData();
        } else if (mType == (R.string.sort_name_AZ)) {
            url = new String[2];
            mPageCount = 1;
            url[0] = URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mCategory_id +
                    mCategory_Id + URL_Class.mURL_GetCategory_SortList_ByName_ASC + URL_Class.mURL_Limit + URL_Class.mURL_Page + mPageCount;
            url[1] = URL_Class.mURL + URL_Class.mURL_MainCategory + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id;
            storeCurrentUrl(URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mCategory_id +
                    mCategory_Id + URL_Class.mURL_GetCategory_SortList_ByName_ASC + URL_Class.mURL_Limit + URL_Class.mURL_Page);
            loadData();
        } else if (mType == (R.string.sort_name_ZA)) {
            url = new String[2];
            mPageCount = 1;
            url[0] = URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id
                    + URL_Class.mURL_GetCategory_SortList_ByName_DESC + URL_Class.mURL_Limit + URL_Class.mURL_Page + mPageCount;
            url[1] = URL_Class.mURL + URL_Class.mURL_MainCategory + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id;
            storeCurrentUrl(URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id
                    + URL_Class.mURL_GetCategory_SortList_ByName_DESC + URL_Class.mURL_Limit + URL_Class.mURL_Page);
            loadData();
        } else if (mType == (R.string.sort_price_LH)) {
            url = new String[2];
            mPageCount = 1;
            url[0] = URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id +
                    URL_Class.mURL_GetCategory_SortList_ByPrice_ASC + URL_Class.mURL_Limit + URL_Class.mURL_Page + mPageCount;
            url[1] = URL_Class.mURL + URL_Class.mURL_MainCategory + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id;
            storeCurrentUrl(URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id +
                    URL_Class.mURL_GetCategory_SortList_ByPrice_ASC + URL_Class.mURL_Limit + URL_Class.mURL_Page);
            loadData();
        } else if (mType == (R.string.sort_price_HL)) {
            url = new String[2];
            mPageCount = 1;
            url[0] = URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mCategory_id +
                    mCategory_Id + URL_Class.mURL_GetCategory_SortList_ByPrice_DESC + URL_Class.mURL_Limit + URL_Class.mURL_Page + mPageCount;
            url[1] = URL_Class.mURL + URL_Class.mURL_MainCategory + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id;
            storeCurrentUrl(URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mCategory_id +
                    mCategory_Id + URL_Class.mURL_GetCategory_SortList_ByPrice_DESC + URL_Class.mURL_Limit + URL_Class.mURL_Page);
            loadData();
        } /*else if (mType == (R.string.sort_rating_H)) {
             url=new String[2];
            mPageCount = 1;
            url[0] = URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id
                    + URL_Class.mURL_GetCategory_SortList_ByRating_DESC + URL_Class.mURL_Limit + URL_Class.mURL_Page + mPageCount;
            url[1] = URL_Class.mURL + URL_Class.mURL_MainCategory + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id;
            storeCurrentUrl(URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id
                    + URL_Class.mURL_GetCategory_SortList_ByRating_DESC + URL_Class.mURL_Limit + URL_Class.mURL_Page);
            loadData();
        } else if (mType == (R.string.sort_rating_L)) {
            url=new String[2];
            mPageCount = 1;
            url[0] = URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mCategory_id +
                    mCategory_Id + URL_Class.mURL_GetCategory_SortList_ByRating_ASC + URL_Class.mURL_Limit + URL_Class.mURL_Page + mPageCount;
            url[1] = URL_Class.mURL + URL_Class.mURL_MainCategory + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id;
            storeCurrentUrl(URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mCategory_id +
                    mCategory_Id + URL_Class.mURL_GetCategory_SortList_ByRating_ASC + URL_Class.mURL_Limit + URL_Class.mURL_Page);
            loadData();
        }*/ else if (mType == (R.string.sort_model_AZ)) {
            url = new String[2];
            mPageCount = 1;
            url[0] = URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mCategory_id +
                    mCategory_Id + URL_Class.mURL_GetCategory_SortList_ByModel_ASC + URL_Class.mURL_Limit + URL_Class.mURL_Page + mPageCount;
            url[1] = URL_Class.mURL + URL_Class.mURL_MainCategory + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id;
            storeCurrentUrl(URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mCategory_id +
                    mCategory_Id + URL_Class.mURL_GetCategory_SortList_ByModel_ASC + URL_Class.mURL_Limit + URL_Class.mURL_Page);
            loadData();
        } else if (mType == (R.string.sort_model_ZA)) {
            url = new String[2];
            mPageCount = 1;
            url[0] = URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id +
                    URL_Class.mURL_GetCategory_SortList_ByModel_DESC + URL_Class.mURL_Limit + URL_Class.mURL_Page + mPageCount;
            url[1] = URL_Class.mURL + URL_Class.mURL_MainCategory + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id;
            storeCurrentUrl(URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id +
                    URL_Class.mURL_GetCategory_SortList_ByModel_DESC + URL_Class.mURL_Limit + URL_Class.mURL_Page);
            loadData();
        } else if (mType == (R.string.filter)) {
            url = new String[2];
            mPageCount = 1;
            // Add a statement to get the data from filter activity through shared preference
            if (!DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_FILTER_ORDER).equals(JSON_Names.KEY_NO_DATA)) {
                url[0] = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_CATEGORY_URL)
                        + mPageCount +
                        URL_Class.mURL_GetFilter_For_Filter_Id +
                        DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_FILTER_ORDER);
            } else {
                url[0] = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_CATEGORY_URL) + mPageCount;
            }
            url[1] = URL_Class.mURL + URL_Class.mURL_MainCategory + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id;
            DataStorage.mStoreSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_FILTER_SHARED_PREFERENCE, JSON_Names.KEY_TRUE_STRING);
            storeCurrentUrl(DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_CATEGORY_URL));
            loadData();
        } else if (mType == 11) {
            url = new String[1];
            mFeatured = true;
            //mOther = true;
            mListView.setVisibility(View.GONE);
            sub_category_drawer.setVisibility(View.GONE);
            filter_and_sorting_holder.setVisibility(View.GONE);

            url[0] = URL_Class.mURL + URL_Class.mURL_FeaturedProduct + Methods.current_language();
            loadData();
        } else if (mType == 22) {
            url = new String[1];
            //mFeatured = true;
            //mOther = true;
            mPageCount = 1;
            mListView.setVisibility(View.GONE);
            sub_category_drawer.setVisibility(View.GONE);
            filter_and_sorting_holder.setVisibility(View.GONE);

            url[0] = URL_Class.mURL + URL_Class.mURL_LatestProduct + Methods.current_language() + URL_Class.mURL_Limit
                    + URL_Class.mURL_Page + mPageCount;
            storeCurrentUrl(URL_Class.mURL + URL_Class.mURL_LatestProduct + Methods.current_language() +
                    URL_Class.mURL_Limit + URL_Class.mURL_Page);
            loadData();
        } else if (mType == 33) {
            url = new String[1];
            //mFeatured = true;
            //mOther = true;
            mPageCount = 1;
            mListView.setVisibility(View.GONE);
            sub_category_drawer.setVisibility(View.GONE);
            filter_and_sorting_holder.setVisibility(View.GONE);

            url[0] = URL_Class.mURL + URL_Class.mURL_SpecialProduct + Methods.current_language()
                    + URL_Class.mURL_Limit + URL_Class.mURL_Page + mPageCount;
            storeCurrentUrl(URL_Class.mURL + URL_Class.mURL_SpecialProduct + Methods.current_language()
                    + URL_Class.mURL_Limit + URL_Class.mURL_Page);
            loadData();
        } else {
            mListView.setVisibility(View.GONE);
            sub_category_drawer.setVisibility(View.GONE);
            filter_and_sorting_holder.setVisibility(View.GONE);
            url = new String[0];
        }


        if (NetworkConnection.connectionChecking(getApplicationContext())) {
            if (!mFeatured) {
                progressBar.setVisibility(View.VISIBLE);
                new API_Get().get_method(url, api_resultGet, "", JSON_Names.KEY_GET_TYPE, true, mContext, "CategoryListing");
            } else {
                new API_Get().get_method(url, api_resultGet, "", JSON_Names.KEY_GET_TYPE, true, mContext, "SpecialFeaturedLatest");
            }
        } else {
            Intent intent = new Intent(Category_Details.this, NoInternetConnection.class);
            startActivity(intent);
            finish();
        }
    }

    public void storeCurrentUrl(String url) {
        DataStorage.mStoreSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_CATEGORY_URL, url);
    }

    public void loadData() {
        collapsingToolbar.setTitle(title);
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.colorAccent));
        if (image_url != null) {
            Methods.glide_image_loader(image_url, mCategory_banner);
        } else {
            mCategory_banner.setImageResource(R.drawable.dummy_banner);
        }
    }

    public void gridRecyclerView() {
        mCategory_recycler.setLayoutManager(new GridLayoutManager(mContext, 2));
        sendType(true);
    }

    public void sendType(boolean type) {

        current_value = type;
        adapter = new Product_Listing_Gird_And_List(getApplicationContext(), mCategory_Details, type, wishListAPIRequest, mCategory_recycler);
        mCategory_recycler.setAdapter(adapter);
        if (!mFeatured && mCategory_Details != null) {
            adapter.setOnLoadMoreListener(new Product_Listing_Gird_And_List.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    mPageCount++;
                    String current_count = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_COUNT);
                    if (mPageCount <= ((Integer.valueOf(current_count) / 6) + 1)) {
                        String url[] = new String[1];
                        if (DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),
                                JSON_Names.KEY_FILTER_SHARED_PREFERENCE).equals("true")) {
                            if (!DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),
                                    JSON_Names.KEY_FILTER_ORDER).equals(JSON_Names.KEY_NO_DATA)) {
                                url[0] = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),
                                        JSON_Names.KEY_CURRENT_CATEGORY_URL) + mPageCount + URL_Class.mURL_GetFilter_For_Filter_Id +
                                        DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_FILTER_ORDER);
                            } else {
                                url[0] = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),
                                        JSON_Names.KEY_CURRENT_CATEGORY_URL) + mPageCount;
                            }

                        } else {
                            url[0] = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),
                                    JSON_Names.KEY_CURRENT_CATEGORY_URL) + mPageCount;
                        }
                        //new Task_List().execute(url);
                        mCategory_Details.add(null);
                        adapter.notifyItemInserted(mCategory_Details.size() - 1);

                        new API_Get().get_method(url, api_resultGet, "", JSON_Names.KEY_GET_TYPE, true, mContext, "PageCalling");
                    }
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void listRecyclerView() {
        mCategory_recycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, JSON_Names.KEY_FALSE_BOOLEAN));
        sendType(JSON_Names.KEY_FALSE_BOOLEAN);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_FILTER_ORDER);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.category_filter:
                DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_FILTER_ORDER);
                Intent intent = new Intent(Category_Details.this, Filter_Activity.class);
                intent.putExtra(JSON_Names.KEY_CATEGORY_ID_SHARED_PREFERENCE, mCategory_Id);
                intent.putExtra(JSON_Names.KEY_IMAGE_URL_SHARED_PREFERENCE, image_url);
                intent.putExtra(JSON_Names.KEY_TITLE_SHARED_PREFERENCE, title);
                startActivity(intent);
                //finish();
                break;
            case R.id.category_sorting:
                sorting_popup_window(v);
                break;
            case R.id.category_type_of_view:
                if (i == 0) {
                    mCategory_viewType.setImageResource(R.drawable.ic_list_black_24dp);
                    listRecyclerView();
                    i = 1;
                } else {
                    mCategory_viewType.setImageResource(R.drawable.ic_view_module_black_24dp);
                    gridRecyclerView();
                    i = 0;
                }
                break;

            case R.id.cart_count_value:
                Intent i2 = new Intent(Category_Details.this, Cart.class);
                startActivity(i2);
                break;
            case R.id.sorting_default:
                storeData(R.string.sort_default);
                break;
            case R.id.sorting_name_AZ:
                storeData(R.string.sort_name_AZ);
                break;
            case R.id.sorting_name_ZA:
                storeData(R.string.sort_name_ZA);
                break;
            case R.id.sorting_price_LH:
                storeData(R.string.sort_price_LH);
                break;
            case R.id.sorting_price_HL:
                storeData(R.string.sort_price_HL);
                break;
            /*case R.id.sorting_rating_H:
                storeData(R.string.sort_rating_H);
                break;
            case R.id.sorting_rating_L:
                storeData(R.string.sort_rating_L);
                break;*/
            case R.id.sorting_model_AZ:
                storeData(R.string.sort_model_AZ);
                break;
            case R.id.sorting_model_ZA:
                storeData(R.string.sort_model_ZA);
                break;
        }
    }

    public void storeData(int type) {
        mSortingList.dismiss();
        Intent sorting = new Intent(Category_Details.this, Category_Details.class);
        sorting.putExtra(JSON_Names.KEY_CATEGORY_ID_SHARED_PREFERENCE, mCategory_Id);
        sorting.putExtra(JSON_Names.KEY_TYPE_SHARED_PREFERENCE, type);
        sorting.putExtra(JSON_Names.KEY_IMAGE_URL_SHARED_PREFERENCE, image_url);
        sorting.putExtra(JSON_Names.KEY_TITLE_SHARED_PREFERENCE, title);
        startActivity(sorting);
        //finish();
    }

    public void sorting_popup_window(View v) {
        mSortingList = new PopupWindow(Category_Details.this);
        View view = getLayoutInflater().inflate(R.layout.activity_sorting__popup, null);
        mSortingList.setContentView(view);

        sort_default = (TextView) view.findViewById(R.id.sorting_default);
        sort_name_AZ = (TextView) view.findViewById(R.id.sorting_name_AZ);
        sort_name_ZA = (TextView) view.findViewById(R.id.sorting_name_ZA);
        sort_price_LH = (TextView) view.findViewById(R.id.sorting_price_LH);
        sort_price_HL = (TextView) view.findViewById(R.id.sorting_price_HL);
        //sort_rating_H = (TextView) view.findViewById(R.id.sorting_rating_H);
        //sort_rating_L = (TextView) view.findViewById(R.id.sorting_rating_L);
        sort_model_AZ = (TextView) view.findViewById(R.id.sorting_model_AZ);
        sort_model_ZA = (TextView) view.findViewById(R.id.sorting_model_ZA);

        mSortingList.setHeight(LayoutParams.WRAP_CONTENT);
        mSortingList.setWidth(LayoutParams.WRAP_CONTENT);
        mSortingList.setOutsideTouchable(true);
        mSortingList.setFocusable(true);
        mSortingList.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mSortingList.showAtLocation(v, Gravity.CENTER, 0, 0);

        sort_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSortingList.dismiss();
            }
        });
        sort_name_AZ.setOnClickListener(this);
        sort_name_ZA.setOnClickListener(this);
        sort_price_LH.setOnClickListener(this);
        sort_price_HL.setOnClickListener(this);
        //sort_rating_H.setOnClickListener(this);
        //sort_rating_L.setOnClickListener(this);
        sort_model_AZ.setOnClickListener(this);
        sort_model_ZA.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        update(menu);
        if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
            menu.findItem(R.id.user_name).setVisible(JSON_Names.KEY_FALSE_BOOLEAN);
            menu.findItem(R.id.my_order).setVisible(JSON_Names.KEY_FALSE_BOOLEAN);
            menu.findItem(R.id.logout).setVisible(JSON_Names.KEY_FALSE_BOOLEAN);
        } else {
            String title = DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_name();
            menu.findItem(R.id.user_name).setTitle(title);
            menu.findItem(R.id.register).setVisible(JSON_Names.KEY_FALSE_BOOLEAN);
            menu.findItem(R.id.login).setVisible(JSON_Names.KEY_FALSE_BOOLEAN);
        }
        return true;
    }

    public void update(Menu menu) {
        TextView textView;
        ImageView imageView;
        View view = menu.findItem(R.id.cart_count).getActionView();
        textView = (TextView) view.findViewById(R.id.cart_count_value);
        imageView = (ImageView) view.findViewById(R.id.cart_image_view);
        final String tempData = DataBaseHandlerCart.getInstance(getApplicationContext()).get_whole_list_count();
        if (!tempData.equals("0")) {
            textView.setText(tempData);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Category_Details.this, Cart.class);
                    startActivity(intent);
                }
            });
            imageView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Intent intent = new Intent(Category_Details.this, Cart.class);
                    startActivity(intent);
                    return false;
                }
            });
        } else {
            textView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        update(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            finish();
        }
        else if (id == R.id.login) {
            Intent intent = new Intent(Category_Details.this, Login.class);
            startActivity(intent);
        } else if (id ==
                R.id.register) {
            Intent intent = new Intent(Category_Details.this, SignUp.class);
            startActivity(intent);
        }
        else if (id == R.id.customer_care) {
            Intent intent = new Intent(getApplicationContext(), Customer_Care.class);
            startActivity(intent);
        }

        else if (id == R.id.offer) {
            Intent intent = new Intent(getApplicationContext(), Offer.class);
            startActivity(intent);
        }
        else if (id == R.id.logout) {
            DataStorage.mRemoveSharedPreferenceString(getApplicationContext(),"profile_pic");
            DataBaseHandlerAccount.getInstance(getApplicationContext()).delete_account_detail();
            DataBaseHandlerWishList.getInstance(getApplicationContext()).delete_wish_list();
            DataBaseHandlerCart.getInstance(getApplicationContext()).delete_cart();
            DataBaseHandlerCartOptions.getInstance(getApplicationContext()).delete_cart_option();
            Intent intent = new Intent(Category_Details.this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.search) {
            Intent intent = new Intent(Category_Details.this, Search.class);
            startActivity(intent);
        } else if (id == R.id.invite) {


            Intent sharingintent = new Intent(Intent.ACTION_SEND);
            sharingintent.setType("text/plain");
            sharingintent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share The App");
            sharingintent.putExtra(android.content.Intent.EXTRA_TEXT, "Download Tasty Town Foods App From Play Store - https://play.google.com/store/apps/details?id=com.pooja_prasad ");
            startActivity(Intent.createChooser(sharingintent, "Share Via"));
        }

        else if (id == R.id.menu_wish_list) {
            if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                Methods.toast(getResources().getString(R.string.must_login));
                DataStorage.mStoreSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN, JSON_Names.KEY_CURRENT_LOGIN_WISH_LIST);
                Intent intent = new Intent(Category_Details.this, Login.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(Category_Details.this, Wish_List.class);
                startActivity(intent);
            }
        } else if (id == R.id.user_name) {
            Intent intent = new Intent(Category_Details.this, MyAccountMainMenu.class);
            startActivity(intent);
        } else if (id == R.id.my_order) {
            Intent intent = new Intent(Category_Details.this, OrderHistory.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        invalidateOptionsMenu();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        if (mCategory_Details != null) {
            sendType(current_value);
        }
        super.onRestart();
    }

    private void setting(String[] data) {
        if (data != null) {
            if (/*mType == 11 || */mType == 22 || mType == 33) {
                mCategory_Details = GetJSONData.getProductDetailsThree(data[0]);
            } else {
                mCategory_Details = GetJSONData.getProductDetails(data[0]);
                mList = GetJSONData.getSubCategoryList(data[1]);
                String array[] = Methods.getArrayList(mList);
                if (array != null) {
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, R.layout.sub_category_list_row, R.id.sub_category_recycler_view_row_text, array);
                    mListView.setAdapter(arrayAdapter);
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(Category_Details.this, Category_Details.class);
                            intent.putExtra(JSON_Names.KEY_CATEGORY_ID_SHARED_PREFERENCE, mList.get(position).getCategory_id());
                            intent.putExtra(JSON_Names.KEY_TYPE_SHARED_PREFERENCE, R.string.sort_category);
                            intent.putExtra(JSON_Names.KEY_TITLE_SHARED_PREFERENCE, mList.get(position).getName());
                            intent.putExtra(JSON_Names.KEY_IMAGE_URL_SHARED_PREFERENCE, mList.get(position).getImage());
                            startActivity(intent);
                            //finish();
                        }
                    });
                } else {
                    mListView.setVisibility(View.GONE);
                    sub_category_drawer.setVisibility(View.GONE);
                }
            }
            sendType(JSON_Names.KEY_TRUE_BOOLEAN);
            progressBar.setVisibility(View.GONE);
            mCategory_filter.setOnClickListener(this);
            mCategory_sort_by.setOnClickListener(this);
            mCategory_viewType.setOnClickListener(this);
        } else {
            sendType(false);
            Methods.toast(getResources().getString(R.string.error));
            finish();
        }
    }

    @Override
    public void result(String[] data, String source) {
        progressBar.setVisibility(View.GONE);
        if (data != null) {
            switch (source) {
                case "CategoryListing":
                    setting(data);
                    break;
                case "WishListPost":
                    Response response = GetJSONData.getResponse(data[0]);
                    if (response != null) {
                        if (response.getmStatus() == 200) {
                            Methods.toast(response.getmMessage());
                        } else {
                            Methods.toast(response.getmMessage());
                        }
                    }
                    break;
                case "SpecialFeaturedLatest":
                    mCategory_Details = GetJSONData.getProductDetailsThree(data[0]);
                    sendType(JSON_Names.KEY_TRUE_BOOLEAN);
                    break;
                case "PageCalling":
                    if (!mCategory_Details.isEmpty()) {
                        mCategory_Details.remove(mCategory_Details.size() - 1);
                        adapter.notifyItemRemoved(mCategory_Details.size());
                        /*if (GetJSONData.getProductDetails(data[0]) != null) {
                            ArrayList<ProductDataSet> mTemp = GetJSONData.getProductDetails(data[0]);
                            if (mTemp != null)
                                for (int i = 0; i < mTemp.size(); i++) {
                                    mCategory_Details.add(mTemp.get(i));
                                }
                        }*/

                        if (/*mType == 11 ||*/ mType == 22 || mType == 33) {
                            if (GetJSONData.getProductDetailsThree(data[0]) != null) {
                                ArrayList<ProductDataSet> mTemp = GetJSONData.getProductDetailsThree(data[0]);
                                if (mTemp != null)
                                    for (int i = 0; i < mTemp.size(); i++) {
                                        mCategory_Details.add(mTemp.get(i));
                                    }
                            }
                        } else {
                            if (GetJSONData.getProductDetails(data[0]) != null) {
                                ArrayList<ProductDataSet> mTemp = GetJSONData.getProductDetails(data[0]);
                                if (mTemp != null)
                                    for (int i = 0; i < mTemp.size(); i++) {
                                        mCategory_Details.add(mTemp.get(i));
                                    }
                            }
                        }
                        adapter.notifyItemInserted(mCategory_Details.size());
                        adapter.setLoaded();
                    }
                    break;
            }
        }
    }
    public static void update_cart(){
        Home.activity.invalidateOptionsMenu();
    }
    @Override
    public void wish_list_api_request(String data, String[] url) {
        new API_Get().get_method(url, api_resultGet, data, JSON_Names.KEY_POST_TYPE, true, getBaseContext(), "WishListPost");
    }
}
