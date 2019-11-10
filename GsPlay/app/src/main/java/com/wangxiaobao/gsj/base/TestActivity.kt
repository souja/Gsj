package com.wangxiaobao.gsj.base

import android.annotation.SuppressLint
import android.support.v4.content.ContextCompat
import android.util.Log
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig
import com.razerdp.widget.animatedpieview.data.SimplePieInfo
import com.wangxiaobao.gsj.common.AppConstants
import com.wangxiaobao.gsj.common.LogTool
import com.wangxiaobao.gsj.enity.HomeCommentModel
import com.wangxiaobao.gsj.enity.HomeComplainListModel
import com.wangxiaobao.gsj.http.RetrofitClient
import com.wangxiaobao.gsj.module.introduce.MyLimitScrollerAdapter
import com.wangxiaobao.gsj.util.extension.setRelativeText
import com.wangxiaobao.waiter.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.test_activity.*

/**
 * Created by ijays on 2018/8/22.
 */
class TestActivity : BaseActivity() {

    private lateinit var adapter: MyLimitScrollerAdapter
    override fun generateLayoutId(): Int {
        return R.layout.test_activity
    }

    override fun onStart() {
        super.onStart()
        scroller.startScroll()
    }

    override fun initView() {

        adapter = MyLimitScrollerAdapter()
        scroller.setDataAdapter(adapter)

//        RetrofitClient.getInstance().getHomeComplainList(1, 1)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    if (it.code.toString() != AppConstants.RESPONSE_SUCCESS) {
//                        LogTool.saveLog("获取首页滚动投诉信息失败")
//                        return@subscribe
//                    }
//                    it.data.forEachIndexed { index, homeComplainListModel ->
//                        homeComplainListModel.content = homeComplainListModel.content + index.toString()
//                    }
//                    adapter.setLimitScrollerView(scroller!!)
//                    adapter.setData(App.getContext(), it.data)
//
//
//                }, {
//                    Log.e("SONGJIE", "首页投诉信息失败==>${it.message}")
//                })
        loadData()
        loadCommentStat(1)
    }

    var pageNo = 1

    @SuppressLint("CheckResult")
    private fun loadData() {
        //这里的商圈id->test
        RetrofitClient.getInstance().getCommentList(1, pageNo, 50)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.code.toString() != AppConstants.RESPONSE_SUCCESS) {
                        LogTool.saveLog("获取评价信息")
                        return@subscribe
                    }

                    val dataList = it.data.records
                    if (dataList?.isNotEmpty() == true) {
                        val homeCommentList = ArrayList<HomeComplainListModel>()
                        dataList.forEachIndexed { index, newCommentModel ->
                            val model = if (pageNo == 1) {
                                HomeComplainListModel(newCommentModel.createDate,
                                        newCommentModel.content + index.toString())
                            } else {
                                HomeComplainListModel(newCommentModel.createDate,
                                        newCommentModel.content + "--$pageNo--" + index.toString())
                            }
                            homeCommentList.add(model)
                        }

                        if (pageNo == 1) {

                            adapter.setLimitScrollerView(scroller!!)
                            adapter.setData(App.getContext(), homeCommentList)
                        } else {
                            val prevList = adapter.getDataList()
                            prevList.addAll(homeCommentList)
                            scroller.cancel()
                            adapter.setData(App.getContext(), prevList)
                        }

                    }

                    if (adapter.count < it.data.count) {
                        pageNo++
                        loadData()
                    }


                }, {
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

                    setupPIeView(homeCommentModel)


                }, {
                    Log.e("SONGJIE", "error===>${it.message}")

                    val pieConfig = AnimatedPieViewConfig().startAngle(-90f)
                            .addData(SimplePieInfo(100.toDouble(),
                                    ContextCompat.getColor(App.getContext(), R.color.good_comment)))
                            .addData(SimplePieInfo(0.toDouble(), ContextCompat.getColor(App.getContext(), R.color.neutral_comment)))
                            .addData(SimplePieInfo(0.toDouble(), ContextCompat.getColor(App.getContext(), R.color.bad)))
                            .pieRadiusRatio(1f)
                            .duration(500)

                    pie_view?.start(pieConfig)
                })
    }

    private fun setupPIeView(homeCommentModel: HomeCommentModel) {
        val pieConfig = AnimatedPieViewConfig().startAngle(-90f)
                .addData(SimplePieInfo(homeCommentModel.higherStarNo.toDouble(),
                        ContextCompat.getColor(App.getContext(), R.color.good_comment)))
                .addData(SimplePieInfo(homeCommentModel.mediumStarNo.toDouble(),
                        ContextCompat.getColor(App.getContext(), R.color.neutral_comment)))
                .addData(SimplePieInfo(homeCommentModel.lowerStarNo.toDouble(),
                        ContextCompat.getColor(App.getContext(), R.color.bad)))
                .duration(500)

        pie_view?.start(pieConfig)
    }

}