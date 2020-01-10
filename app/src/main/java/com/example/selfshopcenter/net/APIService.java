package com.example.selfshopcenter.net;
import com.example.selfshopcenter.bean.*;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface APIService {

    /**
     * 门店信息登陆或注册
     */
    @POST("Export/GetUseBleData")
    Call<UserLoginEntity> UserloginInfo(@Body RequestBody requestBody);



    /**
     * 增加或减少产品
     */
    @POST("AddspinDetails/Addspinfo")
    Call<AddGoodsEntity> AddGoodInfo(@Body RequestBody requestBody);


    /**
     * 增加或减少产品
     */
    @POST("Export/GetUseBleData")
    Call<ClearCarEntity> ClearCarInfo(@Body RequestBody requestBody);


    /**
     * 增加或减少产品
     */
    @POST("AddspinDetails/OrderPay")
    Call<OrderpayResponse> Orderpay(@Body RequestBody requestBody);


    /**
     * 获取刷脸信息
     */
    @POST("AddspinDetails/GetAuthInfo")
    Call<AuthInfoEntity> GetAuthInfo(@Body RequestBody requestBody);


    /**
     * 增加或减少产品
     */
    @POST("Export/GetUseBleData")
    Call<ShopBagEntity> GetshopBag(@Body RequestBody requestBody);
}
