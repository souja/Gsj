package com.wangxiaobao.gsj.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.razerdp.widget.animatedpieview.AnimatedPieView
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig
import com.razerdp.widget.animatedpieview.data.SimplePieInfo
import com.wangxiaobao.gsj.base.App
import com.wangxiaobao.gsj.base.BaseFragment
import com.wangxiaobao.gsj.base.TimeUtils
import com.wangxiaobao.gsj.common.AppConstants
import com.wangxiaobao.gsj.common.LogTool
import com.wangxiaobao.gsj.enity.HomeComplainListModel
import com.wangxiaobao.gsj.http.RetrofitClient
import com.wangxiaobao.gsj.module.introduce.MyLimitScrollerAdapter
import com.wangxiaobao.gsj.util.ListUtil
import com.wangxiaobao.gsj.util.extension.setRelativeText
import com.wangxiaobao.gsj.view.LimitScrollerView
import com.wangxiaobao.waiter.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by ijays on 2018/8/10.
 */
class HomeSubFragment : BaseFragment() {
    /**
     * 男性投诉数量
     */
    private var tvMaleNumber: TextView? = null

    /**
     * 男性投诉比例
     */
    private var tvMaleRate: TextView? = null
    /**
     * 女性投诉数量
     */
    private var tvFeMaleNumber: TextView? = null
    /**
     * 女性投诉数量
     */
    private var tvFeMaleRate: TextView? = null

    /**
     * 好评，中评，差评
     */
    private var tvGoodComment: TextView? = null
    private var tvMediumComment: TextView? = null
    private var tvBadComment: TextView? = null

    /**
     *月投诉人数
     */
    private var tvComplainNumbers: TextView? = null
    /**
     *月投诉平均分
     */
    private var tvComplainRate: TextView? = null
    /**
     * 月评价人数
     */
    private var tvCommentNumber: TextView? = null
    private var tvCommentRate: TextView? = null

    private var pieView: AnimatedPieView? = null

    private var complainContainer: LinearLayout? = null
    private var commentContainer: LinearLayout? = null

    /**
     * 投诉滚动条View
     */
    private var complainScroller: LimitScrollerView? = null
    /**
     * 评价滚动条View
     */
    private var commentScroller: LimitScrollerView? = null

    private lateinit var adapter: MyLimitScrollerAdapter
    /**
     * 评价滚动条adapter
     */
    private lateinit var commentAdapter: MyLimitScrollerAdapter

    private var disposable: Disposable? = null

    private var commentPageNo = 1

    /**
     * 投诉分页指示器
     */
    private var complainPageNo = 1

    companion object {
        fun newInstance(type: Int): HomeSubFragment {
            val fragment = HomeSubFragment()
            val args = Bundle()
            args.putInt("type", type)
            fragment.arguments = args
            return fragment
        }
    }

    override fun generateLayoutID() = R.layout.fragment_home_sub_view

    override fun sizeView(view: View?) {
//        ScreenUtil.initScale(view)
    }

    override fun initView(view: View?) {
        val circleId = arguments?.getInt("type", 0)

        findView(view)

        adapter = MyLimitScrollerAdapter()
        complainScroller?.setDataAdapter(adapter)

        commentAdapter = MyLimitScrollerAdapter()
        commentScroller?.setDataAdapter(commentAdapter)

        executeTask(circleId)
    }

    private fun executeTask(circleId: Int?) {
        if (circleId ?: 0 <= 0) {
            setupPIeView(100.toDouble(), 0.toDouble(), 0.toDouble())
            return
        }
//        if (!this.isVisible){
//            LogTool.saveLog("homeSubFragment不可见,跳过10分钟定时任务")
//            return
//        }
        disposable = Observable.interval(0, 10, TimeUnit.MINUTES)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    LogTool.saveLog("homeSubFragment指定10分钟定时任务")
                    if (mMainActivity.currentMenuId != R.id.home) {
                        LogTool.saveLog("homeSubFragment指定10分钟定时任务跳过，不在home fragment")
                        return@subscribe
                    }
                    loadComplainStat(circleId)
                    loadCommentStat(circleId)
                    loadHomeComplainList(circleId)
                    loadCommentData(circleId ?: 1)
                }, {
                    LogTool.saveLog("HomeSubFragment===>${it.message}")
                    dismissWaitDialog()
                })
    }

    @SuppressLint("CheckResult")
    private fun loadHomeComplainList(circleId: Int?) {
        RetrofitClient.getInstance().getHomeComplainList(circleId ?: 0, complainPageNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .subscribe({
                    if (it.code.toString() != AppConstants.RESPONSE_SUCCESS) {
                        LogTool.saveLog("获取首页滚动投诉信息失败")
                        return@subscribe
                    }
//                    it.data.forEachIndexed { index, homeComplainListModel ->
//                        homeComplainListModel.content = homeComplainListModel.content + index.toString()
//                    }
                    val dataList = it.data
                    if (dataList.isNotEmpty() == true) {
                        val homeComplainList = ArrayList<HomeComplainListModel>()
                        dataList.forEachIndexed { index, homeComplainListModel ->
                            val model = if (complainPageNo == 1) {
                                HomeComplainListModel(homeComplainListModel.complaintDate,
                                        homeComplainListModel.content)
                            } else {
                                HomeComplainListModel(homeComplainListModel.complaintDate,
                                        homeComplainListModel.content)
                            }
                            val startTime = TimeUtils.getDateToString(model.complaintDate,
                                    "yyyy-MM-dd")
                            val endTime = TimeUtils.getNowTime()
                            val days = TimeUtils.getTimeDifference(startTime, endTime)
                            if (days <= 30) {
                                homeComplainList.add(model)
                            }
                        }
                        if (complainPageNo == 1) {
                            if (homeComplainList.size <= 3) {
                                complainContainer?.removeAllViews()
                                complainContainer?.visibility = View.VISIBLE
                                homeComplainList.forEachIndexed { index, model ->
                                    val v = LayoutInflater.from(context).inflate(R.layout.item_home_complain_layout, null)
                                    v.findViewById<TextView>(R.id.tv_time).text = TimeUtils.getDateToString(model.complaintDate,
                                            "yyyy-MM-dd HH:mm")
                                    v.findViewById<TextView>(R.id.tv_complain_msg).text = model.content
                                            ?: ""
                                    complainContainer?.addView(v)
                                }

                            } else {
                                complainContainer?.visibility = View.GONE
                                adapter.setLimitScrollerView(complainScroller!!)
                                adapter.setData(App.getContext(), homeComplainList)
                            }
                        } else {
                            val prevList = adapter.getDataList()
                            prevList.addAll(homeComplainList)
                            complainScroller?.cancel()
                            adapter.setData(App.getContext(), prevList)
                        }
                    }

                }, {
                    Log.e("SONGJIE", "获取首页滚动投诉信息失败==>${it.message}")
                })
    }

    @SuppressLint("CheckResult")
    private fun loadCommentData(circleId: Int) {
        showWaitDialog()
        RetrofitClient.getInstance().getCommentList(circleId, commentPageNo, 50)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .subscribe({
                    if (it.code.toString() != AppConstants.RESPONSE_SUCCESS) {
                        LogTool.saveLog("获取评价信息")
                        return@subscribe
                    }

                    val dataList = it.data.records
                    if (dataList?.isNotEmpty() == true) {
                        val homeCommentList = ArrayList<HomeComplainListModel>()
                        dataList.forEachIndexed { index, newCommentModel ->
                            val model = if (commentPageNo == 1) {
                                HomeComplainListModel(newCommentModel.createDate,
                                        newCommentModel.content)
                            } else {
                                HomeComplainListModel(newCommentModel.createDate,
                                        newCommentModel.content)
                            }

                            val startTime = TimeUtils.getDateToString(model.complaintDate,
                                    "yyyy-MM-dd")
                            val endTime = TimeUtils.getNowTime()
                            val days = TimeUtils.getTimeDifference(startTime, endTime)
                            if (days <= 30) {
                                homeCommentList.add(model)
                            }
                        }

                        if (commentPageNo == 1) {
                            if (homeCommentList.size <= 3) {
                                commentContainer?.visibility = View.VISIBLE
                                commentContainer?.removeAllViews()
                                homeCommentList.forEachIndexed { index, model ->
                                    val v = LayoutInflater.from(context).inflate(R.layout.item_home_complain_layout, null)
                                    v.findViewById<TextView>(R.id.tv_time).text = TimeUtils.getDateToString(model.complaintDate,
                                            "yyyy-MM-dd HH:mm")
                                    v.findViewById<TextView>(R.id.tv_complain_msg).text = model.content
                                            ?: ""
                                    commentContainer?.addView(v)
                                }
                            } else {
                                commentContainer?.visibility = View.GONE
                                commentAdapter.setLimitScrollerView(commentScroller!!)
                                commentAdapter.setData(App.getContext(), homeCommentList)
                            }
                        } else {
                            val prevList = commentAdapter.getDataList()
                            prevList.addAll(homeCommentList)
                            commentScroller?.cancel()
                            commentAdapter.setData(App.getContext(), prevList)
                        }

                    }

                    if (commentAdapter.count < it.data.count) {
                        if ((commentPageNo * 50 < it.data.count) && commentPageNo < 4) {
                            commentPageNo++
                            loadCommentData(circleId)
                        } else {
                            dismissWaitDialog()
                        }
                    } else {
                        dismissWaitDialog()
                    }


                }, {
                    dismissWaitDialog()
                    LogTool.saveLog("获取commentList错误")
                })
    }


    private fun loadCommentStat(circleId: Int?) {
        RetrofitClient.getInstance().getHomeCommentInfo(circleId ?: 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .subscribe({
                    if (it.code.toString() != AppConstants.RESPONSE_SUCCESS) {
                        LogTool.saveLog("首页获取评价信息失败-->${it.message}")
                        return@subscribe
                    }
                    val homeCommentModel = it.data
                    tvGoodComment?.text = String.format(getString(R.string.good_comment),
                            homeCommentModel.higherStarNo)
                    tvMediumComment?.text = String.format(getString(R.string.medium_comment),
                            homeCommentModel.mediumStarNo)
                    tvBadComment?.text = String.format(getString(R.string.bad_comment),
                            homeCommentModel.lowerStarNo)

                    tvCommentNumber?.text = homeCommentModel.monthStarNo.toString()
                            .setRelativeText(.6f, " 条")
                    tvCommentRate?.text = homeCommentModel.evaluationScores.toString()
                            .setRelativeText(.6f, " 分")



                    if (homeCommentModel.higherStarNo == 0 && homeCommentModel.mediumStarNo == 0
                            && homeCommentModel.lowerStarNo == 0) {
                        homeCommentModel.higherStarNo = 100
                    }
                    setupPIeView(homeCommentModel.higherStarNo.toDouble(),
                            homeCommentModel.mediumStarNo.toDouble(),
                            homeCommentModel.lowerStarNo.toDouble())


                }, {
                    Log.e("SONGJIE", "error===>${it.message}")

                    setupPIeView(100.toDouble(), 0.toDouble(), 0.toDouble())
                })
    }

    private fun setupPIeView(good: Double, neutral: Double, bad: Double) {
        val pieConfig = AnimatedPieViewConfig().startAngle(-90f)
                .addData(SimplePieInfo(good, ContextCompat.getColor(App.getContext(), R.color.good_comment)))
                .addData(SimplePieInfo(neutral, ContextCompat.getColor(App.getContext(), R.color.neutral_comment)))
                .addData(SimplePieInfo(bad, ContextCompat.getColor(App.getContext(), R.color.bad)))
                .canTouch(false)
                .duration(500)

        pieView?.start(pieConfig)
    }

    @SuppressLint("CheckResult")
    private fun loadComplainStat(circleId: Int?) {
        RetrofitClient.getInstance().getHomeComplainInfo(circleId ?: 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .subscribe({
                    if (it.code.toString() != AppConstants.RESPONSE_SUCCESS) {
                        LogTool.saveLog("首页获取抱怨信息失败-->${it.message}")
                        return@subscribe
                    }
                    val homeComplainInfo = it.data

                    Log.e("SONGJIE", "homeComplain info-->${homeComplainInfo}")
                    tvMaleNumber?.text = String.format(getString(R.string.people_number), homeComplainInfo.male)

                    tvFeMaleNumber?.text = String.format(getString(R.string.people_number), homeComplainInfo.female)
                    tvMaleRate?.text = "${homeComplainInfo.malePercent}%"
                    tvFeMaleRate?.text = "${homeComplainInfo.femalePercent}%"

                    tvComplainNumbers?.text = homeComplainInfo.complaintNumber.toString()
                            .setRelativeText(.6f, " 条")
                    tvComplainRate?.text = homeComplainInfo.complaintScore
                            .setRelativeText(.6f, " 分")
                }, {
                    LogTool.saveLog("首页获取抱怨信息失败===>${it.message}")
                })

    }

    private fun findView(view: View?) {
        tvMaleNumber = view?.findViewById(R.id.tv_male_number)
        tvFeMaleNumber = view?.findViewById(R.id.tv_female_number)
        tvMaleRate = view?.findViewById(R.id.tv_male_number_rate)
        tvFeMaleRate = view?.findViewById(R.id.tv_female_number_rate)
        tvGoodComment = view?.findViewById(R.id.tv_good_comment_number)
        tvMediumComment = view?.findViewById(R.id.tv_neutral_comment_number)
        tvBadComment = view?.findViewById(R.id.tv_bad_comment_number)
        pieView = view?.findViewById(R.id.pie_view)
        tvComplainNumbers = view?.findViewById(R.id.tv_complain_numbers)
        tvComplainRate = view?.findViewById(R.id.tv_complain_average_numbers)
        tvCommentNumber = view?.findViewById(R.id.tv_comment_numbers)
        tvCommentRate = view?.findViewById(R.id.tv_comment_average_numbers)
        complainScroller = view?.findViewById(R.id.complain_scroller_view)
        commentScroller = view?.findViewById(R.id.comment_scroller_view)
        complainContainer = view?.findViewById(R.id.complain_container)
        commentContainer = view?.findViewById(R.id.comment_container)
    }

    override fun onStart() {
        super.onStart()
        if (!ListUtil.isEmptyList(adapter.getDataList()) && adapter.getDataList().size > 3) {
            complainScroller?.startScroll()
        }

        if (!ListUtil.isEmptyList(commentAdapter.getDataList()) && commentAdapter.getDataList().size > 3) {
            commentScroller?.startScroll()
        }
    }

    override fun onStop() {
        super.onStop()
        complainScroller?.cancel()
        commentScroller?.cancel()
        commentPageNo = 1
        complainPageNo = 1

        dismissWaitDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

}
