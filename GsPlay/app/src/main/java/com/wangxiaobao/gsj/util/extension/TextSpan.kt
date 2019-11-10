package com.wangxiaobao.gsj.util.extension

import android.text.SpannableString
import android.text.style.RelativeSizeSpan

/**
 * Created by ijays on 2018/8/22.
 */
fun String.setRelativeText(relativeSize: Float, suffix: String): SpannableString {
    val text = this + suffix
    val ss = SpannableString(text)
    ss.setSpan(RelativeSizeSpan(relativeSize), ss.length - 1, ss.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
    return ss

}