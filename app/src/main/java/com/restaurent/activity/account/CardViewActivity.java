package com.restaurent.activity.account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import com.restaurent.R;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.restaurent.activity.Cart;
import com.restaurent.activity.Home;
import com.restaurent.activity.NoInternetConnection;
import com.restaurent.activity.Wish_List;
import com.restaurent.activity.user.Availability_of_products;
import com.restaurent.activity.user.CompanyProfile;
import com.restaurent.activity.user.Company_Aims;
import com.restaurent.activity.user.Customer_Care;
import com.restaurent.activity.user.Login;

import com.restaurent.activity.user.Offer;
import com.restaurent.activity.user.SignUp;
import com.restaurent.api_call.API_Get;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.constant_class.URL_Class;
import com.restaurent.db_handler.DataBaseHandlerAccount;
import com.restaurent.db_handler.DataBaseHandlerCart;
import com.restaurent.db_handler.DataBaseHandlerCartOptions;
import com.restaurent.db_handler.DataBaseHandlerWishList;
import com.restaurent.interfaces.API_Result;
import com.restaurent.json_mechanism.GetJSONData;
import com.restaurent.mechanism.Methods;
import com.restaurent.network_checker.NetworkConnection;
import com.restaurent.shared_preferenc_estring.DataStorage;
import com.restaurent.utils.AccountDataSet;
import com.restaurent.utils.Response;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

public class CardViewActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener , API_Result {

    public RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    public ArrayList<AccountDataSet> profileList = new ArrayList<>();
    Toolbar toolbar;
    Button btn_new_address;
    int country_id;
    int zone_id;
    Boolean value;
    API_Result api_result;
    CircleImageView profilePictureView;
    Bitmap fb_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        api_result = this;

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

        url();
        btn_new_address = (Button) findViewById(R.id.btn_new_adds);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_adds_book);

        btn_new_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(CardViewActivity.this, AddsBook_EditAdds.class);
                startActivity(ii);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cart_count_value) {
            cart();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (id == R.id.cart_image_view) {
            cart();
        }
        return false;
    }

    public void cart(){
        Intent intent = new Intent(CardViewActivity.this, Cart.class);
        startActivity(intent);
    }

    public void url() {
        if (NetworkConnection.connectionChecking(getApplicationContext())) {
            String url[] = {URL_Class.mURL + URL_Class.mURL_Get_Customer_Profile + URL_Class.mCustomer_id + String.valueOf(DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_id())};
            new API_Get().get_method(url, api_result, "",
                    JSON_Names.KEY_GET_TYPE, true, getBaseContext(), "CustomerProfile");
        } else {
            Intent intent = new Intent(CardViewActivity.this, NoInternetConnection.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        invalidateOptionsMenu();
        super.onResume();
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
        }
      /*  else if (id == R.id.company_profile) {
            Intent intent = new Intent(getApplicationContext(),CompanyProfile.class);
            startActivity(intent);
        }
        else if (id == R.id.company_aims) {
            Intent intent = new Intent(getApplicationContext(), Company_Aims.class);
            startActivity(intent);
        }*/
        else if (id == R.id.user_name) {
            Intent intent = new Intent(CardViewActivity.this, MyAccountMainMenu.class);
            startActivity(intent);
        } else if (id == R.id.login) {
            Intent intent = new Intent(CardViewActivity.this, Login.class);
            startActivity(intent);
        } else if (id == R.id.register) {
            Intent intent = new Intent(CardViewActivity.this, SignUp.class);
            startActivity(intent);
        }
            else if (id == R.id.invite) {


                Intent sharingintent = new Intent(Intent.ACTION_SEND);
                sharingintent.setType("text/plain");
                sharingintent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share The App");
                sharingintent.putExtra(android.content.Intent.EXTRA_TEXT, "Download Tasty Town Foods App From Play Store - https://play.google.com/store/apps/details?id=com.pooja_prasad ");
                startActivity(Intent.createChooser(sharingintent, "Share Via"));
            }
        else if (id == R.id.customer_care) {
            Intent intent = new Intent(getApplicationContext(), Customer_Care.class);
            startActivity(intent);
        }
        else if (id == R.id.offer) {
            Intent intent = new Intent(getApplicationContext(), Offer.class);
            startActivity(intent);
        }
      /*  else if (id == R.id.availability_of_products) {
            Intent intent = new Intent(getApplicationContext(), Availability_of_products.class);
            startActivity(intent);
        }*/
            else if (id == R.id.logout) {
            DataStorage.mRemoveSharedPreferenceString(getApplicationContext(),"profile_pic");
            DataBaseHandlerAccount.getInstance(getApplicationContext()).delete_account_detail();
            DataBaseHandlerWishList.getInstance(getApplicationContext()).delete_wish_list();
            DataBaseHandlerCart.getInstance(getApplicationContext()).delete_cart();
            DataBaseHandlerCartOptions.getInstance(getApplicationContext()).delete_cart_option();
            Intent intent = new Intent(CardViewActivity.this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_wish_list) {
            if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                Methods.toast(getResources().getString(  R.string.must_login));
                Intent intent = new Intent(CardViewActivity.this, Login.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(CardViewActivity.this, Wish_List.class);
                startActivity(intent);
            }
        } else if (id == R.id.my_order) {
            Intent intent = new Intent(CardViewActivity.this, OrderHistory.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void result(String[] data, String source) {
        if (data != null) {
            if (data[0] != null) {
                if (source.equals("CustomerProfile")) {
                    profileList = GetJSONData.getCustomerAddress(data[0]);
                    mRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(CardViewActivity.this);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new RecyclerViewAdapter(profileList);
                    mRecyclerView.setAdapter(mAdapter);
                } else if (source.equals("AddressDelete")) {
                    Response response = GetJSONData.getResponse(data[0]);
                    if (response != null) {
                        if (response.getmStatus() == 200) {
                            Methods.toast(getResources().getString( R.string.adds_successfully_deleted));
                        } else {
                            Methods.toast(getResources().getString(R.string.error));
                        }
                    }

                }
            } else {
                Methods.toast(getResources().getString(R.string.error));
            }
        } else {
            Methods.toast(getResources().getString(R.string.error));
        }
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.DataObjectHolder> {

        private ArrayList<AccountDataSet> mDataSet;

        public RecyclerViewAdapter(ArrayList<AccountDataSet> myDataSet) {
            mDataSet = myDataSet;
        }

        @Override
        public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_view_row, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final DataObjectHolder holder, final int position) {

            value = mDataSet.get(position).getmAddress_Id().equals(mDataSet.get(position).getmDefaultAddressId());
            holder.hFirstName.setText(mDataSet.get(position).getmFirstName());
            holder.hLastName.setText(mDataSet.get(position).getmLastName());
            holder.hCompany.setText(mDataSet.get(position).getmCompany());
            holder.hAddress1.setText(mDataSet.get(position).getmAddress_1());
            holder.hCity.setText(mDataSet.get(position).getmCity());
            holder.hPostcode.setText(mDataSet.get(position).getmPostcode());
            holder.hCountry.setText(mDataSet.get(position).getmCountry());
            holder.hRegionstate.setText(mDataSet.get(position).getmState());

            if(mDataSet.get(position).getmCountry_id()!=null) {
                country_id = Integer.valueOf(mDataSet.get(position).getmCountry_id());
            }else {
                country_id=0;
            }
            if(mDataSet.get(position).getmZone_id()!=null) {
                zone_id = Integer.valueOf(mDataSet.get(position).getmZone_id());
            }else {
                zone_id=0;
            }

            holder.editItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(v.getContext(), AddsBook_EditAdds.class);
                    i.putExtra(JSON_Names.KEY_ADDRESS_ID, mDataSet.get(position).getmAddress_Id());
                    i.putExtra(JSON_Names.KEY_CUSTOMER_ID, mDataSet.get(position).getmCustomerId());
                    i.putExtra(JSON_Names.KEY_FIRST_NAME, holder.hFirstName.getText().toString());
                    i.putExtra(JSON_Names.KEY_LAST_NAME, holder.hLastName.getText().toString());
                    i.putExtra(JSON_Names.KEY_COMPANY, holder.hCompany.getText().toString());
                    i.putExtra(JSON_Names.KEY_ADDRESS_1, holder.hAddress1.getText().toString());
                    i.putExtra(JSON_Names.KEY_CITY, holder.hCity.getText().toString());
                    i.putExtra(JSON_Names.KEY_POSTCODE, holder.hPostcode.getText().toString());
                    i.putExtra(JSON_Names.KEY_COUNTRY_ID,country_id );
                    i.putExtra(JSON_Names.KEY_ZONE_ID,zone_id );
                    i.putExtra(JSON_Names.KEY_COUNTRY, holder.hCountry.getText().toString());
                    i.putExtra(JSON_Names.KEY_STATE, holder.hRegionstate.getText().toString());
                    i.putExtra(JSON_Names.KEY_DEFAULT_ADDRESS_ID, value);
                    v.getContext().startActivity(i);

                }
            });

            holder.deleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(position);
                }
            });
        }

        public int deleteItem(int index) {
            String tempAddsId = mDataSet.get(index).getmAddress_Id();
            String tempCustomerId = mDataSet.get(index).getmCustomerId();
            String tempDefAddsID = mDataSet.get(index).getmDefaultAddressId();

            if (!tempAddsId.equals(tempDefAddsID)) {
                if (mDataSet.size() > 1) {
                    mDataSet.remove(index);
                    notifyItemRemoved(index);
                    notifyDataSetChanged();
                    if (NetworkConnection.connectionChecking(getApplicationContext())) {
                        String url[] = {URL_Class.mURL + URL_Class.mURL_Delete_Address +
                                URL_Class.mCustomer_id + tempCustomerId + URL_Class.mAddress_Id + tempAddsId};
                        new API_Get().get_method(url, api_result, "",
                                JSON_Names.KEY_GET_TYPE, true, getBaseContext(), "AddressDelete");
                    } else {
                        Intent intent = new Intent(CardViewActivity.this, NoInternetConnection.class);
                        startActivity(intent);
                        finish();
                    }

                } else {
                   Methods.toast(getResources().getString(  R.string.address_warning));
                }
            } else {
                Methods.toast(getResources().getString(  R.string.default_address_warning));
            }
            return index;
        }

        @Override
        public int getItemCount() {
            return this.mDataSet.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {

            TextView hFirstName, hLastName, hCompany, hAddress1, hCity, hPostcode, hCountry, hRegionstate;
            Button editItem, deleteItem;

            public DataObjectHolder(View view) {
                super(view);
                hFirstName = (TextView) view.findViewById(R.id.tv_card_first_name);
                hLastName = (TextView) view.findViewById(R.id.tv_card_last_name);
                hCompany = (TextView) view.findViewById(R.id.tv_card_company);
                hAddress1 = (TextView) view.findViewById(R.id.tv_card_address1);
                hCity = (TextView) view.findViewById(R.id.tv_card_city);
                hPostcode = (TextView) view.findViewById(R.id.tv_card_post_code);
                hCountry = (TextView) view.findViewById(R.id.tv_card_country);
                hRegionstate = (TextView) view.findViewById(R.id.tv_card_state);

                editItem = (Button) view.findViewById(R.id.img_btn_edit);
                deleteItem = (Button) view.findViewById(R.id.img_btn_delete);
            }
        }
    }
}
