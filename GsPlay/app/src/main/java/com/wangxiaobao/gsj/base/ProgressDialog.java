package com.wangxiaobao.gsj.base;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangxiaobao.gsj.common.LogTool;
import com.wangxiaobao.waiter.R;

/**
 * Created by candy on 17-10-30.
 */

public class ProgressDialog extends AppCompatDialog {


    private static final String TAG = ProgressDialog.class.getSimpleName();
    TextView messageView;
    ImageView animationContainer;


    public ProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public ProgressDialog( Context context) {
        super(context, R.style.dialog_tran);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        animationContainer = view.findViewById(R.id.loading_animation);
        messageView = view.findViewById(R.id.message);
        startLoadingAnimation();
        setContentView(view);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
        getWindow().setGravity(Gravity.CENTER);
    }

    private void startLoadingAnimation() {
        RotateAnimation ani = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        ani.setRepeatCount(-1);
        ani.setDuration(1000);
        animationContainer.startAnimation(ani);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        LogTool.saveLog(TAG,"dismiss");
    }

    @Override
    public void show() {
        if (!isShowing()){
            startLoadingAnimation();
        }
        LogTool.saveLog(TAG,"isShowing:"+isShowing());
        super.show();
        LogTool.saveLog(TAG,"isShowing:"+isShowing());


    }

    @Override
    public void hide() {
        super.hide();
        LogTool.saveLog(TAG,"hide");
    }

    public void setMessage(String message) {
        messageView.setText(message);
    }
}
