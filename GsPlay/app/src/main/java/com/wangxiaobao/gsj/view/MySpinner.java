package com.wangxiaobao.gsj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wangxiaobao.waiter.R;

/**
 * 自定义下拉的控件，只做展示用，点击后弹出在SelectPopu实现
 */
public class MySpinner extends RelativeLayout{
    TextView tvHint;
    ImageView iv;
    public MySpinner(Context context) {
        this(context, null);
    }

    public MySpinner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_spinner_custom, this, true);
        tvHint = (TextView)findViewById(R.id.tv_myspinner);
        iv = (ImageView)findViewById(R.id.iv_myspinner);
    }

    /**
     * 设置提示字符串
     * @param hint
     */
    public  void setTextHint(String hint){
        tvHint.setText(hint);
    }

    /**
     * 设置图标点击事件
     * @param listener
     */
    public  void setImageClick(OnClickListener listener){
        iv.setOnClickListener(listener);
    }
}
