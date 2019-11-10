package com.wangxiaobao.gsj.enity.dish;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016-04-12.
 */
public class Dish implements Serializable {
    private String dishId;

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }


    private String vendorShopId;
    private String tag;
    private String salesVolume;
    private String isvalidate;
    private String dishName;
    private String recommendIndex;
    private String description;
    private String dishNameEng;
    private String dishNameOther;
    private String cookStyle;
    private String createDate;
    private String mealTypeId;
    private String chef;
    private String maxUnitPerRequest;
    private String regionId;
    private String status;
    private String defaultPicture;
    private String vendorDishId;
    private String updateDate;
    private String picture;
    private String unit;
    private String createUser;
    private String price;
    private String setFoodDescription;
    private String salePrice;
    private String updateUser;
    private String specifications;
    private String categoryId;
    private String categoryName;
    private String wxbDishId;
    private String recommendCount;
    private String recommendId;
    private String taste;
    private ArrayList<Dish> setFoodDetailResponses;
    private int evaluateCount;
    private int number;
    private int isSetFood;
    private int isPicReplace;
    private int isActive;
    private boolean isOpenMenu;
    private boolean isAdd;
    private boolean isSelect = false;
    private int priceShow = 0;
    private String vendorCategoryId;


    public String getGarnish() {
        return garnish;
    }

    public void setGarnish(String garnish) {
        this.garnish = garnish;
    }

    private String garnish;

    public String getDishSort() {
        return dishSort;
    }

    public void setDishSort(String dishSort) {
        this.dishSort = dishSort;
    }

    private String dishSort;


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean isAdd) {
        this.isAdd = isAdd;
    }


    public int getIsPicReplace() {
        return isPicReplace;
    }

    public void setIsPicReplace(int isPicReplace) {
        this.isPicReplace = isPicReplace;
    }


    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }


    public ArrayList<Dish> getSetFoodDetailResponses() {
        return setFoodDetailResponses;
    }

    public void setSetFoodDetailResponses(ArrayList<Dish> setFoodDetailResponses) {
        this.setFoodDetailResponses = setFoodDetailResponses;
    }


    public String getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(String vipPrice) {
        this.vipPrice = vipPrice;
    }

    private String vipPrice;

    public int getPriceShow() {
        return priceShow;
    }

    public void setPriceShow(int priceShow) {
        this.priceShow = priceShow;
    }


    public void setIsEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    public int getIsSetFood() {
        return isSetFood;
    }

    public void setIsSetFood(int isSetFood) {
        this.isSetFood = isSetFood;
    }

    public String getSetFoodDescription() {
        return setFoodDescription;
    }

    public void setSetFoodDescription(String setFoodDescription) {
        this.setFoodDescription = setFoodDescription;
    }


    public String getRecommendSort() {
        return recommendSort;
    }

    public void setRecommendSort(String recommendSort) {
        this.recommendSort = recommendSort;
    }

    private String recommendSort;

    public String getVendorCategoryId() {
        return vendorCategoryId;
    }

    public void setVendorCategoryId(String vendorCategoryId) {
        this.vendorCategoryId = vendorCategoryId;
    }


    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    private boolean isEnd;

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

    private boolean isFirst;

    public String getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(String recommendId) {
        this.recommendId = recommendId;
    }


    public boolean isOpenMenu() {
        return isOpenMenu;
    }

    public void setOpenMenu(boolean isOpenMenu) {
        this.isOpenMenu = isOpenMenu;
    }


    public String getRecommendCount() {
        return recommendCount;
    }

    public void setRecommendCount(String recommendCount) {
        this.recommendCount = recommendCount;
    }


    public boolean isTotal() {
        return isTotal;
    }

    public void setIsTotal(boolean isTotal) {
        this.isTotal = isTotal;
    }

    private boolean isTotal;

    private boolean isEmpty;
    private boolean isRecommended;

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setIsEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public boolean isRecommended() {
        return isRecommended;
    }

    public void setIsRecommended(boolean isRecommended) {
        this.isRecommended = isRecommended;
    }


    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    private String count;

    public String getWxbDishId() {
        return wxbDishId;
    }

    public void setWxbDishId(String wxbDishId) {
        this.wxbDishId = wxbDishId;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public String getVendorShopId() {
        return vendorShopId;
    }

    public void setVendorShopId(String vendorShopId) {
        this.vendorShopId = vendorShopId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(String salesVolume) {
        this.salesVolume = salesVolume;
    }

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public String getIsvalidate() {
        return isvalidate;
    }

    public void setIsvalidate(String isvalidate) {
        this.isvalidate = isvalidate;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getRecommendIndex() {
        return recommendIndex;
    }

    public void setRecommendIndex(String recommendIndex) {
        this.recommendIndex = recommendIndex;
    }

    public int getEvaluateCount() {
        return evaluateCount;
    }

    public void setEvaluateCount(int evaluateCount) {
        this.evaluateCount = evaluateCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDishNameEng() {
        return dishNameEng;
    }

    public void setDishNameEng(String dishNameEng) {
        this.dishNameEng = dishNameEng;
    }

    public String getDishNameOther() {
        return dishNameOther;
    }

    public void setDishNameOther(String dishNameOther) {
        this.dishNameOther = dishNameOther;
    }

    public String getCookStyle() {
        return cookStyle;
    }

    public void setCookStyle(String cookStyle) {
        this.cookStyle = cookStyle;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getMealTypeId() {
        return mealTypeId;
    }

    public void setMealTypeId(String mealTypeId) {
        this.mealTypeId = mealTypeId;
    }

    public String getChef() {
        return chef;
    }

    public void setChef(String chef) {
        this.chef = chef;
    }

    public String getMaxUnitPerRequest() {
        return maxUnitPerRequest;
    }

    public void setMaxUnitPerRequest(String maxUnitPerRequest) {
        this.maxUnitPerRequest = maxUnitPerRequest;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDefaultPicture() {
        return defaultPicture;
    }

    public void setDefaultPicture(String defaultPicture) {
        this.defaultPicture = defaultPicture;
    }

    public String getVendorDishId() {
        return vendorDishId;
    }

    public void setVendorDishId(String vendorDishId) {
        this.vendorDishId = vendorDishId;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String price) {
        this.salePrice = price;
    }


    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    @Deprecated
    public String getCategoryId() {
        return categoryId;
    }

    @Deprecated
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    @Override
    public String toString() {
        return "Dish{" +
                "dishId='" + dishId + '\'' +
                ", vendorShopId='" + vendorShopId + '\'' +
                ", tag='" + tag + '\'' +
                ", salesVolume='" + salesVolume + '\'' +
                ", isvalidate='" + isvalidate + '\'' +
                ", dishName='" + dishName + '\'' +
                ", recommendIndex='" + recommendIndex + '\'' +
                ", description='" + description + '\'' +
                ", dishNameEng='" + dishNameEng + '\'' +
                ", dishNameOther='" + dishNameOther + '\'' +
                ", cookStyle='" + cookStyle + '\'' +
                ", createDate='" + createDate + '\'' +
                ", mealTypeId='" + mealTypeId + '\'' +
                ", chef='" + chef + '\'' +
                ", maxUnitPerRequest='" + maxUnitPerRequest + '\'' +
                ", regionId='" + regionId + '\'' +
                ", status='" + status + '\'' +
                ", defaultPicture='" + defaultPicture + '\'' +
                ", vendorDishId='" + vendorDishId + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", picture='" + picture + '\'' +
                ", unit='" + unit + '\'' +
                ", createUser='" + createUser + '\'' +
                ", price='" + price + '\'' +
                ", setFoodDescription='" + setFoodDescription + '\'' +
                ", salePrice='" + salePrice + '\'' +
                ", updateUser='" + updateUser + '\'' +
                ", specifications='" + specifications + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", wxbDishId='" + wxbDishId + '\'' +
                ", recommendCount='" + recommendCount + '\'' +
                ", recommendId='" + recommendId + '\'' +
                ", taste='" + taste + '\'' +
                ", setFoodDetailResponses=" + setFoodDetailResponses +
                ", evaluateCount=" + evaluateCount +
                ", number=" + number +
                ", isSetFood=" + isSetFood +
                ", isPicReplace=" + isPicReplace +
                ", isActive=" + isActive +
                ", isOpenMenu=" + isOpenMenu +
                ", isAdd=" + isAdd +
                ", isSelect=" + isSelect +
                ", priceShow=" + priceShow +
                ", vendorCategoryId='" + vendorCategoryId + '\'' +
                ", garnish='" + garnish + '\'' +
                ", dishSort='" + dishSort + '\'' +
                ", vipPrice='" + vipPrice + '\'' +
                ", recommendSort='" + recommendSort + '\'' +
                ", isEnd=" + isEnd +
                ", isFirst=" + isFirst +
                ", isTotal=" + isTotal +
                ", isEmpty=" + isEmpty +
                ", isRecommended=" + isRecommended +
                ", count='" + count + '\'' +
                '}';
    }

}
