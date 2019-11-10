package com.wangxiaobao.gsj.log;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.wangxiaobao.gsj.base.App;
import com.wangxiaobao.gsj.common.HttpUtil;

import com.wangxiaobao.gsj.http.Constant;
import com.wangxiaobao.pushmanager.LogTool;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by candy on 17-6-8.
 */
public class QiNiuTool {
    public static void uploadFile(String strPath,String localFileName,String zipFileName) {
        zipAndUploadFileToQINIU(App.getContext(), strPath, localFileName, zipFileName);
    }

    public static void queryQiNiuToken( final String filePath, final String zipFileName) {
        LogTool.saveLog("queryQiNiuToken");
        String url = PrinterHelperConfig.QINIUYUN_URL;
        HashMap<String, String> params = new HashMap<>();
        params.put("bucket", "wangappstore");
        params.put("key", zipFileName);

        OkHttpUtils.get().url(url)
                .addParams("bucket", "wangappstore")
                .addParams("key",zipFileName)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        QiNiuToken qiNiuToken =new Gson().fromJson(response,QiNiuToken.class);
                        if (qiNiuToken.getCode().equals("0")){
                            uploadFile(qiNiuToken, filePath, zipFileName);
                        }
                    }
                });

    }

    public static void uploadFile(QiNiuToken token, final String filePath, final String logZipFileName) {
        LogTool.saveLog("uploadFile token:" + token.getData() + "        logZipFileName:" + logZipFileName);
        Configuration config = new Configuration.Builder().build();
        UploadManager uploadManager = new UploadManager(config);
        uploadManager.put(filePath + File.separator + logZipFileName, logZipFileName, token.getData(), new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                LogTool.saveLog("test uploadFile complete");
                LogTool.saveLog("key:"+key);
                if (response != null) {
                    if (!info.isOK()) {
                        LogTool.saveLog("uploadFile fail");
                        return;
                    } else {
                        new File(filePath, logZipFileName).delete();
                        final String upload_url = Constant.UPLOAD_ZIP_URL;

                        LogTool.saveLog("test uploadFile success");

                        QiNiuResult result = (QiNiuResult) JsonUtil.jsonToObject(response.toString(), QiNiuResult.class);
                        String picKey = result.getKey() == null ? "" : result.getKey();
                        String picUrl = Constant.QINIU_URL + picKey;
                        LogTool.saveLog("test upload_url:"+picUrl);
                        uploadURL(upload_url,picUrl);
                    }
                } else {
                    LogTool.saveLog("uploadFile response is null");
                }

            }
        }, null);
    }

    private static void uploadURL(String uploadURL, String url){

//        OkHttpUtils
//                .get()
//                .url(uploadURL)
//                .addParams("type", url)
//                .addParams("sn", "THF001")
//                .build()
//                .execute(new StringCallback()
//                {
//                    @Override
//                    public void onError(Request request, Exception e)
//                    {
//                        LogTool.saveLog("上传url失败");
//                    }
//
//                    @Override
//                    public void onResponse(String response)
//                    {
//                    LogTool.saveLog("上传url成功");
//                    }
//                });
    }

    public static void zipAndUploadFileToQINIU(final Context context, final String path, final String localFileName, final String zipFileName) {
        LogTool.saveLog("zipAndUploadFileToQINIU");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LogTool.saveLog("path:"+path+" localFileName:"+localFileName+"  zipFileName:"+zipFileName);
                    if (new File(path, localFileName).exists()) {
                        File zipFile = new File(path, zipFileName);
                        if (zipFile.exists()) {
                            zipFile.delete();
                            LogTool.saveLog("exist zip file ,delete it");
                        } else {
                            LogTool.saveLog("zip file not exist");
                        }
                        String src = path + localFileName;
                        String des = path  + zipFileName;
                        LogTool.saveLog("src:"+src+"   des:"+des);
                        ZipUtil.zip(src,des );
                        File logZipFile = new File(path, zipFileName);
                        if (logZipFile.exists()) {
                            queryQiNiuToken( path, zipFileName);
                        } else {
                            LogTool.saveLog("log file zip not found");
                        }
                    } else {
                        LogTool.saveLog("log src file not found");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    LogTool.saveLog("compress log file fail");
                }


            }
        }).start();
    }
}
