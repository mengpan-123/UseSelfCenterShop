package com.example.selfshopcenter;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.Touch;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.danikula.videocache.HttpProxyCacheServer;
import com.example.selfshopcenter.bean.*;
import com.example.selfshopcenter.commoncls.CommonData;
import com.example.selfshopcenter.commoncls.PrintUtil;
import com.example.selfshopcenter.commoncls.ToastUtil;
import com.example.selfshopcenter.commoncls.VideoApplication;
import com.example.selfshopcenter.net.RetrofitHelper;
import com.example.selfshopcenter.printer.UsbPrintManager;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IndexActivity extends AppCompatActivity {

    private int  Appvercode=0;
    private  VideoView  video;
    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1;

    private UsbPrintManager printer = null;

    String url = "http://www.ikengee.com.cn/test1/index.mp4";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        Appvercode=CommonData.getAppVersioncode(this);
        video = (VideoView) findViewById(R.id.video);
        video.setVideoURI(Uri.parse("android.resource://com.example.selfshopcenter/"+R.raw.index));
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //视频加载完成,准备好播放视频的回调
                mp.start();
                mp.setLooping(true);
            }
        });

       /* Uri uri = Uri.parse("http://www.ikengee.com.cn/test1/index.mp4");//将路径转换成uri
        video.setVideoURI(uri);//为视频播放器设置视频路径
        video.setMediaController(new MediaController(IndexActivity.this));//显示控制栏


        MediaController mc = new MediaController(this);
        mc.setVisibility(View.INVISIBLE);
        video.setMediaController(mc);

        video.start();


        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
            }
        });*/



//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
//            } else {
//                Log.i("aaa", "权限已申请");
//                initVideo();
//            }
//        }



        //设置底部的显示信息
        TextView storename=findViewById(R.id.storename);
        storename.setText("门店编号:"+ CommonData.khid);
        TextView storechinesename=findViewById(R.id.idnexstorename);
        storechinesename.setText(CommonData.khsname);
        TextView appmachine=findViewById(R.id.appmachine);
        appmachine.setText("设备编号:"+ CommonData.deviceid);


        //TextView useposid=findViewById(R.id.useposid);
        //useposid.setText("Version : "+ CommonData.posid);

        TextView appversion=findViewById(R.id.appversion);
        appversion.setText("Version : "+ CommonData.app_version);
        if (CommonData.app_version.equals("")){
            appversion.setText(CommonData.getAppVersion(this));//因为出现过版本号未获取到的的问题，所以这么写
        }


        //清空会员基础信息，清空购物车信息
        Call<ClearCarEntity>  ClearCar= RetrofitHelper.getInstance().ClearCarInfo(CommonData.khid, CommonData.posid);
        ClearCar.enqueue(new Callback<ClearCarEntity>() {
            @Override
            public void onResponse(Call<ClearCarEntity> call, Response<ClearCarEntity> response) {

                if (response.body() != null) {

                    if (response.body().getCode().equals("success")||response.body().getMsg().equals("没有符合条件的数据")) {

                    }
                    else
                    {
                        ToastUtil.showToast(IndexActivity.this, "购物车清除通知", response.body().getMsg());

                    }

                }
            }

            @Override
            public void onFailure(Call<ClearCarEntity> call, Throwable t) {

            }
        });


        //避免万一断网情况下，数据未正常清空。清空失败 这种情况呢？
        CommonData.hyMessage=null;
        CommonData.orderInfo=null;


        CommonData.player.reset();
        CommonData.player= MediaPlayer.create(this,R.raw.main);
        CommonData.player.start();
        CommonData.player.setLooping(false);



        //绑定 开始购物
        Button button_shape=findViewById(R.id.shopping);
        button_shape.setOnClickListener(new View.OnClickListener() {

            @Override
            public  void  onClick(View view) {

                //Toast.makeText(IndexActivity.this,"正在跳转，请等待",Toast.LENGTH_SHORT).show();
                //跳转到商品录入界面
                Intent intent = new Intent(IndexActivity.this, CarItemsActivity.class);
                //Intent intent = new Intent(IndexActivity.this, FinishActivity.class);
                startActivity(intent);

            }
        });


        //自助升级
        Button shengji=findViewById(R.id.uplevel);
        shengji.setOnClickListener(new View.OnClickListener() {

            @Override
            public  void  onClick(View view) {

                PrepareUpdateVersion();
            }
        });



        getPrinter();
        //printBill();

    }


    //USB打印机连接
    private void getPrinter() {
        printer = UsbPrintManager.getInstance();
        printer.init(this);
    }




    /**
     *
     * MembersLogin 会员登录
     * */

    public  void  MembersLogin(View view){


        ToastUtil.showToast(IndexActivity.this, "会员信息验证", "正在开发中");

    }



    //准备预升级
    public   void PrepareUpdateVersion(){

        try {

            Call<UpdateVersionEntity>  updateversion = RetrofitHelper.getInstance().UpdateVersion();
            updateversion.enqueue(new Callback<UpdateVersionEntity>() {
                @Override
                public void onResponse(Call<UpdateVersionEntity> call, Response<UpdateVersionEntity> response) {
                    if (null!=response){
                        if (response.body().getCode().equals("success")){
                            if (response.body().getData().getV_VERSION()> Appvercode){
                                //准备升级
                                url=response.body().getData().getV_Updatepath();

                                EnsureUPdate();
                            }
                            else{
                                ToastUtil.showToast(IndexActivity.this, "消息内容提示", "暂无可升级的内容");
                                return;
                            }

                        }
                        else
                        {
                            ToastUtil.showToast(IndexActivity.this, "消息内容提示", "暂无可升级的内容");
                            return;
                        }
                    }
                }

                @Override
                public void onFailure(Call<UpdateVersionEntity> call, Throwable t) {
                    ToastUtil.showToast(IndexActivity.this, "输入消息通知", t.toString());
                }
            });
        }
        catch(Exception ex){
            ToastUtil.showToast(IndexActivity.this, "输入消息通知", ex.toString());
        }
    }



    public   void  EnsureUPdate(){

        try {

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            //设置在什么网络情况下进行下载
            //request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);

            //设置通知栏标题
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            request.setTitle("任务进行中");
            request.setDescription("应用程序正在下载中");
            request.setAllowedOverRoaming(false);
            //设置文件存放目录（此处如果异常，需要再买呢文件中设置读写权限）
            request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "1.apk");

            DownloadManager downManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            long id = downManager.enqueue(request);

            //确认要下载时 ，先删除 sqlite里面的表数据
            //SQLiteDatabase db = dbHelper.getWritableDatabase();
            //db.delete(CommonData.tablename, null, null);


            queryDownloadProgress(this, id, downManager);
        }
        catch(Exception ex){
            ToastUtil.showToast(IndexActivity.this, "支付通知", "请输入商品条码进行支付");
            return;

        }
    }


    private void queryDownloadProgress(Context   context,long requestId, DownloadManager downloadManager) {


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

                            CommonData.dowloading=false;

                            isGoging=false;
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


                                downloadFileUri = FileProvider.getUriForFile(context,
                                        "com.ceshi.helloworld.fileProvider",
                                        new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "AIINBI.apk"));
                                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            }


                            if (downloadFileUri != null) {

                                install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(install);
                            }

                            //handler.obtainMessage(downloadManager.STATUS_SUCCESSFUL).sendToTarget();//发送到主线程，更新ui
                            break;
                        case DownloadManager.STATUS_FAILED://下载失败
                            isGoging=false;
                            //handler.obtainMessage(downloadManager.STATUS_FAILED).sendToTarget();//发送到主线程，更新ui
                            break;

                        case DownloadManager.STATUS_RUNNING://下载中
                            /**
                             * 计算下载下载率；
                             */
                            CommonData.dowloading=true; //说名正在下载中

                            int totalSize = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                            int currentSize = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                            int progress = (int) (((float) currentSize) / ((float) totalSize) * 100);
                            //handler.obtainMessage(downloadManager.STATUS_RUNNING, progress).sendToTarget();//发送到主线程，更新ui
                            break;

                        case DownloadManager.STATUS_PAUSED://下载停止
                            //handler.obtainMessage(DownloadManager.STATUS_PAUSED).sendToTarget();
                            break;

                        case DownloadManager.STATUS_PENDING://准备下载
                            //handler.obtainMessage(DownloadManager.STATUS_PENDING).sendToTarget();
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





    //查询打印机状态
//    private void getPrintStatus() {
//        String msg = "";
//        int iRet = PrintUtil.getPrintEndStatus();
//        switch (iRet) {
//            case 0:
//                msg = "正常";
//                break;
//            case 1:
//                msg = "打印机未连接或未上电";
//                break;
//            case 2:
//                msg = "打印机和调用库不匹配";
//                break;
//            case 3:
//                msg = "打印头打开";
//                break;
//            case 4:
//                msg = "切刀未复位";
//                break;
//            case 5:
//                msg = "打印头过热";
//                break;
//            case 6:
//                msg = "黑标错误";
//                break;
//            case 7:
//                msg = "纸尽";
//                break;
//            case 8:
//                msg = "纸将尽";
//                break;
//            case -1:
//                msg = "异常";
//                break;
//        }
//        if (iRet != 0) {
//            ToastUtil.showToast(IndexActivity.this, "打印机连接问题", msg);
//        }
//    }
}
