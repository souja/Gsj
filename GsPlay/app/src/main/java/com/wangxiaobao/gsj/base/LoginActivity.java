package com.wangxiaobao.gsj.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wangxiaobao.gsj.common.AppConfig;
import com.wangxiaobao.gsj.common.LogTool;
import com.wangxiaobao.gsj.enity.LoginData;
import com.wangxiaobao.gsj.http.RetrofitObserver;
import com.wangxiaobao.gsj.http.RetrofitClient;
import com.wangxiaobao.gsj.acount.ForgetPasswordActivity;
import com.wangxiaobao.gsj.acount.OperationLoginActivity;
import com.wangxiaobao.gsj.view.StateButton;
import com.wangxiaobao.waiter.R;
import com.wangxiaobao.gsj.common.CommonUtil;
import com.wangxiaobao.gsj.common.UpdateManager;
import com.wangxiaobao.gsj.enity.VersionEntity;
import com.wangxiaobao.gsj.view.LoginScrollView;
import com.wangxiaobao.gsj.view.RoundTextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016-04-06.
 */
public class LoginActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.iv_seepwd)
    ImageView iv_seePwd;
    @BindView(R.id.bt_login)
    StateButton btLogin;


    @BindView(R.id.no_password_tip)
    RoundTextView mNoPasswordTip;


    @BindView(R.id.sl_center)
    LoginScrollView mCenter;

    public boolean needBindPhoneNumber = false;
    @BindView(R.id.forget_password)
    TextView forgetPassword;
    @BindView(R.id.operation_login)
    TextView operationLogin;

    private int REQUEST_BIND_PHONE_NUMBER_CODE = 123;


    //    wxbzpxmb_zhongguo
//    private String mUserName = "wERPcsmd_guanli";
//    private String mUserName = "dm_a";
    private String mUserName = "csdp_b";

//    private String mUserName = "mzdpdjl_zhongguo";

    private String password = "88888888";

    RetrofitClient baseApiService = new RetrofitClient();


    /**
     * 是否在登录界面
     */
    public static boolean isLogining = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        SplashPicManager.getInstance(getApplicationContext()).showSplashActivity();
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.primary_color));
        }

        LogTool.saveLog(TAG, "onCreate");
        isLogining = true;
//        checkNewVersion();


    }

    @Override
    protected int generateLayoutId() {
        return R.layout.activity_login;
    }


    @Override
    protected void initData() {
        super.initData();
    }

    @OnClick({R.id.forget_password, R.id.operation_login, R.id.bt_login, R.id.iv_seepwd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_seepwd:
                if (iv_seePwd.isSelected()) {

                    iv_seePwd.setSelected(false);
                    etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    snapEnd();
                } else {
                    iv_seePwd.setSelected(true);
                    etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    snapEnd();
                }
                break;
            case R.id.bt_login:
                login();
                break;
            case R.id.forget_password:
                startActivity(new Intent(mContext, ForgetPasswordActivity.class));
                break;
            case R.id.operation_login:
                startActivity(new Intent(mContext, OperationLoginActivity.class));
                finish();
                break;
        }
    }


    public void checkNewVersion() {

        LogTool.saveLog(TAG, "checkNewVersion");

        startRequest(mApi.checkNewVersion(getPackageName()))
                .subscribe(new RetrofitObserver<VersionEntity>() {
                    @Override
                    protected void onHandleSuccess(VersionEntity versionEntity) {
                        if (versionEntity != null && !TextUtils.isEmpty(versionEntity.getUrl()) && CommonUtil.compareVersion(versionEntity.getAppVersion(), CommonUtil.getVersionName(mContext)) == 1) {
                            LogTool.saveLog(TAG, "checkNewVersion onResponse versionEntity->" + versionEntity.toString());
                            UpdateManager updateManager = new UpdateManager(mContext, versionEntity);
                            updateManager.showUpdateDialog();
                        } else {
                            LogTool.saveLog(TAG, "No new version");
                        }
                    }
                });

    }


    private void login() {

        if (TextUtils.isEmpty(etAccount.getText().toString().trim())) {
            showToast("用户名不能为空");
            return;
        }

        if (TextUtils.isEmpty(etPwd.getText().toString().trim())) {
            mNoPasswordTip.setVisibility(View.VISIBLE);
            return;
        }
        if (etPwd.getText().length() < 6 || etPwd.getText().length() > 20) {
            showToast("密码长度只能为6-20位");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("account", etAccount.getText().toString().trim());
        map.put("password", etPwd.getText().toString().trim());


        startRequest(mApi.login(map)).subscribe(new RetrofitObserver<LoginData>() {
            @Override
            protected void onHandleSuccess(LoginData loginData) {
                LogTool.saveLog(TAG, "登录成功");
//                checkErpPermission(loginData);
                checkUserPermission(loginData);

            }
        });


    }


    private void loginSuccess(LoginData entity) {

        MobclickAgent.onProfileSignIn(CommonUtil.getUserName(mContext));
        String userName = etAccount.getText().toString().trim();
        String identify = entity.getIdentity();
        String userId = entity.getUserId();
        String erpMerchantId = entity.getMerchantId();
        saveUserNameAndPassword();
        CommonUtil.saveProperty(LoginActivity.this, AppConfig.USER_ID, userId);//save user id
        CommonUtil.saveProperty(LoginActivity.this, AppConfig.USER_IDENTITY, identify); //用户权限
        CommonUtil.saveProperty(LoginActivity.this, AppConfig.MERCHANT_NAME, entity.getMerchantName());
        if (!TextUtils.equals(CommonUtil.getMerchantID(mContext), erpMerchantId)) {
            CommonUtil.clearProperty(this, AppConfig.SP_KEY_LOADED_TABLE_INFO_LIST);
        }
        CommonUtil.saveProperty(LoginActivity.this, AppConfig.MERCHANT_ID, erpMerchantId);
        CommonUtil.saveProperty(LoginActivity.this, AppConfig.KEY_LOGO_URL, entity.getLogoUrl());
        CommonUtil.saveProperty(LoginActivity.this, AppConfig.PREFERENCE_ACCOUNT, entity.getAccount());


        CommonUtil.saveProperty(LoginActivity.this, AppConfig.PREFERENCE_MERCHANT_ACCOUNT, entity.getMerchantAccount());


        LogTool.E(TAG, "Save UserName:" + userName + ",identify:" + identify + ",erpMerchantId:" + erpMerchantId);
        needBindPhoneNumber = TextUtils.isEmpty(entity.getIsPhone()) && TextUtils.equals("5", entity.getAdministratorNo());
        if (!TextUtils.isEmpty(entity.getIsPhone())) {
            CommonUtil.saveProperty(LoginActivity.this, AppConfig.PREFERENCE_PHONE_NUMBER, entity.getIsPhone());
        }
        MainActivity.isOperationLigin = false;
        if (AppConfig.IS_RELEASE_MODE) {
            Map<String, String> map = new HashMap<>();
            map.put("UserName", CommonUtil.getUserName(mContext));
            MobclickAgent.onEvent(mContext, "UserLogin", map);
        }



        isLogining = false;
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();

//        loadStoreInfo(entity);


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
        }
    }


    private void saveUserNameAndPassword() {

        String userName = etAccount.getText().toString();
        String password = etPwd.getText().toString();

        if (!TextUtils.isEmpty(userName)) {
            userName.trim();
            CommonUtil.saveProperty(mContext, AppConfig.USER_NAME, userName);
        }

        if (!TextUtils.isEmpty(password)) {
            CommonUtil.saveProperty(mContext, AppConfig.USER_PASSWORD, password);
        }


    }

    @Override
    protected void initView() {
        super.initView();
        LogTool.I(TAG, "task id：" + getTaskId());
        Log.i(TAG, "LogTool.DEBUG:" + LogTool.DEBUG);
        if (!AppConfig.IS_RELEASE_MODE) {
            etAccount.setText(mUserName);
            etPwd.setText(password);
        }


        mCenter.setOnSizeChangedListenner(new LoginScrollView.OnSizChangeListener() {
            @Override
            public void onSizeChanged() {

                mCenter.post(new Runnable() {
                    @Override
                    public void run() {
                        mCenter.smoothScrollTo(0, 1000);
                    }
                });
            }
        });

        loadCacheUserAndPwd();


        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    mNoPasswordTip.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


//        UpdateManager updateManager = new UpdateManager();
//        updateManager.installSlient("/sdcard/aa.apk");


    }

    private void loadCacheUserAndPwd() {
        String userName = CommonUtil.getPropertyString(mContext, AppConfig.USER_NAME);
        String password = CommonUtil.getPropertyString(mContext, AppConfig.USER_PASSWORD);


        if (!TextUtils.isEmpty(userName)) {
            etAccount.setText(userName);
            etAccount.setSelection(userName.length());
        }
        if (!TextUtils.isEmpty(password)) {
            etPwd.setText(password);
            etPwd.setSelection(password.length());
        } else {
            etPwd.setText("");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void snapEnd() {
        if (!TextUtils.isEmpty(etPwd.getText().toString())) {
            etPwd.setSelection(etPwd.getText().length());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isLogining = false;

        LogTool.saveLog(TAG, "onDestroy");
    }
}
