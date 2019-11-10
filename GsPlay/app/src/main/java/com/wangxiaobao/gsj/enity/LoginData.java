package com.wangxiaobao.gsj.enity;

/**
 * Created by Administrator on 2016-04-25.
 */
public class LoginData {
    String isValidate;
    String isvalidate;
    String userId;
    String account;
    String userName;
    String merchantId;
    String identity;
    String erpMerchantId;
    private String isPhone;
    private String administratorNo;

    public String getMerchantAccount() {
        return merchantAccount;
    }

    public void setMerchantAccount(String merchantAccount) {
        this.merchantAccount = merchantAccount;
    }

    private String merchantAccount;

    public String getWhetherERPMerchant() {
        return whetherERPMerchant;
    }

    public void setWhetherERPMerchant(String whetherERPMerchant) {
        this.whetherERPMerchant = whetherERPMerchant;
    }

    private String whetherERPMerchant;

    public String getIsPhone() {
        return isPhone;
    }

    public void setIsPhone(String isPhone) {
        this.isPhone = isPhone;
    }

    public String getAdministratorNo() {
        return administratorNo;
    }

    public void setAdministratorNo(String administratorNo) {
        this.administratorNo = administratorNo;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    String logoUrl;


    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    String merchantName;
    public String getIsValidate() {
        return isValidate;
    }

    public void setIsValidate(String isValidate) {
        this.isValidate = isValidate;
    }

    public String getIsvalidate() {
        return isvalidate;
    }

    public void setIsvalidate(String isvalidate) {
        this.isvalidate = isvalidate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getErpMerchantId() {
        return erpMerchantId;
    }

    public void setErpMerchantId(String erpMerchantId) {
        this.erpMerchantId = erpMerchantId;
    }


    @Override
    public String toString() {
        return "LoginData{" +
                "isValidate='" + isValidate + '\'' +
                ", isvalidate='" + isvalidate + '\'' +
                ", userId='" + userId + '\'' +
                ", account='" + account + '\'' +
                ", userName='" + userName + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", identity='" + identity + '\'' +
                ", erpMerchantId='" + erpMerchantId + '\'' +
                ", isPhone='" + isPhone + '\'' +
                ", administratorNo='" + administratorNo + '\'' +
                ", whetherERPMerchant='" + whetherERPMerchant + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                ", merchantName='" + merchantName + '\'' +
                '}';
    }
}
