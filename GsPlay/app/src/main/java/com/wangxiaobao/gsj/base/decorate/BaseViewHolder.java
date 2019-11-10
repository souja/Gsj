package com.wangxiaobao.gsj.base.decorate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by ijays on 30/03/2018.
 */
public class BaseViewHolder<T> extends RecyclerView.ViewHolder {


    public BaseViewHolder(@NonNull ViewGroup parent, int layoutResId) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false));
//        ButterKnife.bind(this, itemView);
    }

    public void show(T t, int position) {

    }

    public Context getContext() {
        if (itemView == null) {
            return null;
        }
        return itemView.getContext();
    }
}
