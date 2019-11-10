package com.wangxiaobao.pushmanager;

/**
 * Created by wave on 2018/1/13.
 */

public class MerchantInfo {
    private String merchantId;

    public String getMerchantAccount() {
        return merchantAccount;
    }

    public void setMerchantAccount(String merchantAccount) {
        this.merchantAccount = merchantAccount;
    }

    private String merchantAccount;

    public MerchantInfo() {
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

}
