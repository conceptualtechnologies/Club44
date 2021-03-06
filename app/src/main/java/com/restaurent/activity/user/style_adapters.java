package com.restaurent.activity.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.restaurent.R;

import java.util.List;

/**
 * Created by MY on 10/9/2017.
 */

public class style_adapters extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<style_list> listItemsList;
    private android.content.Context Context;
    private com.android.volley.toolbox.ImageLoader ImageLoader;
    private int focusedItem = 0;


    private android.widget.Button Button;

    public style_adapters(android.content.Context context, List<style_list> listItemsList) {
        this.listItemsList = listItemsList;
        this.Context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_no , null);
        ListRowViewHolder holder = new ListRowViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        style_list advisorname = listItemsList.get(position);
       ListRowViewHolder holder2 = (ListRowViewHolder) holder;
        holder.itemView.setSelected(focusedItem == position);
        holder.getLayoutPosition();


        // Methods.glide_image_loader_fixed_size(advisorname.getImages(), holder2.thumbnail);





       // holder2.HairCurling.setText(advisorname.getEvent_id());
        holder2.HairCut.setText(advisorname.getEvents());
        //   holder2.HairCute.setText(advisorname.getId());

    }


    public void clearAdapter() {
        listItemsList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        return (null != listItemsList ? listItemsList.size() : 0);
    }

    public class ListRowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected ImageView thumbnail;
        protected TextView id, HairCurling, HairStraightening, HairColouring, HairCut, HairCute;
        String c1, data;
        ImageView style_image,style_images,style_imagess;
        android.widget.Button next, previous,save;
        private RatingBar ratingBar;
        private CardView card;
        RequestQueue requestQueue;
        private CheckBox Checkbox1;

        private StringRequest stringRequest;

        SharedPreferences preferences;

        protected RelativeLayout relativeLayout;


        public ListRowViewHolder(View view) {
            super(view);
            // this.thumbnail = (ImageView) view.findViewById(R.id.iv_news_image);
            //  this.id = (TextView) view.findViewById(R.id.txt_id);
            ;
           // this.HairCurling = (TextView) view.findViewById(R.id.tvtableid);

            this.HairCut = (TextView) view.findViewById(R.id.tvevents);



            card=(CardView)view.findViewById(R.id.card_view5);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Context, Event.class);
                    intent.putExtra("event_id",listItemsList.get(getAdapterPosition()).getEvent_id() );
                    intent.putExtra("Events",listItemsList.get(getAdapterPosition()).getEvents() );

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Context.startActivity(intent);
                }
            });
/*            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        //Intent ieventreport = new Intent(Context,style_adapter.class);
                     // Context.startActivity(ieventreport);
                }
            });*/

        }
        public void intent_transfer(int position) {

        }
        @Override
        public void onClick(View v) {

        }
    }



}

