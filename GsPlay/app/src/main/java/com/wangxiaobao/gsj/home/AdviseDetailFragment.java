package com.wangxiaobao.gsj.home;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.wangxiaobao.gsj.base.BaseFragment;
import com.wangxiaobao.gsj.common.CommonUtil;
import com.wangxiaobao.gsj.http.Constant;
import com.wangxiaobao.waiter.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by candy on 18-3-7.
 */

public class AdviseDetailFragment extends BaseFragment {


    @BindView(R.id.content)
    EditText content;


    @BindView(R.id.name)
    EditText name;

    @Override
    public void initView(View view) {
        super.initView(view);
//        mMainActivity.showSjx(R.id.advise_sjx);
    }


    private boolean canCommit() {
        if (TextUtils.isEmpty(content.getText().toString().trim())) {
            showToast("请输入意见内容");
            return false;
        }
        if (TextUtils.isEmpty(name.getText().toString().trim())) {
            showToast("请输入姓名");
            return false;
        }
        return true;
    }


    @Override
    public int generateLayoutID() {
        return R.layout.fragment_advise_detail;
    }


    @OnClick(R.id.save)
    public void addAdvisement() {

        if (canCommit()) {
            Comment comment = new Comment();
            comment.setMerchantId(CommonUtil.getMerchantID(mContext));
            comment.setUserNickname(name.getText().toString());
            comment.setAdvise(content.getText().toString());
            comment.setUserId(CommonUtil.getMerchantAccount(mContext));
            comment.setMerchantName(CommonUtil.getMerchantName(mContext));
            ApiManager.getInstance().addAdvisement(comment);
        }


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addAdvisementSuccess(Constant.Event success) {
        if (success.code == 1) {
//            mMainActivity.showAdvisementFragment();
        }
    }


    @Override
    public void initData() {
        super.initData();
    }



}
