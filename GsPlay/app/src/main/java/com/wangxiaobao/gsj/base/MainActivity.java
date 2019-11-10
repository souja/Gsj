package com.wangxiaobao.gsj.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.wangxiaobao.gsj.common.AppConfig;
import com.wangxiaobao.gsj.common.AppConstants;
import com.wangxiaobao.gsj.common.CommonUtil;
import com.wangxiaobao.gsj.common.LogTool;
import com.wangxiaobao.gsj.common.ScreenUtil;
import com.wangxiaobao.gsj.enity.MapMerchantModel;
import com.wangxiaobao.gsj.enity.TradeAreaModel;
import com.wangxiaobao.gsj.enity.result.JsonListResult;
import com.wangxiaobao.gsj.home.ApiManager;
import com.wangxiaobao.gsj.home.ComplainListFragment;
import com.wangxiaobao.gsj.home.NewHomeFragment;
import com.wangxiaobao.gsj.http.RetrofitClient;
import com.wangxiaobao.gsj.log.JsonUtil;
import com.wangxiaobao.gsj.module.comment.NewCommentListFragment;
import com.wangxiaobao.gsj.module.introduce.MapFragment;
import com.wangxiaobao.gsj.module.introduce.MerchantIntroduceFragment;
import com.wangxiaobao.gsj.util.AudioManager;
import com.wangxiaobao.gsj.view.BottomMenuItem;
import com.wangxiaobao.gsj.view.dialog.CommitSuccessDialog;
import com.wangxiaobao.waiter.R;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

//    private AdviseListFragment mAdviseListFragment;
//    private AdviseDetailFragment mAdviseDetailFragment;
//    private CommentDetailFragment mCommentDetailFragment;
//    private ComplainDetailFragment mComplainDetailFragment;
//    private TradeAreaModel commentAreaSelected;

//    public static final String MESSAGE_COUNT_CHANGED = "com.wangxiaobao.waiter.MESSAGE_COUNT_CHANGED";

    @BindView(R.id.english_title)
    TextView mEnglishText;
    @BindView(R.id.home)
    BottomMenuItem mHomeMenu;
    @BindView(R.id.layout_sjx)
    ViewGroup mSjxLayout;
    @BindView(R.id.actionbar_title)
    TextView mTitle;
    @BindView(R.id.title_bar)
    RelativeLayout mTibleBar;
    @BindView(R.id.bottom_bar)
    LinearLayout mBottomBar;

    /**
     * 添加评价
     */
    public static final int EXTRA_COMMIT_COMMENT = 1004;
    public static String merchantId;
    public static String userId;

    private NewHomeFragment mHomeFragment;
    private NewCommentListFragment mCommentListFragment;
    private ComplainListFragment mComplainListFragment;

    private List<TradeAreaModel> areaList;

    public static boolean isUseTemplatePage;

    public static boolean isOperationLigin;

    public static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 27;
    public int mCurrentMenuId = -1;

    public BottomMenuManager mBottomMenuManager;

    public static boolean MainActivityOnCreated = false;
    public Uri beforeCropUri;  //裁剪前的URI；
    public Uri afterCropUri; //裁剪后的URI；

    private Disposable disposable;

    public static final int DISABLE_EXPAND = 0x00010000;//4.2以上的整形标识
    public static final int DISABLE_EXPAND_LOW = 0x00000001;//4.2以下的整形标识

    @Override
    protected int generateLayoutId() {
        return R.layout.activity_main;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            LogTool.saveLog("状态信息是空的");
        } else {
            LogTool.saveLog("状态信息不是空的");
        }
        /**
         * 如果正在登录直接结束
         */
//        if (LoginActivity.isLogining) {
//            finish();
//            return;
//        }
        //        无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        LogTool.i(TAG, "==================onCreate====================");
        ScreenUtil.setScale(this);

        merchantId = CommonUtil.getMerchantID(mContext);
//        if (TextUtils.isEmpty(merchantId)) {
//            mContext.startActivity(new Intent(mContext, LoginActivity.class));
//            LogTool.saveLog("merchant account 是空的");
//            finish();
//            return;
//        } else {
//            LogTool.saveLog("merchant account 不是空的");
//        }

        MainActivityOnCreated = true;


        userId = CommonUtil.getUserID(mContext);
        mBottomMenuManager = new BottomMenuManager();

        for (int i = 0; i < mBottomBar.getChildCount(); i++) {
            BottomMenuItem bottomMenuItem = (BottomMenuItem) mBottomBar.getChildAt(i);
            mBottomMenuManager.addChild(bottomMenuItem);
        }

        showDefaultFragment();
        initCropUri();
        hideNavigationbar();

        Observable.interval(0, 10, TimeUnit.MINUTES)
                .flatMap((Function<Long, ObservableSource<JsonListResult<TradeAreaModel>>>) aLong ->
                        RetrofitClient.getInstance().getTradingArea().subscribeOn(Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .subscribe(jsonResult -> {
                    if (jsonResult == null) {
                        return;
                    }
                    areaList = jsonResult.getData();
                    LogTool.saveLog("area list==>" + areaList.toString());

                    CommonUtil.saveProperty(App.getContext(), AppConstants.CONS_TRADING_AREA,
                            JsonUtil.objectToJson(areaList));
                }, throwable -> Log.e("SONGJIE", "e=====>" + throwable.getMessage()));

    }

    @Override
    public void sizeView(View view) {
//        ScreenUtil.initScale(view);
    }

    @Override
    protected void initView() {
        super.initView();

//        initHomeMenu();

        disableStatusBar();

        showHideFunction();

        hideNavigationbar();

        CrashReport.removeUserData(App.getContext(), "merchant");
        CrashReport.putUserData(App.getContext(), "merchant", CommonUtil.getMerchantName(App.getContext()));

        if (CommonUtil.getPropertyBoolean(this, "Crash")) {
            ApiManager.getInstance().reportCrashMessage();
            CommonUtil.saveProperty(this, "Crash", false);
        }

        checkIsOperated();
    }

    @OnClick(R.id.honor)
    public void showHonorFragment() {
        if (mCurrentMenuId == R.id.honor) {
            return;
        }
        MapFragment honorFragment = MapFragment.Companion.newInstance(MapFragment.TYPE_TO_SHOP_INTRODUCE);

//        setTitleActionVisibility(View.GONE);
        mTibleBar.setVisibility(View.GONE);
        mCurrentMenuId = R.id.honor;
        mBottomMenuManager.setSelectedId(mCurrentMenuId);
        showFragment(honorFragment);
        showSjx(R.id.honor_sjx);
    }

    @OnClick(R.id.comment)
    public void showCommentFragment() {
        if (mCurrentMenuId == R.id.comment) {
            return;
        }

//        if (commentAreaSelected == null) {
//            commentShopSelectFragment = MapFragment.Companion.newInstance(MapFragment.TYPE_TO_COMMENT);
//            showFragment(commentShopSelectFragment);
//            mTibleBar.setVisibility(View.GONE);
//        } else {
        mCommentListFragment = new NewCommentListFragment();

        showFragment(mCommentListFragment);

        setTitleActionVisibility(View.GONE);
        mCurrentMenuId = R.id.comment;
        showSjx(R.id.comment_sjx);
        mBottomMenuManager.setSelectedId(mCurrentMenuId);
    }

    public void showFragment(Fragment fragment) {
        App.mApp.lastOperateTime = System.currentTimeMillis();
        if (AudioManager.INSTANCE.isPlaying()) {
            AudioManager.INSTANCE.terminatePlay();
        }

        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null) {
            LogTool.saveLog(TAG, "fragment size:" + fragmentList.size());
        } else {
            LogTool.saveLog(TAG, "fragment size 0");
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        if (mCurrentFragment == mHomeFragment) {
//            fragmentTransaction.add(R.id.fragment_container, fragment, fragment.getClass().getSimpleName());
//            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
//            fragmentTransaction.commitAllowingStateLoss();
//        } else {
//            fragmentTransaction.remove(mCurrentFragment);
//        getSupportFragmentManager().popBackStackImmediate();
        fragmentTransaction.add(R.id.fragment_container, fragment, fragment.getClass().getSimpleName());
//        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        fragmentTransaction.commitAllowingStateLoss();
//        fragmentTransaction.commit();
//        }

        List<Fragment> fragmentListA = getSupportFragmentManager().getFragments();
        if (fragmentListA != null) {
            LogTool.saveLog(TAG, "fragment size:" + fragmentListA.size());
        } else {
            LogTool.saveLog(TAG, "fragment size 0");
        }
        mHomeFragment = null;
    }

    /**
     * 显示投诉列表界面
     */
    @OnClick(R.id.complain)
    public void showComplainListFragment() {
        if (mCurrentMenuId == R.id.complain) {
            return;
        }
        App.mApp.isLoadMerchantList = false;
        mComplainListFragment = new ComplainListFragment();

        setTitleActionVisibility(View.GONE);
        mCurrentMenuId = R.id.complain;
        mBottomMenuManager.setSelectedId(mCurrentMenuId);
        showFragment(mComplainListFragment);
        showSjx(R.id.complain_sjx);
    }

    /**
     * 投诉成功后显示列表
     */
    public void showComplainListFragment_suc(MapMerchantModel mapMerchantModel) {
        mComplainListFragment = ComplainListFragment.newInstance(mapMerchantModel);

        setTitleActionVisibility(View.GONE);
        mCurrentMenuId = R.id.complain;
        mBottomMenuManager.setSelectedId(mCurrentMenuId);
        showFragment(mComplainListFragment);
    }

    /**
     * 显示首页界面
     */
    @OnClick(R.id.home)
    public void showHomeFragment() {
        if (mCurrentMenuId == R.id.home) {
            return;
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        if (mHomeFragment == null) {
        mHomeFragment = NewHomeFragment.Companion.newInstance("");
        String tag = mHomeFragment.getClass().getSimpleName();
        fragmentTransaction.replace(R.id.fragment_container, mHomeFragment, tag);
//            fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
//        } else {
//            fragmentTransaction.remove(mCurrentFragment);
//            mHomeFragment.showContentView();
//            getSupportFragmentManager().popBackStackImmediate();
//        }
//        setTitleActionVisibility(View.GONE);
        mTibleBar.setVisibility(View.GONE);
        mCurrentMenuId = R.id.home;
        mBottomMenuManager.setSelectedId(R.id.home);
        showSjx(R.id.home_sjx);

        List<Fragment> fragmentListA = getSupportFragmentManager().getFragments();
        if (fragmentListA != null) {
            LogTool.saveLog(TAG, "fragment size:" + fragmentListA.size());
        } else {
            LogTool.saveLog(TAG, "fragment size 0");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                showToast("获取存储卡权限失败");
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(mContext);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(mContext);
        CommonUtil.sendCountEvent(mContext, "MainActivityOnResume");
        Settings.System.putInt(getContentResolver(), "status_bar_disabled", 1);
    }

    private void initCropUri() {
        File fileRoot = new File(FileTool.FILE_PATH);
        if (!fileRoot.exists()) {
            LogTool.E("App file does not exist,make...");
            fileRoot.mkdirs();
        }
        File beforeFile = new File(FileTool.IMG_BEFORE_CROP);
        File cropFile = new File(FileTool.IMG_AFTER_CROP);
        beforeCropUri = Uri.fromFile(beforeFile);
        afterCropUri = Uri.fromFile(cropFile);
    }

    private void checkIsOperated() {
        App.mApp.lastOperateTime = System.currentTimeMillis();
        disposable = Observable.interval(5, TimeUnit.MINUTES)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    long currentTime = System.currentTimeMillis();
                    long diff = currentTime - App.mApp.lastOperateTime;
                    Log.e("SONGJIE", "====>dispose=====" + diff);
                    if (diff > 5 * 60 * 1000) {
                        LogTool.saveLog("10分钟没有操作，回到欢迎页面");
                        startActivity(new Intent(MainActivity.this, SplashActivity.class));
                    }
                }, throwable -> {

                });
    }

    /**
     * 点击TitleBar5次进入隐藏调试界面
     */
    private void showHideFunction() {
        mTibleBar.setOnClickListener(new View.OnClickListener() {
            final static int COUNTS = 5;//点击次数
            final static long DURATION = 3 * 1000;//规定有效时间
            long[] mHits = new long[COUNTS];

            @Override
            public void onClick(View v) {
                /**
                 * 实现双击方法
                 * src 拷贝的源数组
                 * srcPos 从源数组的那个位置开始拷贝.
                 * dst 目标数组
                 * dstPos 从目标数组的那个位子开始写数据
                 * length 拷贝的元素的个数
                 */
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);

                for (Long i : mHits) {
                    LogTool.i(TAG, i + "");
                }


                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                for (Long i : mHits) {
                    LogTool.i(TAG, i + "");
                }
                if (mHits[0] >= (SystemClock.uptimeMillis() - DURATION)) {
                    startActivity(new Intent(mContext, DebugActivity.class));
                }
            }

        });
    }


    /**
     * 设置英文标题
     *
     * @param title
     */
    public void setEnglishTitle(String title) {
        mEnglishText.setText(title);
    }

    /**
     * 隐藏状态栏
     */
    private void hideNavigationbar() {
        setNavigationBar(this, View.GONE);
    }

    /**
     * @param activity
     * @param
     */
    public static void setNavigationBar(Activity activity, int visible) {
        View decorView = activity.getWindow().getDecorView();
        //显示NavigationBar
        if (View.GONE == visible) {
            int option = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(option);
        }
    }

    /**
     * 禁止状态栏
     */
    @SuppressLint("WrongConstant")
    public void disableStatusBar() {
        Object service = getSystemService("statusbar");
        try {
            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
            Method expand = statusBarManager.getMethod("disable", int.class);
            //判断版本大小
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                expand.invoke(service, DISABLE_EXPAND);
            } else {
                expand.invoke(service, DISABLE_EXPAND_LOW);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSjx(int viewId) {
        for (int i = 0; i < 4; i++) {
            //因为这里只有4个tab
            View view = mSjxLayout.getChildAt(i);
            if (view.getId() == viewId) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void showDefaultFragment() {
        LogTool.i(TAG, "==============showDefaultFragment==============");
//        showRecommendDishFragment(R.id.menu_dish_manager);
        showHomeFragment();
    }

    public void showTitleBar() {
        mTibleBar.setVisibility(View.VISIBLE);
    }

    public void prepareToCommitComment(int type) {
        MapFragment commentShopSelectFragment = MapFragment.Companion.newInstance(type);
        showFragment(commentShopSelectFragment);
        mTibleBar.setVisibility(View.GONE);
    }

    public void showCommitSuccess(MapMerchantModel mapMerchantModel) {
        CommitSuccessDialog dialog = new CommitSuccessDialog();
        dialog.show(getSupportFragmentManager(), "dialog");
        mSjxLayout.postDelayed(() -> showComplainListFragment_suc(mapMerchantModel), 3500);
    }

    public void displayMerchantInfoFragment(MapMerchantModel model) {
        showTitleBar();
        showFragment(MerchantIntroduceFragment.Companion.newInstance(model));

        mCurrentMenuId = R.id.honor;
        mBottomMenuManager.setSelectedId(mCurrentMenuId);
    }

    public int getCurrentMenuId() {
        return mCurrentMenuId;
    }


    public void setTitleActionVisibility(int visibilityFlag) {
        mTibleBar.setVisibility(View.VISIBLE);
    }

    public void setActionBarTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    protected void initData() {
        super.initData();
//        PushManager.getInstance().init();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogTool.saveLog(TAG, "onNewIntent");
//        if (!App.isPrintMode()) {
//            setIntent(intent);
//            showDefaultFragment();
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EXTRA_COMMIT_COMMENT && resultCode == RESULT_OK) {
            mCommentListFragment = new NewCommentListFragment();
            showTitleBar();
            showFragment(mCommentListFragment);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MobclickAgent.onProfileSignOff();
        AppConfig.IS_ERP_YUZHOUREN_MERCHANT = false;
        isUseTemplatePage = false;
        MainActivityOnCreated = false;
        LogTool.saveLog(TAG, "onDestroy");
        LogTool.destory();
        LoginActivity.isLogining = false;
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public void setWaiterTitleColor(int color) {
        mTitle.setTextColor(getResources().getColor(color));
    }

    public void setTitleActionContent(String content) {
    }

    public void setTitleActionBg(int bg) {
    }

    public void setTitleActionBg(Drawable drawable) {
    }

    public void setTitleActionClickListener(View.OnClickListener onClickListener) {
    }

    public void setTitleActionClickListener(int drawableId) {
    }

    /*@SuppressLint("ClickableViewAccessibility")
        private void initHomeMenu() {
            GestureDetector gestureDetector = new GestureDetector(mContext,
                    new GestureDetector.SimpleOnGestureListener() {

                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            return super.onSingleTapUp(e);
                        }

                        @Override
                        public boolean onSingleTapConfirmed(MotionEvent e) {
                            return super.onSingleTapConfirmed(e);
                        }

                        @Override
                        public boolean onDoubleTap(MotionEvent e) {
                            if (mHomeFragment != null && mCurrentMenuId == R.id.home) {
    //                    mHomeFragment.loadStoreInfo();
                            }
                            return super.onDoubleTap(e);
                        }
                    }
            );
            mHomeMenu.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
        }*/

    //    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        LogTool.saveLog("保存状态信息");
////        super.onSaveInstanceState(outState);
//    }
    /**
     * 显示证件照片
     */
    /*@OnClick(R.id.card)
    public void showDocumentFragment() {
        if (mCurrentMenuId == R.id.card) {
            return;
        }
        String url = "https://datav.aliyun.com/share/9ac6f35a35005f20784d956f580df72e";
        DocumentFragment documentFragment = DocumentFragment.newInstance(url);
        setTitleActionVisibility(View.GONE);
        mCurrentMenuId = R.id.card;
        mBottomMenuManager.setSelectedId(mCurrentMenuId);
        showFragment(documentFragment);
    }*/

    /**
     * 显示投诉界面
     */
    /*public void showComplainDetailFragment() {
        if (mCurrentMenuId == -55) {
            return;
        }

        mComplainDetailFragment = new ComplainDetailFragment();

        mCurrentMenuId = -55;

        mTibleBar.setVisibility(View.GONE);

        showFragment(mComplainDetailFragment);
    }*/
    /**
     * 显示评论界面
     */
    /*public void showCommentDetailFragment() {
        if (mCurrentMenuId == -44) {
            return;
        }

        mCommentDetailFragment = new CommentDetailFragment();

        mCurrentMenuId = -44;

        mTibleBar.setVisibility(View.GONE);

        showFragment(mCommentDetailFragment);
    }*/
    /**
     * 显示意见界面
     */
    /*public void showAdvisementDetailFragment() {
        if (mCurrentMenuId == -33) {
            return;
        }
        mAdviseDetailFragment = new AdviseDetailFragment();

        mCurrentMenuId = -33;

        mTibleBar.setVisibility(View.GONE);
        showFragment(mAdviseDetailFragment);
    }
*/
/* @OnClick(R.id.menu_dish_manager)
    public void showRecommendDishFragment() {
        if (mCurrentMenuId == R.id.menu_dish_manager) {
            return;
        }
        RecommendDishListFragment recommendDishListFragment = null;
        recommendDishListFragment = new RecommendDishListFragment();
        showFragment(recommendDishListFragment);
        mCurrentMenuId = R.id.menu_dish_manager;
        mBottomMenuManager.setSelectedId(R.id.menu_dish_manager);
    }*/
   /* @OnClick(R.id.advise)
    public void showAdvisementFragment() {
        if (mCurrentMenuId == R.id.advise) {
            return;
        }

        mAdviseListFragment = new AdviseListFragment();

        setTitleActionVisibility(View.GONE);
        mCurrentMenuId = R.id.advise;
        mBottomMenuManager.setSelectedId(mCurrentMenuId);
        showFragment(mAdviseListFragment);
    }*/
//    @Override
//    public void onClick(View v) {
//
//    }
}
