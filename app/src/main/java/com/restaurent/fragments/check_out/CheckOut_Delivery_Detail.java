package com.restaurent.fragments.check_out;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.restaurent.R;
import com.restaurent.activity.user.Login;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.db_handler.DataBaseHandlerAccount;
import com.restaurent.db_handler.DataBaseHandlerAccountAddress;
import com.restaurent.interfaces.API_Result;
import com.restaurent.interfaces.CheckOutAPIRequest;
import com.restaurent.json_mechanism.GetJSONData;
import com.restaurent.shared_preferenc_estring.DataStorage;
import com.restaurent.utils.AccountDataSet;

import java.util.ArrayList;


public class CheckOut_Delivery_Detail extends Fragment {
    View view;
    Button mDeliveryDetailContinue;
    TextView mShippingFirstName, mShippingLastName, mShippingPhoneNumber, mShippingCompany, mShippingStreetAddress, mShippingCity, mShippingZipCode, mShippingCountry, mShippingState;
    ImageButton mChangeAddress;
    ArrayList<AccountDataSet> accountDataSetAddressList = new ArrayList<>();
    AccountDataSet accountDataSetAddress;
    AccountDataSet mShippingDataSet;
    AccountDataSet mPaymentDataSet;
    ImageButton mCart;
    API_Result api_result;
    CheckOutAPIRequest checkOutAPIRequest;
    String result;
    public CheckOut_Delivery_Detail(){}
    public static final String MY_PREFS_NAME = "MyPrefs";

    String f_name,e_mail,p_number;

    public static CheckOut_Delivery_Detail getInstance(String data) {
        CheckOut_Delivery_Detail checkOut_delivery_detail=new CheckOut_Delivery_Detail();
        Bundle bundle=new Bundle();
        bundle.putString("Data",data);
        checkOut_delivery_detail.setArguments(bundle);
        return checkOut_delivery_detail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api_result = (API_Result) getActivity();
        checkOutAPIRequest= (CheckOutAPIRequest) getActivity();
        if(getArguments()!=null){
            Bundle bundle=getArguments();
            if(bundle.getString("Data")!=null){
                result=bundle.getString("Data");
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.checkout_delivery_detail, container, false);
        mShippingDataSet = new AccountDataSet();
        action();
        accountDataSetAddress = new AccountDataSet();

         mPaymentDataSet = new AccountDataSet();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void action() {
        if (DataBaseHandlerAccount.getInstance(getActivity().getApplicationContext()).check_login()) {
            mShippingFirstName = (TextView) view.findViewById(R.id.shipment_first_name);
            mShippingLastName = (TextView) view.findViewById(R.id.shipment_last_name);
            mShippingPhoneNumber = (TextView) view.findViewById(R.id.shipment_phone_number);
            mShippingCompany = (TextView) view.findViewById(R.id.shipment_company);
            mShippingStreetAddress = (TextView) view.findViewById(R.id.shipment_address);
            mShippingCity = (TextView) view.findViewById(R.id.shipment_city);
            mShippingZipCode = (TextView) view.findViewById(R.id.shipment_zip_code);
            mShippingState = (TextView) view.findViewById(R.id.shipment_state);
            mShippingCountry = (TextView) view.findViewById(R.id.shipment_country);
            mChangeAddress = (ImageButton) view.findViewById(R.id.confirmation_shipping_address_change);
            mCart= (ImageButton) view.findViewById(R.id.check_out_delivery_detail_cart);

            mDeliveryDetailContinue = (Button) view.findViewById(R.id.delivery_continue_btn);

            data_setting(result);

        } else {
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        }

    }

    public void continue_to_delivery_method() {
        checkOutAPIRequest.checkout_delivery_detail_request();
    }

    public AccountDataSet getDefaultAddress(ArrayList<AccountDataSet> list) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getmAddress_Id().equals(list.get(i).getmDefaultAddressId())) {
                    DataStorage.mStoreSharedPreferenceString(getActivity().getApplicationContext(),JSON_Names.KEY_DEFAULT_ADDRESS_ID,list.get(i).getmDefaultAddressId());
                    return list.get(i);
                }
            }
            return null;
        } else {
            return null;
        }
    }

    public void data_setting(String profile_details) {
        accountDataSetAddressList = GetJSONData.getCustomerAddress(profile_details);
        accountDataSetAddress = getDefaultAddress(accountDataSetAddressList);
//        Log.d("Info",accountDataSetAddress.toString());

            mShippingFirstName.setText( DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(),"firstname"));
            mShippingLastName.setText(DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(),"lastname"));
            mShippingPhoneNumber.setText(DataBaseHandlerAccount.getInstance(getActivity().getApplicationContext()).get_customer_mobile_no());
            mShippingCompany.setText(DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(),"company"));

            f_name= DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(),"firstname");
            e_mail = DataBaseHandlerAccount.getInstance(getActivity().getApplicationContext()).get_customer_email_id();
            p_number = DataBaseHandlerAccount.getInstance(getActivity().getApplicationContext()).get_customer_mobile_no();

            /*SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor edt = prefs.edit();
            edt.putString("first_name",f_name);
            edt.putString("email_id",e_mail);
            edt.putString("phone_number",p_number);
            edt.commit();*/


            if (DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(),"address1") != null) {
                String address = DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(),"address1")+ "";
                mShippingStreetAddress.setText(address);
            } else {
                mShippingStreetAddress.setText(DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(),"address1"));
            }
            mShippingCity.setText(DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(),"city"));
            mShippingZipCode.setText(DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(),"postcode"));
            mShippingCountry.setText(DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(),"country"));
            if (DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(),"address1") != null) {
                mShippingState.setText(DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(),"state"));
            } else {
                String dummy_state = "None";
                mShippingState.setText(dummy_state);
            }


        mChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOutAPIRequest.checkout_edit_address();
            }
        });

        mCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mDeliveryDetailContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mShippingDataSet.setmFirstName(mShippingFirstName.getText().toString());
                mShippingDataSet.setmLastName(mShippingLastName.getText().toString());
                mShippingDataSet.setmTelePhone(mShippingPhoneNumber.getText().toString());
                mShippingDataSet.setmCompany(mShippingCompany.getText().toString());
                mShippingDataSet.setmAddress_1(mShippingStreetAddress.getText().toString());
                mShippingDataSet.setmCity(mShippingCity.getText().toString());
                mShippingDataSet.setmPostcode(mShippingZipCode.getText().toString());
                if (DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(),"state") != null) {
                    mShippingDataSet.setmState(DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(),"state"));
                    mShippingDataSet.setmState_Id(accountDataSetAddress.getmZone_id());
                } else {
                    mShippingDataSet.setmState("0");
                    mShippingDataSet.setmState_Id("0");
                }
                mShippingDataSet.setmCountry(DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(),"country"));
                mShippingDataSet.setmEmailId(DataBaseHandlerAccount.getInstance(getActivity().getApplicationContext()).get_customer_email_id());
                mShippingDataSet.setmCountry_id(DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(),"countryid"));
                mShippingDataSet.setmAddress_2("0");
                mPaymentDataSet.setmFirstName(mShippingFirstName.getText().toString());
                mPaymentDataSet.setmLastName(mShippingLastName.getText().toString());
                mPaymentDataSet.setmTelePhone(mShippingPhoneNumber.getText().toString());
                mPaymentDataSet.setmCompany(mShippingCompany.getText().toString());
                mPaymentDataSet.setmAddress_1(mShippingStreetAddress.getText().toString());
                mPaymentDataSet.setmCity(mShippingCity.getText().toString());
                mPaymentDataSet.setmPostcode(mShippingZipCode.getText().toString());
                if (DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(),"state")!= null) {
                    mPaymentDataSet.setmState(DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(),"state"));
                    mPaymentDataSet.setmState_Id(DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(),"zoneid"));
                } else {
                    mPaymentDataSet.setmState("0");
                    mPaymentDataSet.setmState_Id("0");
                }
                mPaymentDataSet.setmCountry(DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(),"country"));
                mPaymentDataSet.setmEmailId(DataBaseHandlerAccount.getInstance(getActivity().getApplicationContext()).get_customer_email_id());
                mPaymentDataSet.setmCountry_id(DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(),"countryid"));
                mPaymentDataSet.setmAddress_2("0");

                if (DataBaseHandlerAccountAddress.getInstance(getActivity().getApplicationContext()).get_Size_Address() == 0) {
                    DataBaseHandlerAccountAddress.getInstance(getActivity().getApplicationContext()).insert_account_shipping_address(mShippingDataSet);
                    DataBaseHandlerAccountAddress.getInstance(getActivity().getApplicationContext()).insert_account_payment_address(mPaymentDataSet);
                } else {
                    DataBaseHandlerAccountAddress.getInstance(getActivity().getApplicationContext()).update_payment_address(mPaymentDataSet);
                    DataBaseHandlerAccountAddress.getInstance(getActivity().getApplicationContext()).update_shipping_address(mShippingDataSet);
                }
                continue_to_delivery_method();

            }
        });

    }
}