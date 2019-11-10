package com.wangxiaobao.gsj.home;

/**
 * Created by candy on 18-3-7.
 */

public class Comment {

    private String commentId;
    private String commentInfo;
    private long createDate;
    private String createUser;
    private String isvalidate;
    private String merchantId;
    private String merchantName;
    private String merchantResponse;
    private long responseDate;
    private String responseStatus;
    private String star;
    private String updateDate;
    private String updateUser;
    private String userId;
    private String userImage;
    private String userNickname;

    public String getAdvise() {
        return advise;
    }

    public void setAdvise(String advise) {
        this.advise = advise;
    }

    private String advise;


    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentInfo() {
        return commentInfo;
    }

    public void setCommentInfo(String commentInfo) {
        this.commentInfo = commentInfo;
    }

    public long getCreateDate() {
        return createDate;
    }


    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getIsvalidate() {
        return isvalidate;
    }

    public void setIsvalidate(String isvalidate) {
        this.isvalidate = isvalidate;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantResponse() {
        return merchantResponse;
    }

    public void setMerchantResponse(String merchantResponse) {
        this.merchantResponse = merchantResponse;
    }

    public long getResponseDate() {
        return responseDate;
    }


    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }


    @Override
    public String toString() {
        return "Comment{" +
                "commentId='" + commentId + '\'' +
                ", commentInfo='" + commentInfo + '\'' +
                ", createDate='" + createDate + '\'' +
                ", createUser='" + createUser + '\'' +
                ", isvalidate='" + isvalidate + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", merchantResponse='" + merchantResponse + '\'' +
                ", responseDate='" + responseDate + '\'' +
                ", responseStatus='" + responseStatus + '\'' +
                ", star='" + star + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", updateUser='" + updateUser + '\'' +
                ", userId='" + userId + '\'' +
                ", userImage='" + userImage + '\'' +
                ", userNickname='" + userNickname + '\'' +
                '}';
    }
}
