package com.example.selfshopcenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.selfshopcenter.commoncls.CommonData;

import java.util.Timer;
import java.util.TimerTask;

public class FinishActivity  extends AppCompatActivity {

    private TextView time;
    private  int i=0;
    private Timer timer=null;
    private TimerTask task=null;
    private  String  printpaytype="";

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


                Intent intent = new Intent(FinishActivity.this, IndexActivity.class);
                startActivity(intent);
            }
        });

        //倒计时30s立即返回到 首界面

        time=(TextView) findViewById(R.id.tv_time);
        i=Integer.parseInt(time.getText().toString());
        startTime();


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
