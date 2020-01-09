package com.example.selfshopcenter.commoncls;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

    public class MyDatabaseHelper extends SQLiteOpenHelper {


        private Context mContext;

        //声明一个建表的语句，用于存储这个门店 和机器编号,和录入日期，app版本，微信支付商户号,门店商户id-》lCorpId,个人id-》userId
        public static final String CREATE_SQL = "create table  " + CommonData.tablename + " ("

                + "khid text  primary key, "

                + "posid text   , "   //后台返回的该机器使用的posid

                + "khsname  text , "

                + "deviceid  text, "

                + "app_version  text, "

                + "date_lr date, "

                + "zfbappid text, "

                + "number integer, "   //流水号

                + "wxappid text ,"   //微信appid

                + "wxshid text )";   //微信商户号


        //构造函数
        public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

            super(context, name, factory, version);

            mContext = context;

        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_SQL);

        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    }
