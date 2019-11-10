package com.wangxiaobao.gsj.base;

import android.view.View;

import com.wangxiaobao.waiter.R;

/**
 * Created by candy on 17-11-1.
 */

public abstract  class ActionActivity extends DataActivity {


    @Override
    protected void initView() {
        super.initView();
        mTitleBar.mRightAction.setTextColor(getResources().getColor(R.color.new_text_light_green_color));
        mTitleBar.mRightAction.setVisibility(View.VISIBLE);
    }
}
