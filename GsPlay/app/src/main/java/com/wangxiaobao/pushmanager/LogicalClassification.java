package com.wangxiaobao.pushmanager;

import java.io.Serializable;
import java.util.Map;

/**
 * TLV报文传输,取出外包后,内部的操作tag作为业务主体判断,其他属性存入map
 *
 * @author libin
 */
public class LogicalClassification implements Serializable {

    private static final long serialVersionUID = -7852210419353700488L;

    private String tag;

    private Map<String, Object> map;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

}
