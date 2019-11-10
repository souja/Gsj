package com.wangxiaobao.gsj.acount;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.wangxiaobao.gsj.base.MainActivity;
import com.wangxiaobao.gsj.common.LogTool;
import com.wangxiaobao.waiter.R;
import com.wangxiaobao.gsj.base.BaseActivity;
import com.wangxiaobao.gsj.base.LoginActivity;
import com.wangxiaobao.gsj.view.TitleBar;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016-04-06.
 */
public class BindPhoneSuccess extends BaseActivity {
    private static final String TAG = BindPhoneSuccess.class.getSimpleName();
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.re_get_verification_code_time)
    TextView reGetVerificationCodeTime;
    @BindView(R.id.content_flag)
    TextView contentFlag;
    private int time = 3;
    private int mFlag = 1;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                LogTool.i(TAG, "time:" + time);
                if (time != 0) {
                    reGetVerificationCodeTime.setText("（" + time-- + "秒后自动跳转）");
                    mHandler.sendEmptyMessageDelayed(1, 1000);
                } else {
                    jump();
                }

            }

        }
    };


    @Override
    protected int generateLayoutId() {
        return R.layout.activity_password_set_success;
    }

    @Override
    protected void initView() {
        super.initView();
        titleBar.setBgColor(R.color.new_primary_black_color);
        titleBar.findViewById(R.id.back).setVisibility(View.GONE);
        mFlag = getIntent().getIntExtra(VerificationCodeActivity.EXTRA_INTENT_FLAG, 1);
        if (mFlag == 1) {
            titleBar.setTitle("绑定成功");
            contentFlag.setText("手机绑定成功");
        } else {
            titleBar.setTitle("密码重设成功");
            contentFlag.setText("密码重设成功");
        }
        mHandler.sendEmptyMessage(1);
    }


    @OnClick(R.id.confirm)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                jump();
                break;

        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private void jump() {
        mHandler.removeCallbacksAndMessages(null);
        if (mFlag == 1) {
            startActivity(new Intent(mContext, MainActivity.class));
        } else {
            startActivity(new Intent(mContext, LoginActivity.class));
        }

        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

}
