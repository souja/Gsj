package com.wangxiaobao.gsj.common;

import android.content.Context;
import android.support.v7.app.AlertDialog;

public class DialogFactory {

    public static AlertDialog SimpleDialog(Context context, String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != null) builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("确定", ((dialog1, which) -> dialog1.dismiss()));
        return builder.create();
    }
}
