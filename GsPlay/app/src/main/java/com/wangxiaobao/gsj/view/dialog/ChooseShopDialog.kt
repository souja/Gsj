package com.wangxiaobao.gsj.view.dialog

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.wangxiaobao.gsj.adapter.BaseRecyclerAdapter
import com.wangxiaobao.gsj.enity.AlphabetModel
import com.wangxiaobao.gsj.enity.MapMerchantModel
import com.wangxiaobao.gsj.enity.TradeAreaModel
import com.wangxiaobao.gsj.http.RetrofitClient
import com.wangxiaobao.gsj.module.introduce.AlphabetAdapter
import com.wangxiaobao.gsj.module.introduce.MapShopAdapter
import com.wangxiaobao.gsj.util.helper.ConvertHelper
import com.wangxiaobao.waiter.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by ijays on 2018/8/15.
 */
class ChooseShopDialog : BottomDialogFragment(), View.OnClickListener {
    /**
     * 字母表list
     */
    private var recyclerAlphabet: RecyclerView? = null
    /**
     *门店的list
     */
    private var recyclerShop: RecyclerView? = null
    /**
     * 字母list适配器
     */
    private lateinit var alphabetAdapter: AlphabetAdapter

    /**
     * 门店adapter
     */
    private lateinit var shopAdapter: MapShopAdapter
    /**
     * 将原始数据转换成字母排序
     */
    private lateinit var transferResult: ConvertHelper.ConvertResult<MapMerchantModel>
    /**
     * 商圈模型
     */
    private var tradeAreaModel: TradeAreaModel? = null

    /**
     *标题
     */
    private var title: String? = null

    private var listener: OnChooseShopListener? = null

    private lateinit var shopList: List<MapMerchantModel>

    override fun getContentViewLayoutId() = R.layout.dialog_choose_shop_layout

    override fun init(inflater: LayoutInflater?, rootView: View?, savedInstanceState: Bundle?) {
        findView(rootView)
    }

    private fun findView(rootView: View?) {
        rootView?.findViewById<TextView>(R.id.tv_circle_name)?.text = title ?: ""
        rootView?.findViewById<View>(R.id.ll_switch)?.setOnClickListener(this)

        recyclerAlphabet = rootView?.findViewById(R.id.recycler_alphabet)
        recyclerShop = rootView?.findViewById(R.id.recycler_shop)

        loadMerchant()

        this.dialog?.setOnKeyListener { dialog, keyCode, event ->
            keyCode == KeyEvent.KEYCODE_BACK
        }


        recyclerShop?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val firstVisiblePosition = (recyclerShop?.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
                if (firstVisiblePosition < 0 || firstVisiblePosition >= shopAdapter.realItemCount) {
                    return
                }
                val entity = shopAdapter.getItem(firstVisiblePosition)
                val value = entity.indexValue
                Log.e("SONGJIE", "----value--$value")
                Log.e("SONGJIE", "----first--$firstVisiblePosition")

                var alphaIndex = 0
                alphabetAdapter.list.forEachIndexed { index, alphabetModel ->
                    if (alphabetModel.alphabet == entity.indexValue) {
                        alphaIndex = index
                    }
                    if (alphabetModel.isSelected) {
                        alphabetModel.isSelected = false
                    }
                }
                alphabetAdapter.list[alphaIndex].isSelected = true
                alphabetAdapter.notifyDataSetChanged()

            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }

        })

    }

    private fun loadMerchant() {
        RetrofitClient.getInstance().getMerchantByArea(tradeAreaModel?.businessCircleId ?: 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe({

                    Log.e("SONGJIE", "merchantArea${it.data.size}")


                    val dataList = it.data
                    dataList.forEach {
                        it.tradeAreaName = tradeAreaModel?.tradingareaName ?: ""
                        it.businessCircleId = tradeAreaModel?.businessCircleId ?: 0

                    }
                    Log.e("SONGJIE", "merchantArea=========${dataList.size}")
                    transferData(dataList)

                    alphabetAdapter = AlphabetAdapter(generateData(transferResult.indexValueList))
                    recyclerAlphabet?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,
                            false)
                    recyclerAlphabet?.adapter = alphabetAdapter

                    recyclerShop?.layoutManager = GridLayoutManager(context, 4, GridLayoutManager.VERTICAL,
                            false)

                    shopAdapter = MapShopAdapter(transferResult.indexStickyEntities.filter {
                        it.originalData != null
                    })
                    shopAdapter.setIndexValuePositionMap(transferResult.indexValuePositionMap)
                    recyclerShop?.adapter = shopAdapter

                    initClickListener()


                }, {
                    Log.e("SONGJIE", "error===>${it.message}")
                })
    }

    private fun initClickListener() {
        alphabetAdapter.onItemClickListener = object : BaseRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val prevSelected = alphabetAdapter.list.filter {
                    it.isSelected == true
                }
                prevSelected[0].isSelected = false

                val alphaModel = alphabetAdapter.getItem(position)
                alphaModel.isSelected = true

                alphabetAdapter.notifyDataSetChanged()
                var position = 0
                for ((index, value) in alphabetAdapter.list.withIndex()) {
                    if (value.alphabet == alphaModel.alphabet) {
                        position = index
                        Log.e("SONGJIE", "position===>$position")
                        break
                    }
                }

                val location = shopAdapter.getIndexValuePosition(alphaModel.alphabet) - position
                Log.e("SONGJIE", "选中的alphabet===>${alphaModel.alphabet}")
                Log.e("SONGJIE", "选中的location===>$location")

                (recyclerShop?.layoutManager as GridLayoutManager).scrollToPositionWithOffset(location,0)
            }

            override fun onItemLongClick(v: View?, position: Int) {
            }
        }


        shopAdapter.onItemClickListener =
                object : BaseRecyclerAdapter.OnItemClickListener {
                    override fun onItemClick(v: View?, position: Int) {
//                val prevSelected = shopAdapter.list.filter {
//                    it.originalData.isSelected == true
//                }
//                if (prevSelected.isNotEmpty()) {
//                    prevSelected[0].originalData.isSelected = false
//                }
//
//                val shopModel = shopAdapter.getItem(position)
//                shopModel.originalData.isSelected = true
//
//                shopAdapter.notifyDataSetChanged()
                        val shopModel = shopAdapter.getItem(position)
                        listener?.onShopSelected(this@ChooseShopDialog, shopModel.originalData)
                    }

                    override fun onItemLongClick(v: View?, position: Int) {

                    }
                }
    }

    private fun generateShopData(): List<MapMerchantModel> {

        val contactStrings = Arrays.asList(resources.getStringArray(R.array.city_array))
        val list = ArrayList<MapMerchantModel>(contactStrings.size)
        contactStrings[0].forEach {
            val model = MapMerchantModel(it.toString(), "q", title!!, 1, false)
            list.add(model)
        }

        return list
    }

    private fun transferData(list: List<MapMerchantModel>) {
        transferResult = ConvertHelper.transfer(list)
        for ((key, value) in transferResult.indexValuePositionMap) {
            Log.e("SONGJIE", "===key===$key")
            Log.e("SONGJIE", "===value===$value")
        }
    }

    private fun generateData(indexValueList: List<String>): List<AlphabetModel> {
        if (indexValueList.isEmpty()) {
            return ArrayList()
        }
        val list = ArrayList<AlphabetModel>()
        indexValueList.forEachIndexed { index, s ->
            val model = if (index == 0) {
                AlphabetModel(s, true)
            } else {
                AlphabetModel(s, false)
            }
            list.add(model)
        }
        return list
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_switch -> {
                listener?.cancel()
                this.dismiss()
            }
        }

    }

    fun setOnChooseShopListener(listener: OnChooseShopListener) {
        this.listener = listener
    }

    interface OnChooseShopListener {
        fun cancel()

        fun onShopSelected(dialogFragment: ChooseShopDialog, mapMerchantModel: MapMerchantModel)
    }

    fun setTradeAreaInfo(tradeArea: TradeAreaModel): ChooseShopDialog {
        this.tradeAreaModel = tradeArea
        this.title = tradeArea.tradingareaName
        return this
    }

    fun setMerchant(list: List<MapMerchantModel>): ChooseShopDialog {
        this.shopList = list
        return this
    }
}
