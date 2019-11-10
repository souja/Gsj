package com.wangxiaobao.gsj.base;

import android.view.View;

/**
 * Created by candy on 16-12-8.
 */
public interface ViewConstructorInterface {



    View buildContentView(int layout);

    void initData();

    void initView();

}
