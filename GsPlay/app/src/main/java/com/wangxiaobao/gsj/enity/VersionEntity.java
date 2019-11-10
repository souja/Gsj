package com.wangxiaobao.gsj.enity;

/**
 * Created by candy on 16-6-30.
 */
public class VersionEntity {


//    {
//        code: "0",
//                errorMessage: "成功",
//            data: {
//        appVersionId: "2e947d58ad854b388087389a79a37815",
//                appVersion: "1.0.0",
//                appNote: null,
//                url: "http://appstore.wangxiaobao.com/FmtLs2oCa5mESk3oa_OIt_g4CsDM"
//    }
//    }


    private String appVersion;

    public String getAppNote() {
        return appNote;
    }

    public void setAppNote(String appNote) {
        this.appNote = appNote;
    }

    private String appNote;
    private String url;


    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public String toString() {
        return "VersionEntity{" +
                "appVersion='" + appVersion + '\'' +
                ", appNote=" + appNote +
                ", url='" + url + '\'' +
                '}';
    }
}


