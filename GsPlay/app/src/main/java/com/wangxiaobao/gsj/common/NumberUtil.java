package com.wangxiaobao.gsj.common;


import android.text.Editable;

/**
 * Created by candy on 16-11-10.
 */
public class NumberUtil {

    private static final String TAG = NumberUtil.class.getSimpleName();

    public static String getPercent(int a, int b) {
        if (b == 0) return "暂无";
        int percent = (int) (((float) a / (float) b) * 100f + 0.5f);
        return percent + "%";
    }


    /**
     * 金额输入框中的内容限制（最大：小数点前五位，小数点后2位）
     *
     * @param edt
     */
    public static void judgeNumber(Editable edt) {
        judge(edt, 5, 2);
    }


    public static void judge(Editable editable, int beforeDot, int afterDot) {
        String temp = editable.toString();
        LogTool.saveLog(TAG, "temp:" + temp);
        int posDot = temp.indexOf(".");
        //直接输入小数点的情况
        if (posDot == 0) {
            editable.insert(0, "0");
            return;
        }
        //连续输入0
        if (temp.equals("00")) {
            editable.delete(1, 2);
            return;
        }
        //输入"08" 等类似情况
        if (temp.startsWith("0") && temp.length() > 1 && (posDot == -1 || posDot > 1)) {
            editable.delete(0, 1);
            return;
        }

        //连续输入小数点 ...
//        条件一:包含小数点
//        条件二:小数点一位的值为'.'
        if (posDot >= 0 && temp.indexOf('.', posDot + 1) >= 0) {
            editable.delete(posDot, posDot + 1);
            return;
        }

        //不包含小数点 不限制小数点前位数
        if (posDot < 0 && beforeDot == -1) {
            //do nothing 仅仅为了理解逻辑而已
            return;
        } else if (posDot < 0 && beforeDot != -1) {
            //不包含小数点 限制小数点前位数
            if (temp.length() <= beforeDot) {
                //do nothing 仅仅为了理解逻辑而已
            } else {
                editable.delete(beforeDot, beforeDot + 1);
            }
            return;
        }

        //如果包含小数点 限制小数点后位数
        if (temp.length() - posDot - 1 > afterDot && afterDot != -1) {
            editable.delete(posDot + afterDot + 1, posDot + afterDot + 2);//删除小数点后多余位数
        }
    }


//    // 方式一：
//    double f = 3.1516;
//    BigDecimal b = new BigDecimal(f);
//    double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//
//// 方式二：
//    new java.text.DecimalFormat("#.00").format(3.1415926);
//// #.00 表示两位小数 #.0000四位小数 以此类推…
//
//    // 方式三：
//    double d = 3.1415926;
//    String result = String.format("%.2f", d);
//// %.2f %. 表示 小数点前任意位数 2 表示两位小数 格式后的结果为f 表示浮点型。
//
////方法四：
//    Math.round(5.2644555 * 100) * 0.01d;
////String.format("%0" + 15 + "d", 23) 23不足15为就在前面补0
}
