package com.wangxiaobao.gsj.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.wangxiaobao.gsj.common.LogTool;

/**
 * Created by candy on 17-9-25.
 */

public class RatioImageView extends ImageView {
    private static final String TAG = RatioImageView.class.getSimpleName();

    public RatioImageView(Context context) {
        super(context);
    }

    public RatioImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RatioImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RatioImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 宽模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        // 宽大小
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        LogTool.i(TAG, "widthSize:" + widthSize);
        // 高大小
        int heightSize;
        // 只有宽的值是精确的才对高做精确的比例校对
        heightSize = (int) (widthSize * (3f / 4f) + 0.5f);
        LogTool.i(TAG, "heightSize:" + heightSize);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize,
                MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
