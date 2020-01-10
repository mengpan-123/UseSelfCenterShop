package com.example.selfshopcenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.selfshopcenter.bean.AuthInfoEntity;
import com.example.selfshopcenter.bean.OrderpayRequest;
import com.example.selfshopcenter.bean.OrderpayResponse;
import com.example.selfshopcenter.commoncls.CommonData;
import com.example.selfshopcenter.commoncls.SplnfoList;
import com.example.selfshopcenter.commoncls.ToastUtil;
import com.example.selfshopcenter.net.RetrofitHelper;
import com.tencent.wxpayface.IWxPayfaceCallback;
import com.tencent.wxpayface.WxPayFace;
import com.tencent.wxpayface.WxfacePayCommonCode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoosepaywayActivity  extends AppCompatActivity {

    private String payWay = "";
    public static final String RETURN_CODE = "return_code";
    public static final String RETURN_MSG = "return_msg";
    public static final String TAG = "ChoosepaywayActivity";
    private String mAuthInfo;   //获取的个人用户信息
    //支付识别码
    private String payAuthCode = "";

    private Boolean isPay=true;


    private String openid;      //刷脸支付获取微信openid

    private String Facecode;      //刷脸支付获取微信人脸识别码

    private Call<AuthInfoEntity> AuthInfoEntityCall;
    private retrofit2.Call<OrderpayResponse> OrderpayResponseCall;
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

                CommonData.payWay="WXFacePay";
                wxFacepay();

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

    //刷脸支付初始化
    public  void wxFacepay(){
        Map<String, String> m1 = new HashMap<>();
        //m1.put("ip", "192.168.1.1"); //若没有代理,则不需要此行
        //m1.put("port", "8888");//若没有代理,则不需要此行*/
        try {
            //1.0 刷脸支付初始化成功
            WxPayFace.getInstance().initWxpayface(ChoosepaywayActivity.this, m1, new IWxPayfaceCallback() {
                @Override
                public void response(Map info) throws RemoteException {
                    if (!isSuccessInfo(info)) {
                        return;
                    }
                    // 2.0微信刷脸  获取rawdata和AuthInfo 数据
                    try {
                        WxPayFace.getInstance().getWxpayfaceRawdata(new IWxPayfaceCallback() {
                            @Override
                            public void response(Map info) throws RemoteException {
                                if (!isSuccessInfo(info)) {
                                    ToastUtil.showToast(ChoosepaywayActivity.this, "接口获取失败", "当前调用微信SDK获取rawdata异常");
                                    return;
                                }

                                String rawdata = info.get("rawdata").toString();
                                try {
                                    //selfgetAuthInfo(rawdata);

                                    //InterfaceGetAuthInfo(rawdata);

                                    PostGetAuthInfo(rawdata);


                                } catch (Exception e) {
                                    e.printStackTrace();
                                    ToastUtil.showToast(ChoosepaywayActivity.this, "温馨提示", "当前调用接口获取设备认证信息异常");
                                }
                            }
                        });
                    }
                    catch(Exception ex){
                        ToastUtil.showToast(ChoosepaywayActivity.this, "接口获取失败", "当前系统未安装微信刷脸程序");
                    }

                }
            });
        }
        catch(Exception ex){
            ToastUtil.showToast(ChoosepaywayActivity.this, "温馨提示", "当前系统未安装微信刷脸程序");
        }
    }


    private boolean isSuccessInfo(Map info) {
        if (info == null) {

            ToastUtil.showToast(ChoosepaywayActivity.this, "温馨提示", "调用返回为空");
            new RuntimeException("调用返回为空").printStackTrace();
            return false;
        }
        String code = (String) info.get(RETURN_CODE);
        String msg = (String) info.get(RETURN_MSG);

        if (code == null || !code.equals(WxfacePayCommonCode.VAL_RSP_PARAMS_SUCCESS)) {
            ToastUtil.showToast(ChoosepaywayActivity.this, "温馨提示", "调用返回非成功信息, 请查看日志");
            new RuntimeException("调用返回非成功信息: " + msg).printStackTrace();
            return false;
        }

        return true;
    }



    private   void  PostGetAuthInfo(String rawdata){

        AuthInfoEntityCall= RetrofitHelper.getInstance().GetAuthInfo(rawdata);
        AuthInfoEntityCall.enqueue(new Callback<AuthInfoEntity>() {
            @Override
            public void onResponse(Call<AuthInfoEntity> call, Response<AuthInfoEntity> response) {
                if (response != null) {

                    AuthInfoEntity body = response.body();
                    if (null != body) {

                        if(body.getCode().equals("success")){

                            mAuthInfo=body.getData().getMsg();

                            double a = Double.valueOf(CommonData.orderInfo.totalPrice)*10*10;
                            int total_fee= new Double(a).intValue();

                            //3.0 然后调用首先获取  facecode。用于人脸支付
                            Map<String, String> m1 = new HashMap<String, String>();
                            m1.put("appid", "wx2d784531bbb9a017"); // 公众号，必填
                            m1.put("mch_id", "1334045901"); // 商户号，必填
                            m1.put("sub_appid", CommonData.wxappid); // 商户号，必填
                            m1.put("sub_mch_id", CommonData.wxshid); // 商户号，必填
                            m1.put("store_id", CommonData.khid); // 门店编号，必填
                            m1.put("out_trade_no", CommonData.orderInfo.prepayId); // 商户订单号， 必填
                            m1.put("total_fee", String.valueOf(total_fee)); // 订单金额（数字），单位：分，必填
                            m1.put("face_authtype", "FACEPAY"); // FACEPAY：人脸凭证，常用于人脸支付    FACEPAY_DELAY：延迟支付   必填
                            m1.put("authinfo", mAuthInfo);
                            m1.put("ask_face_permit", "0"); // 展开人脸识别授权项，详情见上方接口参数，必填
                            m1.put("ask_ret_page", "0"); // 是否展示微信支付成功页，可选值："0"，不展示；"1"，展示，非必填

                            WxPayFace.getInstance().getWxpayfaceCode(m1, new IWxPayfaceCallback() {
                                @Override
                                public void response(final Map info) throws RemoteException {
                                    if (info == null) {
                                        new RuntimeException("调用返回为空").printStackTrace();
                                        return;
                                    }
                                    String code = (String) info.get("return_code"); // 错误码
                                    String msg = (String) info.get("return_msg"); // 错误码描述

                                    if (!code.equals("SUCCESS")) {

                                        //没有成功需要关掉 人脸支付
                                        HashMap<String, String> map = new HashMap<String, String>();
                                        map.put("authinfo", mAuthInfo); // 调用凭证，必填
                                        WxPayFace.getInstance().stopWxpayface(map, new IWxPayfaceCallback() {
                                            @Override
                                            public void response(Map info) throws RemoteException {
                                                if (info == null) {
                                                    new RuntimeException("调用返回为空").printStackTrace();
                                                    return;
                                                }
                                                String code = (String) info.get("return_code"); // 错误码
                                                String msg = (String) info.get("return_msg"); // 错误码描述
                                                if (code == null || !code.equals("SUCCESS")) {
                                                    new RuntimeException("调用返回非成功信息,return_msg:" + msg + "   ").printStackTrace();
                                                    return;
                                                }
                                            }
                                        });
                                    }

                                    if(code.equals("SUCCESS")) {
                                        String faceCode = info.get("face_code").toString(); // 人脸凭证，用于刷脸支付
                                        openid = info.get("openid").toString(); // 人脸凭证，用于刷脸支付
                                        //在这里处理业务逻辑
                                        Facecode = faceCode;//将刷脸支付返回码进行订单调用

                                        wxFaceMoneypay();
                                    }

                                    //WxPayFace.getInstance().releaseWxpayface(payWayActivity.this);

                                }
                            });

                        }
                        else
                        {
                            ToastUtil.showToast(ChoosepaywayActivity.this, "接口异常", body.getMsg());
                            return;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<AuthInfoEntity> call, Throwable t) {

            }
        });
    }


    private void InterfaceGetAuthInfo(String rowdata) {

            // 详细的参数配置表可见上方的“接口参数表”
            Map<String, String> m1 = new HashMap<String, String>();
            m1.put("appid", CommonData.wxappid); // 公众号，必填
            m1.put("mch_id", CommonData.wxshid); // 商户号，必填
//        m1.put("sub_appid", "xxxxxxxxxxxxxx"); // 子商户公众账号ID(非服务商模式不填)
//        m1.put("sub_mch_id", "填您的子商户号"); // 子商户号(非服务商模式不填)
            m1.put("store_id", CommonData.khid); // 门店编号，必填
            m1.put("face_authtype", "FACEID-ONCE"); // 人脸识别模式， FACEID-ONCE`: 人脸识别(单次模式) FACEID-LOOP`: 人脸识别(循环模式), 必填
            m1.put("authinfo", "填您的调用凭证"); // 调用凭证，详见上方的接口参数
//        m1.put("ask_unionid", "1"); // 是否获取union_id    0：获取    1：不获取
            WxPayFace.getInstance().getWxpayfaceUserInfo(m1, new IWxPayfaceCallback() {
                @Override
                public void response(Map info) throws RemoteException {
                    if (info == null) {
                        new RuntimeException("调用返回为空").printStackTrace();
                        return;
                    }
                    String code = (String) info.get("return_code"); // 错误码
                    String msg = (String) info.get("return_msg"); // 错误码描述
                    String openid = info.get("openid").toString(); // openid
                    String sub_openid = "";
                    if (info.get("sub_openid") != null) sub_openid = info.get("sub_openid").toString(); // 子商户号下的openid(服务商模式)
                    String nickName = info.get("nickname").toString(); // 微信昵称
                    String token = "";
                    if (info.get("token") != null) token = info.get("token").toString(); // facesid,用户获取unionid
                    if (code == null || openid == null || nickName == null || !code.equals("SUCCESS")) {
                        new RuntimeException("调用返回非成功信息,return_msg:" + msg + "   ").printStackTrace();
                        return ;
                    }
                /*
                获取union_id逻辑，传入参数ask_unionid为"1"时使用
                String unionid_code = "";
                if (info.get("unionid_code") != null) unionid_code = info.get("unionid_code").toString();
                if (TextUtils.equals(unionid_code,"SUCCESS")) {
                    //获取union_id逻辑
                } else {
                    String unionid_msg = "";
                    if (info.get("unionid_msg") != null) unionid_msg = info.get("unionid_msg").toString();
                    //处理返回信息
                }
                */
       	        /*
       	        在这里处理您自己的业务逻辑
       	        需要注意的是：
       	            1、上述注释中的内容并非是一定会返回的，它们是否返回取决于相应的条件
       	            2、当您确保要解开上述注释的时候，请您做好空指针的判断，不建议直接调用
       	         */
                }
            });

    }



    public void wxFaceMoneypay() {

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
        else if (CommonData.payWay.equals("WXFacePay"))
        {
            payid="3";
        }

        //支付方式
        List<OrderpayRequest.DataBean.PayMapBean> payMap = new ArrayList<OrderpayRequest.DataBean.PayMapBean>();
        OrderpayRequest.DataBean.PayMapBean pmp = new OrderpayRequest.DataBean.PayMapBean();
        pmp.setPayTypeId(payid);
        pmp.setPayVal(Double.valueOf(CommonData.orderInfo.totalPrice));
        payMap.add(pmp);

        OrderpayResponseCall= RetrofitHelper.getInstance().Orderpay(Facecode,openid,CommonData.orderInfo.prepayId,pluMap,payMap);
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
                                Intent intent = new Intent(ChoosepaywayActivity.this, FinishActivity.class);
                                startActivity(intent);
                                finish();
                                return;

                            }
                            else if(response.body().getData().getPaycode().equals("3"))
                            {
                                //说明支付在等待，跳转到支付轮询界面

                                Intent intent = new Intent(ChoosepaywayActivity.this, SearchingPayActivity.class);
                                startActivity(intent);
                                finish();
                                return;
                            }

                        }
                        else{
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("authinfo", mAuthInfo); // 调用凭证，必填
                            WxPayFace.getInstance().stopWxpayface(map, new IWxPayfaceCallback() {
                                @Override
                                public void response(Map info) throws RemoteException {
                                    if (info == null) {
                                        new RuntimeException("调用返回为空").printStackTrace();
                                        return;
                                    }
                                    String code = (String) info.get("return_code"); // 错误码
                                    String msg = (String) info.get("return_msg"); // 错误码描述
                                    if (code == null || !code.equals("SUCCESS")) {
                                        new RuntimeException("调用返回非成功信息,return_msg:" + msg + "   ").printStackTrace();
                                        return;
                                    }
                                }
                            });

                            //释放刷脸资源
                            WxPayFace.getInstance().releaseWxpayface(ChoosepaywayActivity.this);


                            ToastUtil.showToast(ChoosepaywayActivity.this, "支付失败", response.body().getMsg());
                            return;
                        }
                    }

                }
                else
                {
                    ToastUtil.showToast(ChoosepaywayActivity.this, "支付通知", "操作异常");
                    return;
                }

            }

            @Override
            public void onFailure(Call<OrderpayResponse> call, Throwable t) {
                isPay=true;  //只有当这一笔支付完成 之后  才可以重新进行支付
                ToastUtil.showToast(ChoosepaywayActivity.this, "支付通知", "操作异常");
                return;
            }
        });
    }


}
