package com.wangxiaobao.gsj.acount;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wangxiaobao.gsj.common.LogTool;
import com.wangxiaobao.gsj.http.RetrofitClient;
import com.wangxiaobao.gsj.http.RetrofitObserver;
import com.wangxiaobao.gsj.view.StateButton;
import com.wangxiaobao.waiter.R;
import com.wangxiaobao.gsj.base.BaseActivity;
import com.wangxiaobao.gsj.common.CommonUtil;
import com.wangxiaobao.gsj.user.VerifyCode;
import com.wangxiaobao.gsj.view.TitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016-04-06.
 */
public class ForgetPasswordActivity extends BaseActivity {
    private static final String TAG = ForgetPasswordActivity.class.getSimpleName();
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.account_content)
    EditText etAccount;
    @BindView(R.id.confirm)
    StateButton confirm;
    public static String EXTRA_PHONE_NUMBER = "phone_number";
    public static String EXTRA_ACCOUNT = "account";

    @Override
    protected int generateLayoutId() {
        return R.layout.activity_forget_phone_number;
    }

    @Override
    protected void initView() {
        super.initView();
        etAccount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        titleBar.setBgColor(R.color.new_primary_black_color);
    }


    @OnClick(R.id.confirm)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                if (!TextUtils.isEmpty(etAccount.getText().toString())) {
                    getBindPhoneNumber();
                } else {
                    showToast("请输入账号");
                }

                break;

        }
    }


    private void getBindPhoneNumber() {
        LogTool.I(TAG, "getBindPhoneNumber");

        if (TextUtils.isEmpty(etAccount.getText())) {
            showToast("请输入账号");
            return;
        }
        String userName = etAccount.getText().toString().trim();
        Map<String, String> map = new HashMap<>();
        map.put("account", userName);


       startRequest( RetrofitClient.getInstance().getAccountApi().getBindPhoneNumber(map))
               .subscribe(new RetrofitObserver<Phone>() {
                   @Override
                   protected void onHandleSuccess(Phone entity) {
                       VerifyCode verifyCode = CommonUtil.getVerifyCode(mContext, entity.getCellPhone());
                       if (verifyCode != null) {
                           LogTool.i(TAG, " CommonUtil.getVerifyCode:" + verifyCode.toString());
                           long countDown = 59 - (System.currentTimeMillis() / 1000 - verifyCode.getTime());
                           LogTool.i(TAG, "countDown:" + countDown);
                           if (countDown > 0) {
                               jumpToVerifyCodeActivity(entity.getCellPhone(), userName);
                           } else {
                               sendVerifyCode(entity.getCellPhone(), userName);
                           }

                       } else {
                           LogTool.i(TAG, " VerifyCode is null");
                           sendVerifyCode(entity.getCellPhone(), userName);
                       }

                   }
               });


    }

    private void sendVerifyCode(String mPhoneNumber, String userName) {
        LogTool.I(TAG, "sendVerifyCode");
        Map<String, String> map = new HashMap<>();
        map.put("phone", mPhoneNumber);


        getResponse(RetrofitClient.getInstance().getAccountApi().sendVerifyCode(map)).subscribe(new RetrofitObserver() {
            @Override
            protected void onHandleSuccess(Object o) {
                CommonUtil.saveVerifyCode(mContext, mPhoneNumber);
                jumpToVerifyCodeActivity(mPhoneNumber, userName);
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

    private void jumpToVerifyCodeActivity(String mPhoneNumber, String userName) {
        Intent intent = new Intent(mContext, VerificationCodeActivity.class);
        intent.putExtra(VerificationCodeActivity.EXTRA_INTENT_FLAG, 2);
        intent.putExtra(EXTRA_PHONE_NUMBER, mPhoneNumber);
        intent.putExtra(EXTRA_ACCOUNT, userName);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }


}
