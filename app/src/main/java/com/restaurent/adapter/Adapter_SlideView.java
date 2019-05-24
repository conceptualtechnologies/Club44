package com.restaurent.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.restaurent.R;
import com.restaurent.mechanism.Methods;


public class Adapter_SlideView extends PagerAdapter {

    Context mContext;
    String value[];


    public Adapter_SlideView(Context context, String sources[]) {
        this.mContext = context;
        this.value = sources;
    }

    @Override
    public int getCount() {
        return value.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.slider_image, container, false);
        ImageView image = (ImageView) view.findViewById(R.id.home_image);

        //Methods.glide_image_loader(value[position],image);
        Methods.glide_image_loader_banner(value[position],image);
        /*image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.toast(mContext.getResources().getString( R.string.yet_to_build));
            }
        });*/

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
