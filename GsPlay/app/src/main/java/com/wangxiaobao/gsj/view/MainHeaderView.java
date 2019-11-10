package com.wangxiaobao.gsj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wangxiaobao.waiter.R;

/**
 * 首页自定义组合的Header
 */
public class MainHeaderView extends RelativeLayout{
    TextView tv_title,tv_food,tv_scan,tv_card;
    ImageView iv_title_left;

    public MainHeaderView(Context context) {
        this(context, null);
    }

    public MainHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_header,this,true);
        tv_food = (TextView) findViewById(R.id.tv_food_manage);
        tv_scan = (TextView)findViewById(R.id.tv_scan);
        tv_card = (TextView)findViewById(R.id.tv_card_manager);
    }

    public TextView getHeaderTitle() {
        return tv_title;
    }

    /**
     * 刷新UI
     */
    public  void invalidate(){
        this.postInvalidate();
    }

    public TextView getFoodManage() {
        return tv_food;
    }

    public TextView getScan() {
        return tv_scan;
    }

    public TextView getCardManage() {
        return tv_card;
    }
}
