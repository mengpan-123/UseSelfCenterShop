package com.example.selfshopcenter;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.danikula.videocache.HttpProxyCacheServer;
import com.example.selfshopcenter.bean.AdvertiseGetEntity;
import com.example.selfshopcenter.bean.ClearCarEntity;
import com.example.selfshopcenter.bean.DeleteSpinfoEntity;
import com.example.selfshopcenter.bean.SearchOrderEntity;
import com.example.selfshopcenter.bean.SearchPosEntity;
import com.example.selfshopcenter.bean.TenOrderEntity;
import com.example.selfshopcenter.bean.UpdateVersionEntity;
import com.example.selfshopcenter.commoncls.CommonData;
import com.example.selfshopcenter.commoncls.ToastUtil;
import com.example.selfshopcenter.download.DownloadUtils;
import com.example.selfshopcenter.download.JsDownloadListener;
import com.example.selfshopcenter.net.RetrofitHelper;
import com.example.selfshopcenter.net.UpdateDialog;
import com.example.selfshopcenter.printer.PrintUtil;
import com.example.selfshopcenter.printer.UsbPrintManager;
import com.example.selfshopcenter.vediocache.App;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;

public class IndexActivity extends AppCompatActivity {

    private int  Appvercode=0;

    private UsbPrintManager printer = null;

    private String  posUse="0";

    private ProgressDialog progressDialog;



    String  updateurl = "";
    String  Update_v_name="";
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
                int y=location[1]-330;

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

        //显示最近十笔单据记录
        TextView text4=  view.findViewById(R.id.txt4);
        text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(IndexActivity.this, R.style.myNewsDialogStyle);

                // 自定义对话框布局
                View layout = View.inflate(IndexActivity.this, R.layout.activity_previewtemprint,
                        null);
                dialog.setContentView(layout);
                ComsetDialogSize(layout);


                ListView listView = (ListView) layout.findViewById(R.id.ten_xsprint);

                listView.setDividerHeight(20);
                List<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>();

                Call<TenOrderEntity>  tenxs = RetrofitHelper.getInstance().TENXSPRINT();
                tenxs.enqueue(new Callback<TenOrderEntity>() {
                    @Override
                    public void onResponse(Call<TenOrderEntity> call, Response<TenOrderEntity> response) {
                        if (null!=response){
                            if (response.body().getCode().equals("success")){
                                //然后显示出 列表

                                List<TenOrderEntity.DataBean.OrderListBean>  useList=response.body().getData().getOrderList();
                                if (useList.size()==0){
                                    ToastUtil.showToast(IndexActivity.this, "查询结果", "当前门店暂未获取到有效订单");
                                    return;
                                }

                                for (int sm = 0; sm < useList.size(); sm++) {

                                    String bill = useList.get(sm).getBill();
                                    String xs_bill = useList.get(sm).getOut_transid();
                                    String disc = useList.get(sm).getTotaldisc();
                                    String net=useList.get(sm).getTotalAmount();
                                    String saledate =useList.get(sm).getSaledate();

                                    Call<SearchOrderEntity> OrderDetailEntityCall = RetrofitHelper.getInstance().NewPrintById(bill);
                                    OrderDetailEntityCall.enqueue(new Callback<SearchOrderEntity>() {
                                        @Override
                                        public void onResponse(Call<SearchOrderEntity> call, Response<SearchOrderEntity> response) {

                                            if (response!=null) {
                                                SearchOrderEntity body = response.body();
                                                if (null != body) {
                                                    if (body.getCode().equals("success")) {
                                                        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
                                                        Date date = new Date();
                                                        String day = form.format(date);

                                                        String storename = body.getData().getKhsname();


                                                        String Str = "                   欢迎光临                   " + "\n";
                                                        Str += "门店名称:" + storename + "\n";
                                                        Str += "流水号：" + body.getData().getTransId() + "     " + "\n";
                                                        //Str+="商户订单号："+CommonData.outTransId+"     "+"\n";
                                                        Str += "打印日期   " + day + "     " + "\n";
                                                        Str += "===============================================" + "\n";
                                                        Str += "条码     名称     数量        单价     金额" + "\n";

                                                        List<SearchOrderEntity.DataBean.PluMapBean> plumap = body.getData().getPluMap();

                                                        for (int sk = 0; sk < plumap.size(); sk++) {

                                                            String barcode = plumap.get(sk).getBarcode();
                                                            String sname = plumap.get(sk).getPluName();
                                                            double qty = 0;
                                                            double weight = plumap.get(sk).getNWeight();
                                                            double dj = 0;
                                                            dj = plumap.get(sk).getPluPrice();
                                                            if (weight == 0.00) {
                                                                //说明重量是 0.  那就取显示数量
                                                                qty = plumap.get(sk).getPluQty();

                                                            } else {
                                                                //净重含量存在值 则显示重量
                                                                qty = weight;
                                                            }

                                                            double zj = plumap.get(sk).getRealAmount();
                                                            Str += barcode + "\n";
                                                            Str += sname + "     " + qty + "     " + dj + "    " + zj + "  " + "\n";
                                                        }

                                                        //付款方式
                                                        String paytype = body.getData().getPayMap().getPayTypeName();
                                                        String paynet = body.getData().getPayMap().getPayVal();
                                                        int paycount = body.getData().getTotQty();
                                                        Str += "===============================================" + "\n";
                                                        Str += "付款方式             金额          总折扣" + "\n";

                                                        Str += paytype + "             " + paynet + "          " + body.getData().getDisAmount() + "\n";

                                                        Str += "总数量         应收        找零" + "\n";
                                                        Str += "" + paycount + "             " + paynet + "          0.00     " + "\n";

                                                        Str += "===============" + body.getData().getTrans_xsTime() + "=============" + "\n";
                                                        Str += "            谢谢惠顾，请妥善保管小票         " + "\n";
                                                        Str += "               开正式发票，当月有效              " + "\n";

                                                        Map<String, Object> map = new HashMap<String, Object>();
                                                        map.put("xs_bill", bill);
                                                        map.put("xsnet", net);
                                                        map.put("xsdisc", disc);
                                                        map.put("xs_date","销售日期:"+saledate );
                                                        map.put("printstr",Str);
                                                        listitem.add(map);


                                                        SimpleAdapter adapter = new SimpleAdapter(IndexActivity.this, listitem,
                                                                R.layout.acticity_tenprint, new String[]{"xs_bill", "xsnet", "xsdisc", "xs_date","printstr"},
                                                                new int[]{R.id.xsbill, R.id.tv_yuan_price, R.id.tv_disc, R.id.tv_goods_name,R.id.printstr});
                                                        listView.setAdapter(adapter);
                                                        dialog.show();
                                                    }
                                                    else
                                                    {
                                                        ToastUtil.showToast(IndexActivity.this, "查询失败",body.getMsg());
                                                    }
                                                }
                                                else{
                                                    ToastUtil.showToast(IndexActivity.this, "接口访问失败","请检查接口异常");

                                                }


                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<SearchOrderEntity> call, Throwable t) {
                                            ToastUtil.showToast(IndexActivity.this, "接口请求失败","请检查网络异常或接口请求异常");
                                        }
                                    });


                                }

                            }
                            else
                            {
                                //ToastUtil.showToast(LoginActivity.this, "消息内容提示", "暂无可升级的内容");
                                ToastUtil.showToast(IndexActivity.this, "查询结果", "当前设备近期没有销售数据");
                                return;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TenOrderEntity> call, Throwable t) {

                    }
                });



                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        HashMap<String, String> map = (HashMap<String, String>) listView.getItemAtPosition(position);
                        String pr_Str = map.get("printstr");

                        usePrint(pr_Str);

                        dialog.dismiss();
                    }
                });


                TextView closemem=layout.findViewById(R.id.closewindow);
                //设置关闭按钮的事件
                closemem.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

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



    public  void  usePrint(String  printstr){


//        TextView showtext = findViewById(R.id.showtext);
//
//        showtext.setText(printstr);
//        showtext.setSingleLine(false);

       // ToastUtil.showToast(IndexActivity.this, "异常通知", printstr);

        try {

            PrintUtil.printReceipt(null,printstr);
            getPrintStatus();

        }
        catch (Exception e) {

            ToastUtil.showToast(IndexActivity.this, "异常通知", "打印机信息异常");

            e.printStackTrace();
        }

    }


    //查询打印机状态
    private void getPrintStatus() {
        String msg = "";
        int iRet = PrintUtil.getPrintEndStatus();

        switch (iRet) {
            case 0:
                msg = "正常";
                break;
            case 1:
                msg = "打印机未连接或未上电";
                break;
            case 2:
                msg = "打印机和调用库不匹配";
                break;
            case 3:
                msg = "打印头打开";
                break;
            case 4:
                msg = "切刀未复位";
                break;
            case 5:
                msg = "打印头过热";
                break;
            case 6:
                msg = "黑标错误";
                break;
            case 7:
                msg = "纸尽";
                break;
            case 8:
                msg = "纸将尽";
                break;
            case -1:
                msg = "异常";
                break;
        }
        if (iRet != 0) {

            Toast.makeText(IndexActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 动态控制弹出框的大小
     */
    private void ComsetDialogSize(final View mView) {
        mView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                                       int oldRight, int oldBottom) {
               /* int heightNow = v.getHeight();//dialog当前的高度
                int widthNow = v.getWidth();//dialog当前的宽度
                int needWidth = (int) (getWindowManager().getDefaultDisplay().getWidth() * 0.8);//最小宽度为屏幕的0.7倍
                int needHeight = (int) (getWindowManager().getDefaultDisplay().getHeight() * 1);//最大高度为屏幕的0.6倍
                if (widthNow < needWidth || heightNow > needHeight) {
                    if (widthNow > needWidth) {
                        needWidth = FrameLayout.LayoutParams.WRAP_CONTENT;
                    }
                    if (heightNow < needHeight) {
                        needHeight = FrameLayout.LayoutParams.WRAP_CONTENT;
                    }
                    mView.setLayoutParams(new FrameLayout.LayoutParams(needWidth,
                            needHeight));
                }*/

                mView.setLayoutParams(new FrameLayout.LayoutParams(980,
                        1300));



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

                                Update_v_name=response.body().getData().getVersionName();


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

            //更新 版本号
            Call<DeleteSpinfoEntity>  update=  RetrofitHelper.getInstance().UPDATEVERSION(Update_v_name);
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

}
