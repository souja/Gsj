package com.wangxiaobao.gsj.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.wangxiaobao.gsj.common.DensityTools;
import com.wangxiaobao.gsj.view.DialogTools;
import com.wangxiaobao.waiter.R;

import butterknife.ButterKnife;

/**
 * dialogFragment的基类
 * Created by ijays on 16/01/2018.
 */

public class BaseDialogFragment extends DialogFragment {
    private boolean mCanceledOnTouchOutside = true;//是否允许点击空白区域隐藏弹窗
    private boolean mIsHorizontalFull = false;//横向满屏
    private boolean mIsTransparentBackground = false;//背景全透明

    private OnKeyBackListener mKeyBackListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (mKeyBackListener != null) {
                    return mKeyBackListener.onKeyBack(dialog, keyCode, event);
                }
                return false;
            }
        });

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        int screenWidth = DensityTools.getScreenWidth(getContext());
        int windowWidth = mIsHorizontalFull ? screenWidth : screenWidth -
                2 * getResources().getDimensionPixelOffset(R.dimen.dialog_margin);
        getDialog().getWindow().setLayout(windowWidth, WindowManager.LayoutParams.MATCH_PARENT);
        getDialog().getWindow().setBackgroundDrawable(new
                ColorDrawable(Color.TRANSPARENT));//设置该属性才能全屏

        if (mIsTransparentBackground) {
            WindowManager.LayoutParams windowParams = getDialog().getWindow().getAttributes();
            windowParams.dimAmount = 0.0f;
            getDialog().getWindow().setAttributes(windowParams);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = getContentView(inflater, container);
        ButterKnife.bind(this, v);
        init(inflater, v, savedInstanceState);
        return v;
    }

    protected void init(LayoutInflater inflater, View rootView, Bundle savedInstanceState) {

    }

    protected int getLayoutId() {
        return 0;
    }

    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    public BaseDialogFragment setCanceledOnTouchOutside(boolean mCanceledOnTouchOutside) {
        this.mCanceledOnTouchOutside = mCanceledOnTouchOutside;
        return this;
    }

    /**
     * 避免出现 java.lang.IllegalStateException
     * Can not perform this action after onSaveInstanceState
     *
     * @param manager
     * @param tag
     */
    public void showAllowingStateLoss(FragmentManager manager, String tag) {
        DialogTools.showDialogAllowingStateLoss(manager, tag, this);
    }

    public void setOnKeyBackListener(OnKeyBackListener listener) {
        this.mKeyBackListener = listener;
    }

    public interface OnKeyBackListener {
        boolean onKeyBack(DialogInterface dialog, int keyCode, KeyEvent event);
    }

    public BaseDialogFragment setHorizontalFull(boolean isHorizontalFull) {
        mIsHorizontalFull = isHorizontalFull;
        return this;
    }

    public BaseDialogFragment setTransparentBackground(boolean isTransParentBackground) {
        mIsTransparentBackground = isTransParentBackground;
        return this;
    }

}
