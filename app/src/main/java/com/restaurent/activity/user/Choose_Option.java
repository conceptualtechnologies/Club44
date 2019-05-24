package com.restaurent.activity.user;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.restaurent.R;
import com.restaurent.mechanism.Methods;

/**
 * Created by yeshu on 11/10/2017.
 */

public class Choose_Option {
    ImageView product_image;
    public void showDialog(Activity activity, String msg,String image){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.choose_option);
        product_image = (ImageView) dialog.findViewById(R.id.product_image);

        Methods.glide_image_loader_fixed_size(image,product_image);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
