package com.wangxiaobao.gsj.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wangxiaobao.waiter.R;

/**
 * Created by Administrator on 2016-04-06.
 */
public class TitleBar extends RelativeLayout {

    ImageView mBack;
    public  TextView mTitle, mRightAction;
    private Context mContext;
    private int bgColorId=R.color.new_primary_black_color;

    public String mTitleText;
    private boolean mVisibility;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TitleBar(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaiterTitleBar);
        mTitleText = typedArray.getString(R.styleable.WaiterTitleBar_waiter_title);
        mVisibility = typedArray.getBoolean(R.styleable.WaiterTitleBar_right_visibility, false);
        setBgColor(R.color.new_primary_black_color);
        typedArray.recycle();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(mContext).inflate(R.layout.layout_title, this);
        setBackgroundColor(getResources().getColor(bgColorId));
        mBack = (ImageView) findViewById(R.id.back);
        mRightAction = (TextView) findViewById(R.id.right_action);
        mTitle = (TextView) findViewById(R.id.actionbar_title);

        if (mContext instanceof Activity) {
            mBack.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity activity = (Activity) mContext;
                    activity.finish();
                }
            });

        }

        if (!TextUtils.isEmpty(mTitleText)) {
            mTitle.setText(mTitleText);
        }
        mRightAction.setVisibility(mVisibility ? VISIBLE : GONE);
    }

    public void setBgColor(int colorId){
        setBackgroundColor(getResources().getColor(colorId));
    }

    /**
     * 设置左边图标
     *
     * @param ResId
     */
    public void setImageLeft(int ResId) {
        mBack.setImageResource(ResId);

    }

    /**
     * 设置中间标题
     *
     * @param title
     */
    public void setTitle(String title) {
        mTitle.setText(title);
    }


    /**
     * 右边标题
     *
     * @param str
     */
    public void setTitleRight(String str) {
        mRightAction.setText(str);
    }



    public void setRightActionVisiable(int visiableFlag) {
        mRightAction.setVisibility(visiableFlag);
    }

    /**
     * 设置右边标题颜色
     *
     * @param color
     */
    public void setTitleRightColor(int color) {
        mRightAction.setTextColor(color);
    }

    public void setRightBg(int bgId) {
        mRightAction.setBackgroundResource(bgId);
    }

    /**
     * 设置右边是否可见
     *
     * @param vis
     */
    public void setRightVisiable(boolean vis) {
        if (vis)
            mRightAction.setVisibility(VISIBLE);
        else
            mRightAction.setVisibility(INVISIBLE);
    }

    public void disableRightBtn(){
        mRightAction.setEnabled(false);
        mRightAction.setTextColor(getResources().getColor(R.color.white_five));

    }

    public void  enableRightBtn(){
        mRightAction.setEnabled(true);
        mRightAction.setTextColor(getResources().getColor(R.color.white));
    }


    /**
     * 设置左边图标点击事件
     *
     * @param clickListener
     */
    public void setLeftOnClickListener(OnClickListener clickListener) {
        mBack.setOnClickListener(clickListener);
    }

    public void setRightClickListener(OnClickListener listener) {
        mRightAction.setOnClickListener(listener);
    }

    public ImageView getmBack() {
        return mBack;
    }

    public TextView getmTitle() {
        return mTitle;
    }

    public TextView getmRightAction() {
        return mRightAction;
    }

    public void setLeftClick(OnClickListener listener) {
//        if (listener instanceof Activity){
//            Activity activity = (Activity) listener;
//            activity.finish();
//        }
        mBack.setOnClickListener(listener);
    }

    public void setTitleColor(int color) {
        mTitle.setTextColor(getResources().getColor(color));
    }
}
