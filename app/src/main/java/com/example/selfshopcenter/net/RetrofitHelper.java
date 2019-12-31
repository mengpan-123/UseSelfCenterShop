package com.example.selfshopcenter.net;

import com.example.selfshopcenter.bean.*;
import com.example.selfshopcenter.commoncls.CommonData;

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

}
