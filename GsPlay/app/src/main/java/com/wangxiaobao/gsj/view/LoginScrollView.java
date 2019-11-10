package com.wangxiaobao.gsj.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by candy on 16-6-7.
 * <p/>
 * for show login button issue
 */
public class LoginScrollView extends ScrollView {

    public void setOnSizeChangedListenner(OnSizChangeListener onSizeChangedListenner) {
        this.onSizeChangedListenner = onSizeChangedListenner;
    }

    private OnSizChangeListener onSizeChangedListenner;

    public LoginScrollView(Context context) {
        super(context);
    }

    public LoginScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoginScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoginScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (this.onSizeChangedListenner != null && (w != oldw || oldh != h)) {
            onSizeChangedListenner.onSizeChanged();
        }
    }

    public interface OnSizChangeListener {
        void onSizeChanged();
    }
}
