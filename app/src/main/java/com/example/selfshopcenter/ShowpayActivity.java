package com.example.selfshopcenter;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.selfshopcenter.bean.OrderpayRequest;
import com.example.selfshopcenter.commoncls.CommonData;

import java.util.ArrayList;
import java.util.List;

public class ShowpayActivity  extends AppCompatActivity {

    //支付识别码
    private String payAuthCode = "";

    private Boolean isPay=true;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_showpay);  //设置页面

            //设置底部的显示信息
            TextView totaldisc=findViewById(R.id.totalcount);
            totaldisc.setText("数量合计 ："+CommonData.orderInfo.totalCount);

            TextView totalpay=findViewById(R.id.totalpay);
            totalpay.setText("金额合计 ："+CommonData.orderInfo.totalPrice);


            TextView storename=findViewById(R.id.storename);
            storename.setText(CommonData.khsname);


        Resources res = getResources();

        if (CommonData.payWay.equals("WXPaymentCodePay")){
            Drawable d=res.getDrawable(R.mipmap.wxforexe);
            findViewById(R.id.wxuseinfo).setBackgroundDrawable(d);

        }
        else
        {
            Drawable d=res.getDrawable(R.mipmap.aliapyexam);
            findViewById(R.id.wxuseinfo).setBackgroundDrawable(d);
        }

        //返回上衣界面，重选支付方式
        findViewById(R.id.returnchoose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowpayActivity.this, ChoosepaywayActivity.class);

                startActivity(intent);
            }
        });

        //返回购物车
        findViewById(R.id.returnback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowpayActivity.this, CarItemsActivity.class);

                startActivity(intent);
            }
        });

    }


    //增加扫码枪的事件，检测是否录入了支付码
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {


        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            char pressedKey = (char) event.getUnicodeChar();
            payAuthCode += pressedKey;
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            if (!TextUtils.isEmpty(payAuthCode)) {
                if (payAuthCode.contains("\n")) {
                    payAuthCode = payAuthCode.substring(0, payAuthCode.length() - 1);
                }
                //开始支付了的部分
                Orderpay();

                payAuthCode = "";
            }

        }
        return true;

    }


    /**
     * Orderpay
     * Created by zhoupan on 2020/01/07
     * 选择支付方式之后进行支付
     */

    public void Orderpay() {
        //默认值为 true，表示  可以支付了 ，只有为 true 的时候才可以进行支付
        if (!isPay){
            return;
        }

        isPay=false;   //说明 已经开始支付了，避免一次 扫码扫到连个触发两次

        //1.0 初始化所有的产品信息,
        List<OrderpayRequest.DataBean.PluMapBean> pluMap = new ArrayList<OrderpayRequest.DataBean.PluMapBean>();


    }

}
