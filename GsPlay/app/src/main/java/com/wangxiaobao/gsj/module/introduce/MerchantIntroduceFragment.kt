package com.wangxiaobao.gsj.module.introduce

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.wangxiaobao.gsj.base.App
import com.wangxiaobao.gsj.base.BaseFragment
import com.wangxiaobao.gsj.common.AppConstants
import com.wangxiaobao.gsj.common.CommonUtil
import com.wangxiaobao.gsj.common.DialogFactory
import com.wangxiaobao.gsj.common.LogTool
import com.wangxiaobao.gsj.enity.MapMerchantModel
import com.wangxiaobao.gsj.enity.MerchantGSInfo
import com.wangxiaobao.gsj.home.ComplainListFragment
import com.wangxiaobao.gsj.http.RetrofitClient
import com.wangxiaobao.gsj.module.comment.NewCommentListFragment
import com.wangxiaobao.gsj.util.GlideUtil
import com.wangxiaobao.waiter.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 商家介绍
 * Created by ijays on 2018/8/23.
 */
class MerchantIntroduceFragment : BaseFragment() {

    private var tvIntroduction: TextView? = null

    private var ivIntroduction: ImageView? = null

    private var emptyView: View? = null

    private var mapMerchantModel: MapMerchantModel? = null

    companion object {

        fun newInstance(mapMerchantModel: MapMerchantModel): MerchantIntroduceFragment {
            val fragment = MerchantIntroduceFragment()
            val args = Bundle()
            args.putSerializable(AppConstants.EXTRA_DATA, mapMerchantModel)
            fragment.arguments = args
            return fragment
        }
    }

    override fun generateLayoutID() = R.layout.fragment_merchant_introduce

    override fun initView(view: View?) {
        mapMerchantModel = arguments?.getSerializable(AppConstants.EXTRA_DATA) as MapMerchantModel?

        findView(view)

        loadData(mapMerchantModel?.merchantId ?: "1")

        mMainActivity.setActionBarTitle("企业证照")
        mMainActivity.setEnglishTitle("Enterprise License")
    }

    @SuppressLint("CheckResult")
    private fun loadData(merchantId: String) {
        showWaitDialog()
        RetrofitClient.getInstance().getMerchantInfo(merchantId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .subscribe({
                    dismissWaitDialog()
                    if (it.code.toString() != AppConstants.RESPONSE_SUCCESS) {
                        CommonUtil.showShortToast(it.message)
                        emptyView?.visibility = View.VISIBLE
                        return@subscribe
                    }
                    val merchantInfoModel = it.data

                    if (merchantInfoModel == null) {
                        emptyView?.visibility = View.VISIBLE
                    } else {

                        tvIntroduction?.text = merchantInfoModel.introduction ?: ""
                        GlideUtil.display(ivIntroduction, merchantInfoModel.images)
                    }

                }, {
                    dismissWaitDialog()
                    LogTool.saveLog("获取商家信息失败-->${it.message}")
                })
    }

    private fun findView(view: View?) {

        view?.findViewById<TextView>(R.id.tv_shop_name)?.text = mapMerchantModel?.merchantName ?: ""
        view?.findViewById<TextView>(R.id.tv_location_name)?.text = mapMerchantModel?.tradeAreaName
                ?: ""
        view?.findViewById<View>(R.id.view_bg)?.setBackgroundColor(Color.parseColor("#ffffff"))

        tvIntroduction = view?.findViewById(R.id.tv_introduction)
        ivIntroduction = view?.findViewById(R.id.iv_introduction)
        emptyView = view?.findViewById(R.id.ll_container)


        view?.findViewById<View>(R.id.ll_switch)?.setOnClickListener {
            mMainActivity.prepareToCommitComment(MapFragment.TYPE_TO_SHOP_INTRODUCE)
        }

        view?.findViewById<View>(R.id.ll_switchBottom)?.setOnClickListener {
            mMainActivity.prepareToCommitComment(MapFragment.TYPE_TO_SHOP_INTRODUCE)
        }

        view?.findViewById<TextView>(R.id.tv_comment)?.setOnClickListener {
            mMainActivity.mCurrentMenuId = R.id.comment
            mMainActivity.showSjx(R.id.comment_sjx)
            mMainActivity.mBottomMenuManager.setSelectedId(mMainActivity.mCurrentMenuId)
            //获取商家评价列表
            mMainActivity.showFragment(NewCommentListFragment.newInstance(mapMerchantModel))
        }

        view?.findViewById<TextView>(R.id.tv_complain)?.setOnClickListener {
            //获取商家投诉列表
            App.mApp.isLoadMerchantList = true

            mMainActivity.mCurrentMenuId = R.id.complain
            mMainActivity.showSjx(R.id.complain_sjx)
            mMainActivity.mBottomMenuManager.setSelectedId(mMainActivity.mCurrentMenuId)
            //获取商家评价列表
            mMainActivity.showFragment(ComplainListFragment.newInstance(mapMerchantModel))
        }

        view?.findViewById<TextView>(R.id.tv_credit)?.setOnClickListener {
            showWaitDialog()
            //获取商家信用信息
            RetrofitClient.getInstance().getMerchantGSInfo(mapMerchantModel?.merchantId ?: "1")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(this.bindToLifecycle())
                    .subscribe({
                        dismissWaitDialog()
                        if (it.code.toString() != AppConstants.RESPONSE_SUCCESS) {
                            CommonUtil.showShortToast(it.message)
                            return@subscribe
                        }
                        val merchantGsInfo = it.data

                        if (merchantGsInfo == null) {
                            CommonUtil.showShortToast("未查询到相关信息")
                        } else {
                            var showInfo = ""
                            //处罚
                            var punishTag = "\n经营者违法行为惩戒(处罚)信息："
                            var str1 = ""
                            if (merchantGsInfo.wfxwcjInfo != null && merchantGsInfo.wfxwcjInfo.xzcfList != null
                                    && merchantGsInfo.wfxwcjInfo.xzcfList.size > 0) {
                                var xzcfModel = merchantGsInfo.wfxwcjInfo.xzcfList.get(0)
                                if (xzcfModel.penalize_content != null)
                                    str1 = xzcfModel.penalize_content
                            }
                            if (!str1.isEmpty()) punishTag += str1 + "\n"

                            var str2 = ""
                            if (merchantGsInfo.wfxwcjInfo != null && merchantGsInfo.wfxwcjInfo.qtjstsxxList != null
                                    && merchantGsInfo.wfxwcjInfo.qtjstsxxList.size > 0) {
                                var qtjModel = merchantGsInfo.wfxwcjInfo.qtjstsxxList.get(0)
                                if (qtjModel.content != null)
                                    str2 = qtjModel.content
                            }
                            if (!str2.isEmpty()) punishTag += str2 + "\n"
                            if (!str1.isEmpty() || !str2.isEmpty()) punishTag += "\n"

                            if (str1.isEmpty() && str2.isEmpty()) punishTag += "无\n\n"

                            //奖励
                            var prideTag = "经营者奖励信息："
                            var str3 = ""
                            if (merchantGsInfo.jlInfo != null && merchantGsInfo.jlInfo.honorList != null
                                    && merchantGsInfo.jlInfo.honorList.size > 0) {
                                var honor = merchantGsInfo.jlInfo.honorList.get(0)
                                if (honor.honor_name != null)
                                    str3 = honor.honor_name
                                if (honor.year != null)
                                    str3 = honor.year + " " + str3
                            }
                            if (str3.isEmpty()) prideTag += "无\n\n"
                            else prideTag += str3 + "\n\n"

                            //异常
                            var exeTag = "纳入黑名单(异常名录)信息："
                            var str4 = ""
                            if (merchantGsInfo.lrhmdInfo != null && merchantGsInfo.lrhmdInfo.exceptionList != null
                                    && merchantGsInfo.lrhmdInfo.exceptionList.size > 0) {
                                var exceptionModel = merchantGsInfo.lrhmdInfo.exceptionList.get(0)
                                if (exceptionModel.specause_cn != null)
                                    str4 = exceptionModel.specause_cn
                                if (exceptionModel.abntime != null)
                                    str4 = exceptionModel.abntime + " " + str4
                            }
                            if (!str4.isEmpty()) exeTag += str4 + "\n"

                            var str5 = ""
                            if (merchantGsInfo.lrhmdInfo != null && merchantGsInfo.lrhmdInfo.illdisList != null
                                    && merchantGsInfo.lrhmdInfo.illdisList.size > 0) {
                                var illModel = merchantGsInfo.lrhmdInfo.illdisList.get(0)
                                if (illModel.serillreacn != null)
                                    str5 = illModel.serillreacn
                                if (illModel.abntime != null)
                                    str5 = illModel.abntime + " " + str4
                            }
                            if (!str5.isEmpty()) exeTag += str5 + "\n"

                            var str6 = ""
                            if (merchantGsInfo.lrhmdInfo != null && merchantGsInfo.lrhmdInfo.aqhmdList != null
                                    && merchantGsInfo.lrhmdInfo.aqhmdList.size > 0) {
                                var illModel = merchantGsInfo.lrhmdInfo.aqhmdList.get(0)
                                if (illModel.malfeasanse != null)
                                    str6 = illModel.malfeasanse
                            }
                            if (!str6.isEmpty()) exeTag += str6 + "\n"

                            if (str4.isEmpty() && str5.isEmpty() && str6.isEmpty()) exeTag += "无"

                            showInfo += punishTag + prideTag + exeTag

                            DialogFactory.SimpleDialog(mMainActivity, "信用情况", showInfo).show()
                        }

                    }, {
                        dismissWaitDialog()
                        LogTool.saveLog("获取商家信用信息失败-->${it.message}")
                    })

//            App.mApp.isLoadMerchantList = true

//            mMainActivity.mCurrentMenuId = R.id.complain
//            mMainActivity.showSjx(R.id.complain_sjx)
//            mMainActivity.mBottomMenuManager.setSelectedId(mMainActivity.mCurrentMenuId)
//            //获取商家评价列表
//            mMainActivity.showFragment(ComplainListFragment.newInstance(mapMerchantModel))
        }


    }

}