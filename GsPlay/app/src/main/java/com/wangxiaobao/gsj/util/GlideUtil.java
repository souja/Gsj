package com.wangxiaobao.gsj.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.wangxiaobao.waiter.R;

/**
 * Created by ijays on 12/04/2018.
 */
public class GlideUtil {

    private static final String TAG = "GlideUtils";

    private static RequestOptions mRoundOptions = RequestOptions.circleCropTransform()
            .placeholder(R.color.white);

    private static RequestOptions mOptions = RequestOptions
            .centerInsideTransform()
            .placeholder(R.color.white)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    /**
     * 加载网络图片
     *
     * @param view
     * @param url
     */
    public static void display(ImageView view, String url) {
        if (view == null) {
            return;
        }

        Context context = view.getContext();
        if (!isActivityAlive(view.getContext())) {
            return;
        }


        Glide.with(context)
                .applyDefaultRequestOptions(mOptions)
                .load(url)
                .into(view);
    }

    /**
     * 加载本地图片
     *
     * @param view
     * @param resId
     */
    public static void displayNative(ImageView view, @DrawableRes int resId) {
        if (view == null) {
            return;
        }

        Context context = view.getContext();
        if (!isActivityAlive(view.getContext())) {
            return;
        }

        Glide.with(context)
                .load(resId)
                .apply(mOptions)
                .into(view);
    }

    /**
     * 加载本地圆形图片
     *
     * @param view
     * @param resId
     */
    public static void displayRoundImg(ImageView view, @DrawableRes int resId) {
        if (view == null) {
            return;
        }

        Context context = view.getContext();
        if (!isActivityAlive(context)) {
            return;
        }

        Glide.with(context)
                .load(resId)
                .apply(mRoundOptions)
                .into(view);
    }

    /**
     * 加载网络圆形图片
     *
     * @param view
     * @param url
     */
    public static void displayRoundImg(ImageView view, String url) {
        if (view == null) {
            return;
        }

        Context context = view.getContext();
        if (!isActivityAlive(view.getContext())) {
            return;
        }

        Glide.with(context)
                .load(url)
                .apply(mRoundOptions)
                .into(view);
    }

    private static boolean isActivityAlive(Context context) {
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return false;
            }
        }
        return true;
    }

    public static void displayGif(ImageView view, @DrawableRes int resId) {
        if (view == null) {
            return;
        }

        Context context = view.getContext();
        if (!isActivityAlive(view.getContext())) {
            return;
        }

        Glide.with(context)
                .asGif()
                .load(resId)
                .into(view);
    }
}

