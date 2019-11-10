package com.wangxiaobao.gsj.acount;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.wangxiaobao.gsj.base.BaseActivity;
import com.wangxiaobao.gsj.common.LogTool;
import com.wangxiaobao.gsj.http.RetrofitClient;
import com.wangxiaobao.gsj.http.RetrofitObserver;
import com.wangxiaobao.waiter.R;
import com.wangxiaobao.gsj.view.TitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016-04-06.
 */
public class ResetPasswordActivity extends BaseActivity {
    private static final String TAG = ResetPasswordActivity.class.getSimpleName();
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.confirm_pw)
    EditText confirmPw;


    @Override
    protected int generateLayoutId() {
        return R.layout.activity_reset_password;
    }

    @Override
    protected void initView() {
        super.initView();
        titleBar.findViewById(R.id.back).setVisibility(View.GONE);
        titleBar.setBgColor(R.color.new_primary_black_color);
    }


    @OnClick(R.id.confirm)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                if (TextUtils.isEmpty(password.getText())) {
                    showToast("请填写新密码");
                    return;
                }
                if (TextUtils.isEmpty(confirmPw.getText())) {
                    showToast("请填写确认密码");
                    return;
                }

                if (confirmPw.getText().length() < 6 || confirmPw.getText().length() > 15) {
                    showToast("请输入6-15位新密码");
                    return;
                }
                if (!TextUtils.equals(password.getText(), confirmPw.getText())) {
                    showToast("两次密码输入不一致，请重新输入");
                    return;
                }
                resetPassword();

                break;

        }


    }

    private void resetPassword() {
        LogTool.I(TAG, "getVerifyCode");
        Map<String, String> map = new HashMap<>();
        map.put("account", getIntent().getStringExtra(ForgetPasswordActivity.EXTRA_ACCOUNT));
        map.put("pwd", password.getText().toString());
        map.put("phone", getIntent().getStringExtra(ForgetPasswordActivity.EXTRA_PHONE_NUMBER));

        getResponse(RetrofitClient.getInstance().getAccountApi().resetPassword(map)).subscribe(new RetrofitObserver() {
            @Override
            protected void onHandleSuccess(Object o) {
                showToast("充值密码成功");
                Intent intent = new Intent(mContext, BindPhoneSuccess.class);
                intent.putExtra(VerificationCodeActivity.EXTRA_INTENT_FLAG, 2);
                startActivity(intent);
                sendBroadcast(new Intent(BaseActivity.INIENT_FINISH));
            }

        });
//        HttpUtil.with(mContext)
//                .post()
//                .setErrorTip("密码输入有误")
//                .setParams(map)
//                .setType(new TypeToken<JsonResult<BaseResult>>() {
//                }.getType())
//                .load(Constant.ACTION_RESET_PASSWORD, new HttpUtil.EntityCallback<BaseResult>() {
//                    @Override
//                    public void onSuccess(BaseResult entity) {
//                        hideWaitDialog();
//
//
//
//                    }
//
//
//                    @Override
//                    public void onError(Exception e) {
//                        LogTool.E(TAG, "checkVerifyCode", e, false);
//                        hideWaitDialog();
//
//                    }
//
//                    @Override
//                    public void onException() {
//                        hideWaitDialog();
//                    }
//                });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

}
