package com.wangxiaobao.gsj.enity.dish;

import com.wangxiaobao.gsj.enity.result.BaseResult;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-04-12.
 */
public class DishResult  extends BaseResult {
    private ArrayList<Dish> data;

    public ArrayList<Dish> getData() {
        return data;
    }

    public void setData(ArrayList<Dish> data) {
        this.data = data;
    }
}
