package com.example.selfshopcenter.net;
import com.example.selfshopcenter.bean.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;


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
    @POST("AddspinDetails/DeleteSpinfo")
    Call<DeleteSpinfoEntity> DeleteSpinfo(@Body RequestBody requestBody);


    /**
     * 清空购物车
     */
    @POST("Export/GetUseBleData")
    Call<ClearCarEntity> ClearCarInfo(@Body RequestBody requestBody);

    /**
     *查询收银机使用状态
     */
    @POST("Export/GetUseBleData")
    Call<SearchPosEntity> SearchUseStatus(@Body RequestBody requestBody);


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


    /**
     * 增加或减少产品
     */
    @POST("Export/GetUseBleData")
    Call<UpdateVersionEntity> UpdateVersion(@Body RequestBody requestBody);

    /**
     * 查询订单信息
     */
    @POST("AddspinDetails/Searchpayresult")
    Call<SearchPayEntity> QueryOrders(@Body RequestBody requestBody);

    /**
     * 重新打印
     */
    @POST("AddspinDetails/GetOrderInfoByOrderid")
    Call<SearchOrderEntity> NewPrintById(@Body RequestBody requestBody);


    /**
     * 日结
     */
    @POST("AddspinDetails/SearchDialyOrder")
    Call<DialyCloseEntity> DialyClose(@Body RequestBody requestBody);


    /**
     * 重新打印
     */
    @POST("AddspinDetails/Update_Version")
    Call<DeleteSpinfoEntity> UPDATEVERSION(@Body RequestBody requestBody);



    /**
     * 重新打印
     */
    @POST("AddspinDetails/GetSelfCenterGw")
    Call<AdvertiseGetEntity> GETADVERTISE(@Body RequestBody requestBody);


    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);
}
