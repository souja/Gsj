package com.wangxiaobao.gsj.enity.result;


import java.util.List;

/**
 * 网络请求返回基本数据
 */
public class JsonListResult<T> extends BaseResult {
    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

}

