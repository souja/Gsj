package com.wangxiaobao.gsj.home;

import java.util.List;

/**
 * Created by candy on 18-3-7.
 */

public class AdviseListResponse {

    private int count;
    private List<Comment> datas;
    private int pageNum;
    private int pageSize;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Comment> getDatas() {
        return datas;
    }

    public void setDatas(List<Comment> datas) {
        this.datas = datas;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
