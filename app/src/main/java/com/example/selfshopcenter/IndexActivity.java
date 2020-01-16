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


        TextView useposid=findViewById(R.id.useposid);
        useposid.setText("Version : "+ CommonData.posid);

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



        getPrinter();
        //printBill();

    }


    //USB打印机连接
    private void getPrinter() {
        printer = UsbPrintManager.getInstance();
        printer.init(this);
    }



//
//    private void initVideo() {
//        HttpProxyCacheServer proxy = VideoApplication.getProxy(this);
//        //1.我们会将原始url注册进去
//        // proxy.registerCacheListener(, bean.getVideo_url());
//        //2.我们播放视频的时候会调用以下代码生成proxyUrl
//        String proxyUrl = proxy.getProxyUrl(url);
//        if (proxy.isCached(url)) {
//            Log.i("aaaa", "已缓存");
//        } else {
//            Log.i("aaaa", "未缓存");
//        }
//        Log.i("aaaapath", proxyUrl);
//        video.setVideoPath(proxyUrl);
//        video.start();
//        video.findFocus();
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//
//        if (requestCode == MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                initVideo();
//            } else {
//                //"权限已拒绝";
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }


//    private void printBill() {
//        String json="{\"amount\":3997.489999999999,\"cashier\":\"5\",\"change_amount\":0.0,\"dsc_amount\":2498.7000000000003,\"flowno\":\"20180826071228\",\"id\":\"20180826071228\",\"itemRepOrder\":{\"deliveryMoney\":\"仅需518元\",\"deliveryMoneyMsg\":\"\",\"deliveryMsg\":\"配送服务由微东顺路提供\",\"id\":\"20180826071228\",\"retcode\":\"1\",\"retmsg\":\"支付成功\",\"sendUrl\":\"http://t.cn/RkrqO6x\",\"totalCount\":127.0,\"totalDiscountsPrice\":2498.7,\"totalFinalPrice\":3997.49,\"totalPrice\":6496.19},\"itempay\":[{\"amount\":3997.49,\"batchno\":\"\",\"cardno\":\"\",\"flowid\":1,\"last_balance\":0.0,\"mchtid\":\"\",\"paycode\":\"02\",\"payname\":\"储值卡\",\"reference\":\"\",\"termid\":\"\",\"trace\":\"\"}],\"itemsale\":[{\"amount\":52.56,\"barcode\":\"6920152414040\",\"count\":1.0,\"discount\":70.0,\"discountPrice\":22.54,\"discountType\":1,\"finalPrice\":52.56,\"id\":\"322\",\"name\":\"康师傅鲜虾鱼板面\",\"price\":75.1,\"type\":0,\"weight\":0.0},{\"amount\":115.66,\"barcode\":\"6920152418710\",\"count\":2.0,\"discount\":0.0,\"discountPrice\":0.0,\"discountType\":0,\"finalPrice\":57.83,\"id\":\"289\",\"name\":\"康师傅红油爆椒牛肉面\",\"price\":57.83,\"type\":0,\"weight\":0.0},{\"amount\":51.58,\"barcode\":\"6920152414217\",\"count\":2.0,\"discount\":69.0,\"discountPrice\":12.05,\"discountType\":1,\"finalPrice\":25.79,\"id\":\"290\",\"name\":\"康师傅酸辣牛肉面\",\"price\":37.84,\"type\":0,\"weight\":0.0},{\"amount\":21.3,\"barcode\":\"6920152455371\",\"count\":2.0,\"discount\":0.0,\"discountPrice\":26.82,\"discountType\":2,\"finalPrice\":10.65,\"id\":\"299\",\"name\":\"康师傅千椒百味剁椒排骨面\",\"price\":37.47,\"type\":0,\"weight\":0.0},{\"amount\":28.28,\"barcode\":\"6920152472569\",\"count\":2.0,\"discount\":15.0,\"discountPrice\":83.84,\"discountType\":1,\"finalPrice\":14.14,\"id\":\"292\",\"name\":\"非国标商品6920152472569\",\"price\":97.98,\"type\":0,\"weight\":1.82},{\"amount\":245.91,\"barcode\":\"6920152419472\",\"count\":3.0,\"discount\":0.0,\"discountPrice\":0.0,\"discountType\":0,\"finalPrice\":81.97,\"id\":\"300\",\"name\":\"康师傅千椒百味野山椒仔鸡面\",\"price\":81.97,\"type\":0,\"weight\":0.0},{\"amount\":99.68,\"barcode\":\"6920152413579\",\"count\":4.0,\"discount\":0.0,\"discountPrice\":13.81,\"discountType\":2,\"finalPrice\":24.92,\"id\":\"317\",\"name\":\"康师傅丰盛经典梅干菜扣肉面\",\"price\":38.73,\"type\":0,\"weight\":0.0},{\"amount\":108.44999999999999,\"barcode\":\"6920152483411\",\"count\":3.0,\"discount\":0.0,\"discountPrice\":10.07,\"discountType\":2,\"finalPrice\":36.15,\"id\":\"294\",\"name\":\"康师傅雪笋肉丝面\",\"price\":46.22,\"type\":0,\"weight\":0.0},{\"amount\":3.4799999999999995,\"barcode\":\"6920152414194\",\"count\":3.0,\"discount\":20.0,\"discountPrice\":4.77,\"discountType\":1,\"finalPrice\":1.16,\"id\":\"295\",\"name\":\"江南美食笋干老鸭煲面\",\"price\":5.93,\"type\":0,\"weight\":0.0},{\"amount\":14.22,\"barcode\":\"6920152414231\",\"count\":3.0,\"discount\":0.0,\"discountPrice\":86.93,\"discountType\":2,\"finalPrice\":4.74,\"id\":\"296\",\"name\":\"非国标商品6920152414231\",\"price\":91.67,\"type\":0,\"weight\":1.88},{\"amount\":155.6,\"barcode\":\"6920152448243\",\"count\":2.0,\"discount\":0.0,\"discountPrice\":0.0,\"discountType\":0,\"finalPrice\":77.8,\"id\":\"293\",\"name\":\"江南美食笋干烧肉面\",\"price\":77.8,\"type\":0,\"weight\":0.0},{\"amount\":174.7,\"barcode\":\"6951831300149\",\"count\":2.0,\"discount\":0.0,\"discountPrice\":0.0,\"discountType\":0,\"finalPrice\":87.35,\"id\":\"265\",\"name\":\"五谷坊鸡蛋挂面\",\"price\":87.35,\"type\":0,\"weight\":0.0},{\"amount\":54.46,\"barcode\":\"6952150012027\",\"count\":2.0,\"discount\":59.0,\"discountPrice\":18.98,\"discountType\":1,\"finalPrice\":27.23,\"id\":\"266\",\"name\":\"非国标商品6952150012027\",\"price\":46.21,\"type\":0,\"weight\":0.91},{\"amount\":75.28,\"barcode\":\"6952150012041\",\"count\":1.0,\"discount\":0.0,\"discountPrice\":0.0,\"discountType\":0,\"finalPrice\":75.28,\"id\":\"270\",\"name\":\"非国标商品6952150012041\",\"price\":75.28,\"type\":0,\"weight\":4.68},{\"amount\":51.0,\"barcode\":\"6930430300132\",\"count\":3.0,\"discount\":0.0,\"discountPrice\":2.45,\"discountType\":2,\"finalPrice\":17.0,\"id\":\"269\",\"name\":\"苏缘手擀面\",\"price\":19.45,\"type\":0,\"weight\":0.0},{\"amount\":26.06,\"barcode\":\"6930430300248\",\"count\":2.0,\"discount\":0.0,\"discountPrice\":60.6,\"discountType\":2,\"finalPrice\":13.03,\"id\":\"267\",\"name\":\"非国标商品6930430300248\",\"price\":73.63,\"type\":0,\"weight\":1.96},{\"amount\":16.76,\"barcode\":\"6930430386860\",\"count\":1.0,\"discount\":0.0,\"discountPrice\":0.0,\"discountType\":0,\"finalPrice\":16.76,\"id\":\"271\",\"name\":\"苏缘蛋清面\",\"price\":16.76,\"type\":0,\"weight\":0.0},{\"amount\":1.62,\"barcode\":\"6930430300095\",\"count\":2.0,\"discount\":9.0,\"discountPrice\":9.09,\"discountType\":1,\"finalPrice\":0.81,\"id\":\"273\",\"name\":\"苏缘京麦香高筋龙须面\",\"price\":9.9,\"type\":0,\"weight\":0.0},{\"amount\":51.75,\"barcode\":\"6930430300088\",\"count\":3.0,\"discount\":19.0,\"discountPrice\":74.67,\"discountType\":1,\"finalPrice\":17.25,\"id\":\"274\",\"name\":\"非国标商品6930430300088\",\"price\":91.92,\"type\":0,\"weight\":9.81},{\"amount\":103.96,\"barcode\":\"6930430300071\",\"count\":2.0,\"discount\":0.0,\"discountPrice\":33.21,\"discountType\":2,\"finalPrice\":51.98,\"id\":\"272\",\"name\":\"苏缘京麦香爽滑面\",\"price\":85.19,\"type\":0,\"weight\":0.0}],\"itemvip\":{\"amount\":0.0,\"id\":\"\",\"name\":\"\",\"scores\":0.0,\"status\":0},\"pay_amount\":3997.49,\"poscode\":\"1\",\"qty\":127.0,\"sdate\":\"2018-08-26 15:12:23\",\"tradetype\":\"B\"}";
//
//        try {
//            Bitmap prnLogoBmp = BitmapFactory.decodeResource(getResources(), R.drawable.code);
//            PrintUtil.printReceipt(prnLogoBmp, json);
//            getPrintStatus();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            ToastUtil.showToast(IndexActivity.this, "打印机连接问题", e.getMessage());
//        }
//    }

    /**
     *
     * MembersLogin 会员登录
     * */

    public  void  MembersLogin(View view){


        ToastUtil.showToast(IndexActivity.this, "会员信息验证", "正在开发中");

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
