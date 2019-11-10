package com.wangxiaobao.gsj.enity.dish;


/**
 * Created by Administrator on 2016-04-12.
 */
public class RecommendDish {
    private String recommendId;
    private String vendorShopId;
    private String referDishId;
    private String vendorCategoryId;
    private String recommendContent;
    private String recommendTitle;
    private String salePriceOld;
    private String salePriceNew;
    private String updateDate;
    private String updateUser;
    private String createDate;
    private String createUser;
    private String dishName;
    private String dishPic;

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    private String isActive;

    public String getIsValidate() {
        return isValidate;
    }

    public void setIsValidate(String isValidate) {
        this.isValidate = isValidate;
    }

    public String getWxbDishId() {
        return wxbDishId;
    }

    public void setWxbDishId(String wxbDishId) {
        this.wxbDishId = wxbDishId;
    }

    private String isValidate;
    private String wxbDishId;

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getDishPic() {
        return dishPic;
    }

    public void setDishPic(String dishPic) {
        this.dishPic = dishPic;
    }


    public String getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(String recommendId) {
        this.recommendId = recommendId;
    }

    public String getVendorShopId() {
        return vendorShopId;
    }

    public void setVendorShopId(String vendorShopId) {
        this.vendorShopId = vendorShopId;
    }

    public String getReferDishId() {
        return referDishId;
    }

    public void setReferDishId(String referDishId) {
        this.referDishId = referDishId;
    }

    public String getVendorCategoryId() {
        return vendorCategoryId;
    }

    public void setVendorCategoryId(String vendorCategoryId) {
        this.vendorCategoryId = vendorCategoryId;
    }

    public String getRecommendContent() {
        return recommendContent;
    }

    public void setRecommendContent(String recommendContent) {
        this.recommendContent = recommendContent;
    }

    public String getRecommendTitle() {
        return recommendTitle;
    }

    public void setRecommendTitle(String recommendTitle) {
        this.recommendTitle = recommendTitle;
    }

    public String getSalePriceOld() {
        return salePriceOld;
    }

    public void setSalePriceOld(String salePriceOld) {
        this.salePriceOld = salePriceOld;
    }

    public String getSalePriceNew() {
        return salePriceNew;
    }

    public void setSalePriceNew(String salePriceNew) {
        this.salePriceNew = salePriceNew;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
}
