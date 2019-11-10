package com.wangxiaobao.provider;


import android.net.Uri;

/**
 * 存放跟数据库有关的常量
 * @author jacp
 *
 */
public class ContentProviderConstant {

	public static final String CONTENTPROVIDER_PATH="DownloaderContentProvider";
	public static final String AUTHORITY = "com.wangxiaobao.filedownloader";

	public static final Uri DownloaderContentProvider_URI = Uri.parse("content://"+ ContentProviderConstant.AUTHORITY + "/" + CONTENTPROVIDER_PATH);

	public static final String EXTRA_MERCHANT_ACCOUNT="extra_merchant_account";

	public static final String EXTRA_MESSAGE="extra_message";


	/**
	 *获取店铺信息
	 */
	public static final int MERCHANT_INFO = 1;


	/**
	 *获取店铺账号
	 */
	public static final int MERCHANT_ACCOUNT = 1;


	/**
	 *获取店铺账号
	 */
	public static final String GET_MERCHANT_ACCOUNT = "GET_MERCHANT_ACCOUNT";




}
