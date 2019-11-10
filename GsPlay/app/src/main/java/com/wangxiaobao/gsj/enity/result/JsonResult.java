package com.wangxiaobao.gsj.enity.result;


/**
 * 网络请求返回基本数据
 */
public class JsonResult<T> extends BaseResult {
    private T data;
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}

