package com.example.selfshopcenter;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.selfshopcenter.commoncls.CommonData;
import com.example.selfshopcenter.commoncls.MyDatabaseHelper;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class FinishActivity  extends AppCompatActivity {

    private TextView time;
    private  int i=0;
    private Timer timer=null;
    private TimerTask task=null;
    private  String  printpaytype="";


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


                Intent intent = new Intent(FinishActivity.this, IndexActivity.class);
                startActivity(intent);
            }
        });

        //倒计时30s立即返回到 首界面

        time=(TextView) findViewById(R.id.tv_time);
        i=Integer.parseInt(time.getText().toString());
        startTime();


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



   /* protected void print()
    {
        try {
            // 小票标题
            ComA.send(PrintCmd.GetStatus4());
            m_blnStatus = true;
            m_iStatusCount = 0;
            m_iRecValue = -1;
            ShowMessage(getString(R.string.State_query));

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //----------------------------------------------------串口控制类// 走纸换行，再切纸，清理缓存
    	private void PrintFeedCutpaper(int iLine) {
    		try {
    			ComA.send(PrintCmd.PrintFeedline(iLine));
    			ComA.send(PrintCmd.PrintCutpaper(0));
    			ComA.send(PrintCmd.SetClean());
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}

    private class SerialControl extends SerialHelper {
        public SerialControl() {

        }
        @Override
        protected void onDataReceived(final ComBean ComRecData) {
            DispQueue.AddQueue(ComRecData);// 线程定时刷新显示(推荐)
        }


    }*/

}
