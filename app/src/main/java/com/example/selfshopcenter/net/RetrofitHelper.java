package com.example.selfshopcenter.net;

import com.alibaba.fastjson.JSON;
import com.example.selfshopcenter.bean.*;
import com.example.selfshopcenter.commoncls.CommonData;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    private static final long DEFAULT_TIMEOUT = 30;
    private static RetrofitHelper mInstance;
    private APIService mAPIService;


    private RetrofitHelper() {
        initRetrofit();
    }

    public static RetrofitHelper getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitHelper.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitHelper();
                }
            }
        }
        return mInstance;
    }
    /**
     * 这里的url必须时域名前的那段baseurl  比如
     * http://dvp.rongxwy.com/base/rest/v3/CartService/getMchIdByStoreId?storeId=S2101
     * 取http://dvp.rongxwy.com/为他的baseurl
     */
    private void initRetrofit() {
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(CommonData.CommonUrl)
                .addConverterFactory(GsonConverterFactory.create())  //添加JSON解析器，对接口返回的内容进行一定的解析
                .client(getBuilder().build())  //创建这个网络请求是用OkHttp进行访问
                .build();   //开始创建/访问
        mAPIService = retrofit.create(APIService.class);
    }

    private OkHttpClient.Builder getBuilder() {

        Interceptor controlInterceptor = chain -> {
            Request request = chain.request();
            RequestBody oldBody = request.body();
            Buffer buffer = new Buffer();
            assert oldBody != null;
            oldBody.writeTo(buffer);
            String data = buffer.readUtf8();
            return chain.proceed(request);
        };


        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(controlInterceptor)
                .retryOnConnectionFailure(true);
        return builder;
    }


    /**
     * 输入门店号和设备号之后 进行登陆/注册
     *
     * @param deviceid 机器编号
     * @param client_type 门店类型
     * @param client_type 门店编码
     */
    public Call<UserLoginEntity> UserloginInfo(String deviceid, String client_type,String khid) {



        String  s="{\n" +
                "    \"appid\": \""+CommonData.kquser+"\",\n" +
                "    \"apiname\": \"KHLOGIN\",\n" +
                "    \"req_operator\": \"zp\",\n" +
                "    \"data\": {\n" +
                "        \"deviceid\": \""+deviceid+"\",\n" +
                "        \"client_type\":\""+client_type+"\",\n" +
                "        \"khid\": \""+khid+"\",\n" +
                "        \"qyid\": \""+CommonData.QYID+"\"\n" +
                "    }\n" +
                "}\n";


        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        return mAPIService.UserloginInfo(requestBody);
    }

    /**
     * 查询产品基础信息并添加
     *
     * @param barcode 商品条码
     * @param stype  操作类型  SEARCH  查询   ADD添加    REDUCE  移除一个产品
     */
    public Call<AddGoodsEntity> AddGoodInfo(String barcode, String  stype) {



        String  s="{\n" +
                "    \"appid\": \""+CommonData.kquser+"\",\n" +
                "    \"apiname\": \"GetGoodsInfo\",\n" +
                "    \"req_operator\": \"zp\",\n" +
                "    \"data\": {\n" +
                "        \"barcode\": \""+barcode+"\",\n" +
                "        \"qyid\": \""+CommonData.QYID+"\",\n" +
                "        \"posid\":\""+CommonData.posid+"\",\n" +
                "        \"khid\": \""+CommonData.khid+"\",\n" +
                "        \"stype\": \""+stype+"\"\n" +
                "    }\n" +
                "}\n";


        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        return mAPIService.AddGoodInfo(requestBody);
    }


    /**
     * 查询产品基础信息并添加
     *
     * @param barcode 商品条码
     * @param stype  操作类型  SEARCH  查询   ADD添加    REDUCE  移除一个产品
     */
    public Call<DeleteSpinfoEntity> DeleteSpinfo(String barcode, String  stype) {



        String  s="{\n" +
                "    \"appid\": \""+CommonData.kquser+"\",\n" +
                "    \"apiname\": \"DELETESP\",\n" +
                "    \"req_operator\": \"zp\",\n" +
                "    \"data\": {\n" +
                "        \"barcode\": \""+barcode+"\",\n" +
                "        \"qyid\": \""+CommonData.QYID+"\",\n" +
                "        \"posid\":\""+CommonData.posid+"\",\n" +
                "        \"khid\": \""+CommonData.khid+"\",\n" +
                "        \"stype\": \""+stype+"\"\n" +
                "    }\n" +
                "}\n";


        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        return mAPIService.DeleteSpinfo(requestBody);
    }



    /**
     * 清空购物车产品信息
     *
     * @param khid   门店信息
     * @param posid  款台编号
     */
    public Call<ClearCarEntity> ClearCarInfo(String khid, String  posid) {

        String  s="{\n" +
                "    \"appid\": \"keengee\",\n" +
                "    \"apiname\": \"CLEARCAR\",\n" +
                "    \"req_operator\": \"zp\",\n" +
                "    \"data\": {\n" +
                "        \"khid\": \""+khid+"\",\n" +
                "        \"posid\": \""+posid+"\"\n" +
                "    }\n" +
                "}";


        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        return mAPIService.ClearCarInfo(requestBody);
    }


    /**
     * 查询自助收银机使用状态
     */
    public Call<SearchPosEntity> SearchUseStatus() {

        String  s="{\n" +
                "    \"appid\": \"keengee\",\n" +
                "    \"apiname\": \"DEVICECONTROL\",\n" +
                "    \"req_operator\": \"zp\",\n" +
                "    \"data\": {\n" +
                "    \t\"qyid\":\""+CommonData.QYID+"\",\n" +
                "        \"khid\": \""+CommonData.khid+"\",\n" +
                "        \"pc_id\":\""+CommonData.deviceid+"\"\n" +
                "    }\n" +
                "}\n";


        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        return mAPIService.SearchUseStatus(requestBody);
    }

    /***
     * 订单支付接口
     *请求方式 POST，数据格式application/json
     * */
    public Call<OrderpayResponse> Orderpay(
                                          String AuthCode,
                                          String  openid,
                                          String  transId,
                                          List<OrderpayRequest.DataBean.PluMapBean> pluMap,
                                          List<OrderpayRequest.DataBean.PayMapBean> payMap){

        OrderpayRequest requestSignBean = new OrderpayRequest();

        requestSignBean.setAppid(CommonData.kquser);
        requestSignBean.setApiname("ORDERPAY");
        requestSignBean.setReq_operator("zhoupan");

        OrderpayRequest.DataBean   dataBeaninfo=new  OrderpayRequest.DataBean();


        dataBeaninfo.setKhid(CommonData.khid);
        dataBeaninfo.setKhsname(CommonData.khsname);
        dataBeaninfo.setPosid(CommonData.posid);
        dataBeaninfo.setPayWay(CommonData.payWay);


        dataBeaninfo.setPaycode(AuthCode);
        if (CommonData.payWay.equals("AliPaymentCodePay"))
        {
            dataBeaninfo.setWxshid("");  //2020-01-15首先设置一个空字符串，备用
            dataBeaninfo.setAppid(CommonData.zfbappid);
        }
        else if (CommonData.payWay.equals("WXPaymentCodePay"))
        {
            dataBeaninfo.setAppid(CommonData.wxappid);
            dataBeaninfo.setWxshid(CommonData.wxshid);
        }
        else if (CommonData.payWay.equals("WXFacePay"))
        {
            dataBeaninfo.setAppid(CommonData.wxappid);
            dataBeaninfo.setWxshid(CommonData.wxshid);
            dataBeaninfo.setOpenid(openid);

        }

        dataBeaninfo.setPrepayId(CommonData.orderInfo.prepayId);//支付单号


        dataBeaninfo.setPluMap(pluMap);
        dataBeaninfo.setPayMap(payMap);


        //如果是刷脸支付可能需要多使用一些参数



        //String s= JSON.toJSONString(requestSignBean);


        requestSignBean.setData(dataBeaninfo);
        String postJson= JSON.toJSONString(requestSignBean);

//        String  postJson="{\n" +
//                "    \"appid\": \"keengee\",\n" +
//                "    \"apiname\": \"ORDERPAY\",\n" +
//                "    \"req_operator\": \"zp\",\n" +
//                "    \"data\": {\n" +s+
//                "    }\n" +
//                "}";



        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), postJson);
        return mAPIService.Orderpay(requestBody);

    }

    /**
     * 获取刷脸支付使用的 authinfo 信息
     *
     */
    public Call<AuthInfoEntity> GetAuthInfo(String rawdata) {



        String  s="{\n" +
                "    \"appid\": \""+CommonData.kquser+"\",\n" +
                "    \"apiname\": \"GetAuthInfo\",\n" +
                "    \"req_operator\": \"zp\",\n" +
                "    \"data\": {\n" +
                "        \"khid\": \""+CommonData.khid+"\",\n" +
                "        \"rowdata\": \""+rawdata+"\",\n" +
                "        \"appid\": \""+CommonData.wxappid+"\",\n" +
                "        \"device_id\": \""+CommonData.deviceid+"\",\n" +
                "        \"wxshid\": \""+CommonData.wxshid+"\"\n" +
                "    }\n" +
                "}\n";

       /* String  s="{\n" +
                "    \"appid\": \"keengee\",\n" +
                "    \"apiname\": \"GetAuthInfo\",\n" +
                "    \"req_operator\": \"zp\",\n" +
                "    \"data\": {\n" +
                "        \"khid\": \""+CommonData.khid+"\",\n" +
                "        \"rowdata\": \""+rawdata+"\",\n" +
                "        \"device_id\": \""+CommonData.deviceid+"\"\n" +
                "    }\n" +
                "}\n";*/


        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        return mAPIService.GetAuthInfo(requestBody);
    }


    /**
     * 获取刷脸支付使用的 authinfo 信息
     *
     */
    public Call<ShopBagEntity> GetshopBag() {



        String  s="{\n" +
                "    \"appid\": \""+CommonData.kquser+"\",\n" +
                "    \"apiname\": \"GETSHOPBAG\",\n" +
                "    \"req_operator\": \"zp\",\n" +
                "    \"data\": {\n" +
                "        \"khid\": \""+CommonData.khid+"\",\n" +
                "        \"posid\": \""+CommonData.posid+"\",\n" +
                "        \"qyid\": \""+CommonData.QYID+"\"\n" +
                "    }\n" +
                "}\n";


        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        return mAPIService.GetshopBag(requestBody);
    }



    /**
     * 获取刷脸支付使用的 authinfo 信息
     *
     */
    public Call<UpdateVersionEntity> UpdateVersion() {



        String  s="{\n" +
                "    \"appid\": \""+CommonData.kquser+"\",\n" +
                "    \"apiname\": \"UPDATEVERSION\",\n" +
                "    \"req_operator\": \"zp\",\n" +
                "    \"data\": {\n" +
                "       \n" +
                "        \"khid\": \""+CommonData.khid+"\",\n" +
                "        \"qyid\": \""+CommonData.QYID+"\"\n" +
                "    }\n" +
                "}";


        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        return mAPIService.UpdateVersion(requestBody);
    }



    /**
     * 查询交易订单情况
     *
     */
    public Call<SearchPayEntity> QueryOrders() {


        String Str="";
        if (CommonData.payWay.equals("WXPaymentCodePay")){
            Str="        \"appid\": \""+CommonData.wxappid+"\",\n" ;
        }
        else
        {
            Str="        \"appid\": \""+CommonData.zfbappid+"\",\n" ;
        }


        String  s="{\n" +
                "    \"apiname\": \"QUERYORDER\",\n" +
                "    \"appid\": \""+CommonData.kquser+"\",\n" +
                "    \"data\": {\n" +
                "        "+Str+"" +
                "        \"khid\": \""+CommonData.khid+"\",\n" +
                "        \"khsname\": \""+CommonData.khsname+"\",\n" +
                "        \"payWay\": \""+CommonData.payWay+"\",\n" +
                "        \"posid\": \""+CommonData.posid+"\",\n" +
                "        \"prepayId\": \""+CommonData.orderInfo.prepayId+"\",\n" +
                "        \"payval\":\""+CommonData.orderInfo.totalPrice+"\",\n" +
                "        \"wxshid\": \""+CommonData.wxshid+"\"\n" +
                "    },\n" +
                "    \"req_operator\": \"zhoupan\"\n" +
                "}";


        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        return mAPIService.QueryOrders(requestBody);
    }



    public Call<SearchOrderEntity>  NewPrintById(String  trans_id){
        String s="{\n" +
                "    \"appid\": \""+CommonData.kquser+"\",\n" +
                "    \"apiname\": \"NEWPRINT\",\n" +
                "    \"req_operator\": \"zp\",\n" +
                "    \"data\": {\n" +
                "        \"khid\": \""+CommonData.khid+"\",\n" +
                "        \"qyid\": \""+CommonData.QYID+"\",\n" +
                "        \"trans_id\":\""+trans_id+"\"\n" +
                "       \n" +
                "    }\n" +
                "}\n";

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        return mAPIService.NewPrintById(requestBody);

    }

    //日结
    public Call<DialyCloseEntity>  DialyClose(String  saledate){
        String s="{\n" +
                "    \"appid\": \""+CommonData.kquser+"\",\n" +
                "    \"apiname\": \"DIALYCLOSE\",\n" +
                "    \"req_operator\": \"zp\",\n" +
                "    \"data\": {\n" +
                "        \"khid\": \""+CommonData.khid+"\",\n" +
                "        \"qyid\": \""+CommonData.QYID+"\",\n" +
                "        \"posid\":\""+CommonData.posid+"\",\n" +
                "        \"saledate\":\""+saledate+"\",\n" +
                "       \n" +
                "    }\n" +
                "}\n";

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        return mAPIService.DialyClose(requestBody);

    }


    //日结
    public Call<DeleteSpinfoEntity>  UPDATEVERSION(){
        String s="{\n" +
                "    \"appid\": \""+CommonData.kquser+"\",\n" +
                "    \"apiname\": \"SETAPKTIME\",\n" +
                "    \"req_operator\": \"zp\",\n" +
                "    \"data\": {\n" +
                "        \"khid\": \""+CommonData.khid+"\",\n" +
                "        \"qyid\": \""+CommonData.QYID+"\",\n" +
                "        \"version\": \""+CommonData.app_version+"\",\n" +
                "        \"posid\":\""+CommonData.posid+"\"\n" +
                "       \n" +
                "    }\n" +
                "}\n";

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        return mAPIService.UPDATEVERSION(requestBody);

    }


}
