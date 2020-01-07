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
                "    \"appid\": \"keengee\",\n" +
                "    \"apiname\": \"KHLOGIN\",\n" +
                "    \"req_operator\": \"zp\",\n" +
                "    \"data\": {\n" +
                "        \"deviceid\": \""+deviceid+"\",\n" +
                "        \"client_type\":\""+client_type+"\",\n" +
                "        \"khid\": \""+khid+"\"\n" +
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
                "    \"appid\": \"keengee\",\n" +
                "    \"apiname\": \"GetGoodsInfo\",\n" +
                "    \"req_operator\": \"zp\",\n" +
                "    \"data\": {\n" +
                "        \"barcode\": \"2365918224054\",\n" +
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

    /***
     * 订单支付接口
     *请求方式 POST，数据格式application/json
     * */
    public Call<OrderpayRequest> Orderpay(
                                          String AuthCode,
                                          String appid,
                                          String setWxshid,
                                          String  openid,
                                          String  transId,
                                          List<OrderpayRequest.DataBean.PluMapBean> pluMap,
                                          List<OrderpayRequest.DataBean.PayMapBean> payMap){

        OrderpayRequest requestSignBean = new OrderpayRequest();

        requestSignBean.getData().setKhid(CommonData.khid);
        requestSignBean.getData().setPosid(CommonData.posid);
        requestSignBean.getData().setPayWay(CommonData.payWay);
        requestSignBean.getData().setPaycode(AuthCode);
        requestSignBean.getData().setAppid(appid);
        requestSignBean.getData().setWxshid(setWxshid);
        requestSignBean.getData().setPrepayId(CommonData.orderInfo.prepayId);//支付单号


        requestSignBean.getData().setPluMap(pluMap);
        requestSignBean.getData().setPayMap(payMap);


        //如果是刷脸支付可能需要多使用一些参数



        String s= JSON.toJSONString(requestSignBean);



        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        return mAPIService.Orderpay(requestBody);

    }

}
