package com.example.selfshopcenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.selfshopcenter.bean.DialyCloseEntity;
import com.example.selfshopcenter.bean.SearchOrderEntity;
import com.example.selfshopcenter.commoncls.CommonData;
import com.example.selfshopcenter.commoncls.ToastUtil;
import com.example.selfshopcenter.net.RetrofitHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialyCloseActivity  extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailyclose);


        //确认打印
        TextView SureClose = findViewById(R.id.SureClose);
        SureClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView password = findViewById(R.id.inputqyid);
                String  inputpassword=password.getText().toString();
                if (inputpassword.equals("admin")) {

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



    public  void  usePrint(String  printstr){


        TextView showtext = findViewById(R.id.showtext);

        showtext.setText(printstr);
        showtext.setSingleLine(false);
    }
}
