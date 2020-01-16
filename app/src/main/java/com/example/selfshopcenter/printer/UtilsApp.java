package com.example.selfshopcenter.printer;


import android.app.Application;
import android.util.Log;

//import com.admin.plani.printdemo.printer.UsbPrintManager;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class UtilsApp extends Application {
    /**
     * 本身示例
     */
    private static UtilsApp sInstance;

    /**
     * 用于fragment页面有需要的
     */
    private static ScheduledExecutorService scheduledExecutorService;

    public static UtilsApp getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        scheduledExecutorService = new ScheduledThreadPoolExecutor(10, new ThreadPoolExecutor.AbortPolicy());
        UsbPrintManager.getInstance().init(this);
        checkPrint();//检查打印机是否正常
    }

    /**
     * 重新连接打印机
     */
    public static void RestartUSB(){
        UsbPrintManager.getInstance().init(sInstance);
    }

    public static ScheduledExecutorService getScheduledExecutorService(){
        return scheduledExecutorService;
    }


    Runnable checkPrintState = new Runnable() {
        @Override
        public void run() {
            int n = 5;
            for (int i = 1; ;i++ ) {
                try {
                    if (UsbPrintManager.getInstance().checkUsbDeviceTWO()) {
                        n = 5;
                        TimeUnit.MINUTES.sleep(5);
                    } else {
                        TimeUnit.SECONDS.sleep(n*i);
                    }
                } catch (InterruptedException e) {
                    Log.i("检查打印机状态 Runnable 异常" , e.getMessage());
                }
            }
        }
    };

    /**
     *检查权限
     */
    public  void checkPrint(){
        scheduledExecutorService.schedule(checkPrintState, 0, TimeUnit.SECONDS);
    }



}
