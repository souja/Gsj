package com.wangxiaobao.gsj.base.decorate;

import android.view.ViewGroup;

import com.wangxiaobao.gsj.enity.NewCommentModel;
import com.wangxiaobao.gsj.module.comment.viewholder.NewCommentVH;
import com.wangxiaobao.waiter.R;

/**
 * Created by ijays on 30/03/2018.
 */
public class TypeFactoryImpl implements TypeFactory {
    private static final int ITEM_STATISTIC_LAYOUT = 0x01;


    @Override
    public int type(NewCommentModel model) {
        return ITEM_STATISTIC_LAYOUT;
    }

    @Override
    public BaseViewHolder createViewHolder(ViewGroup parent, int type) {
        switch (type) {
            case ITEM_STATISTIC_LAYOUT:
                return new NewCommentVH(parent, R.layout.item_new_comment_layout);
            default:
                break;
        }
        return null;
    }


}
