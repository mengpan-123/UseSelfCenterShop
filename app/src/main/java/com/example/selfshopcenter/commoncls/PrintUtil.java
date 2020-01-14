package com.example.selfshopcenter.commoncls;

import android.graphics.Bitmap;
import android.util.Log;

public class PrintUtil {
   /* private final static String TAG = PrintUtil.class.getName();
    private static MsUsbPrinter printer;

    *//*初始化打印机*//*
    public static boolean initPrinter() {
        if (printer == null) {
            synchronized (PrintUtil.class) {
                if (printer == null) {
                    printer = new MsUsbPrinter(UsbPrintManager.mUsbDevice, UsbPrintManager.mUsbDriver);
                }
            }
        }

        int iStatus = printer.getStatus();
        if (iStatus == 0) {
            printer.setClean();
            return true;
        } else {
            return false;
        }
    }

    //小票打印
    public static void printReceipt(Bitmap logobmp, String printstr) {
        String line = "";
        if (printer == null) {
            synchronized (PrintUtil.class) {
                if (printer == null) {
                    printer = new MsUsbPrinter(UsbPrintManager.mUsbDevice, UsbPrintManager.mUsbDriver);
                }
            }
        }


        int iStatus = printer.getStatus();
        if (iStatus != 0) {
            Log.d(TAG, "打印机状态：" + String.valueOf(iStatus));
            return;
        }

        try {
            if (logobmp != null) {
                //打印logo
                printer.setLeftMargin(200);
                printer.printImg(logobmp);
                printer.setLeftMargin(0);
            }
            //region 收据头
            printer.setFontBold(0);
            printer.setAlignMode(1);
            printer.setFontSize(1, 1);
            printer.printString("测试信息");
            printer.setAlignMode(0);
            printer.setFontSize(0, 0);
            printer.setFeedLine(1);


            printer.printString(printstr);


            //释放bmp
            if (logobmp != null) {
                logobmp = null;
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    //获取打印机状态
    public static int getPrintEndStatus() {
        if (printer == null) {
            synchronized (PrintUtil.class) {
                if (printer == null) {
                    printer = new MsUsbPrinter(UsbPrintManager.mUsbDevice, UsbPrintManager.mUsbDriver);
                }
            }
        }
        try {
            Thread.sleep(500);
            //return printer.getPrintEndStatus();
            return printer.getStatus();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return -1;
        }
    }*/
}
