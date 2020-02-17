package com.example.selfshopcenter.commoncls;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;

import androidx.core.content.FileProvider;

import java.io.File;


public class DownLoadRunnable  implements Runnable {

    private String url;
    private Handler handler;
    private Context mContext;

    public DownLoadRunnable(Context context, String url, Handler handler) {
        this.mContext = context;
        this.url = url;
        this.handler = handler;
    }

    @Override
    public void run() {
        //设置线程优先级为后台，这样当多个线程并发后很多无关紧要的线程分配的CPU时间将会减少，有利于主线程的处理
        //Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        //具体下载方法
        startDownload();
    }

    private long startDownload() {
        //获得DownloadManager对象
        DownloadManager downloadManager=(DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        //获得下载id，这是下载任务生成时的唯一id，可通过此id获得下载信息
        long requestId= downloadManager.enqueue(CreateRequest(url));
        //查询下载信息方法
        queryDownloadProgress(requestId,downloadManager);
        return  requestId;
    }

    private void queryDownloadProgress(long requestId, DownloadManager downloadManager) {


        DownloadManager.Query query=new DownloadManager.Query();
        //根据任务编号id查询下载任务信息
        query.setFilterById(requestId);
        try {
            boolean isGoging=true;
            while (isGoging) {
                Cursor cursor = downloadManager.query(query);
                if (cursor != null && cursor.moveToFirst()) {

                    //获得下载状态
                    int state = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    switch (state) {
                        case DownloadManager.STATUS_SUCCESSFUL://下载成功
                            isGoging=false;

                            //然后实现自动安装
                            handler.obtainMessage(downloadManager.STATUS_SUCCESSFUL).sendToTarget();//发送到主线程，更新ui

                            Uri downloadFileUri;
                            Intent install = new Intent(Intent.ACTION_VIEW);
                            //调用安装方法,进行自动升级
                            //Uri downloadFileUri = downloadManager.getUriForDownloadedFile(requestId);
                            //Uri downloadFileUri = DownloadManager.COLUMN_LOCAL_URI;
                            boolean haveInstallPermission;
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { // 6.0以下
                                downloadFileUri = downloadManager.getUriForDownloadedFile(requestId);

                            }
                            else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
                            { // 6.0 - 7.0
                                String uriString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                                File apkFile = new File(Uri.parse(uriString).getPath());
                                downloadFileUri = Uri.fromFile(apkFile);

                            } else { // Android 7.0 以上

                                //haveInstallPermission = getPackageManager().canRequestPackageInstalls();  //需要 level版本支持


                                downloadFileUri = FileProvider.getUriForFile(mContext,
                                        "com.example.selfshopcenter.fileProvider",
                                        new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "AIINBI_2.apk"));
                                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            }


                            if (downloadFileUri != null) {

                                install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(install);
                            }


                            break;
                        case DownloadManager.STATUS_FAILED://下载失败
                            isGoging=false;
                            handler.obtainMessage(downloadManager.STATUS_FAILED).sendToTarget();//发送到主线程，更新ui
                            break;

                        case DownloadManager.STATUS_RUNNING://下载中
                            /**
                             * 计算下载下载率；
                             */
                            int totalSize = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                            int currentSize = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                            int progress = (int) (((float) currentSize) / ((float) totalSize) * 100);
                            handler.obtainMessage(downloadManager.STATUS_RUNNING, progress).sendToTarget();//发送到主线程，更新ui
                            break;

                        case DownloadManager.STATUS_PAUSED://下载停止
                            handler.obtainMessage(DownloadManager.STATUS_PAUSED).sendToTarget();
                            break;

                        case DownloadManager.STATUS_PENDING://准备下载
                            handler.obtainMessage(DownloadManager.STATUS_PENDING).sendToTarget();
                            break;
                    }
                }
                if(cursor!=null){
                    cursor.close();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private DownloadManager.Request CreateRequest(String url) {

        DownloadManager.Request  request=new DownloadManager.Request(Uri.parse(url));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);// 隐藏notification

        request.setAllowedNetworkTypes(request.NETWORK_WIFI);//设置下载网络环境为wifi

        request.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS,"MyApp.app");//指定apk缓存路径，默认是在SD卡中的Download文件夹

        return  request;
    }

}
