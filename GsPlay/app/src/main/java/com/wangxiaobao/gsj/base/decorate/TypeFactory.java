package com.wangxiaobao.gsj.base.decorate;

import android.view.ViewGroup;

import com.wangxiaobao.gsj.enity.NewCommentModel;

/**
 * Created by ijays on 30/03/2018.
 */
public interface TypeFactory {

    int type(NewCommentModel model);

    BaseViewHolder createViewHolder(ViewGroup parent, int viewType);

}
