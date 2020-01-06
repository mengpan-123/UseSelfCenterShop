package com.example.selfshopcenter.commoncls;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

public class CommonData {
    //自定义一个全局变量，作为表名称，多个地方使用
    public static String    tablename="userinfo";


    //公共的接口访问地址
    public static String    CommonUrl="http://122.112.234.28:8017";


    public  static  String  app_version="";


    public  static  String  khid="";

    public  static  String  posid="";

    public  static  String  deviceid= Build.SERIAL;


    public  static  String khsname="";


    public static HyMessage  hyMessage=null;

    //public static CreateAddAdapter list_adaptor=null;

    public static OrderInfo orderInfo=null;


    //静态字段，增加购物车产品
    public  static String AddCar="SEARCH";
    //静态字段，减少购物车产品
    public  static String SubCar="REDUCE";


  //====================以下是公共方法=================================


    //获取程序的版本号
    public static int getAppVersioncode(Context context) {
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }
    //版本号名称
    public static String getAppVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        String code = "";
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

}
