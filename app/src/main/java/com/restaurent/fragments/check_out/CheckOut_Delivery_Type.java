package com.restaurent.fragments.check_out;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.restaurent.R;
import com.restaurent.db_handler.DataBaseHandlerConfirmOrder;
import com.restaurent.interfaces.CheckOutAPIRequest;
import com.restaurent.json_mechanism.GetJSONData;
import com.restaurent.mechanism.Methods;
import com.restaurent.shared_preferenc_estring.DataStorage;
import com.restaurent.utils.ShippingAndPayment_DataSet;

import java.util.ArrayList;

public class CheckOut_Delivery_Type extends Fragment {
    View view;
    RadioGroup mShippingGroup;
    RadioGroup.LayoutParams layoutParams;
    Button mShippingContinue;
    ArrayList<ShippingAndPayment_DataSet> ShippingList=new ArrayList<>();
    ShippingAndPayment_DataSet dataSet=new ShippingAndPayment_DataSet();
    ImageButton mCart,mDeliveryDetail;
    CheckOutAPIRequest checkOutAPIRequest;
    String result;
    SharedPreferences.Editor editor;
    int id;
    SharedPreferences myPrefs;
    public static final String MY_PREFS_NAME2 = "MyPrefsFile2";

    public CheckOut_Delivery_Type(){

    }

    public static CheckOut_Delivery_Type getInstance(String data){
        CheckOut_Delivery_Type checkOut_delivery_type=new CheckOut_Delivery_Type();
        Bundle bundle=new Bundle();
        bundle.putString("Data",data);
        checkOut_delivery_type.setArguments(bundle);
        return checkOut_delivery_type;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkOutAPIRequest= (CheckOutAPIRequest) getActivity();
        if(getArguments()!=null){
            result=getArguments().getString("Data");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.checkout_delivery_type, container, false);
        action(result);
        return view;
    }

    public void action(String data){
        final int check_list[];
        mShippingGroup= (RadioGroup) view.findViewById(R.id.shipping_type_chooser);
        mShippingContinue= (Button) view.findViewById(R.id.shipping_type_continue);
        mCart= (ImageButton) view.findViewById(R.id.check_out_delivery_type_cart);
        myPrefs = getActivity().getSharedPreferences(MY_PREFS_NAME2, Context.MODE_PRIVATE);
        editor = myPrefs.edit();
        mDeliveryDetail= (ImageButton) view.findViewById(R.id.check_out_delivery_type_delivery_detail);
        ShippingList= GetJSONData.getShippingMethod(data);
        if(ShippingList!=null) {
            check_list = new int[ShippingList.size()];
            for (int i = 0; i < ShippingList.size(); i++) {
                RadioButton radioButton = new RadioButton(getActivity());
                int id = i + 10;
                check_list[i] = id;
                radioButton.setId(id);
                radioButton.setText(ShippingList.get(i).getmTitle());
                radioButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color));
                layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mShippingGroup.addView(radioButton, layoutParams);
            }
            mDeliveryDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
            mCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });


            mShippingContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < ShippingList.size(); i++) {
                        id = mShippingGroup.getCheckedRadioButtonId();
                        if (id == 10) {



                        } else if(id == 20) {



                        }
                        else{
                            Methods.toast("Please select any one type");
                        }


                    }
                    // String data2= String.valueOf(ShippingList.get(0).getmCost());


                    int count=0;
                    for (int t=0;t<check_list.length;t++) {
                        if (id == check_list[t]) {



                            if(DataBaseHandlerConfirmOrder.getInstance(getActivity().getApplicationContext()).get_Size_shipping()==0) {
                                dataSet.setmTitle(ShippingList.get(t).getmTitle());
                                dataSet.setmCode(ShippingList.get(t).getmCode());
                                dataSet.setmCost(ShippingList.get(t).getmCost());
                                dataSet.setmTaxClassId(ShippingList.get(t).getmTaxClassId());
                                dataSet.setmSortOrder(ShippingList.get(t).getmSortOrder());
                                DataBaseHandlerConfirmOrder.getInstance(getActivity().getApplicationContext()).insert_shipping_type(dataSet);
                                DataStorage.mStoreSharedPreferenceString(getActivity().getApplicationContext(),"data", String.valueOf(ShippingList.get(t).getmCost()));

                            }else {
                                dataSet.setmTitle(ShippingList.get(t).getmTitle());
                                dataSet.setmCode(ShippingList.get(t).getmCode());
                                dataSet.setmCost(ShippingList.get(t).getmCost());
                                dataSet.setmTaxClassId(ShippingList.get(t).getmTaxClassId());
                                dataSet.setmSortOrder(ShippingList.get(t).getmSortOrder());
                                DataStorage.mStoreSharedPreferenceString(getActivity().getApplicationContext(),"data", String.valueOf(ShippingList.get(t).getmCost()));
                                DataBaseHandlerConfirmOrder.getInstance(getActivity().getApplicationContext()).update_shipping_type(dataSet);
                            }
                            count++;
                        }
                    }
                    if(count==0) {
                        //Do it in arabic
                    }else {
                        checkOutAPIRequest.checkout_payment_type_request();
                    }


                }
            });
        }
        }}
