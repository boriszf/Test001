package com.example.myapplication.nettools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.example.myapplication.R;
import com.example.myapplication.models.OutputMeta;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AutoUpdater {
    // 下载安装包的网络路径
    private String apkUrl = "http://www.miliotech.com:8886/QingdaoServerInterface/TestVersion/";
    protected String checkUrl = apkUrl + "output-metadata.json";
    private String apkAppUrl = "";
    // 保存APK的文件名
    private static final String saveFileName = "borisApp.apk";
    private static File apkFile;
    // 下载线程
    private Thread downLoadThread;
    private int progress;// 当前进度
    // 应用程序Context
    private Context mContext;
    private boolean intercept = false;
    // 进度条与通知UI刷新的handler和msg常量
    private ProgressBar mProgress;
    private TextView txtStatus;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private static final int SHOWDOWN = 3;

    public AutoUpdater(Context context) {
        mContext = context;
        apkFile = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), saveFileName);
    }

    private void ShowUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setTitle("软件版本更新");
        builder.setMessage("有最新的软件包，请下载并安装!");
        builder.setPositiveButton("立即下载", (dialog, which) -> ShowDownloadDialog());
        builder.setNegativeButton("以后再说", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void ShowDownloadDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setCancelable(false);
        dialog.setTitle("软件版本更新");
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.progress, null);
        mProgress = v.findViewById(R.id.progress);
        txtStatus = v.findViewById(R.id.txtStatus);
        dialog.setView(v);
        dialog.setNegativeButton("取消", (dialog1, which) -> intercept = true);
        dialog.show();
        DownloadApk();
    }

    /**
     * 检查是否更新的内容
     */
    public void CheckUpdate() {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(checkUrl).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();//得到服务的版本信息

                    //得到本地App的版本信息
                    String localVersion = "1";
                    try {
                        localVersion = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                    //得到服务上的版本信息和apk的文件名称
                    String versionName = "1";
                    String outputFile = "";
                    Gson mydata = new Gson();
                    OutputMeta om = mydata.fromJson(result, OutputMeta.class);
                    if (om.getElements().length > 0) {
                        versionName = om.getElements()[0].getVersionName();
                        outputFile = om.getElements()[0].getOutputFile();
                    }

                    //比较本地版本与服务版本信息，决定是否需要下载apk
                    try {
                        Double lv = Double.valueOf(localVersion);
                        Double vn = Double.valueOf(versionName);
                        if (lv < vn) {
                            apkAppUrl = apkUrl + outputFile;
                            if (outputFile.length() > 0)
                                mHandler.sendEmptyMessage(SHOWDOWN);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    /**
     * 从服务器下载APK安装包
     */
    private void DownloadApk() {
        downLoadThread = new Thread(DownApkWork);
        downLoadThread.start();
    }

    private Runnable DownApkWork = new Runnable() {
        @Override
        public void run() {
            try {
                OkHttpClient client = new OkHttpClient.Builder().build();
                Request request = new Request.Builder().url(apkAppUrl).build();
                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    long length = body.contentLength();
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    InputStream ins = body.byteStream();
                    int count = 0;
                    byte[] buf = new byte[1024];
                    while (!intercept) {
                        int numread = ins.read(buf);
                        count += numread;
                        progress = (int) (((float) count / length) * 100);
                        // 下载进度
                        mHandler.sendEmptyMessage(DOWN_UPDATE);
                        if (numread <= 0) {
                            //下载完成100%
                            progress=100;
                            mHandler.sendEmptyMessage(DOWN_UPDATE);
                            break;
                        }
                        fos.write(buf, 0, numread);
                    }
                    fos.flush();
                    fos.close();
                    ins.close();
                    mHandler.sendEmptyMessage(DOWN_OVER);
                }
            } catch (Exception ex) {
                Toast.makeText(mContext, "APK包下载异常：" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
            }
        }
    };

    /**
     * 安装APK内容
     */
    private void installAPK() {
        try {
            if (!apkFile.exists()) {
                return;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//安装完成后打开新版本
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // 给目标应用一个临时授权
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//判断版本大于等于7.0
                //如果SDK版本>=24，即：Build.VERSION.SDK_INT >= 24，使用FileProvider兼容安装apk
                String packageName = mContext.getApplicationContext().getPackageName();
                String authority = new StringBuilder(packageName).append(".fileprovider").toString();
                Uri apkUri = FileProvider.getUriForFile(mContext, authority, apkFile);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            }
            mContext.startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());//安装完之后会提示”完成” “打开”。
        } catch (Exception e) {
            Toast.makeText(mContext, "APK包安装异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case SHOWDOWN:
                    ShowUpdateDialog();
                    break;
                case DOWN_UPDATE:
                    txtStatus.setText(progress + "%");
                    mProgress.setProgress(progress);
                    break;
                case DOWN_OVER:
                    Toast.makeText(mContext, "下载完毕", Toast.LENGTH_SHORT).show();
                    installAPK();
                    break;
                default:
                    break;
            }
        }
    };
}