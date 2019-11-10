package com.wangxiaobao.gsj.base;

/**
 * Created by candy on 18-3-19.
 */

public class CrashMessage {
    public String targetType;
    public String target;
    public String monitorType;
    public String monitorStatus;
    public String moniterTime;


    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getMonitorType() {
        return monitorType;
    }

    public void setMonitorType(String monitorType) {
        this.monitorType = monitorType;
    }

    public String getMonitorStatus() {
        return monitorStatus;
    }

    public void setMonitorStatus(String monitorStatus) {
        this.monitorStatus = monitorStatus;
    }

    public String getMoniterTime() {
        return moniterTime;
    }

    public void setMoniterTime(String moniterTime) {
        this.moniterTime = moniterTime;
    }
}
