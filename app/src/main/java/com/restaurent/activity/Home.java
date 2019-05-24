package com.restaurent.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.restaurent.Application_Context;
import com.restaurent.R;
import com.restaurent.activity.account.MyAccountMainMenu;
import com.restaurent.activity.account.OrderHistory;
import com.restaurent.activity.category.Category_Details;
import com.restaurent.activity.user.Availability_of_products;
import com.restaurent.activity.user.Choose_Option;
import com.restaurent.activity.user.CompanyProfile;
import com.restaurent.activity.user.Company_Aims;
import com.restaurent.activity.user.Customer_Care;
import com.restaurent.activity.user.Even;
import com.restaurent.activity.user.Login;

import com.restaurent.activity.user.Offer;
import com.restaurent.activity.user.SignUp;
import com.restaurent.activity.user.Tabl;
import com.restaurent.adapter.Adapter_Row;
import com.restaurent.adapter.CategoryAdapter;
import com.restaurent.adapter.Home_Adapter;
import com.restaurent.api_call.API_Get;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.constant_class.URL_Class;
import com.restaurent.db_handler.DataBaseHandlerAccount;
import com.restaurent.db_handler.DataBaseHandlerCart;
import com.restaurent.db_handler.DataBaseHandlerCartOptions;
import com.restaurent.db_handler.DataBaseHandlerWishList;
import com.restaurent.fragments.NavigationDrawerFragment;
import com.restaurent.interfaces.API_Result;
import com.restaurent.interfaces.Refresher;
import com.restaurent.json_mechanism.GetJSONData;
import com.restaurent.mechanism.Methods;
import com.restaurent.network_checker.NetworkConnection;
import com.restaurent.shared_preferenc_estring.DataStorage;
import com.restaurent.utils.CategoryDataSet;
import com.restaurent.utils.Navigation_DataSet;
import com.restaurent.utils.ProductDataSet;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;


public class Home extends AppCompatActivity implements
        View.OnClickListener, View.OnTouchListener, API_Result ,Refresher{
    public static AppCompatActivity activity = null;
    Refresher refresher;
    static Context mContext;
    /*final String url[] = {URL_Class.mURL + URL_Class.mURL_FeaturedProduct + Methods.current_language(),
            URL_Class.mURL + URL_Class.mURL_LatestProduct + Methods.current_language(),
            URL_Class.mURL + URL_Class.mURL_SpecialProduct + Methods.current_language()};*/

    final String url[] = {URL_Class.mURL + URL_Class.mURL_FeaturedProduct + Methods.current_language(),
            URL_Class.mURL + URL_Class.mURL_LatestProduct + Methods.current_language() + URL_Class.mURL_Limit + URL_Class.mURL_Page + "1",
            URL_Class.mURL + URL_Class.mURL_SpecialProduct + Methods.current_language() + URL_Class.mURL_Limit + URL_Class.mURL_Page + "1"};
Adapter_Row adapter_row;

    Toolbar toolbar;
    ProgressBar progressBar;
    RecyclerView homeTest;
    API_Result result;
    Bitmap fb_img;
    TextView copy_right_custom_text_view;
    String slide1[];
    ArrayList<ProductDataSet> list = new ArrayList<>();
    HashMap<Integer, ArrayList<ProductDataSet>> whole_list = new HashMap<>();
CircleImageView profilePictureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_appbar);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
       setSupportActionBar(toolbar);

        TextView TitleToolBar = (TextView) toolbar.findViewById(R.id.toolbar_title); // find title control
        TitleToolBar.setText("Home");
       // getSupportActionBar().setDisplayShowTitleEnabled(true);
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
        progressBar = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);
      //  copy_right_custom_text_view = (TextView) findViewById(R.id.copy_right_custom_text_view);

        result = this;
        assert getSupportActionBar() != null;

        getSupportActionBar().setDisplayShowHomeEnabled(true);
refresher=this;
        adapter_row=new Adapter_Row(list,mContext,refresher);
        slide1 = GetJSONData.get_slide(DataStorage.mRetrieveSharedPreferenceString
                (Application_Context.getAppContext(), JSON_Names.KEY_IMAGE));
activity=this;


        NavigationDrawerFragment navigationDrawerFragment =
                (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer2);

        navigationDrawerFragment.setUp(R.id.fragment_navigation_drawer2, (DrawerLayout) findViewById(R.id.drawer_layout),toolbar);

        String copy_right = "" +"" + ""+"\n"+"";
        action_NavigationData();

        mContext = getApplicationContext();
        homeTest = (RecyclerView) findViewById(R.id.home_list_recycler_view);
        homeTest.setLayoutManager(new LinearLayoutManager(this));
      //  copy_right_custom_text_view.setText(copy_right);

        if (NetworkConnection.connectionChecking(getApplicationContext())) {
            if (NetworkConnection.connectionType(getApplicationContext())) {
                progressBar.setVisibility(View.VISIBLE);
                new API_Get().get_method(url, result, "", JSON_Names.KEY_GET_TYPE, true, getBaseContext(), "Home");
            } else {
                two_g_transfer();
            }
        } else {
            Intent intent = new Intent(Home.this, NoInternetConnection.class);
            startActivity(intent);
            finish();
        }

    }

    public void action_NavigationData() {

        String mNavigationData = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_NAVIGATION_DATA);

        CategoryAdapter listAdapter;
        ExpandableListView expListView;

        ArrayList<Navigation_DataSet> mNavigation_dataSets;
        final ArrayList<CategoryDataSet> mParentList;

        final ArrayList<ArrayList<CategoryDataSet>> mChildAdder;

        if (GetJSONData.getNavigationData(mNavigationData) != null) {
            mNavigation_dataSets = GetJSONData.getNavigationData(mNavigationData);
            mParentList = mNavigation_dataSets.get(0).mParentList;
            mChildAdder = mNavigation_dataSets.get(0).mChildAdder;
            expListView = (ExpandableListView) findViewById(R.id.lvExp);
            listAdapter = new CategoryAdapter(this, mParentList, mChildAdder);
            assert expListView != null;
           expListView.setAdapter(listAdapter);
            expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    navigation_reset();
                    Intent i = new Intent(Home.this, Category_Details.class);
                    i.putExtra(JSON_Names.KEY_TITLE_SHARED_PREFERENCE, mChildAdder.get(groupPosition).get(childPosition).getName());
                    i.putExtra(JSON_Names.KEY_IMAGE_URL_SHARED_PREFERENCE, mChildAdder.get(groupPosition).get(childPosition).getImage());
                    i.putExtra(JSON_Names.KEY_CATEGORY_ID_SHARED_PREFERENCE, mChildAdder.get(groupPosition).get(childPosition).getCategory_id());
                    i.putExtra(JSON_Names.KEY_TYPE_SHARED_PREFERENCE, R.string.sort_category);
                    startActivity(i);
                    return false;
                }
            });

        } else {
            Methods.toast(getResources().getString(R.string.error));
            two_g_transfer();
        }
    }

    public void navigation_reset() {
        if (NavigationDrawerFragment.mdrawerLayout.isDrawerOpen(GravityCompat.START)) {
            NavigationDrawerFragment.mdrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        update(menu);

        if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
            menu.findItem(R.id.user_name).setVisible(false);
            menu.findItem(R.id.my_order).setVisible(false);
            menu.findItem(R.id.logout).setVisible(false);
        } else {
            String title = DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_name();
            menu.findItem(R.id.user_name).setTitle(title);
            menu.findItem(R.id.register).setVisible(false);
            menu.findItem(R.id.my_booking).setVisible(false);
            menu.findItem(R.id.my_Events).setVisible(false);
            menu.findItem(R.id.login).setVisible(false);
        }
        return true;
    }

    public void update(Menu menu) {
        View view = menu.findItem(R.id.cart_count).getActionView();
        ImageView t1 = (ImageView) view.findViewById(R.id.cart_image_view);
        TextView t2 = (TextView) view.findViewById(R.id.cart_count_value);
        String tempData = DataBaseHandlerCart.getInstance(getApplicationContext()).get_whole_list_count();
        if (!tempData.equals("0")) {
            t2.setText(tempData);
            t2.setOnClickListener(this);
            t1.setOnTouchListener(this);
        } else {
            t2.setVisibility(View.INVISIBLE);
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.login) {
            login_transfer();
        } else if (id == R.id.user_name) {
            Intent intent = new Intent(Home.this, MyAccountMainMenu.class);
            startActivity(intent);
        }
     else if (id == R.id.search) {
            Intent intent = new Intent(Home.this, Search.class);
            startActivity(intent);
        } else if (id == R.id.register) {
            Intent intent = new Intent(Home.this, SignUp.class);
            startActivity(intent);
        }
        else if (id == R.id.my_booking) {
            Intent intent = new Intent(Home.this, Tabl.class);
            startActivity(intent);
        }
        else if (id == R.id.my_Events) {
            Intent intent = new Intent(Home.this, Even.class);
            startActivity(intent);
        }
        else if (id == R.id.customer_care) {
            Intent intent = new Intent(getApplicationContext(), Customer_Care.class);
            startActivity(intent);
        }

        else if (id == R.id.offer) {
            Intent intent = new Intent(Home.this, Offer.class);
            startActivity(intent);
        }
        else if (id == R.id.invite) {


        Intent sharingintent = new Intent(Intent.ACTION_SEND);
        sharingintent.setType("text/plain");
        sharingintent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share The App");
        sharingintent.putExtra(android.content.Intent.EXTRA_TEXT, "Download Resturant App From Play Store - https://play.google.com/store/apps/details?id=com.club44 ");
        startActivity(Intent.createChooser(sharingintent, "Share Via"));
    }

        else if (id == R.id.logout) {
            DataStorage.mRemoveSharedPreferenceString(getApplicationContext(),"profile_pic");
            DataBaseHandlerAccount.getInstance(getApplicationContext()).delete_account_detail();
            DataBaseHandlerWishList.getInstance(getApplicationContext()).delete_wish_list();
            DataBaseHandlerCart.getInstance(getApplicationContext()).delete_cart();
            DataBaseHandlerCartOptions.getInstance(getApplicationContext()).delete_cart_option();
            Intent intent = new Intent(Home.this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_wish_list) {
            if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                Methods.toast(getResources().getString(R.string.must_login));
                login_transfer();
            } else {
                Intent intent = new Intent(Home.this, Wish_List.class);
                startActivity(intent);
            }
        } else if (id == R.id.my_order) {
            Intent intent = new Intent(Home.this, OrderHistory.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
         finish();
         }

@Override
public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cart_count_value) {
        cart_transfer();
        }
        }

@Override
public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (id == R.id.cart_image_view) {
        cart_transfer();
        }
        return false;
        }

@Override
protected void onResume() {
        invalidateOptionsMenu();
        navigation_reset();
        super.onResume();
        }

public void login_transfer() {
        Intent intent = new Intent(Home.this, Login.class);
        startActivity(intent);
        }

public void cart_transfer() {
        Intent intent = new Intent(Home.this, Cart.class);
        startActivity(intent);
        }

public void two_g_transfer() {
        Intent intent = new Intent(Home.this, Home.class);
        startActivity(intent);
        finish();
        }

@Override
public void result(String[] data, String source) {
        progressBar.setVisibility(View.GONE);
        if (source.equals("Home")) {
        if (data != null) {
        setting(data);
        } else {
        two_g_transfer();
        }
        }
        }

private void setting(String[] data) {
        if (data != null) {
        for (int i = 0; i < data.length; i++) {
        list = GetJSONData.getHomeData(data[i]);

        if (list != null) {
        if (!list.isEmpty()) {
        if (i == 0) {
        list.get(0).setHeading(getResources().getString(R.string.featured));
        } else if (i == 1) {
        list.get(0).setHeading("Products");
        } else if (i == 2) {
        list.get(0).setHeading(getResources().getString(R.string.special));
        }
        whole_list.put(i, list);
        }
        }
        }
final ArrayList<Object> mHomeViewOrder = new ArrayList<>();

        if (slide1 != null) {
        if (slide1.length > 0)
        mHomeViewOrder.add(slide1);
        }
        mHomeViewOrder.add(true);
        if (whole_list != null) {
        for (int i = 0; i <= whole_list.size(); i++) {
        if (whole_list.get(i) != null) {
        mHomeViewOrder.add(whole_list.get(i));
        }
        }
        }

        homeTest.setAdapter(new Home_Adapter(mContext, mHomeViewOrder));
        homeTest.setItemAnimator(new DefaultItemAnimator());

        } else {
        Methods.toast(getResources().getString(R.string.error));
        two_g_transfer();
        }
        }

public static void update_cart(String image){
    Home.activity.invalidateOptionsMenu();
    Choose_Option alert = new Choose_Option();
    alert.showDialog(activity, "Choose Option",image);
}
    public void update_cart2(){

    }

    @Override
    public void refresher() {


       // homeTest.setAdapter(adapter_row);

    }
}
