package com.wangxiaobao.gsj.view.tag;

import android.content.Context;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;

import com.wangxiaobao.gsj.common.CommonUtil;

/**
 * Created by coder4 on 15/11/18.
 */
public class EmojiFilter implements InputFilter {


    private Context mContext;


    public boolean getIsEmoji(char codePoint) {
        if ((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)))
            return false;
        return true;
    }


    public EmojiFilter(Context context) {
        super();
        this.mContext = context;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                               int dend) {
        StringBuffer buffer = new StringBuffer();
        for (int i = start; i < end; i++) {
            char codePoint = source.charAt(i);
            if (!getIsEmoji(codePoint)) {
                buffer.append(codePoint);
            } else {
                CommonUtil.showShortToast(mContext, "不能输入表情");
                i++;
                continue;
            }
        }
        if (source instanceof Spanned) {
            SpannableString sp = new SpannableString(buffer);
            TextUtils.copySpansFrom((Spanned) source, start, end, null,
                    sp, 0);
            return sp;
        } else {
            return buffer;
        }
    }

}