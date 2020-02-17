package com.example.selfshopcenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.selfshopcenter.bean.UpdateVersionEntity;
import com.example.selfshopcenter.bean.UserLoginEntity;
import com.example.selfshopcenter.commoncls.CommonData;
import com.example.selfshopcenter.commoncls.MyDatabaseHelper;
import com.example.selfshopcenter.commoncls.ToastUtil;

import com.example.selfshopcenter.net.RetrofitHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase querydb;

    private String url = "http://192.168.0.108/222/MyApp1.apk";

    private int  Appvercode=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //1.0  首先创建出数据库  BaseInfo ，用于保存信息
        try {
            dbHelper = new MyDatabaseHelper(this, "centertable.db", null, 1);
            querydb = dbHelper.getWritableDatabase();


        } catch (Exception ex) {
            ToastUtil.showToast(LoginActivity.this, "异常通知", "初始化本地数据库信息失败");
            return;
        }

        CommonData.app_version=CommonData.getAppVersion(this);
        Appvercode=CommonData.getAppVersioncode(this);

        //2.0  先从本地选取初始化数据，如果拿到了，说明初始化过，则直接跳转，跳过登录
        InitData(querydb);


        //3.0  比较 app  版本号信息，检测是否需要升级
        //PrepareUpdateVersion();


        if (!CommonData.khid.equals("") && !CommonData.posid.equals("")) {

            //说明已经初始化过了 ，直接跳转到欢迎的首界面,显示跳转
            Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
            startActivity(intent);
            return;
        }
        else
        {
            setContentView(R.layout.activity_login);  //设置页面

            EditText edt = findViewById(R.id.inputmachine);
            edt.setText(CommonData.deviceid);
            edt.setCursorVisible(false);
            edt.setFocusable(false);
            edt.setFocusableInTouchMode(false);

        }


        //否则的话就需要登录，然后绑定相应的登陆事件
        Button Login = findViewById(R.id.Login);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                 //请求接口，获取最新基础信息
                TextView inputkhid = findViewById(R.id.inputkhid);
                TextView inputqyid = findViewById(R.id.inputqyid);

                CommonData.QYID = inputqyid.getText().toString();
                CommonData.khid = inputkhid.getText().toString();
                if (CommonData.khid.length() == 0) {
                    ToastUtil.showToast(LoginActivity.this, "输入消息通知", "请输入完整的门店号或者设备号");
                    return;
                }

                Call<UserLoginEntity>  userlogin= RetrofitHelper.getInstance().UserloginInfo(
                        CommonData.deviceid,"ZY",CommonData.khid);
                userlogin.enqueue(new Callback<UserLoginEntity>() {
                    @Override
                    public void onResponse(Call<UserLoginEntity> call, Response<UserLoginEntity> response) {
                        if (response != null) {
                            UserLoginEntity body = response.body();
                            if (null != body) {
                                  if(body.getCode().equals("success")){

                                      CommonData.khsname=body.getData().getSname();
                                      CommonData.posid=body.getData().getPosid();
                                      CommonData.zfbappid=body.getData().getZfbappid();
                                      CommonData.wxappid=body.getData().getWxappid();
                                      CommonData.wxshid=body.getData().getWxshid();

                                      if (CommonData.zfbappid==null||CommonData.zfbappid.equals("")){
                                          ToastUtil.showToast(LoginActivity.this, "查询配置失败", "当前门店暂未配置支付相关信息，" +
                                                  "暂无法使用，很抱歉");
                                          return;
                                      }

                                      WirttenDataToSqlite();


                                  }
                                  else
                                  {
                                      CommonData.QYID="";
                                      ToastUtil.showToast(LoginActivity.this, "接口异常", body.getMsg());
                                      return;
                                  }

                            } else {
                                ToastUtil.showToast(LoginActivity.this, "接口异常", "接口访问异常，请及时处理");
                                return;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserLoginEntity> call, Throwable t) {
                        ToastUtil.showToast(LoginActivity.this, "接口异常", t.toString());
                        return;
                    }
                });
            }
        });

    }


    public void InitData(SQLiteDatabase querydb) {

        //从本地取出数据来

        Cursor cursor = querydb.query(CommonData.tablename, null, null, null, null, null, null);

        // 查询Book表中所有的数据
        if (null != cursor) {
            if (cursor.moveToFirst()) {

                // 遍历Cursor对象，取出数据并打印
                do {
                    CommonData.khid = cursor.getString(cursor.getColumnIndex("khid"));
                    CommonData.posid = cursor.getString(cursor.getColumnIndex("posid"));
                    CommonData.khsname = cursor.getString(cursor.getColumnIndex("khsname"));
                    CommonData.wxappid = cursor.getString(cursor.getColumnIndex("wxappid"));
                    CommonData.zfbappid = cursor.getString(cursor.getColumnIndex("zfbappid"));
                    CommonData.wxshid = cursor.getString(cursor.getColumnIndex("wxshid"));
                    CommonData.QYID= cursor.getString(cursor.getColumnIndex("qyid"));
                }
                while
                (cursor.moveToNext());
            }

            cursor.close();
        }
    }




    public void WirttenDataToSqlite() {

        try {
            //然后将 以上获取到的 数据 写入到 本地数据库中
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("qyid", CommonData.QYID);
            values.put("khid", CommonData.khid);
            values.put("khsname", CommonData.khsname);
            values.put("posid", CommonData.posid);
            values.put("deviceid", CommonData.deviceid);
            values.put("app_version", CommonData.app_version);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String today = sdf.format(date);

            values.put("date_lr", today);
            values.put("zfbappid", CommonData.zfbappid);
            values.put("wxappid", CommonData.wxappid);
            values.put("number", 1);
            values.put("wxshid", CommonData.wxshid);

            db.insert(CommonData.tablename, null, values);

            values.clear();

        }
        catch (Exception ex) {
            ToastUtil.showToast(LoginActivity.this, "输入消息通知", ex.toString());
        }
        //显示跳转
        Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
        startActivity(intent);
    }


}
