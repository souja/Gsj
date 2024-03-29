package com.wangxiaobao.gsj.common;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by candy on 17-9-7.
 */

public class PriceTextWatcher implements TextWatcher {


    //输入前内容
    private String mBeforeText;
    //输入后内容
    private String mAfterText;

    private static final String TAG = PriceTextWatcher.class.getSimpleName();
    //输入框
    private EditText mEditText;
    //匹配器
    private Pattern mPattern;
    private List<TextWatcher> mTextListWaiter = new ArrayList<>();

    public void addTextChangedListener(TextWatcher watcher) {
        mTextListWaiter.add(watcher);
    }

    public PriceTextWatcher(EditText editText) {
        this.mEditText = editText;
        /**
         * 正则表达式匹配
         * 条件一:如果以0开始,那么小数点前最多只有1位
         * 条件二:小数点后面最多只有2位
         * 条件三:如果不以0开始,小数点前面最多只有5位
         */
        mPattern = Pattern.compile("(([0]|(0[.]\\d{0,2}))|([1-9]\\d{0,4}(([.]\\d{0,2})?)))");
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.i(TAG, "beforeTextChanged:" + charSequence);
        mBeforeText = charSequence.toString();
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.i(TAG, "onTextChanged:" + charSequence);
        if (TextUtils.isEmpty(charSequence)) return;
        mAfterText = charSequence.toString();
        Matcher matcher = mPattern.matcher(mAfterText);
        if (matcher.matches()) {
            Log.i(TAG, "匹配");
        } else {
            Log.i(TAG, "不匹配");
            mEditText.setText(mBeforeText);
            //游标移动到最后一位
            mEditText.setSelection(mEditText.length());
        }

        for (TextWatcher textWatcher : mTextListWaiter) {
            textWatcher.onTextChanged(mEditText.getText(), i, i1, i2);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
