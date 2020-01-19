package com.example.selfshopcenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.appcompat.app.AppCompatActivity;

import com.example.selfshopcenter.bean.SearchPayEntity;
import com.example.selfshopcenter.net.RetrofitHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchingPayActivity  extends AppCompatActivity {

    private String payResult="";
    private  int  i=0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_searchpay);  //设置页面


        try {
            new Thread() {
                @Override
                public void run() {
                    //应该是界面先显示，然后再来执行这个轮询。轮询支付界面，最多10次进行等待
                    while (i < 20) {

                        SystemClock.sleep(2000);

                        getPayresult();

                        if (payResult=="OK"){
                            Intent intent = new Intent(SearchingPayActivity.this, FinishActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        }

                        i++;
                    }

                    if (i>=20){
                        Intent intent = new Intent(SearchingPayActivity.this, PayFailActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }.start();

        }
        catch(Exception ex){

        }


    }


    /**
     * Created by zhoupan on 2019/11/8.
     * 轮询支付结果信息
     * */
    public void getPayresult() {

        Call<SearchPayEntity> query= RetrofitHelper.getInstance().QueryOrders();
        query.enqueue(new Callback<SearchPayEntity>() {
            @Override
            public void onResponse(Call<SearchPayEntity> call, Response<SearchPayEntity> response) {

                if (response.body() != null) {

                    if (response.body().getCode().equals("success")){

                        if (  response.body().getData().getPaycode().equals("200")  ){
                            payResult="OK";
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchPayEntity> call, Throwable t) {

            }
        });
    }

}
