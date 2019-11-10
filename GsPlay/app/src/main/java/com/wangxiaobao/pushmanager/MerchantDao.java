package com.wangxiaobao.pushmanager;

import android.text.TextUtils;

import com.wangxiaobao.gsj.base.App;
import com.wangxiaobao.provider.ContentProviderUtil;

/**
 * Created by jack on 2018/1/25.
 */

public class MerchantDao {






    private String merchantAccount;




    private MerchantDao() {

    }



    public void  init(){
        initMerchantInfo();
    }





    private void  initMerchantInfo(){



        this.merchantAccount = ContentProviderUtil.getMerchantAccount(App.getContext());


        LogTool.saveLog("初始化店铺账号成功:"+this.merchantAccount);


    }

    //MerchantDao
    private static class SingletonHolder {
        private static MerchantDao INSTANCE = new MerchantDao(
        );
    }

    public static MerchantDao getInstance() {
        return SingletonHolder.INSTANCE;
    }





    public String getMerchantAccount(){

        return  merchantAccount;

    }

}
