package com.wangxiaobao.gsj.acount;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.wangxiaobao.gsj.base.BaseActivity;
import com.wangxiaobao.gsj.common.CommonUtil;
import com.wangxiaobao.gsj.common.LogTool;
import com.wangxiaobao.gsj.http.RetrofitClient;
import com.wangxiaobao.gsj.http.RetrofitObserver;
import com.wangxiaobao.gsj.user.VerifyCode;
import com.wangxiaobao.gsj.view.StateButton;
import com.wangxiaobao.gsj.view.TitleBar;
import com.wangxiaobao.waiter.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016-04-06.
 */
public class BindPhoneNumberActivity extends BaseActivity {
    private static final String TAG = BindPhoneNumberActivity.class.getSimpleName();
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.phone_number)
    EditText phoneNumber;
    @BindView(R.id.confirm)
    StateButton confirm;
    public static String EXTRA_PHONE_NUMBER = "phone_number";

    @Override
    protected int generateLayoutId() {
        return R.layout.activity_bindle_phone_number_operation;
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
                if (!TextUtils.isEmpty(phoneNumber.getText().toString())) {
                    if (phoneNumber.getText().toString().startsWith("1")) {
                        VerifyCode verifyCode = CommonUtil.getVerifyCode(mContext, phoneNumber.getText().toString());
                        if (verifyCode != null) {
                            LogTool.i(TAG, " CommonUtil.getVerifyCode:" + verifyCode.toString());
                            long countDown = 59 - (System.currentTimeMillis() / 1000 - verifyCode.getTime());
                            LogTool.i(TAG, "countDown:" + countDown);
                            if (countDown > 0) {
                                jumpToVerifyCodeActivity(phoneNumber.getText().toString());
                            } else {
                                sendVerifyCode(phoneNumber.getText().toString());
                            }

                        } else {
                            LogTool.i(TAG, " VerifyCode is null");
                            sendVerifyCode(phoneNumber.getText().toString());
                        }
                    } else {
                        showToast("请正确填写手机号码");
                    }
                } else {
                    showToast("请填写手机号码");
                }
                break;

        }
    }


    private void sendVerifyCode(String mPhoneNumber) {
        LogTool.I(TAG, "sendVerifyCode mPhoneNumber:" + mPhoneNumber);
        Map<String, String> map = new HashMap<>();
        map.put("phone", mPhoneNumber);

        getResponse(RetrofitClient.getInstance().getAccountApi().sendVerifyCode(map)).subscribe(new RetrofitObserver() {
            @Override
            protected void onHandleSuccess(Object o) {
                CommonUtil.saveVerifyCode(mContext, mPhoneNumber);
                jumpToVerifyCodeActivity(mPhoneNumber);
            }
        });

    }

    private void jumpToVerifyCodeActivity(String mPhoneNumber) {
        Intent intent = new Intent(mContext, VerificationCodeActivity.class);
        intent.putExtra(VerificationCodeActivity.EXTRA_INTENT_FLAG, 1);
        intent.putExtra(EXTRA_PHONE_NUMBER, mPhoneNumber);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

}
