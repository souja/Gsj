package com.wangxiaobao.gsj.util.helper

/**
 * Created by ijays on 2018/8/17.
 */
interface BaseEntity {
    /**
     *要索引的字段数据信息，例如联系人中对姓名进行索引，
     */
    fun getIndexField(): String
}