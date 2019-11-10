package com.wangxiaobao.gsj.adapter;

import java.util.List;

/**
 * Created by ijays on 13/12/2017.
 */

public interface AdapterSet<T> {
    List<T> getList();

    void addList(List<T> listData);

    void setList(List<T> listData);

    void clearList();

    T getItem(int position);

    void addItem(T item);

    void deleteItem(T item);

    void deleteItem(int position);
}
