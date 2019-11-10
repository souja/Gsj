package com.wangxiaobao.gsj.base.decorate;

import android.view.ViewGroup;

import java.util.List;

/**
 * Created by ijays on 30/03/2018.
 */
public class StatisticListAdapter extends TypeAdapter<BaseViewHolder, Visitable> {
    private TypeFactoryImpl mTypeFactory;

    public StatisticListAdapter(List<Visitable> listData) {
        super(listData);
        mTypeFactory = new TypeFactoryImpl();
    }


    @Override
    public int getRealItemViewType(int position) {
        return getItem(position).type(mTypeFactory);
    }

    @Override
    public BaseViewHolder onRealCreateViewHolder(ViewGroup parent, int viewType) {
        return mTypeFactory.createViewHolder(parent, viewType);
    }

    @Override
    public void onRealBindViewHolder(BaseViewHolder holder, int position) {
        super.onRealBindViewHolder(holder,position);
        holder.show(getItem(position), position);
    }

}
