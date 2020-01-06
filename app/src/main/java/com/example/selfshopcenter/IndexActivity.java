package com.example.selfshopcenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.selfshopcenter.commoncls.CommonData;

public class IndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);


        //设置底部的显示信息
        TextView storename=findViewById(R.id.storename);
        storename.setText("门店编号:"+ CommonData.khid);
        TextView storechinesename=findViewById(R.id.idnexstorename);
        storechinesename.setText(CommonData.khsname);
        TextView appmachine=findViewById(R.id.appmachine);
        appmachine.setText("设备编号:"+ CommonData.deviceid);


        TextView appversion=findViewById(R.id.appversion);
        appversion.setText("Version : "+ CommonData.app_version);
        if (CommonData.app_version.equals("")){
            appversion.setText(CommonData.getAppVersion(this));//因为出现过版本号未获取到的的问题，所以这么写
        }


        //清空会员基础信息，清空购物车信息




        //避免万一断网情况下，数据未正常清空。清空失败 这种情况呢？
        CommonData.hyMessage=null;
        CommonData.orderInfo=null;



        //绑定 开始购物
        Button button_shape=findViewById(R.id.shopping);
        button_shape.setOnClickListener(new View.OnClickListener() {

            @Override
            public  void  onClick(View view) {

                //Toast.makeText(IndexActivity.this,"正在跳转，请等待",Toast.LENGTH_SHORT).show();
                //跳转到商品录入界面
                Intent intent = new Intent(IndexActivity.this, CarItemsActivity.class);
                startActivity(intent);
            }
        });
    }
}
