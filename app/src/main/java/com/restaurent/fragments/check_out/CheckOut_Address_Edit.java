package com.restaurent.fragments.check_out;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.restaurent.R;
import com.restaurent.activity.NoInternetConnection;
import com.restaurent.adapter.SpinnerAdapter;
import com.restaurent.constant_class.JSON_Names;
import com.restaurent.constant_class.URL_Class;
import com.restaurent.db_handler.DataBaseHandlerAccount;
import com.restaurent.interfaces.CheckOutAPIRequest;
import com.restaurent.json_mechanism.GetJSONData;
import com.restaurent.network_checker.NetworkConnection;
import com.restaurent.shared_preferenc_estring.DataStorage;
import com.restaurent.utils.AccountDataSet;
import com.restaurent.utils.SpinnerDataSet;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckOut_Address_Edit extends Fragment {
    EditText mChangeAddressFirstName, mChangeAddressLastName, mChangeAddressCompany,
            mChangeAddressAddress, mChangeAddressCity, mChangeAddressPostCode;
    Spinner mChangeAddressCountry, mChangeAddressState;
    Button mChangeAddressBack, mChangeAddressContinue;
    View view;
    HashMap<String, String> edit_detail = new HashMap<>();
    ArrayList<SpinnerDataSet> countryArrayList = new ArrayList<>();
    HashMap<Integer, ArrayList<SpinnerDataSet>> stateArrayList = new HashMap<>();
    int mChangeAddress_Country_ID, mChangeAddress_State_ID;
    AccountDataSet accountDataSet;
    TextView error_edit_pro_first_name, error_edit_pro_last_name,
            error_edit_pro_adds, error_edit_pro_city, error_edit_pro_postcode,
            error_edit_pro_country, error_edit_pro_state;
    String result;
    CheckOutAPIRequest checkOutAPIRequest;

    public CheckOut_Address_Edit() {
    }

    public static CheckOut_Address_Edit getInstance(String data) {
        CheckOut_Address_Edit checkOut_address_edit = new CheckOut_Address_Edit();
        Bundle bundle = new Bundle();
        bundle.putString("Data", data);
        checkOut_address_edit.setArguments(bundle);
        return checkOut_address_edit;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            result = getArguments().getString("Data");
        }
        checkOutAPIRequest= (CheckOutAPIRequest) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.checkout_address_edit, container, false);
        action();
        setting(result);
        return view;
    }

    public void action() {
        accountDataSet = new AccountDataSet();
        mChangeAddressFirstName = (EditText) view.findViewById(R.id.et_edit_pro_first_name);
        mChangeAddressLastName = (EditText) view.findViewById(R.id.et_edit_pro_last_name);
        mChangeAddressCompany = (EditText) view.findViewById(R.id.et_edit_pro_company);
        mChangeAddressAddress = (EditText) view.findViewById(R.id.et_edit_pro_address);
        mChangeAddressCity = (EditText) view.findViewById(R.id.et_edit_pro_city);
        mChangeAddressPostCode = (EditText) view.findViewById(R.id.et_edit_pro_postcode);
        mChangeAddressCountry = (Spinner) view.findViewById(R.id.spinner_edit_pro_country);
        mChangeAddressState = (Spinner) view.findViewById(R.id.spinner_edit_pro_region_or_state);
        mChangeAddressBack = (Button) view.findViewById(R.id.btn_edit_pro_back);
        mChangeAddressContinue = (Button) view.findViewById(R.id.btn_edit_pro_continue);

        error_edit_pro_first_name = (TextView) view.findViewById(R.id.tv_error_edit_pro_first_name);
        error_edit_pro_last_name = (TextView) view.findViewById(R.id.tv_error_edit_pro_last_name);
        error_edit_pro_adds = (TextView) view.findViewById(R.id.tv_error_edit_pro_address);
        error_edit_pro_city = (TextView) view.findViewById(R.id.tv_error_edit_pro_city);
        error_edit_pro_postcode = (TextView) view.findViewById(R.id.tv_error_edit_pro_postcode);
        error_edit_pro_country = (TextView) view.findViewById(R.id.tv_error_edit_pro_country);
        error_edit_pro_state = (TextView) view.findViewById(R.id.tv_error_edit_pro_region_state);

    }

    @SuppressWarnings("unchecked")
    public void setting(String country_list) {

        final HashMap<String, Object> country_data_set = GetJSONData.getCountryList(country_list);
        if (country_data_set != null) {
            stateArrayList = (HashMap<Integer, ArrayList<SpinnerDataSet>>) country_data_set.get(JSON_Names.KEY_STATE);
            countryArrayList = (ArrayList<SpinnerDataSet>) country_data_set.get(JSON_Names.KEY_COUNTRY);
            SpinnerAdapter adapter_country = new SpinnerAdapter(getActivity(),
                    android.R.layout.simple_spinner_item, countryArrayList);
            adapter_country.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mChangeAddressCountry.setAdapter(adapter_country);
            mChangeAddressCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mChangeAddress_Country_ID = countryArrayList.get(position).get_id();
                    if (mChangeAddress_Country_ID != -1) {
                        if (stateArrayList.containsKey(mChangeAddress_Country_ID) &&
                                stateArrayList.get(mChangeAddress_Country_ID) != null) {
                            SpinnerAdapter adapter_state = new SpinnerAdapter(getActivity(),
                                    android.R.layout.simple_spinner_item, stateArrayList.get(mChangeAddress_Country_ID));
                            adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            mChangeAddressState.setAdapter(adapter_state);
                            mChangeAddressState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    mChangeAddress_State_ID = stateArrayList.get(mChangeAddress_Country_ID).get(position).get_id();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        } else {
                            List<String> noStates = new ArrayList<>();
                            noStates.add("--- None ---");
                            ArrayAdapter<String> adapter_State = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_nodata, R.id.spinner_state_no_data, noStates);
                            mChangeAddressState.setAdapter(adapter_State);
                            mChangeAddressState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    mChangeAddress_State_ID = 0;
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            mChangeAddressBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
            mChangeAddressContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mChangeAddressFirstName.getText().toString().length() != 0) {
                        if (mChangeAddressFirstName.getText().toString().length() > 0 && mChangeAddressFirstName.getText().toString().length() <= 32) {
                            if (mChangeAddressLastName.getText().toString().length() != 0) {
                                if (mChangeAddressLastName.getText().toString().length() > 0 && mChangeAddressLastName.getText().toString().length() <= 32) {
                                    if (mChangeAddressAddress.getText().toString().length() != 0) {
                                        if (mChangeAddressAddress.getText().toString().length() > 3 && mChangeAddressAddress.getText().toString().length() <= 128) {
                                            if (mChangeAddressCity.getText().toString().length() != 0) {
                                                if (mChangeAddress_Country_ID != -1) {
                                                    if (mChangeAddress_State_ID != -1) {
                                                        String c_id = String.valueOf(DataBaseHandlerAccount.getInstance(getActivity().getApplicationContext()).get_customer_id());
                                                        String a_id = DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(), JSON_Names.KEY_DEFAULT_ADDRESS_ID);
                                                        if (a_id != null) {
                                                            edit_detail.put(JSON_Names.KEY_ADDRESS_ID, a_id);
                                                        } else {
                                                            edit_detail.put(JSON_Names.KEY_ADDRESS_ID, "0");
                                                        }
                                                        edit_detail.put(JSON_Names.KEY_CUSTOMER_ID, c_id);
                                                        edit_detail.put(JSON_Names.KEY_FIRST_NAME, mChangeAddressFirstName.getText().toString());
                                                        edit_detail.put(JSON_Names.KEY_LAST_NAME, mChangeAddressLastName.getText().toString());
                                                        edit_detail.put(JSON_Names.KEY_COMPANY, mChangeAddressCompany.getText().toString());
                                                        edit_detail.put(JSON_Names.KEY_ADDRESS_1, mChangeAddressAddress.getText().toString());
                                                        edit_detail.put(JSON_Names.KEY_CITY, mChangeAddressCity.getText().toString());
                                                        edit_detail.put(JSON_Names.KEY_POSTCODE, mChangeAddressPostCode.getText().toString());
                                                        edit_detail.put(JSON_Names.KEY_COUNTRY, String.valueOf(mChangeAddress_Country_ID));
                                                        edit_detail.put(JSON_Names.KEY_STATE, String.valueOf(mChangeAddress_State_ID));
                                                        if (NetworkConnection.connectionChecking(getActivity().getApplicationContext())) {
                                                            accountDataSet.setmFirstName(mChangeAddressFirstName.getText().toString());
                                                            accountDataSet.setmLastName(mChangeAddressLastName.getText().toString());

                                                            if(writeOut()!=null) {
                                                                checkOutAPIRequest.checkout_edit_address_post(writeOut());
                                                            }

                                                        } else {
                                                            Intent intent = new Intent(getActivity(), NoInternetConnection.class);
                                                            startActivity(intent);
                                                            getActivity().finish();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (mChangeAddressFirstName.getText().toString().equals("")) {
                        error_edit_pro_first_name.setVisibility(View.VISIBLE);
                    } else {
                        error_edit_pro_first_name.setVisibility(View.GONE);
                    }
                    if ((mChangeAddressFirstName.getText().toString().length() >= 1) &&
                            (mChangeAddressFirstName.getText().toString().length() <= 32)) {
                        error_edit_pro_first_name.setVisibility(View.GONE);
                    } else {
                        error_edit_pro_first_name.setVisibility(View.VISIBLE);
                    }
                    if (mChangeAddressLastName.getText().toString().equals("")) {
                        error_edit_pro_last_name.setVisibility(View.VISIBLE);
                    } else {
                        error_edit_pro_last_name.setVisibility(View.GONE);
                    }
                    if ((mChangeAddressLastName.getText().toString().length() >= 1) &&
                            (mChangeAddressLastName.getText().toString().length() <= 32)) {
                        error_edit_pro_last_name.setVisibility(View.GONE);
                    } else {
                        error_edit_pro_last_name.setVisibility(View.VISIBLE);
                    }
                    if (mChangeAddressAddress.getText().toString().equals("")) {
                        error_edit_pro_adds.setVisibility(View.VISIBLE);
                    } else {
                        error_edit_pro_adds.setVisibility(View.GONE);
                    }
                    if ((mChangeAddressAddress.getText().toString().length() >= 3) &&
                            (mChangeAddressAddress.getText().toString().length() <= 128)) {
                        error_edit_pro_adds.setVisibility(View.GONE);
                    } else {
                        error_edit_pro_adds.setVisibility(View.VISIBLE);
                    }
                    if (mChangeAddressCity.getText().toString().equals("")) {
                        error_edit_pro_city.setVisibility(View.VISIBLE);
                    } else {
                        error_edit_pro_city.setVisibility(View.GONE);
                    }
                    if ((mChangeAddressCity.getText().toString().length() >= 2) &&
                            (mChangeAddressCity.getText().toString().length() <= 128)) {
                        error_edit_pro_city.setVisibility(View.GONE);
                    } else {
                        error_edit_pro_city.setVisibility(View.VISIBLE);
                    }
                    if (mChangeAddress_Country_ID == -1) {
                        error_edit_pro_country.setVisibility(View.VISIBLE);
                    } else {
                        error_edit_pro_country.setVisibility(View.GONE);
                    }

                    if (mChangeAddress_State_ID == -1) {
                        error_edit_pro_state.setVisibility(View.VISIBLE);
                    } else {
                        error_edit_pro_state.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    public void continue_to_delivery_method() {
        DataBaseHandlerAccount.getInstance(getActivity().getApplicationContext()).
                update_account_detail_name(accountDataSet);
    }

    public String writeOut()  {
        try {
            StringBuilder s = new StringBuilder();
            Boolean first = true;
            for (Map.Entry<String, String> entry : edit_detail.entrySet()) {
                if (first) {
                    first = false;
                } else {
                    s.append(URL_Class.mAnd_Symbol);
                }
                s.append(URLEncoder.encode(entry.getKey(), URL_Class.mConvertType));
                s.append(URL_Class.mEqual_Symbol);
                s.append(URLEncoder.encode(entry.getValue(), URL_Class.mConvertType));
            }

            return s.toString();
        }catch (Exception e){
            return null;
        }
    }
}
