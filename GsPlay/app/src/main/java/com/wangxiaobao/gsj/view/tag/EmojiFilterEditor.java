package com.wangxiaobao.gsj.view.tag;

import android.content.Context;
import android.text.InputFilter;
import android.util.AttributeSet;

import java.util.ArrayList;

/**
 * Created by candy on 17-9-7.
 */

public class EmojiFilterEditor extends android.support.v7.widget.AppCompatEditText {
    ArrayList<InputFilter> inputFilterArrayList;

    public EmojiFilterEditor(Context context) {
        this(context, null);
    }

    public EmojiFilterEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (inputFilterArrayList==null){
            inputFilterArrayList= new ArrayList<>();
        }
        inputFilterArrayList.add(new EmojiFilter(context));
        setFilters(inputFilterArrayList.toArray(new InputFilter[]{}));
    }

    @Override
    public void setFilters(InputFilter[] filters) {
        if (inputFilterArrayList ==null)inputFilterArrayList = new ArrayList<>();
        if (filters != null) {
            for (InputFilter inputFilter : filters) {
                if (!(inputFilter instanceof EmojiFilter)) {
                    inputFilterArrayList.add(inputFilter);
                }
            }
        }
        super.setFilters(inputFilterArrayList.toArray(new InputFilter[]{}));
    }


}
