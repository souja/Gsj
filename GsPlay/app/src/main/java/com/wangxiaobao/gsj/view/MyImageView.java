package com.wangxiaobao.gsj.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.wangxiaobao.gsj.common.LogTool;

/**
 * Created by candy on 17-7-19.
 */

public class MyImageView extends android.support.v7.widget.AppCompatImageView {
    private static final String TAG = MyImageView.class.getSimpleName();

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getDrawable();
        int width = MeasureSpec.getSize(widthMeasureSpec);//高度根据使得图片的宽度充满屏幕计算而得
        if (d != null) {
            // ceil not round - avoid thin vertical gaps along the left/right edges
             int height = (int) Math.ceil((float) width * (float) d.getIntrinsicHeight() / (float) d.getIntrinsicWidth());


            LogTool.i(TAG,"onMeasure width:"+width+" height:"+height);
            setMeasuredDimension(width, height);

        } else {
//            int height = (int) Math.ceil((float) width * (float) d.getIntrinsicHeight() / (float) d.getIntrinsicWidth());
            LogTool.i(TAG,"onMeasure width:"+width+" height:"+width);
            setMeasuredDimension(width, width);
        }
    }

}
