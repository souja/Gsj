package com.wangxiaobao.gsj.home;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.wangxiaobao.gsj.base.BaseFragment;
import com.wangxiaobao.gsj.base.MainActivity;
import com.wangxiaobao.gsj.common.CommonUtil;
import com.wangxiaobao.gsj.http.RetrofitObserver;
import com.wangxiaobao.waiter.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class DocumentFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    @BindView(R.id.lisview)
    RecyclerView mRecyclerView;

    private DocumentImageListAdapter imageListAdapter;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DocumentFragment newInstance(String param1) {
        DocumentFragment fragment = new DocumentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);

        }
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
    public void initView(View view) {
        super.initView(view);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(20));

        imageListAdapter = new DocumentImageListAdapter(mContext);


        mRecyclerView.setAdapter(imageListAdapter);


        mMainActivity.setActionBarTitle("企业证照");
        mMainActivity.setEnglishTitle("Enterprise License");

//        mMainActivity.showSjx(R.id.card_sjx);


    }

    @Override
    public void initData() {
        super.initData();
        loadStoreInfo();
    }

    private void loadStoreInfo() {


        JSONObject result = new JSONObject();
        try {
            result.put("account", CommonUtil.getMerchantAccount(mContext));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());


        startQuest(mApi.getStoreInfo(body)).subscribe(new RetrofitObserver<StoreInfo>() {
            @Override
            protected void onHandleSuccess(StoreInfo storeInfo) {
//                MainActivity.mStoreInfo = storeInfo;
//                if (!TextUtils.isEmpty(MainActivity.mStoreInfo.getSubjectImg())) {
//                    String[] picList = MainActivity.mStoreInfo.getSubjectImg().split("\\|");
//                    imageListAdapter.addAll(Arrays.asList(picList));
//                }

            }


        });

    }


    @Override
    public int generateLayoutID() {
        return R.layout.fragment_document;
    }



}
