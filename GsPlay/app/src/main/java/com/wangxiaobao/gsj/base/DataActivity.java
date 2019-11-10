package com.wangxiaobao.gsj.base;

import android.view.View;
import android.widget.RelativeLayout;

import com.wangxiaobao.gsj.common.LogTool;
import com.wangxiaobao.waiter.R;
import com.wangxiaobao.gsj.view.TitleBar;

import butterknife.BindView;

/**
 * Created by Administrator on 2016-04-05.
 */
public abstract class DataActivity extends BaseActivity {
    private static final String TAG = DataActivity.class.getSimpleName();
    @BindView(R.id.title_bar)
    protected TitleBar mTitleBar;
    @BindView(R.id.empty_view)
    protected RelativeLayout mEmptyView;

    @Override
    protected int getRootLayout() {
        return R.layout.activity_new_base;
    }

    @Override
    protected void initView() {
        super.initView();
        findViewById(R.id.layout_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
            }
        });
        mEmptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
            }
        });
        mTitleBar.mRightAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onActionClick();
            }
        });
        mTitleBar.getmBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackClick();
            }
        });
    }

    @Override
    public void showShowEmptyView() {
        super.showShowEmptyView();

        LogTool.saveLog(TAG, "显示空界面");
        mNetworkErrorLayout.setVisibility(View.GONE);
        mDataContainer.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorLayout() {
        super.showErrorLayout();
        mEmptyView.setVisibility(View.GONE);
        LogTool.saveLog(TAG, "显示错误界面");
    }

    @Override
    public void showSuccessLayout() {
        super.showSuccessLayout();
        mEmptyView.setVisibility(View.GONE);
        LogTool.saveLog(TAG, "显示成功界面");
    }

    protected void onActionClick() {

    }

    protected void onBackClick() {

        finish();

    }


    public abstract void reload();

    protected void setActionText(String text) {
        mTitleBar.mRightAction.setText(text);
    }

    protected void setActionVisiable(int visiable) {
        mTitleBar.mRightAction.setVisibility(visiable);
    }


    protected void enableOnSureClick() {
        mTitleBar.mRightAction.setEnabled(true);
        mTitleBar.mRightAction.setTextColor(mContext.getResources().getColor(R.color.new_text_light_green_color));
    }

    protected void disableOnSureClick() {
        mTitleBar.mRightAction.setEnabled(false);
        mTitleBar.mRightAction.setTextColor(mContext.getResources().getColor(R.color.new_text_aqua_green_color));
    }

}
