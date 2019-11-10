package com.wangxiaobao.gsj.base.decorate;

import android.view.View;


import com.wangxiaobao.gsj.adapter.AdapterSet;
import com.wangxiaobao.gsj.adapter.XXXAdapter;

import java.util.List;

/**
 * Created by ijays on 30/03/2018.
 */
public abstract class TypeAdapter<VH extends BaseViewHolder,T> extends XXXAdapter<VH> implements AdapterSet<Visitable> {
    private List<Visitable> mListData;
    private OnItemClickListener mOnItemClickListener;

    public TypeAdapter(List<Visitable> mDataList) {
        this.mListData = mDataList;
    }

    @Override
    public int getRealItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    @Override
    public void onRealBindViewHolder(VH holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemLongClick(v, position);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public List<Visitable> getList() {
        return mListData;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void addList(List<Visitable> listData) {
        this.mListData.addAll(0,listData);
        notifyDataSetChanged();
    }

    @Override
    public void setList(List<Visitable> listData) {
        if (listData != null) {
            this.mListData.clear();
            this.mListData.addAll(listData);
            notifyDataSetChanged();
        }
    }

    @Override
    public void clearList() {
        this.mListData.clear();
        notifyDataSetChanged();
    }

    @Override
    public Visitable getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public void addItem(Visitable item) {

        this.mListData.add(item);
        notifyDataSetChanged();
    }

    @Override
    public void deleteItem(Visitable item) {

        this.mListData.remove(item);
        notifyDataSetChanged();
    }

    @Override
    public void deleteItem(int position) {

        this.mListData.remove(position);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);

        void onItemLongClick(View v, int position);
    }
}
