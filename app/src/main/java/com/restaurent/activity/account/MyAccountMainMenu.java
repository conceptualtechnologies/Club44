package com.restaurent.activity.account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.restaurent.R;
import com.restaurent.activity.Cart;
import com.restaurent.activity.Home;
import com.restaurent.activity.Search;
import com.restaurent.activity.Wish_List;
import com.restaurent.activity.user.Availability_of_products;
import com.restaurent.activity.user.CompanyProfile;
import com.restaurent.activity.user.Company_Aims;
import com.restaurent.activity.user.Customer_Care;
import com.restaurent.activity.user.Login;

import com.restaurent.activity.user.Offer;
import com.restaurent.activity.user.SignUp;
import com.restaurent.db_handler.DataBaseHandlerAccount;
import com.restaurent.db_handler.DataBaseHandlerCart;
import com.restaurent.db_handler.DataBaseHandlerCartOptions;
import com.restaurent.db_handler.DataBaseHandlerWishList;
import com.restaurent.mechanism.Methods;
import com.restaurent.shared_preferenc_estring.DataStorage;
import com.restaurent.utils.ItemForMultipleSelection;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAccountMainMenu extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    Toolbar toolbar;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    CircleImageView profilePictureView;
    Bitmap fb_img;
    private ArrayList<ItemForMultipleSelection> mMyAcMainList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment);

        toolbar = (Toolbar) findViewById(R.id.actionbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
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

        mRecyclerView = (RecyclerView) findViewById(R.id.fragment_multiple_selection_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(MyAccountMainMenu.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyACMainMenuAdapter(getMyAMMenuList());
        mRecyclerView.setAdapter(mAdapter);
    }

    public ArrayList<ItemForMultipleSelection> getMyAMMenuList() {

        for (int i = 0; i < 2; i++) {

            ItemForMultipleSelection itemForMSelection = new ItemForMultipleSelection();

           if (i == 0) {
                itemForMSelection.setmMyAccount(getResources().getString(R.string.menu_account));
                itemForMSelection.setmEditAcInfo(getResources().getString(R.string.menu_edit_ac));
                itemForMSelection.setmChangePwd(getResources().getString(R.string.menu_change_pwd));
                itemForMSelection.setmModifyAddsBook(getResources().getString(R.string.menu_modify_adds));
                itemForMSelection.setId(i);
            }
            if (i == 1) {
                itemForMSelection.setmMyOrders(getResources().getString(R.string.menu_order));
                itemForMSelection.setmViewOrderH(getResources().getString(R.string.menu_order_history));
                //itemForMSelection.setmRewardPoints(getResources().getString(R.string.menu_reward_points));
                //itemForMSelection.setmTransactions(getResources().getString(R.string.menu_transactions));
                itemForMSelection.setId(i);
            }

            mMyAcMainList.add(itemForMSelection);

        }
        return mMyAcMainList;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cart_count_value) {
            Intent intent = new Intent(MyAccountMainMenu.this, Cart.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        invalidateOptionsMenu();
        super.onResume();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (id == R.id.cart_image_view) {
            Intent intent = new Intent(MyAccountMainMenu.this, Cart.class);
            startActivity(intent);
        }
        return false;
    }

    public class MyACMainMenuAdapter extends RecyclerView.Adapter<MyACMainMenuAdapter.ViewHolder> {

        private ArrayList<ItemForMultipleSelection> mDatasetMyAcMMenu;

        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView mPrimaryTextView, mText1, mText2, mText3, mText4;

            public ViewHolder(final View itemView) {
                super(itemView);

                mPrimaryTextView = (TextView) itemView.findViewById(R.id.list_item_primary_text);
                mText1 = (TextView) itemView.findViewById(R.id.tv_text_1);
                mText2 = (TextView) itemView.findViewById(R.id.tv_text_2);
                mText3 = (TextView) itemView.findViewById(R.id.tv_text_3);
                mText4 = (TextView) itemView.findViewById(R.id.tv_text_4);

            }
        }


        // Provide a suitable constructor (depends on the kind of dataset)
        public MyACMainMenuAdapter(ArrayList<ItemForMultipleSelection> mytransactions) {
            this.mDatasetMyAcMMenu = mytransactions;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_latest, parent, false);
            // set the view's size, margins, paddings and layout parameters
            return new ViewHolder(v);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int i) {

            if (i == 0) {
                viewHolder.mPrimaryTextView.setText(mDatasetMyAcMMenu.get(i).getmMyAccount());
                viewHolder.mText1.setText(mDatasetMyAcMMenu.get(i).getmEditAcInfo());
                viewHolder.mText2.setText(mDatasetMyAcMMenu.get(i).getmChangePwd());
                viewHolder.mText3.setText(mDatasetMyAcMMenu.get(i).getmModifyAddsBook());

                viewHolder.mText1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ie = new Intent(getApplicationContext(), MyAccountInfo.class);
                        startActivity(ie);
                    }
                });

                viewHolder.mText2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ie = new Intent(getApplicationContext(), ChangePwd.class);
                        startActivity(ie);
                    }
                });

                viewHolder.mText3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ie = new Intent(getApplicationContext(), CardViewActivity.class);
                        startActivity(ie);
                    }
                });

                viewHolder.mText4.setVisibility(View.GONE);
            }
            if (i == 1) {
                viewHolder.mPrimaryTextView.setText(mDatasetMyAcMMenu.get(i).getmMyOrders());
                viewHolder.mText1.setText(mDatasetMyAcMMenu.get(i).getmViewOrderH());
                //viewHolder.mText2.setText(mDatasetMyAcMMenu.get(i).getmRewardPoints());
                //viewHolder.mText3.setText(mDatasetMyAcMMenu.get(i).getmTransactions());


                viewHolder.mText1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ie = new Intent(getApplicationContext(), OrderHistory.class);
                        startActivity(ie);
                    }
                });

                viewHolder.mText2.setVisibility(View.GONE);
                viewHolder.mText3.setVisibility(View.GONE);
                viewHolder.mText4.setVisibility(View.GONE);

            }
        }

        @Override
        public int getItemCount() {
            return this.mDatasetMyAcMMenu.size();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

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
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            finish();
        } else if (id == R.id.user_name) {
            Methods.toast(getResources().getString(R.string.already_in_your_account));
        }
     /*   else if (id == R.id.company_profile) {
            Intent intent = new Intent(getApplicationContext(),CompanyProfile.class);
            startActivity(intent);
        }
        else if (id == R.id.company_aims) {
            Intent intent = new Intent(getApplicationContext(), Company_Aims.class);
            startActivity(intent);
        }*/
        else if (id == R.id.customer_care) {
            Intent intent = new Intent(getApplicationContext(), Customer_Care.class);
            startActivity(intent);
        }
        else if (id == R.id.offer) {
            Intent intent = new Intent(getApplicationContext(), Offer.class);
            startActivity(intent);
        }

        else if (id == R.id.login) {
            Intent intent = new Intent(MyAccountMainMenu.this, Login.class);
            startActivity(intent);
        } else if (id == R.id.search) {
            Intent intent = new Intent(MyAccountMainMenu.this, Search.class);
            startActivity(intent);
        } else if (id == R.id.register) {
            Intent intent = new Intent(MyAccountMainMenu.this, SignUp.class);
            startActivity(intent);
        } else if (id == R.id.invite) {


            Intent sharingintent = new Intent(Intent.ACTION_SEND);
            sharingintent.setType("text/plain");
            sharingintent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share The App");
            sharingintent.putExtra(android.content.Intent.EXTRA_TEXT, "Download Tasty Town Foods App From Play Store - https://play.google.com/store/apps/details?id=com.pooja_prasad ");
            startActivity(Intent.createChooser(sharingintent, "Share Via"));
        }

        else if (id == R.id.logout) {
            DataStorage.mRemoveSharedPreferenceString(getApplicationContext(),"profile_pic");
            DataBaseHandlerAccount.getInstance(getApplicationContext()).delete_account_detail();
            DataBaseHandlerWishList.getInstance(getApplicationContext()).delete_wish_list();
            DataBaseHandlerCart.getInstance(getApplicationContext()).delete_cart();
            DataBaseHandlerCartOptions.getInstance(getApplicationContext()).delete_cart_option();
            Intent intent = new Intent(MyAccountMainMenu.this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_wish_list) {
            if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                Methods.toast(getResources().getString(R.string.must_login));
                Intent intent = new Intent(MyAccountMainMenu.this, Login.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MyAccountMainMenu.this, Wish_List.class);
                startActivity(intent);
            }
        } else if (id == R.id.my_order) {
            Intent intent = new Intent(MyAccountMainMenu.this, OrderHistory.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
