package com.wangxiaobao.gsj.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wangxiaobao.gsj.common.ScreenUtil;
import com.wangxiaobao.gsj.util.GlideUtil;
import com.wangxiaobao.gsj.util.extension.StringExtensionKt;
import com.wangxiaobao.gsj.view.RecyclerViewBanner;
import com.wangxiaobao.pushmanager.CommonUtils;
import com.wangxiaobao.waiter.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 大屏的屏保页面
 * Created by ijays on 2018/10/10.
 */
public class SplashActivity extends BaseActivity {
    @BindView(R.id.rv_banner)
    RecyclerViewBanner rvBanner;

    @Override
    protected int generateLayoutId() {
        return R.layout.activity_splash_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        ScreenUtil.setScale(this);
    }

    @Override
    protected void initView() {
        List<Banner> banners = new ArrayList<>();
        banners.add(new Banner(R.drawable.ic_gsj_new, 0));

//        if (StringExtensionKt.getDeviceType(CommonUtils.getSn()) == 0) {
//            //春熙路设备
//            banners.add(new Banner(R.drawable.icon_splash_bg_chunxi_road, 1));
//        } else {
//            banners.add(new Banner(R.drawable.icon_splash_fusenmei_bg, 1));
//        }

        rvBanner.setRvBannerData(banners);

        rvBanner.setOnSwitchRvBannerListener((position, bannerView) -> {
            bannerView.removeAllViews();
            Banner banner = banners.get(position);
            switch (banner.getType()) {
                case 0:
                    View view = LayoutInflater.from(SplashActivity.this)
                            .inflate(R.layout.item_splash_first, null);
                    view.findViewById(R.id.tv_to_main).setOnClickListener(v -> goMain());
                    GlideUtil.displayNative(view.findViewById(R.id.iv), banner.getUrl());
                    bannerView.addView(view);
                    break;
                case 1:
                    if (StringExtensionKt.getDeviceType(CommonUtils.getSn()) == 0) {

                        View view1 = LayoutInflater.from(SplashActivity.this)
                                .inflate(R.layout.item_splash_chunxi_road, null);
                        view1.findViewById(R.id.fl_container).setOnClickListener(v -> goMain());
                        GlideUtil.displayNative(view1.findViewById(R.id.iv), banner.getUrl());
                        bannerView.addView(view1);
                    } else {
                        View view2 = LayoutInflater.from(SplashActivity.this)
                                .inflate(R.layout.item_splash_second, null);
                        view2.findViewById(R.id.fl_container).setOnClickListener(v -> goMain());
                        GlideUtil.displayNative(view2.findViewById(R.id.iv), banner.getUrl());
                        bannerView.addView(view2);
                    }
                    break;
                default:
                    break;
            }
        });
//        Toast.makeText(SplashActivity.this, Build.SERIAL, 5000).show();
//        Log.e(SplashActivity.this.getClass().getName(), "SN:" + Build.SERIAL);
    }

    //    @OnClick(R.id.tv_to_main)
//    void onClickMain() {
//        startActivity(new Intent(this, MainActivity.class));
//    }
    private void goMain() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
    }

    @Override
    public void onBackPressed() {

    }

    private class Banner {

        int type;
        int imgRes;

        public Banner(int url, int type) {
            this.imgRes = url;
            this.type = type;
        }

        public int getUrl() {
            return imgRes;
        }

        public int getType() {
            return type;
        }
    }

}
