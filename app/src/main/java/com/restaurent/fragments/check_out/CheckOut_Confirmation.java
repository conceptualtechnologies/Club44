package com.restaurent.fragments.check_out;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.restaurent.R;
import com.restaurent.activity.NoInternetConnection;
import com.restaurent.activity.user.Phone_Number;
import com.restaurent.adapter.CheckOut_Adapter;
import com.restaurent.adapter.CheckOut_Confirmation_Shipping_List;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.db_handler.DataBaseHandlerAccount;
import com.restaurent.db_handler.DataBaseHandlerAccountAddress;
import com.restaurent.interfaces.API_Result;
import com.restaurent.interfaces.CheckOutAPIRequest;
import com.restaurent.interfaces.CheckOutTransaction;
import com.restaurent.json_mechanism.GetJSONData;
import com.restaurent.network_checker.NetworkConnection;
import com.restaurent.shared_preferenc_estring.DataStorage;
import com.restaurent.utils.AccountDataSet;
import com.restaurent.utils.ConfirmResponseDataSet;
import com.restaurent.utils.ProductDataSet;

import java.util.ArrayList;
import java.util.HashMap;

public class CheckOut_Confirmation extends Fragment {
    View view;
    EditText delivery_address;
   public static Button mChangeOrder, mPlaceOrder,mconfirm_phone_no;
    String order_id = JSON_Names.KEY_NO_DATA;
    RecyclerView mPurchasedList,mPurchasedShippingList;
    TextView mShippingAddress;
    ArrayList<ConfirmResponseDataSet> totals_list = new ArrayList<>();
    ConfirmResponseDataSet confirmResponseDataSet;
    ImageButton mCart, mDeliveryDetail, mDeliveryType, mPaymentType;
    API_Result api_result;
    CheckOutTransaction checkOutTransaction;
    CheckOutAPIRequest checkOutAPIRequest;
    String result;
TextView delivery_error;
    public CheckOut_Confirmation(){}

    public static CheckOut_Confirmation getInstance(String data){
        CheckOut_Confirmation checkOutConfirmation=new CheckOut_Confirmation();
        Bundle bundle=new Bundle();
        bundle.putString("Data",data);
        checkOutConfirmation.setArguments(bundle);
        return checkOutConfirmation;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api_result= (API_Result) getActivity();
        checkOutTransaction= (CheckOutTransaction) getActivity();
        checkOutAPIRequest= (CheckOutAPIRequest) getActivity();
        if(getArguments()!=null){
            result=getArguments().getString("Data");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.checkout_confirmation_place_order, container, false);
        action(result);
        return view;
    }

    @SuppressWarnings("unchecked")
    public void action(final String data) {

        mShippingAddress = (TextView) view.findViewById(R.id.confirmation_shipping_address);
        mPurchasedList = (RecyclerView) view.findViewById(R.id.confirmation_shipping_product_list);
        mChangeOrder = (Button) view.findViewById(R.id.confirmation_shipping_product_modification);
        mPlaceOrder = (Button) view.findViewById(R.id.confirmation_shipping_place_order);
        mconfirm_phone_no = (Button) view.findViewById(R.id.confirmation_phone_no);
        delivery_address=(EditText)view.findViewById(R.id.enter_confirmation_shipping_address);
        delivery_error=(TextView) view.findViewById(R.id.address_error);
        delivery_error.setVisibility(View.GONE);
        delivery_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
if(count==0){
    delivery_error.setVisibility(View.VISIBLE);
}
else{
    delivery_error.setVisibility(View.GONE);
}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        DataStorage.mStoreSharedPreferenceString(getActivity(),"address1",delivery_address.getText().toString());
        mPlaceOrder.setVisibility(View.GONE);
     //   mCart = (ImageButton) view.findViewById(R.id.check_out_confirm_order_cart);
      //  mDeliveryDetail = (ImageButton) view.findViewById(R.id.check_out_confirm_order_delivery_detail);
       // mDeliveryType = (ImageButton) view.findViewById(R.id.check_out_confirm_order_delivery_type);
     //   mPaymentType = (ImageButton) view.findViewById(R.id.check_out_confirm_order_payment_type);
        mPurchasedShippingList= (RecyclerView) view.findViewById(R.id.confirmation_shipping_purchase_list);
        HashMap<String, Object> order_whole_list = GetJSONData.get_confirmation_detail(data);
        if (order_whole_list != null) {
            confirmResponseDataSet = (ConfirmResponseDataSet) order_whole_list.get("order_data");
            totals_list = (ArrayList<ConfirmResponseDataSet>) order_whole_list.get("order_data_total");


        if(totals_list!=null) {
            mPurchasedShippingList.setLayoutManager(new LinearLayoutManager(getActivity()));
            mPurchasedShippingList.setAdapter(new CheckOut_Confirmation_Shipping_List(getActivity().getApplicationContext(), totals_list));
            /*int size = (int) getResources().getDisplayMetrics().density;
            if (size == 3) {
                int dp = (int) (getResources().getDimension(R.dimen._40sdp) / size);
                mPurchasedShippingList.getLayoutParams().height = (dp * (totals_list.size() + 4));
            } else if (size == 2) {
                int dp = (int) (getResources().getDimension(R.dimen._30sdp) / size);
                mPurchasedShippingList.getLayoutParams().height = (dp * (totals_list.size() + 2));
            } else {
                int dp = (int) (getResources().getDimension(R.dimen._30sdp) / size);
                mPurchasedShippingList.getLayoutParams().height = (dp * (totals_list.size()));
            }*/
        }

      /*  mCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });*/

/*        mDeliveryDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack("Second", 1);
            }
        });

        mDeliveryType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack("Third",3);
            }
        });

        mPaymentType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });*/

        AccountDataSet dataSet = DataBaseHandlerAccountAddress.getInstance(getActivity().getApplicationContext()).get_account_shipping_address();
        String address = DataBaseHandlerAccount.getInstance(getActivity()).get_customer_name() + " " + DataBaseHandlerAccount.getInstance(getActivity()).get_customer_mobile_no() + " "+DataBaseHandlerAccount.getInstance(getActivity()).get_customer_email_id() ;
        mShippingAddress.setText(address);

        String result = DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(), JSON_Names.KEY_CART_DATA);
        ArrayList<ProductDataSet> mDataSet = GetJSONData.get_cart_detail(result);
        if (mDataSet != null) {
            mPurchasedList.setLayoutManager(new LinearLayoutManager(getActivity()));
            mPurchasedList.setAdapter(new CheckOut_Adapter(getActivity().getApplicationContext(), mDataSet));
           /* int size = (int) getResources().getDisplayMetrics().density;
            if (size == 3) {
                int dp = (int) (getResources().getDimension(R.dimen._120sdp) / size);
                mPurchasedList.getLayoutParams().height = (dp * (mDataSet.size() + 4));
            } else if (size == 2) {
                int dp = (int) (getResources().getDimension(R.dimen._115sdp) / size);
                mPurchasedList.getLayoutParams().height = (dp * (mDataSet.size() + 2));
            } else {
                int dp = (int) (getResources().getDimension(R.dimen._115sdp) / size);
                mPurchasedList.getLayoutParams().height = (dp * (mDataSet.size()));
            }*/
        }

        mChangeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOutTransaction.checkout_cart_changer();
            }
        });
mconfirm_phone_no.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Phone_Number alert = new Phone_Number();
        alert.showDialog(getActivity());
    }
});
        mPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkConnection.connectionChecking(getActivity().getApplicationContext())) {


if(delivery_address.getText().toString().length()==0){
    delivery_error.requestFocus();
    delivery_error.setVisibility(View.VISIBLE);
}
else {

    checkOutAPIRequest.checkout_confirmation();

    order_id = confirmResponseDataSet.getmOrderId();

    checkOutAPIRequest.checkout_place_order(order_id);
}
                } else {
                    Intent intent = new Intent(getActivity(), NoInternetConnection.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
    }

}}
