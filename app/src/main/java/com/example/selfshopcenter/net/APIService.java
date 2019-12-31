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
}
