package com.wangxiaobao.gsj.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wangxiaobao.gsj.common.LogTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by jack on 2017/10/28.
 */

public abstract class BaseAdapter<T, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    private static final String TAG = BaseAdapter.class.getSimpleName();
    /**
     * 标识是否显示EmptyView
     */
    protected boolean isShowEmptyView = false;
    protected List<T> datas;
    protected Context context;



    public BaseAdapter(Context context) {
        this(context, null);
    }

    public BaseAdapter(Context context, List<T> datas) {
        this.context = context;
        this.datas = datas;
        if (datas == null) {
            this.datas = new ArrayList<>();
        }
    }

    public void removeItem(T t) {
            removeItem(datas.indexOf(t));
    }

    public void removeItem(int position) {
        if (datas != null && datas.size() > position) {
            datas.remove(position);
            notifyItemRemoved(position);
        }
    }


    public void addAll(List<T> list) {
        datas.addAll(list);
        notifyDataSetChanged();
    }

    public void addItem(T t) {
        datas.add(t);
        notifyDataSetChanged();
    }


    public List<T> getDatas() {
        return datas;
    }

    public void setData(List<T> datas) {
        this.datas = datas;
        if (this.datas == null) {
            this.datas = new ArrayList<>();
            isShowEmptyView = true;
        } else {
            isShowEmptyView = false;
        }
        notifyDataSetChanged();
    }


    protected View createItemView(ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(getItemViewLayout(), viewGroup,false);
    }



    public T getItem(int position){
        if (datas!=null &&datas.size()>position){
         return    datas.get(position);
        }
        return null;
    }

    protected abstract int getItemViewLayout();

    public abstract void convert(int position, V holder, T t);


    @Override
    public void onBindViewHolder(V holder, int position) {
        LogTool.saveLog(TAG, "onBindViewHolder position:" + position);

        convert(position, holder, getItem(position));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }


}
