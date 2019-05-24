package com.restaurent.mechanism;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.Toast;

import com.restaurent.Application_Context;
import com.restaurent.R;
import com.restaurent.constant_class.URL_Class;
import com.restaurent.utils.CategoryDataSet;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Methods {

    public static void toast(String message) {
        Toast.makeText(Application_Context.getAppContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static String[] getArrayList(ArrayList<CategoryDataSet> list) {

        if (list.size() != 0) {
            String array[] = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                array[i] = list.get(i).getName();
            }
            return array;
        } else {
            return null;
        }
    }

    public static String convertInputStreamToString(BufferedReader bufferedReader) {
        try {
            String line;
            String result = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public static void glide_image_loader(String url, final ImageView imageView) {
        Glide.with(Application_Context.getAppContext()).load(url).asBitmap().priority(Priority.IMMEDIATE).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.mipmap.icon).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                imageView.setImageBitmap(resource);
            }
        });
    }

    public static void glide_image_loader_fixed_size(String url, final ImageView imageView) {
        Glide.with(Application_Context.getAppContext()).load(url).asBitmap().override(300, 300).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    public static void glide_image_loader_banner(String url, final ImageView imageView) {
        Glide.with(Application_Context.getAppContext()).load(url).centerCrop().priority(Priority.IMMEDIATE).diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.icon).into(imageView);
    }

    public static boolean isEmailValidator(String inputEmail) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(URL_Class.mMail_Pattern);
        matcher = pattern.matcher(inputEmail);
        return matcher.matches();
    }

    public static String current_language() {
        return URL_Class.mLanguage_ENGLISH;
    }

}
