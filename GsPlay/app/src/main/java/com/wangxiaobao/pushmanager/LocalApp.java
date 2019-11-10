package com.wangxiaobao.pushmanager;

/**
 * 本地APP版本信息
 * Created by wenchaoy on 2017/10/30.
 */

public class LocalApp {
    private String packageName;
    private String appVersion;
    private boolean appIdentity;

    public boolean isAppIdentity() {
        return appIdentity;
    }

    public void setAppIdentity(boolean appIdentity) {
        this.appIdentity = appIdentity;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LocalApp){
            LocalApp localApp = (LocalApp) obj;
            return this.getPackageName().equals(localApp.getPackageName());
        }else {
            return false;
        }
    }
}
