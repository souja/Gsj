package com.wangxiaobao.gsj.view.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.wangxiaobao.waiter.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ijays on 20/01/2018.
 */

public class BottomDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = getContentView(inflater, container);
        ButterKnife.bind(this, v);
        init(inflater, v, savedInstanceState);
        return v;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    protected void init(LayoutInflater inflater, View rootView, Bundle savedInstanceState) {

    }

    protected int getContentViewLayoutId() {
        return 0;
    }

    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(getContentViewLayoutId(), container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        Window window = getDialog().getWindow();
        if (window != null) {

            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new
                    ColorDrawable(Color.TRANSPARENT));//设置该属性才能全屏
            window.setGravity(Gravity.BOTTOM);
        }
    }

}