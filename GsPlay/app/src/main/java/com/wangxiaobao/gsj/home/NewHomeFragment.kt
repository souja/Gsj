package com.wangxiaobao.gsj.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.wangxiaobao.gsj.base.App
import com.wangxiaobao.gsj.base.BaseFragment
import com.wangxiaobao.gsj.common.AppConstants
import com.wangxiaobao.gsj.common.CommonUtil
import com.wangxiaobao.gsj.common.LogTool
import com.wangxiaobao.gsj.common.ScreenUtil
import com.wangxiaobao.gsj.enity.TradeAreaModel
import com.wangxiaobao.gsj.http.RetrofitClient
import com.wangxiaobao.gsj.log.JsonUtil
import com.wangxiaobao.gsj.util.ListUtil
import com.wangxiaobao.gsj.util.extension.getHeadImgUrl
import com.wangxiaobao.pushmanager.CommonUtils
import com.wangxiaobao.waiter.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by ijays on 2018/8/10.
 */
class NewHomeFragment : BaseFragment(), View.OnClickListener {

    /**
     * 三个区域的View
     */
    private var section1: TextView? = null
    private var section2: TextView? = null
    private var section3: TextView? = null

    /**
     * 当前frgment的指示器
     */
    private var currentIndex = 0

    private var fragList = ArrayList<HomeSubFragment>()

    private var tradeList = ArrayList<TradeAreaModel>()

    companion object {
        fun newInstance(url: String): NewHomeFragment {
            val fragment = NewHomeFragment()
            val args = Bundle()
            args.putString("string", url)
            fragment.arguments = args
            return fragment
        }
    }

    override fun generateLayoutID() = R.layout.fragment_home

    override fun sizeView(view: View?) {
//        ScreenUtil.initScale(view)
    }

    private fun findView(view: View?) {
        section1 = view?.findViewById(R.id.tv_section_1)
        section2 = view?.findViewById(R.id.tv_section_2)
        section3 = view?.findViewById(R.id.tv_section_3)
        val headImg = view?.findViewById<ImageView>(R.id.geo_map)

        section1?.setOnClickListener(this)
        section2?.setOnClickListener(this)
        section3?.setOnClickListener(this)

        headImg?.setImageResource(CommonUtils.getSn().getHeadImgUrl())
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_section_1 -> {
                if (currentIndex == 0) {
                    return
                }
                currentIndex = 0

                childFragmentManager.beginTransaction().replace(R.id.fl_container, fragList[0]).commit()
                section1?.setBackgroundResource(R.drawable.icon_section_selected)
                section2?.setBackgroundResource(R.drawable.icon_section_unselected)
                section3?.setBackgroundResource(R.drawable.icon_section_unselected)
            }
            R.id.tv_section_2 -> {
                if (currentIndex == 1) {
                    return
                }
                currentIndex = 1

                childFragmentManager.beginTransaction().replace(R.id.fl_container, fragList[1]).commit()
                section1?.setBackgroundResource(R.drawable.icon_section_unselected)
                section2?.setBackgroundResource(R.drawable.icon_section_selected)
                section3?.setBackgroundResource(R.drawable.icon_section_unselected)
            }
            R.id.tv_section_3 -> {
                if (currentIndex == 2) {
                    return
                }
                currentIndex = 2

                childFragmentManager.beginTransaction().replace(R.id.fl_container, fragList[2]).commit()
                section1?.setBackgroundResource(R.drawable.icon_section_unselected)
                section2?.setBackgroundResource(R.drawable.icon_section_unselected)
                section3?.setBackgroundResource(R.drawable.icon_section_selected)
            }
        }
    }

    @SuppressLint("CheckResult")
    override fun initView(view: View?) {
        findView(view)

        val areaInfo = CommonUtil.getPropertyString(App.getContext(), AppConstants.CONS_TRADING_AREA)
        val tradingAreaInfo = JsonUtil.getObjectList(areaInfo, TradeAreaModel::class.java)

        RetrofitClient.getInstance().tradingArea.subscribeOn(Schedulers.io())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.code.toString() != AppConstants.RESPONSE_SUCCESS) {
                        return@subscribe
                    }
                    tradeList = it.data as ArrayList<TradeAreaModel>

                    LogTool.saveLog("trading area ==>$areaInfo")
                    handleData(tradeList)
                }, {
                    LogTool.saveLog("获取trading area错误==>${it.message}")

                    if (this.isAdded) {
                        handleData(tradingAreaInfo)
                    }
                })
    }

    fun handleData(tradingAreaInfo: MutableList<TradeAreaModel>) {
        if (!ListUtil.isEmptyList(tradingAreaInfo)) {
            Log.e("Handle Data", "tradingAreaInfo " + tradingAreaInfo.size)
            fragList.clear()
            for ((index, value) in tradingAreaInfo.withIndex()) {
                fragList.add(HomeSubFragment.newInstance(value.businessCircleId))
                when (index) {//Top3商圈Tab按钮
                    //TODO 先暂时这样写
                    0 -> {//Top3:第一个tab
                        section1?.text = tradingAreaInfo[0].tradingareaName
                        section1?.visibility = View.VISIBLE
                    }
                    1 -> {
                        section2?.text = tradingAreaInfo[1].tradingareaName
                        section2?.visibility = View.VISIBLE
                    }
                    2 -> {
                        section3?.text = tradingAreaInfo[2].tradingareaName
                        section3?.visibility = View.VISIBLE
                    }
                }
            }

            childFragmentManager.beginTransaction().add(R.id.fl_container, fragList[0]).commitAllowingStateLoss()
        } else {
            childFragmentManager.beginTransaction().add(R.id.fl_container, HomeSubFragment()).commitNowAllowingStateLoss()
        }
    }

}