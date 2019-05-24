package com.restaurent.activity.account;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.restaurent.utils.OrderHistoryData;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderHistoryOrderInfo extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    public Button btn_orderhis_countinue;
    public TextView txtOrderId, txtOrderStatus;
    public TextView txtDateAdded;
    public TextView txtpaymethod;
    public TextView txtshipmethod;
    public TextView txtpayname;
    public TextView txtpaycompany;
    public TextView txtpayadds1;
    public TextView txtpayadds2;
    public TextView txtpaycity;
    public TextView txtpayzone;
    public TextView txtpaycountry;
    public TextView txtsname;
    public TextView txtscompany;
    public TextView txtsadds1;
    public TextView txtsadds2;
    public TextView txtscity;
    public TextView txtszone;
    public TextView txtscountry;
    public TextView txtsubtotalValue, txtsubtotalName, txtFinalTotalValue, txtFinalTotalName;
    public TextView payInfAdds, shipInfAdds;
    public RelativeLayout payInfAddsRLayout, shipInfAddsRLayout;
    public LinearLayout mLinearLayoutShipping, mLinearLayoutFlateTax;
    Toolbar toolbar;
    private ArrayList<OrderHistoryData> ROrdersProductList = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<OrderHistoryData>>> ROrdersPOptionsList = new ArrayList<>();
    private ArrayList<OrderHistoryData> ROrdersOTotalsList = new ArrayList<>();
    CircleImageView profilePictureView;
    Bitmap fb_img;
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_history_order_info);

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

        txtOrderId = (TextView) findViewById(R.id.tv_o_details1_oid_data);
        txtDateAdded = (TextView) findViewById(R.id.tv_o_details1_date_add_data);
        txtpaymethod = (TextView) findViewById(R.id.tv_o_details1_payment_data);
        txtshipmethod = (TextView) findViewById(R.id.tv_o_details1_shipment_data);
        txtOrderStatus = (TextView) findViewById(R.id.tv_o_details1_ostatus_data);

        txtpayname = (TextView) findViewById(R.id.tv_o_pay_adds_f_name);

        txtpaycompany = (TextView) findViewById(R.id.tv_o_pay_adds_company);
        txtpayadds1 = (TextView) findViewById(R.id.tv_o_pay_adds_adds1);
        txtpayadds2 = (TextView) findViewById(R.id.tv_o_pay_adds_adds2);
        txtpaycity = (TextView) findViewById(R.id.tv_o_pay_adds_city);

        txtpayzone = (TextView) findViewById(R.id.tv_o_pay_adds_zone);
        txtpaycountry = (TextView) findViewById(R.id.tv_o_pay_adds_country);

        txtsname = (TextView) findViewById(R.id.tv_o_ship_adds_f_name);

        txtscompany = (TextView) findViewById(R.id.tv_o_ship_adds_company);
        txtsadds1 = (TextView) findViewById(R.id.tv_o_ship_adds_adds1);
        txtsadds2 = (TextView) findViewById(R.id.tv_o_ship_adds_adds2);
        txtscity = (TextView) findViewById(R.id.tv_o_ship_adds_city);

        txtszone = (TextView) findViewById(R.id.tv_o_ship_adds_zone);
        txtscountry = (TextView) findViewById(R.id.tv_o_ship_adds_country);


        txtsubtotalName = (TextView) findViewById(R.id.tv_order_his_subtotal);
        txtsubtotalValue = (TextView) findViewById(R.id.tv_order_his_subtotal_data);
        txtFinalTotalName = (TextView) findViewById(R.id.tv_order_his_total);
        txtFinalTotalValue = (TextView) findViewById(R.id.tv_order_his_total_data);

        payInfAdds = (TextView) findViewById(R.id.tv_order_his_o_pay_adds_title);
        payInfAdds.setOnClickListener(this);
        shipInfAdds = (TextView) findViewById(R.id.tv_order_his_o_ship_adds_title);
        shipInfAdds.setOnClickListener(this);
        payInfAddsRLayout = (RelativeLayout) findViewById(R.id.layout_l_order_his_o_pay_adds);
        payInfAddsRLayout.setVisibility(View.GONE);
        shipInfAddsRLayout = (RelativeLayout) findViewById(R.id.layout_l_order_his_o_ship_adds);
        shipInfAddsRLayout.setVisibility(View.GONE);

        if (ROrdersProductList != null) {
            ROrdersProductList = (ArrayList<OrderHistoryData>) getIntent().getSerializableExtra("OrdersProductList");
        }
        if (ROrdersPOptionsList != null) {
            ROrdersPOptionsList = (ArrayList<ArrayList<ArrayList<OrderHistoryData>>>) getIntent().getSerializableExtra("OrdersPOptionsList");
        }
        if (ROrdersOTotalsList != null) {
            ROrdersOTotalsList = (ArrayList<OrderHistoryData>) getIntent().getSerializableExtra("OrdersOTotalsList");
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            txtOrderId.setText(extras.getString("orderid"));
            txtDateAdded.setText(extras.getString("dateadded"));
            txtpaymethod.setText(extras.getString("paymethod"));
            txtshipmethod.setText(extras.getString("shipmethod"));
            txtOrderStatus.setText(extras.getString("orderstatus"));

            String tempfname = extras.getString("payfname");
            String templname = extras.getString("paylname");
            String pcustomerName;
            if (tempfname != null && templname != null) {
                pcustomerName = tempfname.concat(" " + templname);
                txtpayname.setText(pcustomerName);
            }
            if (tempfname != null && templname == null) {
                txtpayname.setText(tempfname);
            }
            if (tempfname == null && templname != null) {
                txtpayname.setText(templname);
            }
            if (tempfname == null && templname == null) {
                txtpayname.setVisibility(View.GONE);
            }
            if ((tempfname != null && tempfname.equals("")) && (templname != null && templname.equals(""))) {
                txtpayname.setVisibility(View.GONE);
            }
            String tempCmpy = extras.getString("paycompany");
            if (tempCmpy != null && !tempCmpy.equals("")) {
                txtpaycompany.setText(tempCmpy);
            } else {
                txtpaycompany.setVisibility(View.GONE);
            }
            String tempAdds1 = extras.getString("payadds1");
            if (tempAdds1 != null && !tempAdds1.equals("")) {
                txtpayadds1.setText(tempAdds1);
            } else {
                txtpayadds1.setVisibility(View.GONE);
            }
            String tempAdds2 = extras.getString("payadds2");
            if (tempAdds2 != null && !tempAdds2.equals("")) {
                txtpayadds2.setText(tempAdds2);
            } else {
                txtpayadds2.setVisibility(View.GONE);
            }
            String tempCity = extras.getString("paycity");
            String tempPostCode = extras.getString("paypcode");
            String tempSCityPCode;
            if (tempCity != null && tempPostCode != null) {
                tempSCityPCode = tempCity.concat(" " + tempPostCode);
                txtpaycity.setText(tempSCityPCode);
            }
            if (tempCity != null && tempPostCode == null) {
                txtpaycity.setText(tempCity);
            }
            if (tempCity == null && tempPostCode != null) {
                txtpaycity.setText(tempPostCode);
            }
            if (tempCity == null && tempPostCode == null) {
                txtpaycity.setVisibility(View.GONE);
            }
            if ((tempCity != null && tempCity.equals("")) && (tempPostCode != null && tempPostCode.equals(""))) {
                txtpaycity.setVisibility(View.GONE);
            }
            String tempZone = extras.getString("payzone");
            if (tempZone != null && !tempZone.equals("")) {
                txtpayzone.setText(tempZone);
            } else {
                txtpayzone.setVisibility(View.GONE);
            }
            String tempCntry = extras.getString("paycountry");
            if (tempCntry != null && !tempCntry.equals("")) {
                txtpaycountry.setText(tempCntry);
            } else {
                txtpaycountry.setVisibility(View.GONE);
            }


            String tempfname1 = extras.getString("sfname");
            String templname1 = extras.getString("slname");
            String scustomerName;
            if (tempfname1 != null && templname1 != null) {
                scustomerName = tempfname1.concat(" " + templname1);
                txtsname.setText(scustomerName);
            }
            if (tempfname1 != null && templname1 == null) {
                txtsname.setText(tempfname1);
            }
            if (tempfname1 == null && templname1 != null) {
                txtsname.setText(templname1);
            }
            if (tempfname1 == null && templname1 == null) {
                txtsname.setVisibility(View.GONE);
            }
            if ((tempfname1 != null && tempfname1.equals("")) && (templname1 != null && templname1.equals(""))) {
                txtsname.setVisibility(View.GONE);
            }
            String tempCmpy1 = extras.getString("scompany");
            if (tempCmpy1 != null && !tempCmpy1.equals("")) {
                txtscompany.setText(tempCmpy1);
            } else {
                txtscompany.setVisibility(View.GONE);
            }
            String tempAdds11 = extras.getString("sadds1");
            if (tempAdds11 != null && !tempAdds11.equals("")) {
                txtsadds1.setText(tempAdds11);
            } else {
                txtsadds1.setVisibility(View.GONE);
            }
            String tempAdds21 = extras.getString("sadds2");
            if (tempAdds21 != null && !tempAdds21.equals("")) {
                txtsadds2.setText(tempAdds21);
            } else {
                txtsadds2.setVisibility(View.GONE);
            }
            String tempCity1 = extras.getString("scity");
            String tempPostCode1 = extras.getString("spcode");
            String tempPCityPCode;
            //txtscity.setText(tempCity1);
            if (tempCity1 != null && tempPostCode1 != null) {
                tempPCityPCode = tempCity1.concat(" " + tempPostCode1);
                txtscity.setText(tempPCityPCode);
            }
            if (tempCity1 != null && tempPostCode1 == null) {
                txtscity.setText(tempCity1);
            }
            if (tempCity1 == null && tempPostCode1 != null) {
                txtscity.setText(tempPostCode1);
            }
            if (tempCity1 == null && tempPostCode1 == null) {
                txtscity.setVisibility(View.GONE);
            }
            if ((tempCity1 != null && tempCity1.equals("")) && (tempPostCode1 != null && tempPostCode1.equals(""))) {
                txtscity.setVisibility(View.GONE);
            }
            String tempZone1 = extras.getString("szone");
            if (tempZone1 != null && !tempZone1.equals("")) {
                txtszone.setText(tempZone1);
            } else {
                txtszone.setVisibility(View.GONE);
            }
            String tempCntry1 = extras.getString("scountry");
            if (tempCntry1 != null && !tempCntry1.equals("")) {
                txtscountry.setText(tempCntry1);
            } else {
                txtscountry.setVisibility(View.GONE);
            }

        }
        LayoutInflater calInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLinearLayoutShipping = (LinearLayout) findViewById(R.id.layout_l_order_his_shipping);
        mLinearLayoutFlateTax = (LinearLayout) findViewById(R.id.layout_l_order_his_tax);


        for (int t = 0; t < ROrdersOTotalsList.size(); t++) {
            String tempCode = ROrdersOTotalsList.get(t).getmCode();

            switch (tempCode) {
                case "sub_total": {
                    String tempsubtName = ROrdersOTotalsList.get(t).getmTitle();
                    String tempsubtValue = ROrdersOTotalsList.get(t).getmValue();
                    txtsubtotalName.setText(tempsubtName);
                    txtsubtotalValue.setText(tempsubtValue);
                    break;
                }
                case "shipping": {
                    View shipView = calInflater.inflate(R.layout.activity_payship_content, null);
                    int width = LinearLayout.LayoutParams.MATCH_PARENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;

                    TextView shipTitle, shipValue;
                    shipTitle = (TextView) shipView.findViewById(R.id.tv_order_his_title);
                    shipValue = (TextView) shipView.findViewById(R.id.tv_order_his_title_value);
                    shipTitle.setText(ROrdersOTotalsList.get(t).getmTitle());
                    shipValue.setText(ROrdersOTotalsList.get(t).getmValue());

                    LinearLayout.LayoutParams shipParams = new LinearLayout.LayoutParams(width, height);
                    shipView.setLayoutParams(shipParams);
                    mLinearLayoutShipping.addView(shipView);
                    break;

                }
                case "tax": {
                    View taxView = calInflater.inflate(R.layout.activity_payship_content, null);
                    int width = LinearLayout.LayoutParams.MATCH_PARENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;

                    TextView taxTitle, taxValue;
                    taxTitle = (TextView) taxView.findViewById(R.id.tv_order_his_title);
                    taxValue = (TextView) taxView.findViewById(R.id.tv_order_his_title_value);
                    taxTitle.setText(ROrdersOTotalsList.get(t).getmTitle());
                    taxValue.setText(ROrdersOTotalsList.get(t).getmValue());

                    LinearLayout.LayoutParams taxParams = new LinearLayout.LayoutParams(width, height);
                    taxView.setLayoutParams(taxParams);
                    mLinearLayoutFlateTax.addView(taxView);
                    break;

                }
                case "total": {
                    String temptotalName = ROrdersOTotalsList.get(t).getmTitle();
                    String temptotalValue = ROrdersOTotalsList.get(t).getmValue();
                    txtFinalTotalName.setText(temptotalName);
                    txtFinalTotalValue.setText(temptotalValue);
                    break;
                }
                default:
                    View shipView = calInflater.inflate(R.layout.activity_payship_content, null);
                    int width = LinearLayout.LayoutParams.MATCH_PARENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;

                    TextView shipTitle, shipValue;
                    shipTitle = (TextView) shipView.findViewById(R.id.tv_order_his_title);
                    shipValue = (TextView) shipView.findViewById(R.id.tv_order_his_title_value);
                    shipTitle.setText(ROrdersOTotalsList.get(t).getmTitle());
                    shipValue.setText(ROrdersOTotalsList.get(t).getmValue());

                    LinearLayout.LayoutParams shipParams = new LinearLayout.LayoutParams(width, height);
                    shipView.setLayoutParams(shipParams);
                    mLinearLayoutShipping.addView(shipView);
                    break;
            }
        }

        btn_orderhis_countinue = (Button) findViewById(R.id.btn_order_his_o_info_continue);
        btn_orderhis_countinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout parent = (LinearLayout) findViewById(R.id.layout_l_dynamic_content);
        for (int x = 0; x < ROrdersProductList.size(); x++) {
            View vw = inflater.inflate(R.layout.order_history_oinfo_recycrow, null);
            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;

            TextView txtproductname, txtmodel, txtquantity, txtprice, txttotal/*,
                    txtColorName, txtSizeName*/;
            LinearLayout option_holder = (LinearLayout) vw.findViewById(R.id.layout_l_order_his_options);

            ImageView imgProductList;
            imgProductList = (ImageView) vw.findViewById(R.id.imageview_product_list);

            txtproductname = (TextView) vw.findViewById(R.id.tv_order_his_recycle_title);
            txtmodel = (TextView) vw.findViewById(R.id.tv_order_his_recycle_model_data);
            txtquantity = (TextView) vw.findViewById(R.id.tv_order_his_recycle_quantity_data);
            txtprice = (TextView) vw.findViewById(R.id.tv_order_his_recycle_price_data);
            txttotal = (TextView) vw.findViewById(R.id.tv_order_his_recycle_total_data);

            /*txtColorName = (TextView) vw.findViewById(R.id.tv_order_his_recycle_color);
            txtSizeName = (TextView) vw.findViewById(R.id.tv_order_his_recycle_size);*/

            Methods.glide_image_loader_fixed_size(ROrdersProductList.get(x).getmImageUrl(), imgProductList);

            String proname = ROrdersProductList.get(x).getmName();
            String model = ROrdersProductList.get(x).getmModel();
            String quantity = ROrdersProductList.get(x).getmQuantity();
            String price = ROrdersProductList.get(x).getmPrice();
            String ltotal = ROrdersProductList.get(x).getmProductsTotal();

            String connectQuantity = "Quantity" + " : " + quantity;
            String connectModel = "Model" + " : " + model;

            txtproductname.setText(proname);
            txtmodel.setText(connectModel);
            txtquantity.setText(connectQuantity);
            txtprice.setText(price);
            txttotal.setText(ltotal);

            if (!(ROrdersPOptionsList.size() == 0)) {
                int p = 0;
                for (int q = 0; q < ROrdersPOptionsList.get(p).size(); q++)  // product x
                {
                    if (ROrdersPOptionsList.get(p).get(q).size() != 0) {
                        for (int r = 0; r < ROrdersPOptionsList.get(p).get(q).size(); r++)  // product x option r
                        {
                            if ((ROrdersProductList.get(x).getmOrderProductId()).equals(ROrdersPOptionsList.get(p).get(q).get(r).getmOptionsOrderProductId())) {

                                View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.cart_option_holder, null, false);
                                TextView mOptionTitle = (TextView) view.findViewById(R.id.lblListItem);

                                String optionDataString = ROrdersPOptionsList.get(p).get(q).get(r).getmOptionsName() + " : "
                                        + ROrdersPOptionsList.get(p).get(q).get(r).getmOptionsValue();
                                mOptionTitle.setText(optionDataString);
                                option_holder.addView(view);

                            }

                        }
                    }
                }
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            params.setMargins(0, 4, 0, 4);
            vw.setLayoutParams(params);
            parent.addView(vw);
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
            startActivity(new Intent(OrderHistoryOrderInfo.this, OrderHistory.class));
            finish();
        } else if (id == R.id.user_name) {
            onBackPressed();
            finish();
        }

        else if (id == R.id.login) {
            Intent intent = new Intent(OrderHistoryOrderInfo.this, Login.class);
            startActivity(intent);
        } else if (id == R.id.search) {
            Intent intent = new Intent(OrderHistoryOrderInfo.this, Search.class);
            startActivity(intent);
        } else if (id == R.id.register) {
            Intent intent = new Intent(OrderHistoryOrderInfo.this, SignUp.class);
            startActivity(intent);
        } else if (id == R.id.invite) {


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

        else if (id == R.id.logout) {
            DataStorage.mRemoveSharedPreferenceString(getApplicationContext(),"profile_pic");
            DataBaseHandlerAccount.getInstance(getApplicationContext()).delete_account_detail();
            DataBaseHandlerWishList.getInstance(getApplicationContext()).delete_wish_list();
            DataBaseHandlerCart.getInstance(getApplicationContext()).delete_cart();
            DataBaseHandlerCartOptions.getInstance(getApplicationContext()).delete_cart_option();
            Intent intent = new Intent(OrderHistoryOrderInfo.this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_wish_list) {
            if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                Methods.toast(getResources().getString(R.string.must_login));
                Intent intent = new Intent(OrderHistoryOrderInfo.this, Login.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(OrderHistoryOrderInfo.this, Wish_List.class);
                startActivity(intent);
            }
        } else if (id == R.id.my_order) {
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cart_count_value) {
            Intent intent = new Intent(OrderHistoryOrderInfo.this, Cart.class);
            startActivity(intent);
        } else if (id == R.id.tv_order_his_o_pay_adds_title) {
            if (payInfAddsRLayout.getVisibility() == View.GONE) {
                payInfAdds.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_less_black_24dp, 0);
                payInfAddsRLayout.setVisibility(View.VISIBLE);
            } else {
                payInfAdds.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_more_black_24dp, 0);
                payInfAddsRLayout.setVisibility(View.GONE);
            }

        } else if (id == R.id.tv_order_his_o_ship_adds_title) {
            if (shipInfAddsRLayout.getVisibility() == View.GONE) {
                shipInfAdds.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_less_black_24dp, 0);
                shipInfAddsRLayout.setVisibility(View.VISIBLE);
            } else {
                shipInfAdds.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_more_black_24dp, 0);
                shipInfAddsRLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (id == R.id.cart_image_view) {
            Intent intent = new Intent(OrderHistoryOrderInfo.this, Cart.class);
            startActivity(intent);
        }
        return false;
    }

}

