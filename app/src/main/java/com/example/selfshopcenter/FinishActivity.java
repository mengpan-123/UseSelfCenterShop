package com.example.selfshopcenter;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.selfshopcenter.commoncls.CommonData;
import com.example.selfshopcenter.commoncls.MyDatabaseHelper;
import com.example.selfshopcenter.commoncls.SplnfoList;
import com.example.selfshopcenter.commoncls.ToastUtil;
import com.example.selfshopcenter.printer.PrintUtil;
import com.example.selfshopcenter.printer.UsbPrintManager;


import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class FinishActivity  extends AppCompatActivity {

    private TextView time;
    private  int i=0;
    private Timer timer=null;
    private TimerTask task=null;
    private  String  printpaytype="";

    private UsbPrintManager printer = null;


   // SerialControl ComA;//串口

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_finishpay);  //设置页面

        TextView totalmoney = findViewById(R.id.havepaynet);

        totalmoney.setText("￥" + String.valueOf(CommonData.orderInfo.totalPrice));

        TextView ordernumber = findViewById(R.id.ordernumber);
        ordernumber.setText(CommonData.orderInfo.prepayId);


        //只会有一种支付方式
        if (CommonData.payWay.equals("WXPaymentCodePay")){
            printpaytype="微信支付";
            //微信支付时，调用 交易上报接口
        }
        else if(CommonData.payWay.equals("AliPaymentCodePay")){
            printpaytype="支付宝支付";

        }
        else{
            printpaytype="刷脸支付";
        }

        TextView paytype = findViewById(R.id.paytype);
        paytype.setText(printpaytype);


        /**
         * Created by zhoupan on 2019/11/8.
         * 点此立即返回到首页
         * */
        TextView comback=findViewById(R.id.backhome);


        comback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timer.cancel();//手动点击之后取消定时器

                Intent intent = new Intent(FinishActivity.this, IndexActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //倒计时30s立即返回到 首界面

        time=(TextView) findViewById(R.id.tv_time);
        i=Integer.parseInt(time.getText().toString());
        startTime();


        CommonData.player.reset();
        CommonData.player= MediaPlayer.create(this,R.raw.finishpay);
        CommonData.player.start();
        CommonData.player.setLooping(false);

        //一旦支付成功，这个参数就自动加上1
        CommonData.number=CommonData.number+1;


        //1.0  首先  更新本地数据库的记录号，避免机器无端关机导致其等于0
        try {
            MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "centertable.db", null, 1);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("number", CommonData.number);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String today = sdf.format(date);

            values.put("date_lr", today);

            db.update(CommonData.tablename,values,"khid= ?",new String[] { CommonData.khid });


        } catch (Exception ex) {
            //如果创建异常,不能影响其他
            ToastUtil.showToast(FinishActivity.this, "打印机连接问题", ex.getMessage());
        }


        try {


            //打印相关
            getPrinter();

            printBill();
        }
        catch(Exception  ex){

        }


    }


    //@Override
    /*public void onDestroy(){
        if (printer != null) {
            //printer.onDestory();
        }
        super.onDestroy();
    }*/

    //USB打印机连接
    private void getPrinter() {
        printer = UsbPrintManager.getInstance();
        printer.init(this);
    }



    private void printBill() {


        StringBuffer sbb = new StringBuffer();

        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String day=form.format(date);



        String Str="                   欢迎光临                   "+"\n";
        Str+="流水号："+CommonData.orderInfo.prepayId+"     "+"\n";

        Str+="日期   "+day+"     "+"\n";
        Str+="==============================================="+"\n";
        Str+="条码     名称     数量        单价     金额"+"\n";

        for (Map.Entry<String, List<SplnfoList>> entry : CommonData.orderInfo.spList.entrySet()) {

            String  barcode=entry.getValue().get(0).getBarcode();
            String  sname=entry.getValue().get(0).getPluName();
            String  qty="0";
            String weight=entry.getValue().get(0).getNweight();
            String   dj="0";
            if (weight.equals("0")||weight.equals("0.0")||weight.equals("0.00")){
                //说明重量是 0.  那就取显示数量
                qty = String.valueOf(entry.getValue().get(0).getPackNum());
                dj=entry.getValue().get(0).getMainPrice();

            }else {
                //净重含量存在值 则显示重量
                qty = entry.getValue().get(0).getNweight();
                //根据 实际售价/重量  计算  单价

                dj=entry.getValue().get(0).getMainPrice();
            }

            String zj=entry.getValue().get(0).getRealPrice();

            Str+= barcode+"\n";
            Str+= sname+"     "+qty+"     "+dj+"    "+zj+"  "+"\n";
        }

        //付款方式
        Str+="==============================================="+"\n";
        Str+="付款方式             金额          总折扣"+"\n";

        Str+=printpaytype+"             "+CommonData.orderInfo.totalPrice+ "          "+CommonData.orderInfo.totalDisc+"\n";

        Str+="总数量         应收        找零"+"\n";
        Str+=""+CommonData.orderInfo.totalCount+"             "+CommonData.orderInfo.totalPrice+"          0.00     "+"\n";



        if (  CommonData.hyMessage!=null) {
            //如果会员信息不等于null 的，则需要打印会员基础信息
            Str += "会员卡号:" + CommonData.hyMessage.cardnumber + "\n";
            //Str += "本次消费获得会员积分：" + CommonData.+ "\n";

        }


        try {
            Bitmap prnLogoBmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo_print);
            PrintUtil.printReceipt(null,Str);
            getPrintStatus();

        }
        catch (Exception e) {
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

            Toast.makeText(FinishActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }



    public void startTime() {
        timer = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                if (i > 0) {   //加入判断不能小于0
                    i--;
                    Message message = mHandler.obtainMessage();
                    message.arg1 = i;
                    mHandler.sendMessage(message);
                }else {
                    Intent intent = new Intent(FinishActivity.this, IndexActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.schedule(task, 1000);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            time.setText(msg.arg1 + "");
            startTime();
        };
    };


}
