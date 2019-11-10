package com.wangxiaobao.gsj.view;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.wangxiaobao.gsj.common.PriceTextWatcher;

/**
 * Created by candy on 17-9-7.
 */

public class PriceEditor extends android.support.v7.widget.AppCompatEditText {
    private static final String TAG = PriceEditor.class.getSimpleName();


    private PriceTextWatcher mPriceTextWaiter = new PriceTextWatcher(this);

    public PriceEditor(Context context) {
        this(context, null);
    }

    public PriceEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.addTextChangedListener(mPriceTextWaiter);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }


    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        mPriceTextWaiter.addTextChangedListener(watcher);
    }

}
