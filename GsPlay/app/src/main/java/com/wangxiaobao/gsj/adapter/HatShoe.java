package com.wangxiaobao.gsj.adapter;

import android.view.View;

/**
 * 添加、删除header/footer
 * Created by ijays on 08/12/2017.
 */

public interface HatShoe {
    void addHeaderView(View v);

    void addFooterView(View v);

    void removeHeaderView(View v);

    void removeFooterView(View v);

    void removeAllHeaderView();

    void removeAllFooterView();
}
