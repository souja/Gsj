package com.wangxiaobao.gsj.util;

import java.util.List;

/**
 * Created by ijays on 20/12/2017.
 */

public class ListUtil {
    /**
     * 判断List是否无数据
     *
     * @param list
     * @return
     */
    public static boolean isEmptyList(List list) {
        return list == null || list.isEmpty();
    }

    /**
     * 得到List的数据数量，null情况下返回0
     *
     * @param list
     * @return
     */
    public static int getListSize(List list) {
        return list == null ? 0 : list.size();
    }
}
