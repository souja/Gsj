package com.wangxiaobao.gsj.module.introduce

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.CameraUpdate
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.MapView
import com.amap.api.maps2d.model.BitmapDescriptorFactory
import com.amap.api.maps2d.model.CameraPosition
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.MarkerOptions
import com.wangxiaobao.gsj.base.App
import com.wangxiaobao.gsj.base.BaseFragment
import com.wangxiaobao.gsj.base.MainActivity
import com.wangxiaobao.gsj.common.AppConstants
import com.wangxiaobao.gsj.common.CommonUtil
import com.wangxiaobao.gsj.common.LogUtils
import com.wangxiaobao.gsj.enity.MapMerchantModel
import com.wangxiaobao.gsj.enity.TradeAreaModel
import com.wangxiaobao.gsj.home.ComplainDetailFragment
import com.wangxiaobao.gsj.log.JsonUtil
import com.wangxiaobao.gsj.module.comment.CommitCommentActivity
import com.wangxiaobao.gsj.view.dialog.ChooseShopDialog
import com.wangxiaobao.waiter.R

/**
 * Created by ijays on 2018/8/15.
 */
class MapFragment : BaseFragment(), ChooseShopDialog.OnChooseShopListener {


    private var mapView: MapView? = null
    /**
     * 地图控制器
     */
    private var aMap: AMap? = null

    private var cameraUpdate: CameraUpdate? = null

    /**
     * 记录进入此页面的状态，可能为商家介绍，评价，投诉
     */
    private var pageType = 0

    override fun generateLayoutID() = R.layout.fragment_map_b

    companion object {
        /**
         * 选中门店后到评价页面
         */
        const val TYPE_TO_COMMENT = 1
        /**
         * 选中门店后到商家介绍页面
         */
        const val TYPE_TO_SHOP_INTRODUCE = 2
        /**
         * 选中门店后到投诉页面
         */
        const val TYPE_TO_COMPLAIN = 3

        fun newInstance(pageType: Int): MapFragment {
            val fragment = MapFragment()
            val args = Bundle()
            args.putInt(AppConstants.EXTRA_PAGE_TYPE, pageType)
            fragment.arguments = args
            return fragment
        }

    }

    override fun initView(frameLayout: FrameLayout?, savedInstanceState: Bundle?) {
        pageType = arguments?.getInt(AppConstants.EXTRA_PAGE_TYPE) ?: TYPE_TO_SHOP_INTRODUCE

        mapView = frameLayout?.findViewById(R.id.map_view)

        setHintView(frameLayout)

        mapView?.onCreate(savedInstanceState)
        if (aMap == null) {
            aMap = mapView?.map
        }

        val settings = aMap?.uiSettings
        settings?.isZoomControlsEnabled = false
        settings?.isZoomGesturesEnabled = false

        val areaInfo = CommonUtil.getPropertyString(App.getContext(), AppConstants.CONS_TRADING_AREA)
        val tradingAreaInfo = JsonUtil.getObjectList(areaInfo, TradeAreaModel::class.java)
        Log.e("SONGJIE", "trading area size==>${tradingAreaInfo.size}")

        makePoint(tradingAreaInfo)
    }

    private fun setHintView(frameLayout: FrameLayout?) {
        when (pageType) {
//            TYPE_TO_SHOP_INTRODUCE -> mMainActivity.showSjx(R.id.honor_sjx)
            //投诉
            TYPE_TO_COMPLAIN -> {
                frameLayout?.findViewById<TextView>(R.id.hint_title)?.text = ("您要投诉哪个商家？")
            }
            //评论
            TYPE_TO_COMMENT -> frameLayout?.findViewById<TextView>(R.id.hint_title)?.text = ("您要评价哪个商家？")
        }
    }

    //根据地址绘制需要显示的点
    fun makePoint(placeList: MutableList<TradeAreaModel>) {
        if (placeList.isEmpty()) {
            LogUtils.saveLog("商圈信息为空，返回")
            //默认为成都
            cameraUpdate = CameraUpdateFactory.newCameraPosition(CameraPosition(LatLng(30.6574200000,
                    104.0658400000), 17f, 30f, 0f))
            aMap?.moveCamera(cameraUpdate)//地图移向指定区域
            return
        }

        var longtitudeTotal = 0.0
        var latitudeTotal = 0.0

        for ((index, value) in placeList.withIndex()) {
            val v = LayoutInflater.from(context).inflate(R.layout.custom_info_window_layout, null)
            v.findViewById<TextView>(R.id.tv_name).text = value.tradingareaName

            val latLng = LatLng(value.latitude.toDouble(), value.longitude.toDouble())

            longtitudeTotal += value.longitude.toDouble()
            latitudeTotal += value.latitude.toDouble()

            Log.e("SONGJIE", "long==>${value.longitude},latitude==>${value.latitude}")
            aMap?.addMarker(MarkerOptions().title(value.tradingareaName)
                    .position(latLng)
                    .anchor(.5f, .5f)
                    .snippet(index.toString())
                    .icon(BitmapDescriptorFactory.fromView(v)))
        }


        val defaultTradeArea = placeList[0]
//        val locationLatLng = LatLng(defaultTradeArea.latitude.toDouble(), defaultTradeArea.longitude.toDouble())
        val locationLatLng = LatLng(latitudeTotal / placeList.size, longtitudeTotal / placeList.size)
        //改变可视区域为指定位置
        //CameraPosition4个参数分别为位置，缩放级别，目标可视区域倾斜度，可视区域指向方向（正北逆时针算起，0-360）
        cameraUpdate = CameraUpdateFactory.newCameraPosition(CameraPosition(locationLatLng, 20f, 30f, 0f))
        aMap?.moveCamera(cameraUpdate)//地图移向指定区域

        aMap?.setOnMarkerClickListener { p0 ->
            val dialog = ChooseShopDialog().setTradeAreaInfo(placeList[p0.snippet.toInt()])
            dialog.show(childFragmentManager, "tag")

            dialog.setOnChooseShopListener(this)
            true
        }
    }

    override fun cancel() {

    }

    /**
     * 选中了门店
     */
    override fun onShopSelected(dialogFragment: ChooseShopDialog, mapMerchantModel: MapMerchantModel) {
        dialogFragment.dismiss()
        when (pageType) {
            TYPE_TO_COMMENT -> CommitCommentActivity.startActivity(mMainActivity, mapMerchantModel, MainActivity.EXTRA_COMMIT_COMMENT)
            TYPE_TO_COMPLAIN -> mMainActivity.showFragment(ComplainDetailFragment.newInstance(mapMerchantModel))
            TYPE_TO_SHOP_INTRODUCE -> mMainActivity.displayMerchantInfoFragment(mapMerchantModel)
        }
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }
}