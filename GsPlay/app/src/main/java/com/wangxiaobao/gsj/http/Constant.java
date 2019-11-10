package com.wangxiaobao.gsj.http;


import com.wangxiaobao.waiter.BuildConfig;

public class Constant {
    public static final String UPLOAD_ZIP_URL = "http://www.wangxiaobao.cc/tc-web/common/tcp/sendLog2OrderPrintClient.htm";
    public final static String HEADER_KEY = "X-Requested-With";
    public final static String HEADER_VALUE = "XMLHttpRequest";
    public final static String HOST_QI_NIU = "https://adv.wangxiaobao.cc/";
    private final static String DEV_HOST = "https://dev.wangxiaobao.co/";
    private final static String TEST_HOST = "https://www.wangxiaobao.co/";
    private final static String PREVIEW_HOST = "https://preview.wangxiaobao.co/";
    private final static String PRODUCTION_HOST = "https://www.wangxiaobao.cc/";
    public static final String QINIU_URL = "https://appstore.wangxiaobao.cc/";


    public static final String HOST = getHostWebBaseUrl();


    public static final String BASE_URL = "";
    public final static String HOST_WEB_BASE_URL = "";
    public final static String HOST_MANAGE_BASE_URL = "";


    public static String DATA_ACTION = "tc-web/web/";
    public static final String DATA_URL = HOST + DATA_ACTION;


    public static final String ACTION_LOGIN = "login/app/noErp/doLogin.htm";
    public static final String LOGIN_URL = "login/app/noErp/doLogin.htm";
    public final static String ACTION_SAVE_SERVICE_LIST = "merchant/noErp/addDinnerTime.htm";

    public final static String ACTION_GET_SERVICE_LIST = "merchant/noErp/findDinnerTimeByCondition.htm";
    public final static String ACTION_FIND_SERVICE_TYPE_LIST = "constantCode/findByType.htm";

    public final static String ACTION_GET_QINIU_TOKEN = "common/qiniu/getQiniuToken.htm";
    public final static String ACTION_UPDATE_PASSWORD = "user/updatePassword.htm";

    public final static String ACTION_FIND_TABLE_LIST = "tableResources/findPageBy.htm";

    public final static String ACTION_CONFIG_TABLE = "tableResources/setQrCodeUrl.htm";
    public final static String ACTION_SET_URL_CONFIG = "merchant/setUrlConfig.htm";

    public final static String ACTION_GET_MERCHANT = "merchant/noErp/merchantInfo.htm";
    public final static String ACTION_UPDATE_LOGON = "merchant/noErp/updateLogo.htm";


    public final static String ACTION_GET_VERIFY_CODE = "login/app/noErp/verifyCode.htm";
    public final static String ACTION_GET_DISH_UTILS = "constantCode/getDishUnits.htm";


    /**
     * get erp merchant
     */

    public final static String ACTION_GET_ERP_MERCHANT_NAME = "api/wzp/merchant/erp/getERPMerchantInfo.htm";
    public final static String ACTION_UPDATE_RECOMMEND_DISH_SORT = "dishPopRecommend/updateSort/recommendIds/";
    public final static String ACTION_DELETE_RECOMMEND = "/dishPopRecommend/deleteRecommends/recommendIds/";
    public final static String ACTION_QUERY_DISH_RECOMMEND_DISH_LIST = "dishPopRecommend/findDishPopRecommends.htm";
    public final static String ACTION_QUERY_UPDATE_INFO = "appversion/findCurrentVersion.htm";


    public final static String ACTION_CHECK_VERIFY_CODE = "login/app/noErp/accountStrengthen/isValidate.htm";
    public final static String ACTION_GET_BIND_PHONE_NUMBER = "accountStrengthen/updatePwdNewYZ.htm";
    public final static String ACTION_RESET_PASSWORD = "login/app/noErp/accountStrengthen/updatePwdNew.htm";
    public final static String ACTION_OPERATION_LOGIN = "accountStrengthen/operatorLanding.htm";
    public final static String ACTION_BIND_PHONE = "accountStrengthen/updatePhone.htm";


    public final static String ACTION_DELETE_QR_URL = "tableResources/updateQrCodeUrl.htm";
    public final static String ACTION_SET_ALL_QRCODE = "tableResources/setAllQrCodeUrl.htm";


    public final static String ACTION_RESET_MENU_BACKGROUND_URL = "merchant/resetMenuBackgroundUrl.htm";
    public final static String ACTION_RESET_PAY_BACKGROUND_URL = "merchant/resetPayBackgroundUrl.htm";


    public static final String WEB_HOST = BuildConfig.SERVER_MODE == 1 ? DEV_HOST
            : (BuildConfig.SERVER_MODE == 2 ? TEST_HOST
            : (BuildConfig.SERVER_MODE == 3 ? PREVIEW_HOST : PRODUCTION_HOST));

    public static final String getHost() {

        String host;
        switch (BuildConfig.SERVER_MODE) {
            case 1:
                host = "http://develop.source3g.com:14100/";
                break;
            //debug  测试环境
            case 2:
                host = "http://dev.source3g.com:14100/";
//                host = "http://dev.source3g.com:13016/";
                break;
            //preview
            case 3:
                host = "http://preview.source3g.com:14100/";
                break;
            case 4:
//                    https://decoder.wangxiaobao.com/  集码器app权限正式环境地址
                host = "http://api.source3g.com/";
                break;
            default:
                host = "http://api.source3g.com/";
                break;
        }
        return host;
    }


    public static String HOST_WEB_BASE = getHostWebBaseUrl();

    public static String getHostWebBaseUrl() {
        String host = "http://47.108.31.18:8082";
/*
        switch (BuildConfig.SERVER_MODE) {
            case 1:
                host = "http://dev.wangxiaobao.co/";
                break;
            //debug
            case 2:
                host = "http://www.wangxiaobao.co/";
                break;
            //preview
            case 3:
                host = "http://preview.wangxiaobao.co/";
                break;
            case 4:
                host = "http://www.wangxiaobao.cc/";
                break;
            default:
                host = "http://www.wangxiaobao.cc/";
                break;
        }
*/
        return host;
    }


    public static class Event {
        public int code;
    }
}


