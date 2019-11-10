package com.wangxiaobao.gsj.acount;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wangxiaobao.gsj.common.AppConfig;
import com.wangxiaobao.gsj.http.RetrofitClient;
import com.wangxiaobao.gsj.http.RetrofitObserver;
import com.wangxiaobao.gsj.view.StateButton;
import com.wangxiaobao.waiter.R;
import com.wangxiaobao.gsj.base.BaseActivity;
import com.wangxiaobao.gsj.common.CommonUtil;
import com.wangxiaobao.gsj.common.LogTool;
import com.wangxiaobao.gsj.view.TitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016-04-06.
 */
public class VerificationCodeActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = VerificationCodeActivity.class.getSimpleName();
    @BindView(R.id.phone_number)
    TextView phoneNumber;
    @BindView(R.id.verification_code_content)
    AppCompatEditText verificationCodeContent;
    @BindView(R.id.get_verification_code)
    TextView getVerificationCode;
    @BindView(R.id.next)
    StateButton next;
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.forget_phone_number)
    TextView forgetPhoneNumber;
    @BindView(R.id.layout_forget_pw)
    LinearLayout layoutForgetPw;
    @BindView(R.id.layout_forget_phone_number)
    LinearLayout layoutForgetPhoneNumber;
    private String mPhoneNumber;
    private int mFlag = 1;
    public static final String PREFERENCE_COUNT_DOWN = "count_down";
    public static final String EXTRA_INTENT_FLAG = "intent_flag";






    @Override
    protected int generateLayoutId() {
        return R.layout.activity_verification_phone_number;
    }

    @Override
    protected void initView() {
        super.initView();
        mFlag = getIntent().getIntExtra(EXTRA_INTENT_FLAG, 1);
        mPhoneNumber = getIntent().getStringExtra(BindPhoneNumberActivity.EXTRA_PHONE_NUMBER);
        if (mFlag == 1) {
            layoutForgetPw.setVisibility(View.GONE);
            phoneNumber.setVisibility(View.VISIBLE);
            phoneNumber.setText("请输入" + parsePhoneNumber(mPhoneNumber) + "收到的短信验证码");
        } else {
            phoneNumber.setVisibility(View.GONE);
            layoutForgetPhoneNumber.setVisibility(View.GONE);
            layoutForgetPw.setVisibility(View.VISIBLE);
            forgetPhoneNumber.setText(parsePhoneNumber(mPhoneNumber));
        }

        updatePhoneNumberStatus();

        verificationCodeContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6) {
                    enableNext();
                } else {
                    disableNext();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        titleBar.setBgColor(R.color.new_primary_black_color);
        disableNext();

        mHandler.sendEmptyMessage(1);
        if (mFlag == 2) {
            layoutForgetPhoneNumber.setVisibility(View.VISIBLE);
        }

    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                updatePhoneNumberStatus();
            }

        }
    };

    private void updatePhoneNumberStatus() {
        long countDown = 59 - (System.currentTimeMillis() / 1000 - CommonUtil.getVerifyCode(mContext,mPhoneNumber).getTime());
        if (countDown > 0) {
            getVerificationCode.setText("重新获取(" + countDown + "秒)");
            getVerificationCode.setTextColor(getResources().getColor(R.color.pinkish_grey));
            mHandler.sendEmptyMessageDelayed(1, 1 * 1000);
            getVerificationCode.setOnClickListener(null);
            if (mFlag == 2) {
                layoutForgetPhoneNumber.setVisibility(View.VISIBLE);
            }
        } else {
            getVerificationCode.setOnClickListener(VerificationCodeActivity.this);
            getVerificationCode.setText("发送验证码");
            getVerificationCode.setTextColor(getResources().getColor(R.color.primary_color));
            getVerificationCode.setOnClickListener(VerificationCodeActivity.this);
        }
    }


    private void updatePhoneNumber(String phoneNumber, String yzm) {
        LogTool.I(TAG, "sendVerifyCode");
        Map<String, String> map = new HashMap<>();
        map.put("yzm", yzm);
        map.put("account", CommonUtil.getPropertyString(mContext, AppConfig.PREFERENCE_ACCOUNT));
        map.put("phone", phoneNumber);


        getResponse(RetrofitClient.getInstance().getAccountApi().updatePhoneNumber(map)).subscribe(new RetrofitObserver() {
            @Override
            protected void onHandleSuccess(Object o) {
                Intent intent = new Intent(mContext, BindPhoneSuccess.class);
                intent.putExtra(EXTRA_INTENT_FLAG, mFlag);
                startActivity(intent);
                CommonUtil.saveProperty(mContext, AppConfig.PREFERENCE_PHONE_NUMBER, mPhoneNumber);
                sendBroadcast(new Intent(BaseActivity.INIENT_FINISH));
            }
        });


//        HttpUtil.with(mContext)
//                .get()
//                .setParams(map)
//                .setType(new TypeToken<JsonResult<BaseResult>>() {
//                }.getType())
//                .load(Constant.ACTION_BIND_PHONE, new HttpUtil.EntityCallback<BaseResult>() {
//                    @Override
//                    public void onSuccess(BaseResult entity) {
//                        hideWaitDialog();
//
//
//                    }
//
//
//                    @Override
//                    public void onError(Exception e) {
//                        LogTool.E(TAG, "updatePhoneNumber", e, false);
//                        hideWaitDialog();
//
//                    }
//
//                    @Override
//                    public void onException() {
//                        hideWaitDialog();
////                        Intent intent = new Intent(mContext, BindPhoneSuccess.class);
////                        intent.putExtra(EXTRA_INTENT_FLAG, 1);
////                        startActivity(intent);
////                        sendBroadcast(new Intent(BaseActivity.INIENT_FINISH));
//                    }
//                });


    }


    private void checkVerifyCode(String yzm) {
        LogTool.I(TAG, "sendVerifyCode");
        Map<String, String> map = new HashMap<>();
        map.put("yzm", yzm);
        map.put("phone", mPhoneNumber);

        getResponse(RetrofitClient.getInstance().getAccountApi().checkVerifyCode(map)).subscribe(new RetrofitObserver() {
            @Override
            protected void onHandleSuccess(Object o) {
                Intent intent = new Intent(mContext, ResetPasswordActivity.class);
                intent.putExtra(ForgetPasswordActivity.EXTRA_PHONE_NUMBER, mPhoneNumber);
                intent.putExtra(ForgetPasswordActivity.EXTRA_ACCOUNT, getIntent().getStringExtra(ForgetPasswordActivity.EXTRA_ACCOUNT));
                startActivity(intent);
            }
        });

//        HttpUtil.with(mContext)
//                .get()
//                .setParams(map)
//                .setType(new TypeToken<JsonResult<BaseResult>>() {
//                }.getType())
//                .load(Constant.ACTION_CHECK_VERIFY_CODE, new HttpUtil.EntityCallback<BaseResult>() {
//                    @Override
//                    public void onSuccess(BaseResult entity) {
//                        hideWaitDialog();
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
//
//                    }
//
//                    @Override
//                    public void onException() {
//                        hideWaitDialog();
//                    }
//                });


    }


    private void sendVerifyCode() {
        LogTool.saveLog(TAG, "sendVerifyCode");
        Map<String, String> map = new HashMap<>();
        map.put("phone", mPhoneNumber);

        getResponse(RetrofitClient.getInstance().getAccountApi().sendVerifyCode(map)).subscribe(new RetrofitObserver() {
            @Override
            protected void onHandleSuccess(Object o) {
                showToast("获取验证码成功");
                CommonUtil.saveVerifyCode(mContext,mPhoneNumber);
                mHandler.sendEmptyMessage(1);
            }
        });

//        HttpUtil.with(mContext)
//                .get()
//                .setParams(map)
//                .setType(new TypeToken<JsonResult<BaseResult>>() {
//                }.getType())
//                .load(Constant.ACTION_GET_VERIFY_CODE, new HttpUtil.EntityCallback<BaseResult>() {
//                    @Override
//                    public void onSuccess(BaseResult entity) {
//                        hideWaitDialog();
//
//                    }
//
//
//                    @Override
//                    public void onError(Exception e) {
//                        LogTool.E(TAG, "loadMerchant", e, false);
//                        hideWaitDialog();
//                    }
//
//                    @Override
//                    public void onException() {
//                        hideWaitDialog();
//                    }
//                });


    }


    private void disableNext() {
        next.setOnClickListener(null);
        next.setNormalBackgroundColor(getResources().getColor(R.color.pinkish_grey));
        next.setPressedBackgroundColor(getResources().getColor(R.color.pinkish_grey));
        next.setPressedTextColor(getResources().getColor(R.color.white));
    }

    private void enableNext() {
        next.setOnClickListener(this);
        next.setNormalBackgroundColor(getResources().getColor(R.color.primary_color));
        next.setPressedBackgroundColor(getResources().getColor(R.color.primary_text_color));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    private String parsePhoneNumber(String pNumber) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(pNumber) && pNumber.length() > 6) {
            for (int i = 0; i < pNumber.length(); i++) {
                char c = pNumber.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }


    @OnClick({R.id.next, R.id.get_verification_code})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:
                if (TextUtils.isEmpty(verificationCodeContent.getText())) {
                    showToast("请输入验证码");
                    return;
                }
                if (mFlag == 2) {
                    checkVerifyCode(verificationCodeContent.getText().toString());
                } else {
                    updatePhoneNumber(mPhoneNumber, verificationCodeContent.getText().toString());
                }

                break;
            case R.id.get_verification_code:

                sendVerifyCode();
                break;

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

}
