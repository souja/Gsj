package com.wangxiaobao.message;

import java.io.Serializable;

/**
 * Created by jack on 2018/1/25.
 */

public class AliPushMessage implements Serializable {

    private String title;
    private String content;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
