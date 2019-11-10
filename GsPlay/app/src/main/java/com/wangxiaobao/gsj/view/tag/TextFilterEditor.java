package com.wangxiaobao.gsj.view.tag;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.text.InputFilter;
import android.util.AttributeSet;

import java.util.ArrayList;

/**
 * Created by candy on 17-9-7.
 */

public class TextFilterEditor extends TextInputEditText{
    ArrayList<InputFilter> inputFilterArrayList;

    public TextFilterEditor(Context context) {
        this(context, null);
    }

    public TextFilterEditor(Context context, AttributeSet attrs) {
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
