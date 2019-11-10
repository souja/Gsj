package com.wangxiaobao.gsj.log;

/**
 * Created by wenchaoy on 2016-04-21.
 */
public class QiNiuResult {
    String hash;
    String key;



    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    @Override
    public String toString() {
        return "QiNiuResult{" +
                "hash='" + hash + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}