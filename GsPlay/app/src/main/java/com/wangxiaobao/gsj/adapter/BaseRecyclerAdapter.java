package com.wangxiaobao.gsj.adapter;

import android.view.View;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by ijays on 13/12/2017.
 */

public abstract class BaseRecyclerAdapter<VH extends DataRecyclerViewHolder, T>
        extends XXXAdapter<VH> implements AdapterSet<T> {

    private OnItemClickListener mOnItemClickListener;
    private List<T> mListData;

    public BaseRecyclerAdapter(List<T> listData) {
        super();
        this.mListData = listData;
        if (mListData==null){
            mListData =new ArrayList<>();
        }
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getRealItemViewType(int position) {
        return 0;
    }




    @Override
    public void onRealBindViewHolder(VH viewHolder, final int position) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

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
    public int getRealItemCount() {
        if (mListData == null)
            return 0;
        return mListData.size();
    }

    @Override
    public List<T> getList() {
        return mListData;
    }

    @Override
    public void addList(List<T> listData) {
        this.mListData.addAll(listData);
        notifyDataSetChanged();
    }

    @Override
    public void setList(List<T> listData) {
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
    public T getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public void addItem(T item) {

        this.mListData.add(item);
        notifyDataSetChanged();
    }

    @Override
    public void deleteItem(T item) {

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
