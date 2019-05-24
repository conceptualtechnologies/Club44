package com.restaurent.activity.user;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.restaurent.R;
import com.restaurent.adapter.AllUserproduct_adapter;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.shared_preferenc_estring.DataStorage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Special_product extends AppCompatActivity {
    RecyclerView mRecyclerView1;
    View v;
    Context mContext;
    private List<Allproducts_list> listItemsList = new ArrayList<>();
    AllUserproduct_adapter address_adapter1;
    RequestQueue queue1;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mRecyclerView1 = (RecyclerView) findViewById(R.id.product_recyclerview);
        //   mRecyclerView1.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).color(Color.BLUE).build());
        mRecyclerView1.setLayoutManager(new GridLayoutManager(mContext, 2));
        queue1 = Volley.newRequestQueue(getApplicationContext());
        Load_special_product();
    }

    private void Load_special_product() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://club44.in/api/fetch_alluserproducts?language_id=1", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", response.toString());

                try {
                    JSONArray sportarraydata2 = response.getJSONArray("data");
                    if (address_adapter1 != null) {
                        address_adapter1.clearAdapter();

                        for (int i = 0; i < sportarraydata2.length(); i++) {
                            JSONObject sportarraydata = sportarraydata2.getJSONObject(i);
                            Allproducts_list advisor_customer_group_list = new Allproducts_list();
                            advisor_customer_group_list.setProduct_id(sportarraydata.getString("product_id"));
                            advisor_customer_group_list.setShop_id(sportarraydata.getString("shop_id"));
                            advisor_customer_group_list.setModel(sportarraydata.getString("model"));
                            advisor_customer_group_list.setOffer(sportarraydata.getString("offer"));
                            advisor_customer_group_list.setOffer_price(sportarraydata.getString("offer_price"));
                            advisor_customer_group_list.setCategory_id(sportarraydata.getString("category_id"));
                            advisor_customer_group_list.setPrice(sportarraydata.getString("price"));
                            advisor_customer_group_list.setImage(sportarraydata.getString("image"));
                            advisor_customer_group_list.setDescription(sportarraydata.getString("description"));
                            advisor_customer_group_list.setMeta_title(sportarraydata.getString("meta_title"));

                            // advisor_customer_group_list.setImage(sportarraydata.getString("image"));

                            listItemsList.add(advisor_customer_group_list);
                            address_adapter1 = new AllUserproduct_adapter(getApplicationContext(), listItemsList);
                            mRecyclerView1.setAdapter(address_adapter1);

                        }
                    } else {
                        for (int i = 0; i < sportarraydata2.length(); i++) {
                            JSONObject sportarraydata = sportarraydata2.getJSONObject(i);
                            Allproducts_list advisor_customer_group_list = new Allproducts_list();
                            advisor_customer_group_list.setProduct_id(sportarraydata.getString("product_id"));
                            advisor_customer_group_list.setShop_id(sportarraydata.getString("shop_id"));
                            advisor_customer_group_list.setModel(sportarraydata.getString("model"));
                            advisor_customer_group_list.setPrice(sportarraydata.getString("price"));
                            advisor_customer_group_list.setOffer(sportarraydata.getString("offer"));
                            advisor_customer_group_list.setOffer_price(sportarraydata.getString("offer_price"));
                            advisor_customer_group_list.setCategory_id(sportarraydata.getString("category_id"));
                            advisor_customer_group_list.setImage(sportarraydata.getString("image"));
                            advisor_customer_group_list.setDescription(sportarraydata.getString("description"));
                            advisor_customer_group_list.setMeta_title(sportarraydata.getString("meta_title"));

                            // advisor_customer_group_list.setImage(sportarraydata.getString("image"));

                            listItemsList.add(advisor_customer_group_list);
                            address_adapter1 = new AllUserproduct_adapter(getApplicationContext(), listItemsList);
                            mRecyclerView1.setAdapter(address_adapter1);

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // adapter.notifyDataSetChanged();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue1.add(jsonObjectRequest);
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
        if (id == android.R.id.home) {
            DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CART_DATA);
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}