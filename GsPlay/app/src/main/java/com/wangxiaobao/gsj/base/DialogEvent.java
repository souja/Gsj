package com.wangxiaobao.gsj.base;

import java.io.Serializable;

/**
 * Created by candy on 17-10-30.
 */

public class DialogEvent implements Serializable {



    public static final  int EVENT_HIDE_DIALOG=1;
    public static final  int EVENT_SHOW_DIALOG=2;
    public static final  int EVENT_DISMISS_DIALOG=3;

    @Override
    public String toString() {
        return "DialogEvent{" +
                "cancelable=" + cancelable +
                ", message='" + message + '\'' +
                '}';
    }


    public int eventCode;

    public DialogEvent() {



    }


    public DialogEvent(boolean cancelable, String message) {
        this.cancelable = cancelable;
        this.message = message;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }


    private boolean cancelable;
    private String message;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
