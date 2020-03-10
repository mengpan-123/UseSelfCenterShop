package com.example.selfshopcenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.selfshopcenter.bean.SearchPosEntity;
import com.example.selfshopcenter.commoncls.ToastUtil;
import com.example.selfshopcenter.net.RetrofitHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoUseActivity  extends AppCompatActivity {

    private String  posUse="0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nouse);


        //重新检测
        TextView print = findViewById(R.id.surePrint);
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetStoreStatus();

            }
        });

    }

    public  void  GetStoreStatus(){

        //查询收音机使用状态
        Call<SearchPosEntity> search= RetrofitHelper.getInstance().SearchUseStatus();
        search.enqueue(new Callback<SearchPosEntity>() {
            @Override
            public void onResponse(Call<SearchPosEntity> call, Response<SearchPosEntity> response) {

                if (response.body() != null) {

                    if (response.body().getCode().equals("success")||response.body().getMsg().equals("没有符合条件的数据")) {
                        posUse=response.body().getData().getPosstatus();//等于1 启动，等于0禁用

                        if (posUse.equals("1")) {
                            //跳转到商品录入界面
                            Intent intent = new Intent(NoUseActivity.this, IndexActivity.class);
                            //Intent intent = new Intent(IndexActivity.this, FinishActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            ToastUtil.showToast(NoUseActivity.this, "温馨提示", "当前收银机已被禁止使用，请联系管理员在后台启用");

                        }

                    }
                    else
                    {
                        ToastUtil.showToast(NoUseActivity.this, "接口异常", response.body().getMsg());

                    }

                }
            }

            @Override
            public void onFailure(Call<SearchPosEntity> call, Throwable t) {

            }
        });

    }


}
