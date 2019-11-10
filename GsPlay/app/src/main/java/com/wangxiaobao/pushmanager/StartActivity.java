package com.wangxiaobao.pushmanager;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

/**
 * Created by wave on 2018/1/14.
 */

public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView =new TextView(this);
        textView.setText("kkkkk");
        setContentView(textView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },3*1000);
    }
}
