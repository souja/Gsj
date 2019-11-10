package com.wangxiaobao.gsj.home;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wangxiaobao.gsj.base.BaseFragment;
import com.wangxiaobao.gsj.base.MainActivity;
import com.wangxiaobao.gsj.common.CommonUtil;
import com.wangxiaobao.gsj.enity.dish.Dish;
import com.wangxiaobao.gsj.http.RetrofitClient;
import com.wangxiaobao.gsj.http.RetrofitObserver;
import com.wangxiaobao.gsj.http.RetrofitObserverOption;
import com.wangxiaobao.waiter.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

/**
 * Created by candy on 16-9-2.
 */
public class RecommendDishListFragment extends BaseFragment {

    @BindView(R.id.list_view)
    RecyclerView mListView;

    public RecommendDishListAdapter getRecommendDishListAdapter() {
        return mRecommendDishListAdapter;
    }

    private RecommendDishListAdapter mRecommendDishListAdapter;


    @Override
    public int generateLayoutID() {
        return R.layout.fragment_recommend_dish_manager;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        mListView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecommendDishListAdapter = new RecommendDishListAdapter(mContext, this);
//        mRecommendDishListAdapter.setNeedFooter(true);
        mListView.addItemDecoration(new SpaceItemDecoration(120));
        MainActivity mainActivity = (MainActivity) mContext;
        mainActivity.setActionBarTitle("推荐");
        mainActivity.setTitleActionVisibility(View.GONE);

        mMainActivity.setEnglishTitle("Recommended");

//        mMainActivity.showSjx(R.id.recommend_dish_sjx);

    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.top = space;
        }
    }


    @Override
    protected void showEmptyView() {
        super.showEmptyView();
        mDataContainer.setVisibility(View.VISIBLE);
    }

    @Override
    protected void showDataView() {
        super.showDataView();
    }

    @Override
    public void initData() {
        super.initData();
        loadRecommendDishList();
    }

    @Override
    protected void reload() {
        super.reload();
        loadRecommendDishList();
    }

    public void loadRecommendDishList() {


        RetrofitObserverOption retrofitObserverOption = new RetrofitObserverOption();
        retrofitObserverOption.setViewMode(RetrofitObserverOption.FRAGMENT_VIEW_MODE);
        retrofitObserverOption.setNeedShowHttpErrorLayout(true);
        startQuest(RetrofitClient.getInstance().getRecommendApi().loadRecommendDishList(CommonUtil.getMerchantID(mContext)))
                .subscribe(new RetrofitObserver<List<Dish>>(retrofitObserverOption) {
                    @Override
                    protected void onHandleSuccess(List<Dish> dishes) {

                        if (dishes == null || dishes.size() == 0) {
                            EmptyData emptyData = new EmptyData();
                            emptyData.drawable = R.drawable.ic_empty_recommend_dish;
                            emptyData.text = "商家还没有推荐菜~";
                            EventBus.getDefault().post(emptyData);
                        } else {
                            mRecommendDishListAdapter.setDishList(dishes);
                            mListView.setAdapter(mRecommendDishListAdapter);
                        }


                    }
                });


    }
}
