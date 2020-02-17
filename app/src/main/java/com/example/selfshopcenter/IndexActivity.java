package com.example.selfshopcenter;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.os.Handler;
import android.os.Message;
import android.text.method.Touch;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RelativeLayout;
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
import com.example.selfshopcenter.commoncls.DownLoadRunnable;
import com.example.selfshopcenter.commoncls.PrintUtil;
import com.example.selfshopcenter.commoncls.ToastUtil;
import com.example.selfshopcenter.commoncls.VideoApplication;
import com.example.selfshopcenter.net.RetrofitHelper;
import com.example.selfshopcenter.net.UpdateDialog;
import com.example.selfshopcenter.printer.UsbPrintManager;
import com.example.selfshopcenter.vediocache.App;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IndexActivity extends AppCompatActivity {

    private int  Appvercode=0;
    private  VideoView  video;
    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1;

    private UpdateDialog downloadDialog;

    private String  posUse="0";


    private UsbPrintManager printer = null;

    String  updateurl = "http://52.81.85.108:8080/uploadapk/AIINBI_2.apk";
    VideoView  videoView;

    String VIDEO_URL = "http://52.81.85.108:8080/uploadapk/index.mp4";  //视频播放的文件

    //下载的后台
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case DownloadManager.STATUS_SUCCESSFUL:
                    downloadDialog.setProgress(100);
                    canceledDialog();
                    Toast.makeText(IndexActivity.this, "下载任务已经完成！", Toast.LENGTH_SHORT).show();
                    break;

                case DownloadManager.STATUS_RUNNING:
                    //int progress = (int) msg.obj;
                    downloadDialog.setProgress((int) msg.obj);
                    //canceledDialog();
                    break;

                case DownloadManager.STATUS_FAILED:
                    canceledDialog();
                    break;

                case DownloadManager.STATUS_PENDING:
                    showDialog();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        Appvercode=CommonData.getAppVersioncode(this);


        videoView=findViewById(R.id.video);

        HttpProxyCacheServer proxy = getProxy();
        String proxyUrl = proxy.getProxyUrl(VIDEO_URL);
        try {
            videoView.setVideoPath(proxyUrl);


        } catch (Exception e) {
            Toast.makeText(this,"播放失败",Toast.LENGTH_SHORT);
            e.printStackTrace();
        }
        //以下是视频成功播放并且缓存的首要条件
        //1:主要下面这一段是解决视频播放黑屏的重难点，让mp.start();
        //2:需要在mainfest中添加android:name="com.example.thesameproc.App"属性

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                mp.start();
                mp.setLooping(true);

            }
        });



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

                //查询收音机使用状态
                Call<SearchPosEntity>  search= RetrofitHelper.getInstance().SearchUseStatus();
                search.enqueue(new Callback<SearchPosEntity>() {
                    @Override
                    public void onResponse(Call<SearchPosEntity> call, Response<SearchPosEntity> response) {

                        if (response.body() != null) {

                            if (response.body().getCode().equals("success")) {
                                posUse=response.body().getData().getPosstatus();//等于1 启动，等于0禁用

                                if (posUse.equals("1")) {
                                    //跳转到商品录入界面
                                    Intent intent = new Intent(IndexActivity.this, CarItemsActivity.class);
                                    //Intent intent = new Intent(IndexActivity.this, FinishActivity.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    ToastUtil.showToast(IndexActivity.this, "温馨提示", "当前收银机已被禁止使用，请联系管理员在后台启用");
                                }

                            }
                            else
                            {
                                ToastUtil.showToast(IndexActivity.this, "购物车清除通知", response.body().getMsg());

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<SearchPosEntity> call, Throwable t) {

                    }
                });



            }
        });


        //手动点击自助升级
        Button shengji=findViewById(R.id.uplevel);
        shengji.setOnClickListener(new View.OnClickListener() {

            @Override
            public  void  onClick(View view) {

                PrepareUpdateVersion();
            }
        });


        //重新打印小票
        Button newprint=findViewById(R.id.clickprint);
        newprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void  onClick(View view) {

                Intent intent = new Intent(IndexActivity.this, NewPrintActivity.class);
                startActivity(intent);
            }
        });



        getPrinter();
        //printBill();

    }


    private HttpProxyCacheServer getProxy() {
        return App.getProxy(getApplicationContext());
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
                                updateurl=response.body().getData().getV_Updatepath();
                                showDialog();
                                //最好是用单线程池，或者intentService取代
                                new Thread(new DownLoadRunnable(IndexActivity.this,updateurl, handler)).start();
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





    private void showDialog() {
        if(downloadDialog==null){
            downloadDialog = new UpdateDialog(this);
        }

        if(!downloadDialog.isShowing()){
            downloadDialog.show();
        }
    }

    private void canceledDialog() {
        if(downloadDialog!=null&&downloadDialog.isShowing()){
            downloadDialog.dismiss();
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
