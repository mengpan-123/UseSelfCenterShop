package com.example.selfshopcenter;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.danikula.videocache.HttpProxyCacheServer;
import com.example.selfshopcenter.bean.AdvertiseGetEntity;
import com.example.selfshopcenter.bean.ClearCarEntity;
import com.example.selfshopcenter.bean.DeleteSpinfoEntity;
import com.example.selfshopcenter.bean.SearchPosEntity;
import com.example.selfshopcenter.bean.UpdateVersionEntity;
import com.example.selfshopcenter.commoncls.CommonData;
import com.example.selfshopcenter.commoncls.ToastUtil;
import com.example.selfshopcenter.download.DownloadUtils;
import com.example.selfshopcenter.download.JsDownloadListener;
import com.example.selfshopcenter.net.RetrofitHelper;
import com.example.selfshopcenter.net.UpdateDialog;
import com.example.selfshopcenter.printer.UsbPrintManager;
import com.example.selfshopcenter.vediocache.App;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;

public class IndexActivity extends AppCompatActivity {

    private int  Appvercode=0;

    private String  posUse="0";

    private ProgressDialog progressDialog;

    private UsbPrintManager printer = null;

    String  updateurl = "";

    VideoView  videoView;

    String VIDEO_URL = "http://52.81.85.108:8080/uploadapk/index.mp4";  //视频播放的文件


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        Appvercode=CommonData.getAppVersioncode(this);

        //视频的查询与播放
        GetAdvertisement();


        //检查门店使用状态，是否可以使用
        GetStoreStatus(true);


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
        try {
            Call<ClearCarEntity> ClearCar = RetrofitHelper.getInstance().ClearCarInfo(CommonData.khid, CommonData.posid);
            ClearCar.enqueue(new Callback<ClearCarEntity>() {
                @Override
                public void onResponse(Call<ClearCarEntity> call, Response<ClearCarEntity> response) {

                    if (response.body() != null) {

                        if (response.body().getCode().equals("success") || response.body().getMsg().equals("没有符合条件的数据")) {

                        } else {
                            ToastUtil.showToast(IndexActivity.this, "购物车清除通知", response.body().getMsg());

                        }

                    }
                }

                @Override
                public void onFailure(Call<ClearCarEntity> call, Throwable t) {

                }
            });
        }
        catch(Exception ex)
        {
            ToastUtil.showToast(IndexActivity.this, "购物车清除通知", ex.toString());

        }


        //避免万一断网情况下，数据未正常清空。清空失败 这种情况呢？
        CommonData.hyMessage=null;
        CommonData.orderInfo=null;






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

                            if (response.body().getCode().equals("success")||response.body().getMsg().equals("没有符合条件的数据")) {
                                posUse=response.body().getData().getPosstatus();//等于1 启动，等于0禁用

                                if (posUse.equals("1")) {
                                    Intent intent = new Intent(IndexActivity.this, CarItemsActivity.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    //设备禁用
                                    Intent intent = new Intent(IndexActivity.this, NoUseActivity.class);
                                    startActivity(intent);
                                }

                            }
                            else
                            {
                                //门店禁用
                                Intent intent = new Intent(IndexActivity.this, NoUseActivity.class);
                                startActivity(intent);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<SearchPosEntity> call, Throwable t) {

                    }
                });

            }
        });


        getPrinter();
        //printBill();


        // 创建PopupWindow对象
        LayoutInflater inflater = LayoutInflater.from(this);

        // 引入窗口配置文件
        View view = inflater.inflate(R.layout.activity_settings, null);
        // 创建PopupWindow对象
        final PopupWindow pop = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);



        Button btn = findViewById(R.id.basesettings);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void  onClick(View view) {


                GetStoreStatus(false);

                int[]  location=new int[2];
                btn.getLocationInWindow(location);
                int x=location[0]-40;
                int y=location[1]-250;

                if (pop.isShowing()) {
                    // 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
                    pop.dismiss();
                } else {
                    // 显示窗口
                    pop.showAtLocation(view, Gravity.TOP | Gravity.START, 20, y);
                    //pop.showAsDropDown(v);

                }
            }
        });

        //手动点击检测 自动升级
        TextView text1=  view.findViewById(R.id.txt1);
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrepareUpdateVersion();
            }
        });


        //重新打印小票
        TextView text2=  view.findViewById(R.id.txt2);
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IndexActivity.this, NewPrintActivity.class);
                startActivity(intent);
            }
        });

        //日结,暂时先跳转到一个界面 核对
        TextView text3=  view.findViewById(R.id.txt3);
        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IndexActivity.this, DialyCloseActivity.class);
                startActivity(intent);
            }
        });


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


    //获取首页播放的广告内容
    public   void   GetAdvertisement(){

        try {

            Call<AdvertiseGetEntity>  updateversion = RetrofitHelper.getInstance().GETADVERTISE();
            updateversion.enqueue(new Callback<AdvertiseGetEntity>() {
                @Override
                public void onResponse(Call<AdvertiseGetEntity> call, Response<AdvertiseGetEntity> response) {
                    if (null!=response){
                        if (response.body().getCode().equals("success")){

                            VIDEO_URL=response.body().getData().getPath();
                        }
                        else
                        {
                            VIDEO_URL = "http://52.81.85.108:8080/uploadapk/index.mp4";
                        }

                        videoView=findViewById(R.id.video);

                        HttpProxyCacheServer proxy = getProxy();
                        String proxyUrl = proxy.getProxyUrl(VIDEO_URL);
                        try {
                            videoView.setVideoPath(proxyUrl);


                        } catch (Exception e) {
                            Toast.makeText(IndexActivity.this,"播放失败",Toast.LENGTH_SHORT);
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
                    }
                }

                @Override
                public void onFailure(Call<AdvertiseGetEntity> call, Throwable t) {
                    ToastUtil.showToast(IndexActivity.this, "消息提示", "暂未找寻到视频，请等待");
                }
            });
        }
        catch(Exception ex){
            ToastUtil.showToast(IndexActivity.this, "输入消息通知", ex.toString());
        }
    }



    public  void  GetStoreStatus(boolean  voice){

        //查询收音机使用状态
        Call<SearchPosEntity>  search= RetrofitHelper.getInstance().SearchUseStatus();
        search.enqueue(new Callback<SearchPosEntity>() {
            @Override
            public void onResponse(Call<SearchPosEntity> call, Response<SearchPosEntity> response) {

                if (response.body() != null) {

                    if (response.body().getCode().equals("success")||response.body().getMsg().equals("没有符合条件的数据")) {
                        posUse=response.body().getData().getPosstatus();//等于1 启动，等于0禁用

                        if (posUse.equals("1")) {

                            //声音播放先放在这里，避免到时候 没到这个界面，声音先播了
                            if(voice) {
                                CommonData.player.reset();
                                CommonData.player = MediaPlayer.create(IndexActivity.this, R.raw.main);
                                CommonData.player.start();
                                CommonData.player.setLooping(false);
                            }

                        }
                        else
                        {
                            //设备禁用
                            Intent intent = new Intent(IndexActivity.this, NoUseActivity.class);
                            startActivity(intent);
                        }

                    }
                    else
                    {
                        //门店禁用
                        Intent intent = new Intent(IndexActivity.this, NoUseActivity.class);
                        startActivity(intent);
                    }

                }
            }

            @Override
            public void onFailure(Call<SearchPosEntity> call, Throwable t) {

            }
        });

    }


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

                                downFile(updateurl);

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



    public void downFile(final String url) {
        progressDialog = new ProgressDialog(IndexActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("正在下载");
        progressDialog.setMessage("请稍候...");
        progressDialog.setProgress(0);
        progressDialog.show();

        DownloadUtils downloadUtils = new DownloadUtils(new JsDownloadListener() {
            @Override
            public void onStartDownload(long length) {
                setMax(length);
            }

            @Override
            public void onProgress(int progress) {
                downLoading(progress);
            }

            @Override
            public void onFinishDownload() {
                //downSuccess();
            }

            @Override
            public void onFail(String errorInfo) {
                ToastUtil.showToast(IndexActivity.this, "输入消息通知", errorInfo);
            }
        });

        downloadUtils.download(url, new File(getApkPath(), "index.apk"), new Subscriber() {
            @Override
            public void onCompleted() {
                downSuccess();
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToast(IndexActivity.this, "输入消息通知", e.getMessage());
            }

            @Override
            public void onNext(Object o) {
            }
        });
    }


    public void downSuccess() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(IndexActivity.this);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("下载完成");
        builder.setMessage("是否安装");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", (dialog, which) -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(IndexActivity.this, "com.example.selfshopcenter.fileProvider", new File(getApkPath(), "index.apk"));
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(new File(getApkPath(), "index.apk")), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }


            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            //更新 版本
            //首先更新下来 这个最新的 版本号，不确定能否得到
            CommonData.app_version=CommonData.getAppVersion(this);

            Call<DeleteSpinfoEntity>  update=  RetrofitHelper.getInstance().UPDATEVERSION();
            update.enqueue(new Callback<DeleteSpinfoEntity>() {
                @Override
                public void onResponse(Call<DeleteSpinfoEntity> call, Response<DeleteSpinfoEntity> response) {
                    if (null !=response){
                        if (response.body().getCode().equals("success")){

                        }
                    }
                }

                @Override
                public void onFailure(Call<DeleteSpinfoEntity> call, Throwable t) {

                }
            });

        });
        builder.setNegativeButton("取消", (dialog, which) -> {

        });
        builder.create().show();
    }




    public String getApkPath() {
        String directoryPath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            directoryPath = getExternalFilesDir("apk").getAbsolutePath();
        } else {
            directoryPath = getFilesDir() + File.separator + "apk";
        }
        File file = new File(directoryPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return directoryPath;
    }

    public void setMax(final long total) {
        if (progressDialog != null) {
            progressDialog.setMax((int) total);
        }
    }

    public void downLoading(final int i) {
        if (progressDialog != null) {
            progressDialog.setProgress(i);
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
