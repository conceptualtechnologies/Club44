package com.restaurent.adapter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.restaurent.R;
import com.restaurent.activity.user.Allproducts_list;
import com.restaurent.interfaces.API_Result;
import com.restaurent.mechanism.Methods;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MY on 20-Jan-18.
 */

public class AllUserproduct_adapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Allproducts_list> listItemsList;
    private android.content.Context Context;
    private com.android.volley.toolbox.ImageLoader ImageLoader;
    private int focusedItem = 0;


    private android.widget.Button Button;

    public AllUserproduct_adapter(android.content.Context context, List<Allproducts_list> listItemsList) {
        this.listItemsList = listItemsList;
        this.Context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_product , null);
        AllUserproduct_adapter.ListRowViewHolder holder = new AllUserproduct_adapter.ListRowViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Allproducts_list advisorname = listItemsList.get(position);
        AllUserproduct_adapter.ListRowViewHolder holder2 = (AllUserproduct_adapter.ListRowViewHolder) holder;
        holder.itemView.setSelected(focusedItem == position);
        holder.getLayoutPosition();


        //Methods.glide_image_loader_fixed_size(advisorname.getImages(), holder2.thumbnail);



        //   Methods.glide_image_loader_fixed_size(advisorname.getImage(),holder2.image);
        Methods.glide_image_loader(advisorname.getImage(),holder2.image);
        holder2.category_name.setText(advisorname.getModel());
        holder2.offer.setText(advisorname.getOffer());

        if (advisorname.getOffer_price()!= null) {

            if (!advisorname.getOffer_price().isEmpty()) {
                holder2.view_product.setText(advisorname.getPrice());
                holder2.view_product.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder2.offer_price.setText(advisorname.getOffer_price());

            } else {

                holder2.view_product.setText(advisorname.getPrice());
//                    total.setText(mProductDataSet.getPrice());
                //  txt_product_price.setVisibility(View.GONE);
            }
        }else {

            holder2. offer_price.setText(advisorname.getPrice());
        }
        //    holder2.image.setText(advisorname.getImage());
        // holder2.shop_address.setText(advisorname.getShop_address());
      /* holder2.address.setText(advisorname.getAddress());
        holder2.fax.setText(advisorname.getFax_no());
        holder2.kind.setText(advisorname.getKind_attention());
        holder2.renmark.setText(advisorname.getRemark());
        holder2.packing_rate.setText(advisorname.getPacking_rate());
        holder2.packing.setText(advisorname.getPacking_percentage());
        holder2.prepared_designation.setText(advisorname.getPrepared_designation());
        holder2.prepared_mobile.setText(advisorname.getPrepared_mobile());
        holder2.prepared_name.setText(advisorname.getPrepared_name());
        holder2.mobile.setText(advisorname.getMobile());
        holder2.email.setText(advisorname.getEmail());
        holder2.total.setText(advisorname.getTotal_amount());
        holder2.other.setText(advisorname.getOther_charges());
        holder2.gst_invoice.setText(advisorname.getGst_percentage());
        holder2.gst_rate.setText(advisorname.getGst_rate());

        holder2.net.setText(advisorname.getNet_amount());


        holder2.date1.setText(advisorname.getDate());


        //   holder2.HairCute.setText(advisorname.getId());*/

    }


    public void clearAdapter() {
        listItemsList.clear();
        //  notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        return (null != listItemsList ? listItemsList.size() : 0);
    }

    public class ListRowViewHolder extends RecyclerView.ViewHolder implements API_Result, View.OnClickListener{
        protected ImageView thumbnail;

        protected TextView category_name,offer, Comapany_name,shop_name,shop_address,date1,net,invoice,other,address,fax,kind,mobile,email,invoice_no,date,prepared_name,prepared_designation,packing,packing_rate,renmark,gst_invoice,gst_rate,prepared_mobile,total,order_no, HairStraightening, HairColouring, HairCut, HairCute;
        String c1, data;
        ImageView style_image,style_images,style_imagess,edit,delete;
        android.widget.Button next, previous,save;
        private RatingBar ratingBar;
        private CardView card;
        ImageView image;

        RequestQueue requestQueue;
        private CheckBox Checkbox1;
        private LinearLayout linearlayout;


        private StringRequest stringRequest;

        SharedPreferences preferences;
        ProgressDialog progressDialog;
        API_Result api_result;
        TextView view_product,offer_price;
        protected RelativeLayout relativeLayout;


        public ListRowViewHolder(View view) {
            super(view);
            // this.thumbnail = (ImageView) view.findViewById(R.id.iv_news_image);
            //  this.id = (TextView) view.findViewById(R.id.txt_id);
            api_result = (API_Result) this;

            progressDialog = new ProgressDialog(Context);
            this.category_name= (TextView) view.findViewById(R.id.category_name);
            this.offer= (TextView) view.findViewById(R.id.fetch_offer);
            this.offer_price= (TextView) view.findViewById(R.id.offer_price);
            this.view_product= (TextView) view.findViewById(R.id.view_product);

            this.image=(ImageView) view.findViewById(R.id.iv_category_image);
            this.card =(CardView) view.findViewById(R.id.cardview_userproduct);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                //    intent_transfer(getAdapterPosition());

                }
            });




         /*  this.address= (TextView) view.findViewById(R.id.address_invoice);
            this.fax= (TextView) view.findViewById(R.id.fax_invoice);
            this.kind= (TextView) view.findViewById(R.id.kind_invoice);
            this.mobile= (TextView) view.findViewById(R.id.mobile_invoice);
            this.email= (TextView) view.findViewById(R.id.email_invoice);
            this.invoice_no= (TextView) view.findViewById(R.id.invoice_no_invoice);
            this.date1= (TextView) view.findViewById(R.id.date_invoice);
            this.prepared_name= (TextView) view.findViewById(R.id.prepared_invoice);
            this.prepared_mobile= (TextView) view.findViewById(R.id.prepared_mobile_invoice);
            this.prepared_designation= (TextView) view.findViewById(R.id.prepared_designation_invoice);
            this.renmark= (TextView) view.findViewById(R.id.remark_invoice);
            this.total= (TextView) view.findViewById(R.id.total_invoice);
            this.packing= (TextView) view.findViewById(R.id.packing_invoice);
            this.packing_rate= (TextView) view.findViewById(R.id.packing_rate_invoice);
            this.gst_invoice= (TextView) view.findViewById(R.id.gst_invoice);
            this.gst_rate= (TextView) view.findViewById(R.id.gst_rate_invoice);
            this.other= (TextView) view.findViewById(R.id.other_invoice);
            this.net= (TextView) view.findViewById(R.id.net_amount_invoice);
*/

        }




        @Override
        public void onClick(View v) {

        }

        @Override
        public void result(String[] data, String source) {

        }
    }


}
