package com.projects.instaclient.adapters;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.projects.instaclient.R;
import com.projects.instaclient.helpers.Helpers;

public class Converters {

    @BindingAdapter("app:setDrawable")
    public static void setImageViewDrawable(ImageView imageView, Drawable drawable) {
        imageView.setImageDrawable(drawable);
    }

    @BindingAdapter("app:setLikesText")
    public static void setLikesText(TextView textView, int likes) {
        textView.setText(Helpers.convertLikesToText(likes));
    }

}
