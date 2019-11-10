package com.wangxiaobao.gsj.enity.result;


import com.wangxiaobao.gsj.enity.VersionEntity;

public class VersionInfoResult extends BaseResult {
    private VersionEntity data;

    public VersionEntity getData() {
        return data;
    }

    public void setData(VersionEntity data) {
        this.data = data;
    }

}
