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
import com.example.selfshopcenter.bean.OrderpayResponse;
import com.example.selfshopcenter.bean.UserLoginEntity;
import com.example.selfshopcenter.commoncls.CommonData;
import com.example.selfshopcenter.commoncls.SplnfoList;
import com.example.selfshopcenter.commoncls.ToastUtil;
import com.example.selfshopcenter.net.RetrofitHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowpayActivity  extends AppCompatActivity {

    //支付识别码
    private String payAuthCode = "";

    private Boolean isPay=true;

    private retrofit2.Call<OrderpayResponse> OrderpayResponseCall;


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
        CommonData.player.reset();
        if (CommonData.payWay.equals("WXPaymentCodePay")){
            Drawable d=res.getDrawable(R.mipmap.wxforexe);
            //2020-02-13暂时注释，
            if (CommonData.YN_PAY.equals("1")) {
                findViewById(R.id.wxuseinfo).setBackgroundDrawable(d);
            }
            else
            {
                findViewById(R.id.wxuseinfo).setVisibility(View.GONE);

            }
            CommonData.player=MediaPlayer.create(this,R.raw.weixin);
        }
        else
        {
            Drawable d=res.getDrawable(R.mipmap.aliapyexam);
            if (CommonData.YN_PAY.equals("1")) {
                findViewById(R.id.wxuseinfo).setBackgroundDrawable(d);
            }
            else
            {
                findViewById(R.id.wxuseinfo).setVisibility(View.GONE);

            }
            CommonData.player=MediaPlayer.create(this,R.raw.zhifub);
        }

        CommonData.player.start();
        CommonData.player.setLooping(false);


        //返回上衣界面，重选支付方式
       if (CommonData.YN_PAY.equals("1")) {
        findViewById(R.id.returnchoose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowpayActivity.this, ChoosepaywayActivity.class);

                startActivity(intent);
            }
        });

           findViewById(R.id.Father).setVisibility(View.GONE);
           findViewById(R.id.SUB_FATHER).setVisibility(View.GONE);
        }
       else {
           findViewById(R.id.returnchoose).setVisibility(View.GONE);


           findViewById(R.id.SUB_FATHER).setVisibility(View.VISIBLE);
           findViewById(R.id.Father).setVisibility(View.VISIBLE);

       }

        //返回购物车
        findViewById(R.id.returnback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowpayActivity.this, CarItemsActivity.class);

                startActivity(intent);
            }
        });


        //确认支付
        TextView print = findViewById(R.id.SurePrint);
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView inputqyid = findViewById(R.id.inputqyid);

                String  inputnumber = inputqyid.getText().toString();

                payAuthCode = inputnumber;

                Orderpay();

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

        try {

            for (Map.Entry<String, List<SplnfoList>> entry : CommonData.orderInfo.spList.entrySet()) {

                OrderpayRequest.DataBean.PluMapBean payMapcls = new OrderpayRequest.DataBean.PluMapBean();
                payMapcls.setBarcode(entry.getValue().get(0).getBarcode());
                payMapcls.setGoodsId(entry.getValue().get(0).getGoodsId());
                payMapcls.setPluQty(entry.getValue().get(0).getPackNum());
                payMapcls.setRealPrice(Double.valueOf(entry.getValue().get(0).getRealPrice()));  //单项实付金额
                payMapcls.setPluPrice(entry.getValue().get(0).getMainPrice());  //单品单价
                payMapcls.setPluDis(entry.getValue().get(0).getTotaldisc());  //单品优惠
                payMapcls.setPluAmount(Double.valueOf(entry.getValue().get(0).getRealPrice()));   //单项小计
                pluMap.add(payMapcls);
            }
        } catch (Exception ex) {

        }
        String payid="1";
        if (CommonData.payWay.equals("AliPaymentCodePay"))
        {
            payid="2";
        }
        else if (CommonData.payWay.equals("WXPaymentCodePay"))
        {
            payid="1";
        }

        //支付方式
        List<OrderpayRequest.DataBean.PayMapBean> payMap = new ArrayList<OrderpayRequest.DataBean.PayMapBean>();
        OrderpayRequest.DataBean.PayMapBean pmp = new OrderpayRequest.DataBean.PayMapBean();
        pmp.setPayTypeId(payid);
        pmp.setPayVal(Double.valueOf(CommonData.orderInfo.totalPrice));
        payMap.add(pmp);

        OrderpayResponseCall= RetrofitHelper.getInstance().Orderpay(payAuthCode,"",CommonData.orderInfo.prepayId,pluMap,payMap);
        OrderpayResponseCall.enqueue(new Callback<OrderpayResponse>() {
            @Override
            public void onResponse(Call<OrderpayResponse> call, Response<OrderpayResponse> response) {
                isPay=true;  //只有当这一笔支付完成 之后  才可以重新进行支付
                if(response != null) {
                    if (null != response.body()) {

                        if (response.body().getCode().equals("success")) {
                            //返回成功
                            //拿到交易流水号
                            CommonData.paytrad_no = response.body().getData().getOut_trad_no();
                            if(response.body().getData().getPaycode().equals("200")) {
                                //跳转到支付成功界面(因为现在已经没有轮询的方式)
                                Intent intent = new Intent(ShowpayActivity.this, FinishActivity.class);
                                startActivity(intent);
                                finish();
                                return;

                            }
                            else if(response.body().getData().getPaycode().equals("-3"))
                            {
                                //说明支付在等待，跳转到支付轮询界面

                                Intent intent = new Intent(ShowpayActivity.this, SearchingPayActivity.class);
                                startActivity(intent);
                                finish();
                                return;
                            }
                            else{

                                ToastUtil.showToast(ShowpayActivity.this, "支付失败", response.body().getData().getErrmsg());
                                return;
                            }

                        }
                        else{
                            ToastUtil.showToast(ShowpayActivity.this, "支付失败", response.body().getMsg());
                            return;
                        }
                    }

                }
                else
                {
                    ToastUtil.showToast(ShowpayActivity.this, "支付通知", "操作异常");
                    return;
                }

            }

            @Override
            public void onFailure(Call<OrderpayResponse> call, Throwable t) {
                isPay=true;  //只有当这一笔支付完成 之后  才可以重新进行支付
                ToastUtil.showToast(ShowpayActivity.this, "支付通知", "操作异常");
                return;
            }
        });
    }

}
