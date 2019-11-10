package com.wangxiaobao.gsj.module.comment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.TextView
import com.wangxiaobao.gsj.base.BaseFragment
import com.wangxiaobao.gsj.base.decorate.StatisticListAdapter
import com.wangxiaobao.gsj.common.AppConstants
import com.wangxiaobao.gsj.common.LogTool
import com.wangxiaobao.gsj.enity.MapMerchantModel
import com.wangxiaobao.gsj.enity.NewCommentModel
import com.wangxiaobao.gsj.enity.WrapModel
import com.wangxiaobao.gsj.http.Response
import com.wangxiaobao.gsj.http.RetrofitClient
import com.wangxiaobao.gsj.module.introduce.MapFragment
import com.wangxiaobao.gsj.util.AudioManager
import com.wangxiaobao.gsj.util.ClickUtil
import com.wangxiaobao.gsj.util.ListUtil
import com.wangxiaobao.gsj.view.XXXRecyclerView
import com.wangxiaobao.waiter.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by ijays on 2018/8/13.
 */
class NewCommentListFragment : BaseFragment(), View.OnClickListener {

    override fun generateLayoutID() = R.layout.fragment_new_comment_layout


    private var goBackView: View? = null

    private var emptyView: View? = null

    private var recyclerView: XXXRecyclerView? = null

    private var pageNo = 1

    private lateinit var adapter: StatisticListAdapter

    private var mapMerchantModel: MapMerchantModel? = null

    private var disposable: Disposable? = null

    companion object {
        const val REQUEST_COMMIT_COMMENT = 9090

        fun newInstance(mapMerchantModel: MapMerchantModel?): NewCommentListFragment {
            val fragment = NewCommentListFragment()
            val args = Bundle()
            args.putSerializable(AppConstants.EXTRA_DATA, mapMerchantModel)
            fragment.arguments = args
            return fragment

        }
    }

    override fun initView(view: View?) {
        mapMerchantModel = arguments?.getSerializable(AppConstants.EXTRA_DATA) as MapMerchantModel?

        findView(view)

        mMainActivity.setActionBarTitle("用户评价")
        mMainActivity.setEnglishTitle("Evaluate")

        loadData()

        initListener()
    }

    private fun initListener() {
        recyclerView?.setOnLoadMoreListener {
            pageNo++
            loadData()
        }
    }

    private fun loadData() {
        if (mMainActivity.currentMenuId != R.id.comment) {
            return
        }
        showWaitDialog()
        if (mapMerchantModel != null) {
            loadMerchantCommentList()
        } else {
            disposable = RetrofitClient.getInstance().getNewCommentList(pageNo, 20)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(this.bindToLifecycle())
                    .subscribe({
                        handleData(it)

                    }, {
                        dismissWaitDialog()
                        recyclerView?.stopLoadMore()
                        Log.e("SONGJIE", "--->${it.message}")
                        if (pageNo == 1 && emptyView?.visibility != View.VISIBLE) {
                            emptyView?.visibility = View.VISIBLE
                        }
                    })
        }
    }

    private fun loadMerchantCommentList() {
        disposable = RetrofitClient.getInstance().getMerchantCommentList(mapMerchantModel?.merchantId, pageNo, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .subscribe({
                    handleData(it)

                }, {
                    dismissWaitDialog()
                    recyclerView?.stopLoadMore()
                    Log.e("SONGJIE", "--->${it.message}")
                    if (pageNo == 1 && emptyView?.visibility != View.VISIBLE) {
                        emptyView?.visibility = View.VISIBLE
                    }
                })
    }

    private fun handleData(it: Response<WrapModel<MutableList<NewCommentModel>>>) {
        dismissWaitDialog()
        recyclerView?.stopLoadMore()
        if (it.code.toString() != AppConstants.RESPONSE_SUCCESS) {
            LogTool.saveLog("获取评价信息")
            return
        }

        val newCommentList = it.data.records
        if (pageNo == 1) {
            if (!ListUtil.isEmptyList(newCommentList)) {
                adapter.list.addAll(newCommentList!!)
                adapter.notifyDataSetChanged()
                recyclerView?.smoothScrollToPosition(0)
            } else {
                emptyView?.visibility = View.VISIBLE
            }
        } else {
            if (!ListUtil.isEmptyList(newCommentList)) {
                adapter.list.addAll(newCommentList!!)
                adapter.notifyDataSetChanged()
            }
        }

        recyclerView?.isLoadable = !(adapter.list.size < 5 || adapter.list.size == it.data.count)
    }

    private fun findView(view: View?) {
        recyclerView = view?.findViewById(R.id.recycler_view)
        emptyView = view?.findViewById(R.id.ll_container)
        view?.findViewById<TextView>(R.id.add_comment)?.setOnClickListener(this)

        adapter = StatisticListAdapter(ArrayList())

        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,
                false)
        recyclerView?.adapter = adapter

        if (mapMerchantModel != null) {
            goBackView = view?.findViewById(R.id.goBack)
            goBackView?.visibility = View.VISIBLE
            goBackView?.setOnClickListener(this)
        }
    }


    override fun onClick(v: View) {
        when (v?.id) {
            R.id.goBack -> {
                mMainActivity.displayMerchantInfoFragment(mapMerchantModel)
            }
            R.id.add_comment -> {
                if (ClickUtil.isFastClick()) {
                    return
                }
                if (mapMerchantModel == null) {
                    if (AudioManager.isPlaying()) {
                        AudioManager.terminatePlay()
                    }
                    mMainActivity.prepareToCommitComment(MapFragment.TYPE_TO_COMMENT)
//                startActivity(Intent(context,TestActivity::class.java))
                } else {
                    // 直接跳转提交评价页面
                    val intent = Intent(context, CommitCommentActivity::class.java)
                    intent.putExtra(AppConstants.EXTRA_DATA, mapMerchantModel)
                    startActivityForResult(intent, REQUEST_COMMIT_COMMENT)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_COMMIT_COMMENT) {
            if (resultCode == Activity.RESULT_CANCELED) {
                mMainActivity.showFragment(MapFragment.newInstance(MapFragment.TYPE_TO_COMMENT))
            }

            if (resultCode == Activity.RESULT_OK) {
                LogTool.saveLog("提交成功后，刷新数据")
                if (mapMerchantModel != null) {
                    showWaitDialog()
                    adapter.list.clear()
                    pageNo = 1
                    loadMerchantCommentList()
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        dismissWaitDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (disposable != null && disposable?.isDisposed == false) {
            disposable?.dispose()
        }
    }
}