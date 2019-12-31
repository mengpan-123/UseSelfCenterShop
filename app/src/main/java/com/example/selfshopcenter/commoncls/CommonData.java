package com.example.selfshopcenter.commoncls;

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
}
