package com.wangxiaobao.gsj.user;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by candy on 17-5-12.
 */
@DatabaseTable(tableName = "tb_verify_code")
public class VerifyCode {
    @DatabaseField(id = true)
    String phoneNumber;
    @DatabaseField
    long time;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "VerifyCode{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", time=" + time +
                '}';
    }
}
