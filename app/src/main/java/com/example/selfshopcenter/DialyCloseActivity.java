package com.example.selfshopcenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.selfshopcenter.bean.DialyCloseEntity;
import com.example.selfshopcenter.bean.SearchOrderEntity;
import com.example.selfshopcenter.commoncls.CommonData;
import com.example.selfshopcenter.commoncls.ToastUtil;
import com.example.selfshopcenter.net.RetrofitHelper;
import com.example.selfshopcenter.printer.PrintUtil;
import com.example.selfshopcenter.printer.UsbPrintManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialyCloseActivity  extends AppCompatActivity {


    private UsbPrintManager printer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailyclose);
        TextView password = findViewById(R.id.inputqyid);

        //确认日结
        TextView SureClose = findViewById(R.id.SureClose);
        SureClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView password = findViewById(R.id.inputqyid);

                String  inputpassword=password.getText().toString();
                if (inputpassword.equals("admin")) {

                    getPrinter();
                    //打印当日流水
                    GetDialyInfo();

                } else {
                    ToastUtil.showToast(DialyCloseActivity.this, "提示信息", "管理员密码输入错误，请检查");
                }

            }
        });



        //返回上一页
        TextView returnback = findViewById(R.id.returnback);
        returnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DialyCloseActivity.this, IndexActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }


    public void  GetDialyInfo() {

        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String day = form.format(date);

        Call<DialyCloseEntity> diaclose = RetrofitHelper.getInstance().DialyClose(day);
        diaclose.enqueue(new Callback<DialyCloseEntity>() {
            @Override
            public void onResponse(Call<DialyCloseEntity> call, Response<DialyCloseEntity> response) {


                if (response != null) {
                    DialyCloseEntity body = response.body();
                    if (null != body) {
                        if (body.getCode().equals("success")) {

                            String Str = "                   欢迎光临                   " + "\n";
                            Str += "门店名称:" + CommonData.khsname + "\n";
                            Str += "设备号：" + CommonData.deviceid + "     " + "\n";

                            Str += "当日订单总数：" + body.getData().getTotalcount() + "\n";
                            Str += "当日订单总流水：" + body.getData().getTotalnet() + "\n";
                            Str += "其中支付宝总金额：" + body.getData().getZfbnet() + "\n";
                            Str += "其中微信支付总金额：" + body.getData().getWxnet() + "\n";


                            SimpleDateFormat secform = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date secdate = new Date();
                            String time = secform.format(secdate);

                            Str += "===============================================" + "\n";
                            Str += "打印时间：" + time + "\n";

                            usePrint(Str);
                        }
                    }
                }


            }

            @Override
            public void onFailure(Call<DialyCloseEntity> call, Throwable t) {

            }
        });

    }


    //USB打印机连接
    private void getPrinter() {
        printer = UsbPrintManager.getInstance();
        printer.init(this);
    }



    public  void  usePrint(String  printstr){


//        TextView showtext = findViewById(R.id.showtext);
//
//        showtext.setText(printstr);
//        showtext.setSingleLine(false);

        try {

            PrintUtil.printReceipt(null,printstr);
            getPrintStatus();

        }
        catch (Exception e) {
            //Toast.makeText(DialyCloseActivity.this, "打印机信息异常", Toast.LENGTH_LONG).show();
            ToastUtil.showToast(DialyCloseActivity.this, "异常通知", "打印机信息异常");

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

            Toast.makeText(DialyCloseActivity.this, msg, Toast.LENGTH_LONG).show();
        }
    }
}
