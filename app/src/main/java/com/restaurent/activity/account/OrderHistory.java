package com.restaurent.activity.account;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.restaurent.R;
import com.restaurent.activity.Cart;
import com.restaurent.activity.NoInternetConnection;
import com.restaurent.activity.Search;
import com.restaurent.activity.user.Availability_of_products;
import com.restaurent.activity.user.Customer_Care;
import com.restaurent.activity.user.Login;

import com.restaurent.activity.user.Offer;
import com.restaurent.activity.user.SignUp;
import com.restaurent.api_call.API_Get;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.constant_class.URL_Class;
import com.restaurent.db_handler.DataBaseHandlerAccount;
import com.restaurent.db_handler.DataBaseHandlerCart;
import com.restaurent.interfaces.API_Result;
import com.restaurent.network_checker.NetworkConnection;
import com.restaurent.shared_preferenc_estring.DataStorage;
import com.restaurent.utils.OrderHistoryData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderHistory extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, API_Result {

    public ArrayList<OrderHistoryData> tempOProductList = new ArrayList<>();
    public ArrayList<OrderHistoryData> tempOOtotalsList = new ArrayList<>();
    public ArrayList<ArrayList<ArrayList<OrderHistoryData>>> tempOOptionsList = new ArrayList<>();
    public Integer mOH_JArrayLength = 0;
    android.support.v7.widget.Toolbar toolbar;
    Button btn_continue;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<OrderHistoryData> mOrderHListMain = new ArrayList<>();
    private ArrayList<ArrayList<OrderHistoryData>> mOrdersProductList = new ArrayList<>();
    private ArrayList<ArrayList<OrderHistoryData>> mOrdersOTotalsList = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<OrderHistoryData>>> mOrdersPOptionsList = new ArrayList<>();
    private ArrayList<Integer> nopCout = new ArrayList<>();
    ProgressDialog dialog;
    API_Result api_result;
    CircleImageView profilePictureView;
    Bitmap fb_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myorder_main);

        dialog = new ProgressDialog(this);
        api_result = this;

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.actionbar);
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

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.actionbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btn_continue = (Button) findViewById(R.id.btn_my_order_continue);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        btn_continue.setOnClickListener(this);

        send_request();

    }

    public void send_request() {
        if (NetworkConnection.connectionChecking(getApplicationContext())) {
            dialog.setTitle("Please wait!");
            dialog.setMessage("Loading . . .");
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            String url[] = {URL_Class.mURL + URL_Class.mGet_Customer_Order + URL_Class.mCustomer_id + String.valueOf(DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_id())};
            new API_Get().get_method(url, api_result, "", JSON_Names.KEY_GET_TYPE, true, getBaseContext(), "CustomerProfile");
        } else {
            Intent intent = new Intent(OrderHistory.this, NoInternetConnection.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cart_count_value) {
            Intent intent = new Intent(OrderHistory.this, Cart.class);
            startActivity(intent);
        } else if (id == R.id.btn_my_order_continue) {
            onBackPressed();
            finish();
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
            Intent intent = new Intent(OrderHistory.this, Cart.class);
            startActivity(intent);
        }
        return false;
    }

    public void noOfProducts() {

        for (int c = 0; c < mOrdersProductList.size(); c++) {
            nopCout.add(mOrdersProductList.get(c).size());
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
            menu.findItem(R.id.my_order).setVisible(false);
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
            onBackPressed();
            finish();
        } else if (id == R.id.login) {
            Intent intent = new Intent(OrderHistory.this, Login.class);
            startActivity(intent);
        } else if (id == R.id.register) {
            Intent intent = new Intent(OrderHistory.this, SignUp.class);
            startActivity(intent);
        } else if (id == R.id.search) {
            Intent intent = new Intent(OrderHistory.this, Search.class);
            startActivity(intent);
        }

        else if (id == R.id.customer_care) {
            Intent intent = new Intent(getApplicationContext(), Customer_Care.class);
            startActivity(intent);
        }
        else if (id == R.id.offer) {
            Intent intent = new Intent(getApplicationContext(), Offer.class);
            startActivity(intent);
        }        return super.onOptionsItemSelected(item);
    }

    @Override
    public void result(String[] data, String source) {
        dialog.cancel();
        if (source.equals("CustomerProfile")) {
            if (data != null) {
                try {
                    JSONObject jobject = new JSONObject(data[0]);
                    JSONArray jarray = jobject.getJSONArray("orders");
                    mOH_JArrayLength = jarray.length();

                    if (mOH_JArrayLength != 0) {
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jgetobject = jarray.getJSONObject(i);

                            OrderHistoryData basemydataObject = new OrderHistoryData(jgetobject.optString("order_id"), jgetobject.optString("invoice_no"), jgetobject.optString("invoice_prefix"),
                                    jgetobject.optString("store_id"), jgetobject.optString("store_name"), jgetobject.optString("store_url"), jgetobject.optString("customer_id"),
                                    jgetobject.optString("firstname"), jgetobject.optString("lastname"), jgetobject.optString("telephone"), jgetobject.optString("fax"),
                                    jgetobject.optString("email"), jgetobject.optString("payment_firstname"), jgetobject.optString("payment_lastname"), jgetobject.optString("payment_company"),
                                    jgetobject.optString("payment_address_1"), jgetobject.optString("payment_address_2"), jgetobject.optString("payment_postcode"), jgetobject.optString("payment_city"),
                                    jgetobject.optString("payment_zoneid"), jgetobject.optString("payment_zone"), jgetobject.optString("payment_zone_code"), jgetobject.optString("payment_countryid"),
                                    jgetobject.optString("payment_country"), jgetobject.optString("payment_iso_code_2"), jgetobject.optString("payment_iso_code_3"), jgetobject.optString("payment_address_format"),
                                    jgetobject.optString("payment_method"),
                                    jgetobject.optString("shipping_firstname"), jgetobject.optString("shipping_lastname"), jgetobject.optString("shipping_company"),
                                    jgetobject.optString("shipping_address_1"), jgetobject.optString("shipping_address_2"), jgetobject.optString("shipping_postcode"), jgetobject.optString("shipping_city"),
                                    jgetobject.optString("shipping_zoneid"), jgetobject.optString("shipping_zone"), jgetobject.optString("shipping_zone_code"), jgetobject.optString("shipping_countryid"),
                                    jgetobject.optString("shipping_country"), jgetobject.optString("shipping_iso_code_2"), jgetobject.optString("shipping_iso_code_3"), jgetobject.optString("shipping_address_format"),
                                    jgetobject.optString("shipping_method"),
                                    jgetobject.optString("comment"), jgetobject.optString("total"), jgetobject.optString("order_status_id"),
                                    jgetobject.optString("order_status"), jgetobject.optString("language_id"), jgetobject.optString("currency_id"), jgetobject.optString("currency_code"),
                                    jgetobject.optString("currency_value"), jgetobject.optString("date_modified"), jgetobject.optString("date_added"), jgetobject.optString("ip"));

                            mOrderHListMain.add(basemydataObject);

                            JSONArray jsonArray1 = jgetobject.getJSONArray("products");
                            ArrayList<OrderHistoryData> subtemplist1 = new ArrayList<>();
                            ArrayList<ArrayList<OrderHistoryData>> subtempListInner = new ArrayList<>();
                            for (int j = 0; j < jsonArray1.length(); j++) {
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);

                                OrderHistoryData submydataObject1 = new OrderHistoryData(jsonObject1.optString("order_product_id"), jsonObject1.optString("order_id"), jsonObject1.optString("product_id"), jsonObject1.optString("name"),
                                        jsonObject1.optString("model"), jsonObject1.optString("quantity"), jsonObject1.optString("price"), jsonObject1.optString("total"),
                                        jsonObject1.optString("tax"), jsonObject1.optString("reward"), jsonObject1.optString("image"));

                                subtemplist1.add(submydataObject1);

                                JSONArray jsonArray1Sub = jsonObject1.getJSONArray("options");
                                ArrayList<OrderHistoryData> subtemplist1Sub = new ArrayList<>();
                                for (int jj = 0; jj < jsonArray1Sub.length(); jj++) {
                                    JSONObject jsonObject1Sub = jsonArray1Sub.getJSONObject(jj);

                                    OrderHistoryData submydataObject1Sub = new OrderHistoryData(jsonObject1Sub.optString("order_option_id"), jsonObject1Sub.optString("order_id"), jsonObject1Sub.optString("order_product_id"), jsonObject1Sub.optString("product_option_id"),
                                            jsonObject1Sub.optString("product_option_value_id"), jsonObject1Sub.optString("name"), jsonObject1Sub.optString("value"), jsonObject1Sub.optString("type"));

                                    subtemplist1Sub.add(submydataObject1Sub);
                                }
                                subtempListInner.add(subtemplist1Sub);
                            }
                            mOrdersPOptionsList.add(subtempListInner);
                            mOrdersProductList.add(subtemplist1);

                            JSONArray jsonArray2 = jgetobject.getJSONArray("order_totals");
                            ArrayList<OrderHistoryData> subtemplist2 = new ArrayList<>();
                            for (int k = 0; k < jsonArray2.length(); k++) {
                                JSONObject jsonObject2 = jsonArray2.getJSONObject(k);

                                OrderHistoryData submydataObject2 = new OrderHistoryData(jsonObject2.optString("order_total_id"), jsonObject2.optString("order_id"), jsonObject2.optString("code"),
                                        jsonObject2.optString("title"), jsonObject2.optString("value"), jsonObject2.optString("sort_order"), jsonObject2.optString("text"));

                                subtemplist2.add(submydataObject2);
                            }
                            mOrdersOTotalsList.add(subtemplist2);
                        }
                    }

                } catch (Exception e) {
                    mOrderHListMain = null;
                    mOrdersPOptionsList = null;
                    mOrdersProductList = null;
                    mOrdersOTotalsList = null;
                }

                if (mOH_JArrayLength != 0) {
                    mRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(OrderHistory.this);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    noOfProducts();
                    if (mOrderHListMain != null) {
                        mAdapter = new OrderHistoryAdapter(mOrderHListMain, nopCout);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                } else {
                    btn_continue.setVisibility(View.GONE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderHistory.this);
                    builder.setMessage(R.string.empty_order_history).setCancelable(false);
                    builder.setPositiveButton(R.string._continue, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            onBackPressed();
                            finish();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        }
    }


    public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

        private ArrayList<OrderHistoryData> mDatasetMain;
        private ArrayList<Integer> mNOPCount;

        // Provide a suitable constructor (depends on the kind of dataset)
        public OrderHistoryAdapter(ArrayList<OrderHistoryData> myDatasetMain, ArrayList<Integer> nopcount) {
            this.mDatasetMain = myDatasetMain;
            this.mNOPCount = nopcount;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.myorder_recycle_row, parent, false);
            // set the view's size, margins, paddings and layout parameters
            return new ViewHolder(v);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final int temp = position;

            holder.txtOrderStatus.setText(mDatasetMain.get(position).getmOrderStatus());
            holder.txtOrderId.setText(mDatasetMain.get(position).getmOrderId());
            holder.txtDateAdded.setText(mDatasetMain.get(position).getmDateAdded());
            String nopcount = mNOPCount.get(position).toString();
            holder.txtNoOfPro.setText(nopcount);
            holder.txtTotal.setText(mDatasetMain.get(position).getmTotal());
            if (mOrdersProductList.get(position) != null) {
                holder.btnView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), OrderHistoryOrderInfo.class);
                        i.putExtra("orderid", mDatasetMain.get(temp).getmOrderId());
                        i.putExtra("dateadded", mDatasetMain.get(temp).getmDateAdded());
                        i.putExtra("paymethod", mDatasetMain.get(temp).getmPayMethod());
                        i.putExtra("shipmethod", mDatasetMain.get(temp).getmShipMethod());
                        i.putExtra("payfname", mDatasetMain.get(temp).getmPayFirstName());
                        i.putExtra("paylname", mDatasetMain.get(temp).getmPayLastName());
                        i.putExtra("paycompany", mDatasetMain.get(temp).getmPayCompany());
                        i.putExtra("payadds1", mDatasetMain.get(temp).getmPayAdds1());
                        i.putExtra("payadds2", mDatasetMain.get(temp).getmPayAdds2());
                        i.putExtra("paycity", mDatasetMain.get(temp).getmPayCity());
                        i.putExtra("paypcode", mDatasetMain.get(temp).getmPayPostCode());
                        i.putExtra("payzone", mDatasetMain.get(temp).getmPayZone());
                        i.putExtra("paycountry", mDatasetMain.get(temp).getmPayCountry());
                        i.putExtra("sfname", mDatasetMain.get(temp).getmShipFirstName());
                        i.putExtra("slname", mDatasetMain.get(temp).getmShipLastName());
                        i.putExtra("scompany", mDatasetMain.get(temp).getmShipCompany());
                        i.putExtra("sadds1", mDatasetMain.get(temp).getmShipAdds1());
                        i.putExtra("sadds2", mDatasetMain.get(temp).getmShipAdds2());
                        i.putExtra("scity", mDatasetMain.get(temp).getmShipCity());
                        i.putExtra("spcode", mDatasetMain.get(temp).getmShipPostcode());
                        i.putExtra("szone", mDatasetMain.get(temp).getmShipZone());
                        i.putExtra("scountry", mDatasetMain.get(temp).getmShipCountry());
                        i.putExtra("orderstatus", mDatasetMain.get(temp).getmOrderStatus());
                        i.putExtra("comment", mDatasetMain.get(temp).getmComment());
                        tempOProductList = mOrdersProductList.get(temp);
                        i.putExtra("OrdersProductList", tempOProductList);
                        tempOOptionsList.add(mOrdersPOptionsList.get(temp));
                        i.putExtra("OrdersPOptionsList", tempOOptionsList);
                        tempOOtotalsList = mOrdersOTotalsList.get(temp);
                        i.putExtra("OrdersOTotalsList", tempOOtotalsList);
                        i.putExtra("total", mDatasetMain.get(temp).getmTotal());
                        v.getContext().startActivity(i);
                        finish();
                    }
                });
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return this.mDatasetMain.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView txtOrderId, txtOrderStatus;
            public TextView txtDateAdded;
            public TextView txtNoOfPro;
            public TextView txtTotal;
            public Button btnView;

            public ViewHolder(View v) {
                super(v);
                txtOrderId = (TextView) v.findViewById(R.id.tv_my_order_recycle_order_id_data);
                txtDateAdded = (TextView) v.findViewById(R.id.tv_my_order_recycle_date_add_data);
                txtNoOfPro = (TextView) v.findViewById(R.id.tv_my_order_recycle_no_of_pro_data);
                txtTotal = (TextView) v.findViewById(R.id.tv_my_order_recycle_total_data);
                txtOrderStatus = (TextView) v.findViewById(R.id.tv_my_order_recycle_order_status_data);
                btnView = (Button) v.findViewById(R.id.img_btn_my_order_recycle_view);
            }
        }

    }
}