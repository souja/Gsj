package com.wangxiaobao.gsj.view;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangxiaobao.waiter.R;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016-04-07.
 */
public class DialogTools {
    private static final String TAG = DialogTools.class.getSimpleName();

    /**
     * 返回一个位于屏幕下端的Dialog;
     *
     * @param context
     * @return
     */
    private static Dialog getTransDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.dialog_tran);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        return dialog;
    }


    /**
     * 登录加载
     *
     * @param context
     * @return
     */
    public static Dialog getLoadingDialog(Context context, String message) {
        if (TextUtils.isEmpty(message)) {
            message = "加载数据中...";
        }
        Dialog dialog = new Dialog(context, R.style.dialog_tran);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        ImageView ivLoading =  view.findViewById(R.id.loading_animation);
        TextView tv =  view.findViewById(R.id.message);
        tv.setText(message);
        RotateAnimation ani = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        ani.setRepeatCount(-1);
        ani.setDuration(1000);
        ivLoading.startAnimation(ani);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.CENTER);
        return dialog;
    }

    /**
     * 避免出现 java.lang.IllegalStateException
     * Can not perform this action after onSaveInstanceState
     *
     * @param manager
     * @param tag
     */
    public static void showDialogAllowingStateLoss(FragmentManager manager, String tag,
                                                   DialogFragment dialogFragment) {
        try {
            Field dismissed = DialogFragment.class.getDeclaredField("mDismissed");
            dismissed.setAccessible(true);
            dismissed.set(dialogFragment, false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            Field shown = DialogFragment.class.getClass().getDeclaredField("mShownByMe");
            shown.setAccessible(true);
            shown.set(dialogFragment, true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        FragmentTransaction ft = manager.beginTransaction();
        ft.add(dialogFragment, tag);
        ft.commitAllowingStateLoss();
    }



}
