package com.example.selfshopcenter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //1.0  首先创建出数据库  BaseInfo ，用于保存信息
        try {
            dbHelper = new MyDatabaseHelper(this, "userinfo.db", null, 1);
            querydb = dbHelper.getWritableDatabase();

        } catch (Exception ex) {
            //如果创建异常
            //Toast.makeText(PosLoginActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
            ToastUtil.showToast(LoginActivity.this, "异常通知", "初始化本地Sqlite信息失败");
            return;
        }

        CommonData.app_version=CommonData.getAppVersion(this);


        //2.0  先从本地选取初始化数据，如果拿到了，说明初始化过，则直接跳转，跳过登录
        InitData(querydb);

        //3.0  比较 app  版本号信息，是否需要升级
        PrepareUpdateVersion();


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

                                      WirttenDataToSqlite();

                                  }
                                  else
                                  {
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

            values.put("khid", CommonData.khid);
            values.put("khsname", CommonData.khsname);
            values.put("posid", CommonData.posid);
            values.put("deviceid", CommonData.deviceid);
            values.put("app_version", CommonData.app_version);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String today = sdf.format(date);

            values.put("date_lr", today);
            values.put("mch_id", "");
            values.put("number", 1);

            db.insert(CommonData.tablename, null, values);

            values.clear();

        }
        catch (Exception ex) {

        }
        //显示跳转
        Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
        startActivity(intent);
    }



    //准备预升级
    public   void PrepareUpdateVersion(){

        try {

        }
        catch(Exception ex){
            ToastUtil.showToast(LoginActivity.this, "输入消息通知", ex.toString());
        }
    }



    public   void  EnsureUPdate(){

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //设置在什么网络情况下进行下载
        //request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);

        //设置通知栏标题
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle("任务进行中");
        request.setDescription("应用程序正在下载中");
        request.setAllowedOverRoaming(false);
        //设置文件存放目录
        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "AIINBI.apk");

        DownloadManager downManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        long id= downManager.enqueue(request);

        //确认要下载时 ，先删除 sqlite里面的表数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(CommonData.tablename,null,null);


        queryDownloadProgress(this,id,downManager);
    }


    private void queryDownloadProgress(Context   context,long requestId, DownloadManager downloadManager) {


        DownloadManager.Query query=new DownloadManager.Query();
        //根据任务编号id查询下载任务信息
        query.setFilterById(requestId);
        try {
            boolean isGoging=true;
            while (isGoging) {
                Cursor cursor = downloadManager.query(query);
                if (cursor != null && cursor.moveToFirst()) {

                    //获得下载状态
                    int state = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    switch (state) {
                        case DownloadManager.STATUS_SUCCESSFUL://下载成功
                            isGoging=false;
                            Uri downloadFileUri;
                            Intent install = new Intent(Intent.ACTION_VIEW);
                            //调用安装方法,进行自动升级
                            //Uri downloadFileUri = downloadManager.getUriForDownloadedFile(requestId);
                            //Uri downloadFileUri = DownloadManager.COLUMN_LOCAL_URI;
                            boolean haveInstallPermission;
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { // 6.0以下
                                downloadFileUri = downloadManager.getUriForDownloadedFile(requestId);

                            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
                            { // 6.0 - 7.0
                                String uriString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                                File apkFile = new File(Uri.parse(uriString).getPath());
                                downloadFileUri = Uri.fromFile(apkFile);

                            } else { // Android 7.0 以上

                                //haveInstallPermission = getPackageManager().canRequestPackageInstalls();  //需要 level版本支持


                                downloadFileUri = FileProvider.getUriForFile(context,
                                        "com.ceshi.helloworld.fileProvider",
                                        new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "AIINBI.apk"));
                                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            }


                            if (downloadFileUri != null) {

                                install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(install);
                            }

                            //handler.obtainMessage(downloadManager.STATUS_SUCCESSFUL).sendToTarget();//发送到主线程，更新ui
                            break;
                        case DownloadManager.STATUS_FAILED://下载失败
                            isGoging=false;
                            //handler.obtainMessage(downloadManager.STATUS_FAILED).sendToTarget();//发送到主线程，更新ui
                            break;

                        case DownloadManager.STATUS_RUNNING://下载中
                            /**
                             * 计算下载下载率；
                             */
                            int totalSize = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                            int currentSize = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                            int progress = (int) (((float) currentSize) / ((float) totalSize) * 100);
                            //handler.obtainMessage(downloadManager.STATUS_RUNNING, progress).sendToTarget();//发送到主线程，更新ui
                            break;

                        case DownloadManager.STATUS_PAUSED://下载停止
                            //handler.obtainMessage(DownloadManager.STATUS_PAUSED).sendToTarget();
                            break;

                        case DownloadManager.STATUS_PENDING://准备下载
                            //handler.obtainMessage(DownloadManager.STATUS_PENDING).sendToTarget();
                            break;
                    }
                }
                if(cursor!=null){
                    cursor.close();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
