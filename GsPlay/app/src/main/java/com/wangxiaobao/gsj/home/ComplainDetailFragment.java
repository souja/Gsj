package com.wangxiaobao.gsj.home;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wangxiaobao.gsj.base.App;
import com.wangxiaobao.gsj.base.BaseFragment;
import com.wangxiaobao.gsj.common.AppConstants;
import com.wangxiaobao.gsj.common.CommonUtil;
import com.wangxiaobao.gsj.common.LogTool;
import com.wangxiaobao.gsj.enity.AddComplainModel;
import com.wangxiaobao.gsj.enity.ComplainTypeModel;
import com.wangxiaobao.gsj.enity.MapMerchantModel;
import com.wangxiaobao.gsj.enity.VoiceRecognizeModel;
import com.wangxiaobao.gsj.enity.result.JsonListResult;
import com.wangxiaobao.gsj.http.Constant;
import com.wangxiaobao.gsj.http.RetrofitClient;
import com.wangxiaobao.gsj.module.introduce.MapFragment;
import com.wangxiaobao.gsj.util.AudioManager;
import com.wangxiaobao.gsj.view.dialog.RecordDialogFragment;
import com.wangxiaobao.waiter.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by candy on 18-3-7.
 */

public class ComplainDetailFragment extends BaseFragment implements RecordDialogFragment.OnUploadAudioListener {

    @BindView(R.id.merchant_name)
    TextView merchantName;
    @BindView(R.id.merchant_address)
    TextView merchant_address;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.llContainer)
    LinearLayout llContainer;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.age)
    EditText age;
    @BindView(R.id.man_iv)
    ImageView manIv;
    @BindView(R.id.man_text)
    TextView manText;
    @BindView(R.id.man)
    RelativeLayout man;
    @BindView(R.id.woman_iv)
    ImageView womanIv;
    @BindView(R.id.woman_text)
    TextView womanText;
    @BindView(R.id.woman)
    RelativeLayout woman;
    @BindView(R.id.rg_complainType)
    RadioGroup mRadioGroup;

    private MapMerchantModel merchantModel;
    private Boolean isVoice;
    private String voiceUrl;
    private String inputContent;
    private View pureTextView;
    private ImageView voiceImg;
    private String complainType;

    List<ComplainTypeModel> complainTypeList;

    public static ComplainDetailFragment newInstance(MapMerchantModel model) {
        Bundle args = new Bundle();
        args.putSerializable(AppConstants.EXTRA_DATA, model);
        ComplainDetailFragment fragment = new ComplainDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        isVoice = false;
        voiceUrl = "";
        Bundle b = getArguments();
        if (b != null) {
            merchantModel = (MapMerchantModel) b.getSerializable(AppConstants.EXTRA_DATA);
            merchantName.setText(merchantModel.getMerchantName());
            merchant_address.setText(merchantModel.getTradeAreaName());
        }
        unselectWoman();
        unSelectMan();

        addPureTextView();
    }

    private void addPureTextView() {
        llContainer.removeAllViews();
        pureTextView = LayoutInflater.from(getContext()).inflate(R.layout.item_pure_text_input, null);
        TextView inputView = pureTextView.findViewById(R.id.etComment);
        inputView.setHint("最多可以输入200字");
        voiceImg = pureTextView.findViewById(R.id.iv_voice);
        pureTextView.findViewById(R.id.ll_voice).setOnClickListener(view -> {
            RecordDialogFragment dialog = new RecordDialogFragment();
            dialog.setTitle("按住说话 进行投诉");
            dialog.show(getChildFragmentManager(), "dialog");
            dialog.setOnUploadAudioListener(ComplainDetailFragment.this);
        });
        llContainer.addView(pureTextView);
    }

    private boolean canCommit() {
        if (complainType == null) {
            CommonUtil.showShortToast("请选择投诉类型");
            return false;
        }
        if (!isVoice) {
            TextView content = pureTextView.findViewById(R.id.etComment);
            inputContent = content.getText().toString();
            if (TextUtils.isEmpty(inputContent.trim())) {
                CommonUtil.showShortToast("请输入投诉内容");
                return false;
            }
        } else {
            if (TextUtils.isEmpty(inputContent.trim())) {
                CommonUtil.showShortToast("语音转化失败，请重新录入");
                return false;
            }
        }

        if (TextUtils.isEmpty(phone.getText().toString().trim())) {
            CommonUtil.showShortToast("请输入电话号码");
            return false;
        }

        if (!phone.getText().toString().startsWith("1")) {
            CommonUtil.showShortToast("电话号码必须以1开头");
            return false;
        }
        if (phone.getText().length() != 11) {
            CommonUtil.showShortToast("电话号码必须是11位");
            return false;
        }
        if (TextUtils.isEmpty(name.getText().toString().trim())) {
            CommonUtil.showShortToast("请输入姓名");
            return false;
        }
        if (TextUtils.isEmpty(age.getText().toString().trim())) {
            CommonUtil.showShortToast("请输入年龄");
            return false;
        }
        if (TextUtils.isEmpty(age.getText().toString().trim())) {
            CommonUtil.showShortToast("请输入年龄");
            return false;
        }
        if (Integer.valueOf(age.getText().toString()).intValue() > 100) {
            CommonUtil.showShortToast("年龄不得大于100");
            return false;
        }
        if (Integer.valueOf(age.getText().toString()).intValue() < 0) {
            CommonUtil.showShortToast("年龄不得小于0");
            return false;
        }

        if (getSex() == 0) {
            CommonUtil.showShortToast("请选择性别");
            return false;
        }
        return true;
    }

    @Override
    public int generateLayoutID() {
        return R.layout.fragment_complain_detail;
    }

    @SuppressLint("CheckResult")
    @OnClick(R.id.save)
    public void addComplain() {
        if (canCommit()) {
            showWaitDialog();
            //todo 雷波->凌晨调用此接口的时候失败了，白天再试试
            //{"age":"22","comlpaintTypeName":"其他","content":"测试","merchantId":"2088",
            // "mobile":"15366668888","sex":1,"userName":"哈哈哈","voiceUrl":""}
            //{"code":"1","message":"失败","data":"操作失败"}
            Log.i("fengmin", voiceUrl);
            AddComplainModel addModel = new AddComplainModel(complainType, inputContent,
                    phone.getText().toString(), getSex(), name.getText().toString(),
                    age.getText().toString(), merchantModel.getMerchantId(), voiceUrl);

            RetrofitClient.getInstance()
                    .addComplain(addModel)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(response -> {
                dismissWaitDialog();
                if (response.getCode() != 0) {
                    CommonUtil.showShortToast(response.getMessage());
                    LogTool.saveLog("提交评价==>" + response.getMessage());
                    return;
                }
                CommonUtil.showShortToast("提交投诉成功");
//                mMainActivity.showComplainListFragment_suc();
                mMainActivity.showCommitSuccess(merchantModel);
            }, throwable -> {
                dismissWaitDialog();
                CommonUtil.showShortToast("提交投诉失败");
            });

        }
    }

    public int getSex() {
        if (woman.isSelected()) {
            return 2;
        }
        if (man.isSelected()) {
            return 1;
        }
        return 0;
    }

    public void selectMan() {
        manText.setTextColor(Color.WHITE);
        manIv.setImageResource(R.drawable.man_b);
        man.setSelected(true);


    }

    public void unSelectMan() {
        manText.setTextColor(Color.parseColor("#ff999999"));
        manIv.setImageResource(R.drawable.man);
        man.setSelected(false);
    }

    @OnClick({R.id.ll_switch, R.id.ll_switchBottom})
    public void changeMerchant() {
        App.mApp.isLoadMerchantList = false;
        mMainActivity.prepareToCommitComment(MapFragment.TYPE_TO_COMPLAIN);
    }

    @OnClick()
    public void voiceBtn_click() {


    }

    @OnClick(R.id.man)
    public void changeMan() {


        if (!manText.isSelected()) {
            selectMan();
            unselectWoman();
        }

    }


    public void unselectWoman() {
        womanText.setTextColor(Color.parseColor("#ff999999"));
        womanIv.setImageResource(R.drawable.woman);
        woman.setSelected(false);
//        woman.setBackground(getResources().getDrawable(R.drawable.woman_grey_color));
    }


    public void selectWoman() {
        womanText.setTextColor(Color.WHITE);
        womanIv.setImageResource(R.drawable.woman_b);
        woman.setSelected(true);
//        woman.setBackground(getResources().getDrawable(R.drawable.woman_grey_color_select));
    }

    @OnClick(R.id.woman)
    public void changeWoman() {


        if (!woman.isSelected()) {
            selectWoman();
            unSelectMan();
        }


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addCommentSuccess(Constant.Event success) {
        if (success.code == 3) {
            mMainActivity.showComplainListFragment();
        }
    }

    @Override
    public void initData() {
        super.initData();
        mRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.rb_a:
                    complainType = complainTypeList.get(0).getName();
                    break;
                case R.id.rb_b:
                    complainType = complainTypeList.get(1).getName();
                    break;
                case R.id.rb_c:
                    complainType = complainTypeList.get(2).getName();
                    break;
                case R.id.rb_d:
                    complainType = complainTypeList.get(3).getName();
                    break;
                case R.id.rb_e:
                    complainType = complainTypeList.get(4).getName();
                    break;
                case R.id.rb_f:
                    complainType = complainTypeList.get(5).getName();
                    break;
                case R.id.rb_g:
                    complainType = complainTypeList.get(6).getName();
                    break;
                case R.id.rb_h:
                    complainType = complainTypeList.get(7).getName();
                    break;
                case R.id.rb_i:
                    complainType = complainTypeList.get(8).getName();
                    break;
                case R.id.rb_j:
                    complainType = complainTypeList.get(9).getName();
                    break;
                case R.id.rb_k:
                    complainType = complainTypeList.get(10).getName();
                    break;
                case R.id.rb_l:
                    complainType = complainTypeList.get(11).getName();
                    break;
                case R.id.rb_m:
                    complainType = complainTypeList.get(12).getName();
                    break;
                case R.id.rb_n:
                    complainType = complainTypeList.get(13).getName();
                    break;
            }
        });
        getComplainTypeList();
    }

    private Disposable disposable;

    private void getComplainTypeList() {
        showWaitDialog();
        disposable = RetrofitClient.getInstance()
                .getComplainTypeList(merchantModel != null ? merchantModel.getMerchantId() : "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        jsonListResult -> handleData(jsonListResult),
                        throwable -> {
                            dismissWaitDialog();
                            CommonUtil.showShortToast("投诉类型列表为空");
                        });
    }


    private void handleData(JsonListResult<ComplainTypeModel> modelList) {
        dismissWaitDialog();
        if (modelList.getData().size() == 0 || modelList == null) {
            showEmptyView();
            EmptyData emptyData = new EmptyData();
            emptyData.drawable = R.drawable.ic_empty_complain;
            emptyData.text = "商家还没有收到投诉~";
            EventBus.getDefault().post(emptyData);
        } else {
            complainTypeList = modelList.getData();
            for (int i = 0; i < complainTypeList.size(); i++) {
                ComplainTypeModel model = complainTypeList.get(i);
                switch (i) {
                    case 0:
                        RadioButton btnA = mDataContainer.findViewById(R.id.rb_a);
                        btnA.setText(model.getName());
                        btnA.setVisibility(View.VISIBLE);
                        btnA.setTag(0);
                        break;
                    case 1:
                        RadioButton btnB = mDataContainer.findViewById(R.id.rb_b);
                        btnB.setText(model.getName());
                        btnB.setVisibility(View.VISIBLE);
                        btnB.setTag(1);
                        break;
                    case 2:
                        RadioButton btnC = mDataContainer.findViewById(R.id.rb_c);
                        btnC.setText(model.getName());
                        btnC.setVisibility(View.VISIBLE);
                        btnC.setTag(2);
                        break;
                    case 3:
                        RadioButton btnD = mDataContainer.findViewById(R.id.rb_d);
                        btnD.setText(model.getName());
                        btnD.setVisibility(View.VISIBLE);
                        btnD.setTag(3);
                        break;
                    case 4:
                        RadioButton btnE = mDataContainer.findViewById(R.id.rb_e);
                        btnE.setText(model.getName());
                        btnE.setVisibility(View.VISIBLE);
                        btnE.setTag(4);
                        break;
                    case 5:
                        RadioButton btnF = mDataContainer.findViewById(R.id.rb_f);
                        btnF.setText(model.getName());
                        btnF.setVisibility(View.VISIBLE);
                        btnF.setTag(5);
                        break;
                    case 6:
                        RadioButton btnG = mDataContainer.findViewById(R.id.rb_g);
                        btnG.setText(model.getName());
                        btnG.setVisibility(View.VISIBLE);
                        btnG.setTag(6);
                        break;
                    case 7:
                        RadioButton btnH = mDataContainer.findViewById(R.id.rb_h);
                        btnH.setText(model.getName());
                        btnH.setVisibility(View.VISIBLE);
                        btnH.setTag(7);
                        break;
                    case 8:
                        RadioButton btnI = mDataContainer.findViewById(R.id.rb_i);
                        btnI.setText(model.getName());
                        btnI.setVisibility(View.VISIBLE);
                        btnI.setTag(8);
                        break;
                    case 9:
                        RadioButton btnJ = mDataContainer.findViewById(R.id.rb_j);
                        btnJ.setText(model.getName());
                        btnJ.setVisibility(View.VISIBLE);
                        btnJ.setTag(9);
                        break;
                    case 10:
                        RadioButton btnK = mDataContainer.findViewById(R.id.rb_k);
                        btnK.setText(model.getName());
                        btnK.setVisibility(View.VISIBLE);
                        btnK.setTag(10);
                        break;
                    case 11:
                        RadioButton btnL = mDataContainer.findViewById(R.id.rb_l);
                        btnL.setText(model.getName());
                        btnL.setVisibility(View.VISIBLE);
                        btnL.setTag(11);
                        break;
                    case 12:
                        RadioButton btnM = mDataContainer.findViewById(R.id.rb_m);
                        btnM.setText(model.getName());
                        btnM.setVisibility(View.VISIBLE);
                        btnM.setTag(12);
                        break;
                    case 13:
                        RadioButton btnN = mDataContainer.findViewById(R.id.rb_n);
                        btnN.setText(model.getName());
                        btnN.setVisibility(View.VISIBLE);
                        btnN.setTag(13);
                        break;
                }
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onUploadAudio(@NotNull String filePath) {
        showWaitDialog();

        RetrofitClient.getInstance().resolveAudio(filePath).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(it -> {
            dismissWaitDialog();
            if (it.getCode() != 0) {
//                    CommonUtil.showShortToast(it.getMessage());
                LogTool.saveLog("提交评价录音==>" + it.getMessage());
                return;
            }
            isVoice = true;
            VoiceRecognizeModel voiceRecognizeModel = it.getData();
            voiceUrl = voiceRecognizeModel.getVoiceUrl();
            inputContent = voiceRecognizeModel.getMessage();
            View v = null;

            llContainer.removeAllViews();
            v = LayoutInflater.from(getContext()).inflate(R.layout.item_voice_turn_commit, null);
            TextView content = v.findViewById(R.id.tv_turn_text);
            GifImageView voiceImg = v.findViewById(R.id.iv_voice_icon);
            content.setText(voiceRecognizeModel.getMessage());
            v.findViewById(R.id.iv_close).setOnClickListener(view -> {
                inputContent = "";
                voiceUrl = "";
                isVoice = false;
                AudioManager.INSTANCE.terminatePlay();
                addPureTextView();
            });
            voiceImg.setOnClickListener(view -> {
                if (AudioManager.INSTANCE.isPlaying()) {
                    return;
                }
                voiceImg.setImageResource(R.drawable.gif_voice_play_list);
                LogTool.saveLog("播放音频");

                GifDrawable gifDrawable = (GifDrawable) voiceImg.getDrawable();
                gifDrawable.start();
                AudioManager.INSTANCE.playOnlineAudio(voiceRecognizeModel.getVoiceUrl());
                AudioManager.INSTANCE.setOnCompletionListener(() -> gifDrawable.stop());
            });
            llContainer.addView(v);

        }, throwable -> {
            dismissWaitDialog();
            CommonUtil.showShortToast("音频解析失败");
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
