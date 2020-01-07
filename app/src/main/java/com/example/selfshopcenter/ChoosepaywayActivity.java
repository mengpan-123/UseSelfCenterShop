package com.example.selfshopcenter;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.selfshopcenter.commoncls.CommonData;
import com.example.selfshopcenter.commoncls.ToastUtil;

public class ChoosepaywayActivity  extends AppCompatActivity {

    private String payWay = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosepayway);



        //设置底部的显示信息
        TextView totaldisc=findViewById(R.id.totaldisc);
        totaldisc.setText(CommonData.orderInfo.totalDisc);

        TextView totalpay=findViewById(R.id.totalpay);
        totalpay.setText(CommonData.orderInfo.totalPrice);



        ImageView zfb = findViewById(R.id.zfb);
        ImageView wx = findViewById(R.id.wx);
        ImageView wx_face = findViewById(R.id.wx_face);

        TextView returnback = findViewById(R.id.returnback);


        zfb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                CommonData.payWay = "AliPaymentCodePay";

                Intent intent = new Intent(ChoosepaywayActivity.this, ShowpayActivity.class);
                startActivity(intent);
                finish();
            }
        });
        wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonData.payWay = "WXPaymentCodePay";

                Intent intent = new Intent(ChoosepaywayActivity.this, ShowpayActivity.class);
                startActivity(intent);
                finish();

            }
        });

        wx_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ToastUtil.showToast(ChoosepaywayActivity.this, "支付方式通知", "功能正在开发中");

            }
        });



        //返回上一页

        returnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoosepaywayActivity.this, CarItemsActivity.class);
                startActivity(intent);
            }
        });
    }
}
