package com.restaurent.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.restaurent.R;
import com.restaurent.activity.user.Tabl;
import com.restaurent.activity.user.style_adapter;
import com.restaurent.activity.user.style_list;
import com.restaurent.interfaces.API_Result;
import com.restaurent.shared_preferenc_estring.DataStorage;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Book_Table.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Book_Table#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Book_Table extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static String TAG = "Under_process_Data";
    private List<style_list> listItemsList = new ArrayList<style_list>();
    private RecyclerView mRecyclerView;
    style_adapter test_adapter;



    EditText get_dates;
    EditText get_times;
    TextView get_table_number;
    String Date;
    private Boolean upflag = false;
    private Uri selectedImage = null;
    private String[] arraySpinner;
    SharedPreferences pref;
    SharedPreferences.Editor edt;
    GridLayoutManager linearLayoutManager;
    RequestQueue queue;
    Spinner ss, sss;
    String data;
    String to,from;
    View view;


    ProgressDialog progressDialog;
    private static int RESULT_LOAD = 1;
    String img_Decodable_Str;
    Button check, submit;
    ImageView image1;
    public static TextView tvDisplayDate;
    private int myear;
    private int mmonth;
    private int mday;
    DatePickerDialog _date;
    static final int DATE_DIALOG_ID = 999;
    String s;

    API_Result api_result;
    private int daysOfMonth = 31;
    LayoutInflater inflater;
    private DatePickerDialog toDatePickerDialog;
    private NumberPicker monthPicker;
    private NumberPicker yearPicker;
    private NumberPicker dayPicker;

    private Calendar cal = Calendar.getInstance();

    public static final String MONTH_KEY = "monthValue";
    public static final String DAY_KEY = "dayValue";
    public static final String YEAR_KEY = "yearValue";

    int monthVal = -1, dayVal = -1, yearVal = -1;
    private DatePickerDialog.OnDateSetListener listener;
    public static final String Dates = "dates";
    private OnFragmentInteractionListener mListener;

    public Book_Table() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Book_Table.
     */
    // TODO: Rename and change types and number of parameters
    public static Book_Table newInstance(String param1, String param2) {
        Book_Table fragment = new Book_Table();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view= inflater.inflate(R.layout.fragment_book__table, container, false);




            mRecyclerView = (RecyclerView)view. findViewById(R.id.home_list_recycler_view);

            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            GridLayoutManager lLayout = new GridLayoutManager(getActivity(), 2);
            mRecyclerView.setLayoutManager(lLayout);
            queue = Volley.newRequestQueue(getActivity());
            // new Background().execute();
            pref = getActivity().getPreferences(Context.MODE_PRIVATE);
      /*

        edt = pref.edit();*/


            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            // set prompts.xml to be the layout file of the alertdialog builder
            setCurrentDateOnView();
            addListenerOnButton();
            get_table_number = (TextView)view. findViewById(R.id.tv_tablenumber);
            get_dates = (EditText)view. findViewById(R.id.followup_detail_date);
//        get_dates.setText(DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),"date"));



            check=(Button)view.findViewById(R.id.btn_check);

            this.arraySpinner = new String[]{
                    "11:00 PM", "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM",
            };
            ss = (Spinner)view. findViewById(R.id.totime);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, arraySpinner);
            ss.setAdapter(adapter);
            ss.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    to = ss.getSelectedItem().toString();
                    DataStorage.mStoreSharedPreferenceString(getActivity(),"to",to);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            this.arraySpinner = new String[]{
                    "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM",
            };
            sss = (Spinner)view. findViewById(R.id.fromtime);
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, arraySpinner);
            sss.setAdapter(adapter1);
            sss.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    from = sss.getSelectedItem().toString();
                    DataStorage.mStoreSharedPreferenceString(getActivity(),"from",from);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {


                }
            });
            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Check_tables();
                }
            });
return view;
        }

    public void setCurrentDateOnView() {
        tvDisplayDate = (TextView) view.findViewById(R.id.tvDate);
        final Calendar c = Calendar.getInstance();
        myear = c.get(Calendar.YEAR);
        mmonth = c.get(Calendar.MONTH);
        mday = c.get(Calendar.DAY_OF_MONTH);
        String day;

        String mon;


        // set current date into textview
        tvDisplayDate.setText(new StringBuilder()
                .append(myear).append("-").append(mmonth + 1).append("-")
                .append(mday).append(" "));
        Date = tvDisplayDate.getText().toString();
        DataStorage.mStoreSharedPreferenceString(getActivity(),"date",Date);
    }



    private void addListenerOnButton() {

        tvDisplayDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onCreateDialog(DATE_DIALOG_ID);

            }

        });

    }



    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                _date = new DatePickerDialog(getActivity(), datePickerListener, myear, mmonth,
                        mday) {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar maxDate = Calendar.getInstance();
                        _date.getDatePicker().setMinDate(maxDate.getTimeInMillis());
                        if (dayOfMonth < mday) {
                            //Toast.makeText(Datepicker.this,"please choose correct date",Toast.LENGTH_LONG).show();
                        } else if (mday >= dayOfMonth && year == myear && monthOfYear < mmonth) {
                            view.updateDate(myear, mmonth, mday);
                        } else {

                        }
                    }
                };
                return _date;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            myear = selectedYear;
            mmonth = selectedMonth;
            mday = selectedDay;
            String day;
            if (mday < 10) {
                day = "0";
            } else {
                day = "";
            }
            String mon;
            if (mmonth < 10) {
                mon = "/0";
            } else {
                mon = "/";
            }

            // set selected date into textview
            tvDisplayDate.setText(new StringBuilder().append(day).append(mday)
                    .append("-").append(mmonth + 1).append("-").append(myear)
                    .append(" "));
            Date=tvDisplayDate.getText().toString();

        }
    };






    public void Check_tables() {
        //advisor_adapter.notifyDataSetChanged();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCancelable(true);
        new Background().execute(to,Date,from);

    }



    class Background extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String date= params[0];
            String time = params[1];
            String tia = params[2];
            data = null;
            int tmp;
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String str_url="http://club44.in/matchtable.php?date="+Date+"&time="+to+"&tia="+from;
                String perfect_url=str_url.replace(" ","%20");
                HttpPost httpPost = new HttpPost(perfect_url);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
                //nameValuePairs.add(new BasicNameValuePair("date1", Rate));
                // nameValuePairs.add(new BasicNameValuePair("id", s));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpRes = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpRes.getEntity();
                data = EntityUtils.toString(httpEntity);

                Log.d("Data", data);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;

        }


        @Override
        protected void onPostExecute(String s) {
            try {
                progressDialog.dismiss();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray sportarraydata2 = jsonObject.getJSONArray("categories");
                if(test_adapter!=null){
                    test_adapter.clearAdapter();
                    for (int i = 0; i < sportarraydata2.length(); i++) {
                        JSONObject sportarraydata = sportarraydata2.getJSONObject(i);
                        style_list advisor_customer_group_list = new style_list();

                        advisor_customer_group_list.setTable_no(sportarraydata.getString("tablenumber"));
                        advisor_customer_group_list.setTable_id(sportarraydata.getString("tableid"));
                        listItemsList.add(advisor_customer_group_list);
                        test_adapter = new style_adapter(getActivity(), listItemsList);
                        mRecyclerView.setAdapter(test_adapter);
                    }
                }
                else{
                    for (int i = 0; i < sportarraydata2.length(); i++) {
                        JSONObject sportarraydata = sportarraydata2.getJSONObject(i);
                        style_list advisor_customer_group_list = new style_list();

                        advisor_customer_group_list.setTable_no(sportarraydata.getString("tablenumber"));
                        advisor_customer_group_list.setTable_id(sportarraydata.getString("tableid"));
                        listItemsList.add(advisor_customer_group_list);
                        test_adapter = new style_adapter(getActivity(), listItemsList);
                        mRecyclerView.setAdapter(test_adapter);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
