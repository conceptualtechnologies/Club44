package com.restaurent.fragments.check_out;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.restaurent.PayuActivity;
import com.restaurent.R;
import com.restaurent.db_handler.DataBaseHandlerConfirmOrder;
import com.restaurent.interfaces.CheckOutAPIRequest;
import com.restaurent.json_mechanism.GetJSONData;
import com.restaurent.mechanism.Methods;
import com.restaurent.utils.ShippingAndPayment_DataSet;

import java.util.ArrayList;

public class CheckOut_Payment_Type extends Fragment {
    View view;
    private RadioGroup mPaymentGroup;
    private Button mPaymentContinue;
    FragmentManager fragmentManager;
    ArrayList<ShippingAndPayment_DataSet> PaymentList = new ArrayList<>();
    ShippingAndPayment_DataSet dataSet = new ShippingAndPayment_DataSet();
    RadioGroup.LayoutParams layoutParams;
    ImageButton mCart, mDeliveryDetail, mDeliveryType;
    CheckOutAPIRequest checkOutAPIRequest;
    String result;
    private RadioButton radioButton;
int id;
    private String s;


    public CheckOut_Payment_Type() {
    }

    public static CheckOut_Payment_Type getInstance(String data) {
        CheckOut_Payment_Type checkOut_payment_type = new CheckOut_Payment_Type();
        Bundle bundle = new Bundle();
        bundle.putString("Data", data);
        checkOut_payment_type.setArguments(bundle);
        return checkOut_payment_type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkOutAPIRequest = (CheckOutAPIRequest) getActivity();
        if (getArguments() != null) {
            result = getArguments().getString("Data");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.checkout_payment_type, container, false);
        action(result);
        return view;
    }

    public void action(String data) {
        final int check_list[];
        PaymentList = GetJSONData.getPaymentMethod(data);
        mPaymentGroup = (RadioGroup) view.findViewById(R.id.payment_type_chooser);
        mPaymentContinue = (Button) view.findViewById(R.id.payment_type_continue);
        mCart = (ImageButton) view.findViewById(R.id.check_out_payment_type_cart);
        mDeliveryDetail = (ImageButton) view.findViewById(R.id.check_out_payment_type_delivery_detail);
        mDeliveryType = (ImageButton) view.findViewById(R.id.check_out_payment_type_delivery_type);
        //cod = (RadioButton) view.findViewById(R.id.cod);
        //payu = (RadioButton) view.findViewById(R.id.payu);


//        mPaymentGroup.check(cod.getId());


        mDeliveryDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack("Second", 1);
            }
        });

        mDeliveryType.setOnClickListener(new View.OnClickListener() {
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


        /*mPaymentContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = mPaymentGroup.getCheckedRadioButtonId();
                // find which radioButton is checked by id
                if (selectedId == cod.getId()) {

                    checkOutAPIRequest.checkout_confirmation();


                } else {

                    Intent in = new Intent(getActivity().getApplicationContext(), PayuActivity.class);
                    startActivity(in);

                }

            }

        });*/

        if (PaymentList != null) {
            check_list = new int[PaymentList.size()];
            for (int i = 0; i < PaymentList.size(); i++) {

                    radioButton = new RadioButton(getActivity());
                    int id = i + 10;
                    check_list[i] = id;
                    radioButton.setId(id);
                    radioButton.setText(PaymentList.get(i).getmTitle());
                    radioButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color));
                    layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    mPaymentGroup.addView(radioButton, layoutParams);

            }


            mPaymentContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < PaymentList.size(); i++)

                     id = mPaymentGroup.getCheckedRadioButtonId();

                    int count = 0;
                    for (int t = 0; t < check_list.length; t++) {
                        if (id == check_list[t]) {
                            if (DataBaseHandlerConfirmOrder.getInstance(getActivity().getApplicationContext()).get_Size_payment() == 0) {
                                dataSet.setmTitle(PaymentList.get(t).getmTitle());
                                dataSet.setmCode(PaymentList.get(t).getmCode());
                                dataSet.setmTerms(PaymentList.get(t).getmTerms());
                                dataSet.setmSortOrder(PaymentList.get(t).getmSortOrder());
                                DataBaseHandlerConfirmOrder.getInstance(getActivity().getApplicationContext()).insert_payment_type(dataSet);
                            } else {
                                dataSet.setmTitle(PaymentList.get(t).getmTitle());
                                dataSet.setmCode(PaymentList.get(t).getmCode());
                                dataSet.setmTerms(PaymentList.get(t).getmTerms());
                                dataSet.setmSortOrder(PaymentList.get(t).getmSortOrder());
                                DataBaseHandlerConfirmOrder.getInstance(getActivity().getApplicationContext()).update_payment_type(dataSet);
                            }
                            count++;
                        }
                    }
                    if (count == 0) {
                        Methods.toast("Please select any one type");//Do it in arabic

                    } else  {

                        s = dataSet.getmTitle().toString().trim();
Log.e("Payment Type",s);
                       // Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();
                           if (s.equals("Payu")){

                            Intent in = new Intent(getActivity().getApplicationContext(), PayuActivity.class);
                            startActivity(in);
                        }

                        else if (s.equals("Cod")){

                            checkOutAPIRequest.checkout_confirmation();
                        }
                        else{
                               checkOutAPIRequest.checkout_confirmation();
                               }


                        //dataSet.getmTitle().toString() == "Payu"

                    }
                    /*else {

                    }*/

               }
            });


        }
    }
}
