package com.wangxiaobao.gsj.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.LinearLayout;

import com.wangxiaobao.waiter.R;

/**
 * Created by candy on 16-10-19.
 */
public class RoundLinearLayout extends LinearLayout {


    private static final String TAG = RoundLinearLayout.class.getSimpleName();
    private int mRadius;
    private int mBgColor;
    private Context mContext;

    private final int mDefaultRadius = 15;

    public RoundLinearLayout(Context context) {
        this(context, null);
    }

    public RoundLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public RoundLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RoundLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        Log.i(TAG,"init");
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.roundtextview);
        mRadius = typedArray.getDimensionPixelSize(R.styleable.roundtextview_corner_size, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDefaultRadius, context.getResources().getDisplayMetrics()));
        mBgColor = typedArray.getColor(R.styleable.roundtextview_roundtextview_background, getResources().getColor(R.color.primary_color));
        setBackgroundDrawable(null);
        typedArray.recycle();
    }


    public void setBgColorResource(int color) {
        mBgColor = mContext.getResources().getColor(color);
        invalidateBg();
    }


    private void invalidateBg() {
        setBackgroundDrawable(null);
    }


    @Override
    public void setBackgroundDrawable(Drawable background) {
        Log.i(TAG,"setBackgroundDrawable");
        float[] outerR = new float[]{mRadius, mRadius, mRadius, mRadius, mRadius, mRadius, mRadius, mRadius};
        RoundRectShape roundRectShape = new RoundRectShape(outerR, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(mBgColor);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable.getPaint().setAntiAlias(true);
        super.setBackgroundDrawable(shapeDrawable);
    }
}
