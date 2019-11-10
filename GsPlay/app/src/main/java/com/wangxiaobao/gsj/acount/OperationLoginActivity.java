package com.wangxiaobao.gsj.acount;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangxiaobao.gsj.base.MainActivity;
import com.wangxiaobao.gsj.common.AppConfig;
import com.wangxiaobao.gsj.enity.LoginData;
import com.wangxiaobao.gsj.http.RetrofitClient;
import com.wangxiaobao.gsj.http.RetrofitObserver;
import com.wangxiaobao.gsj.view.StateButton;
import com.wangxiaobao.waiter.BuildConfig;
import com.wangxiaobao.waiter.R;
import com.wangxiaobao.gsj.base.App;
import com.wangxiaobao.gsj.base.BaseActivity;
import com.wangxiaobao.gsj.common.CommonUtil;
import com.wangxiaobao.gsj.common.LogTool;
import com.wangxiaobao.gsj.base.LoginActivity;
import com.wangxiaobao.gsj.view.LoginScrollView;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016-04-06.
 */
public class OperationLoginActivity extends BaseActivity {
    private static final String TAG = OperationLoginActivity.class.getSimpleName();
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.iv_seepwd)
    ImageView ivSeepwd;
    @BindView(R.id.operation_account)
    EditText merchantAccount;
    @BindView(R.id.bt_login)
    StateButton btLogin;
    @BindView(R.id.merchant_login)
    TextView merchantLogin;
    @BindView(R.id.sl_center)
    LoginScrollView mCenter;

    @Override
    protected int generateLayoutId() {
        return R.layout.activity_operation;
    }

    @Override
    protected void initView() {
        super.initView();

        merchantAccount.setOnEditorActionListener((v, actionId, event) ->
                (event.getKeyCode() == KeyEvent.KEYCODE_ENTER));

        mCenter.setOnSizeChangedListenner(() ->
                mCenter.post(() -> mCenter.smoothScrollTo(0, 1000)));

        if (BuildConfig.SERVER_MODE != 4) {
            etAccount.setText("WangCaiBaoApi");
            etPwd.setText("wcb1234");
            merchantAccount.setText("WCBBJ");
        }
        loadCacheUserAndPwd();
    }

    private void loadCacheUserAndPwd() {
        String userName = CommonUtil.getPropertyString(mContext, AppConfig.PROXY_USER_NAME);
        String password = CommonUtil.getPropertyString(mContext, AppConfig.PROXY_USER_PASSWORD);
        String merchantName = CommonUtil.getPropertyString(mContext, AppConfig.PROXY_MERCHANT_NAME);

        if (!TextUtils.isEmpty(userName)) {
            etAccount.setText(userName);
            etAccount.setSelection(userName.length());
        }

        if (!TextUtils.isEmpty(merchantName)) {
            merchantAccount.setText(merchantName);
            merchantAccount.setSelection(merchantName.length());
        }
        if (!TextUtils.isEmpty(password)) {
            etPwd.setText(password);
            etPwd.setSelection(password.length());
        } else {
            etPwd.setText("");
        }
    }

    @OnClick({R.id.bt_login, R.id.merchant_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                if (TextUtils.isEmpty(etAccount.getText())) {
                    showToast("请输入运营人员账号");
                    return;
                }
                if (TextUtils.isEmpty(etPwd.getText())) {
                    showToast("请输入密码");
                    return;
                }
                if (TextUtils.isEmpty(merchantAccount.getText())) {
                    showToast("请输入代运营的商家账号");
                    return;
                }
                login(etAccount.getText().toString(), etPwd.getText().toString(), merchantAccount.getText().toString());
                break;


            case R.id.merchant_login:
                startActivity(new Intent(mContext, LoginActivity.class));
                finish();
                break;
        }
    }

    private void login(String account, String pw, String merchantAccount) {
        LogTool.I(TAG, "getVerifyCode");
        Map<String, String> map = new HashMap<>();
        map.put("internalAccount", account);
        map.put("pwd", pw);
        map.put("account", merchantAccount);

        startRequest(RetrofitClient.getInstance().getService().operationLogin(map)).subscribe(new RetrofitObserver<LoginData>() {
            @Override
            protected void onHandleSuccess(LoginData loginData) {
//                checkErpPermission(loginData);
                checkUserPermission(loginData);
            }
        });

    }

    private void checkUserPermission(LoginData entity) {
        if (TextUtils.equals("5", entity.getAdministratorNo()) || TextUtils.equals("6", entity.getAdministratorNo())) {
//            if (BuildConfig.IS_ERP && TextUtils.equals("6", entity.getAdministratorNo())) {
//                hideWaitDialog();
//                showToast("您没有权限玩这个哦！");
//                CommonUtil.sendLoginStatus(mContext, 0);
//            } else {
            loginSuccess(entity);
//            }
        } else {
            showToast("您没有权限玩这个哦！");
            CommonUtil.sendLoginStatus(mContext, 0);
        }
    }

    private void saveUserNameAndPassword() {

        String userName = etAccount.getText().toString();
        String password = etPwd.getText().toString();
        String merchantName = merchantAccount.getText().toString();

        if (!TextUtils.isEmpty(userName)) {
            userName.trim();
            CommonUtil.saveProperty(mContext, AppConfig.PROXY_USER_NAME, userName);
        }
        if (!TextUtils.isEmpty(password)) {
            CommonUtil.saveProperty(mContext, AppConfig.PROXY_USER_PASSWORD, password);
        }
        if (!TextUtils.isEmpty(merchantName)) {
            CommonUtil.saveProperty(mContext, AppConfig.PROXY_MERCHANT_NAME, merchantName);
        }

    }

    private void loginSuccess(LoginData entity) {
        CommonUtil.sendLoginStatus(mContext, 1);
        saveUserNameAndPassword();
        MainActivity.isOperationLigin = true;
        String identify = entity.getIdentity();
        String userId = entity.getUserId();
        String erpMerchantId = entity.getMerchantId();
        CommonUtil.saveProperty(mContext, AppConfig.USER_ID, userId);//save user id
        CommonUtil.saveProperty(mContext, AppConfig.USER_IDENTITY, identify); //用户权限
        CommonUtil.saveProperty(mContext, AppConfig.MERCHANT_NAME, entity.getMerchantName());
        if (!TextUtils.equals(CommonUtil.getMerchantID(mContext), erpMerchantId)) {
            CommonUtil.clearProperty(this, AppConfig.SP_KEY_LOADED_TABLE_INFO_LIST);
        }
        CommonUtil.saveProperty(mContext, AppConfig.MERCHANT_ID, erpMerchantId);
        CommonUtil.saveProperty(mContext, AppConfig.KEY_LOGO_URL, entity.getLogoUrl());
        CommonUtil.saveProperty(mContext, AppConfig.OPERATION_LOGIN_USER_NAME, entity.getAccount());


        startActivity(new Intent(mContext, MainActivity.class));


    }


    private void checkErpPermission(LoginData entity) {
        if (TextUtils.isEmpty(entity.getWhetherERPMerchant())) {
            showToast("您还没有设置门店类型！");
            CommonUtil.sendLoginStatus(mContext, 0);
            return;
        }

        if (TextUtils.equals("2", entity.getWhetherERPMerchant())) {
//            if (BuildConfig.IS_ERP) {
//                showToast("您的账号没有权限，请下载无ERP版本");
//                hideWaitDialog();
//                CommonUtil.sendLoginStatus(mContext, 0);
//            } else {

//            }
        } else {
//            if (BuildConfig.IS_ERP) {
//                checkUserPermission(entity);
//            } else {
            showToast("您的账号没有权限，请下载ERP版本");
            CommonUtil.sendLoginStatus(mContext, 0);
//            }
        }
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
