package com.wangxiaobao.gsj.home;

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

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class HonorFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.text_list)
    RecyclerView mTextList;


//    @BindView(R.id.image_list)
//    RecyclerView mImageList;


    private HonorListAdapter mHonorListAdapter;
//    private ImageListAdapter mImageListAdapter;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HonorFragment newInstance(String param1) {
        HonorFragment fragment = new HonorFragment();
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


    @Override
    public void initView(View view) {
        super.initView(view);

        mTextList.setLayoutManager(new LinearLayoutManager(mContext));

//        mImageList.setLayoutManager(new LinearLayoutManager(mContext));

        mHonorListAdapter = new HonorListAdapter(mContext);
//        mImageListAdapter = new ImageListAdapter(mContext);


        mTextList.setAdapter(mHonorListAdapter);
//        mImageList.setAdapter(mImageListAdapter);


        mMainActivity.setActionBarTitle("介绍");

        mMainActivity.setEnglishTitle("Introduce");

        mMainActivity.showSjx(R.id.honor_sjx);
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
                bindView();
            }


        });

    }


    private void bindView() {


//        if (!TextUtils.isEmpty(honorInfo)) {
//
//            List<String> textList = new ArrayList<>();
//            List<String> imageList = new ArrayList<>();
//            String[] textArray = honorInfo.split("\\|");
//
//            第一条说明|@IMGhttps://img.wangxiaobao.cc/gsj/pride/C1-0.jpg|第二条说明|@IMGhttps://img.wangxiaobao.cc/gsj/pride/C1-1.jpg
//            for (String text : textArray) {
//                if (text.contains("@IMG")) {
//                    String replaceText = text.replace("@IMG", "");
//                    imageList.add(replaceText);
//                } else {
//                textList.add(text);
//                }
//            }


//            List<Honor> list = new ArrayList<>();
//
//            List<String> removeImageList = new ArrayList<>();
//            for (int i = 0; i < textList.size(); i++) {
//                Honor honor = new Honor();
//                honor.setText(textList.get(i));
//
//                if (i < imageList.size()) {
//                    honor.setImage(imageList.get(i));
//                    removeImageList.add(imageList.get(i));
//                }
//                list.add(honor);
//            }
//
//            imageList.removeAll(removeImageList);
//
//
//            for (String image : imageList) {
//                Honor honor = new Honor();
//                honor.setImage(image);
//            }


//            mHonorListAdapter.setData(textList);
//
//        } else {
//
//            EmptyData emptyData = new EmptyData();
//            emptyData.drawable = R.drawable.ic_empty_honor;
//            emptyData.text = "商家还没有荣誉~";
//            EventBus.getDefault().post(emptyData);
//
//
//        }


    }

    @Override
    public int generateLayoutID() {
        return R.layout.fragment_honor;
    }


//    @Override
//    public void onClick(View v) {
//
//    }
}
