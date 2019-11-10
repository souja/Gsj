package com.wangxiaobao.gsj.enity;

public class ComplainTypeModel extends BaseModel{

    /**
     * complaintTypeId : 1
     * name : 服务质量
     * sortCode : 1
     * createDate : 1571800670000
     * updateDate : 1571800675000
     * createUser : null
     * updateUser : null
     */

    private int complaintTypeId;
    private String name;
    private int sortCode;
    private long createDate;
    private long updateDate;
    private Object createUser;
    private Object updateUser;

    public int getComplaintTypeId() {
        return complaintTypeId;
    }

    public void setComplaintTypeId(int complaintTypeId) {
        this.complaintTypeId = complaintTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSortCode() {
        return sortCode;
    }

    public void setSortCode(int sortCode) {
        this.sortCode = sortCode;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public Object getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Object createUser) {
        this.createUser = createUser;
    }

    public Object getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Object updateUser) {
        this.updateUser = updateUser;
    }
}
