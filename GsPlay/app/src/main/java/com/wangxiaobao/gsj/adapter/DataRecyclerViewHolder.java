package com.wangxiaobao.gsj.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * 绑定数据的RecyclerView列表对应的ViewHolder基类
 * Created by ijays on 13/12/2017.
 */

public abstract class DataRecyclerViewHolder<T> extends RecyclerView.ViewHolder {

    protected Context mContext;

    public DataRecyclerViewHolder(@NonNull ViewGroup parent, int layoutResId) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false));
        ButterKnife.bind(this, itemView);
        this.mContext = parent.getContext();
    }

    public void showViewContent(T t) {

    }

    public void showViewContent(T t, int position) {

    }


    public Context getContext() {
        if (itemView == null) {
            return null;
        }
        return itemView.getContext();
    }
}