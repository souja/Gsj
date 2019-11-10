package com.wangxiaobao.gsj.common;

/**
 * Created by candy on 16-6-30.
 */


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wangxiaobao.waiter.R;
import com.wangxiaobao.gsj.enity.VersionEntity;
import com.wangxiaobao.gsj.view.RoundTextView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import okhttp3.Call;

/**
 * @author haifeng
 */

public class UpdateManager {
    private static final String TAG = UpdateManager.class.getSimpleName();
    private String mSavePath;

    private Context mContext;
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;

    private VersionEntity mVersionEntity;

    public UpdateManager() {
        EventBus.getDefault().register(this);
    }

    public UpdateManager(Context context, VersionEntity versionEntity) {
        this.mContext = context;
        mVersionEntity = versionEntity;
        EventBus.getDefault().register(this);
    }


    class ViewHolder {
        TextView updateInfo;
    }


    class Toast {

        public String message;

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showToast(Toast toast) {
        CommonUtil.showShortToast(toast.message);
    }


    public synchronized boolean hasRootAhth() {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();
            int exitValue = process.waitFor();
            if (exitValue == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.d("*** DEBUG ***", "Unexpected error - Here is what I know: "
                    + e.getMessage());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //静默安装
    public void installSlient(String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String cmd = "pm install -r " + path;
                Process process = null;
                DataOutputStream os = null;
                BufferedReader successResult = null;
                BufferedReader errorResult = null;
                StringBuilder successMsg = null;
                StringBuilder errorMsg = null;
                try {
                    //静默安装需要root权限
                    process = Runtime.getRuntime().exec("su");
                    os = new DataOutputStream(process.getOutputStream());
                    os.write(cmd.getBytes());
                    os.writeBytes("\n");
                    os.writeBytes("exit\n");
                    os.flush();
                    //执行命令
                    process.waitFor();
                    //获取返回结果
                    successMsg = new StringBuilder();
                    errorMsg = new StringBuilder();
                    successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    String s;
                    while ((s = successResult.readLine()) != null) {
                        successMsg.append(s);
                    }
                    while ((s = errorResult.readLine()) != null) {
                        errorMsg.append(s);
                    }


                    //显示结果
//                    Toast toast = new Toast();
//                    toast.message = "成功消息：" + successMsg.toString() + "\n" + "错误消息: " + errorMsg.toString();
//                    EventBus.getDefault().post(toast);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast toast = new Toast();
                    toast.message = e.getMessage();
                    EventBus.getDefault().post(toast);
                } finally {

                    try {
                        if (os != null) {
                            os.close();
                        }
                        if (process != null) {
                            process.destroy();
                        }
                        if (successResult != null) {
                            successResult.close();
                        }
                        if (errorResult != null) {
                            errorResult.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


//        CommonUtil.showShortToast("成功消息：" + successMsg.toString() + "\n" + "错误消息: " + errorMsg.toString());
    }


    public void showUpdateDialog() {

        final Dialog dialog = new Dialog(mContext);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(null);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        final View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_update, null);
        TextView versionName = (TextView) contentView.findViewById(R.id.version_name);
        versionName.setText(mVersionEntity.getAppVersion());
        final RoundTextView progress = (RoundTextView) contentView.findViewById(R.id.progress);
        ListView listView = (ListView) contentView.findViewById(R.id.version_log_listview);

        if (!TextUtils.isEmpty(mVersionEntity.getAppNote())) {

            ArrayAdapter<String> versionLogListAdapter = new ArrayAdapter<String>(mContext, R.layout.item_update) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    ViewHolder viewHolder;
                    if (convertView == null) {
                        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_update, null);
                        viewHolder = new ViewHolder();
                        viewHolder.updateInfo = (TextView) convertView.findViewById(R.id.update_info);
                        convertView.setTag(viewHolder);
                    } else {
                        viewHolder = (ViewHolder) convertView.getTag();
                    }
                    viewHolder.updateInfo.setText(getItem(position));
                    return convertView;
                }
            };

            String[] logArray = mVersionEntity.getAppNote().split("\\|");
            versionLogListAdapter.addAll(logArray);
            listView.setAdapter(versionLogListAdapter);
        } else {
            listView.setVisibility(View.GONE);
        }


        progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress.setBgColorResource(R.color.grey);
                progress.setText("正在下载...");
                progress.setOnClickListener(null);
                String url = mVersionEntity.getUrl();
                mSavePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Waiter";
                OkHttpUtils//
                        .get()//
                        .url(url)//
                        .build()//
                        .execute(new FileCallBack(mSavePath, "Waiter")//
                        {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                LogTool.E(TAG, "onError :", e);
                            }

                            @Override
                            public void onResponse(File response, int i) {
                                if (hasRootAhth()) {
                                    installSlient(response.getAbsolutePath());
                                } else {
                                    installApk(response);
                                }
                                dialog.dismiss();
                            }

                            @Override
                            public void inProgress(float progress1, long total, int id) {
                                super.inProgress(progress1, total, id);
                                progress.setText((int) (100 * progress1) + "%");
                                Log.e(TAG, "inProgress :" + (int) (100 * progress1));
                            }


                        });


            }
        });


        dialog.setContentView(contentView);
        dialog.show();
        WindowManager m = dialog.getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (d.getWidth() * 0.9);
        dialog.getWindow().setAttributes(p);
    }


    /**
     * 安装APK文件
     */
    private void installApk(File file) {
        if (!file.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }
}

