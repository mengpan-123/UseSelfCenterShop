package com.example.selfshopcenter.printer;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.selfshopcenter.commoncls.CommonData;

import java.text.SimpleDateFormat;
import java.util.Date;


public class PrintUtil {
    private final static String TAG = PrintUtil.class.getName();
    private static MsUsbPrinter printer;

    /*初始化打印机*/
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
    public static void printReceipt(Bitmap logobmp,String  str) {
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
            printer.printString(CommonData.khsname);
            printer.setAlignMode(0);
            printer.setFontSize(0, 0);
            printer.setFeedLine(1);


            line =str;
            printer.printString(line);

            //获取当前时间并 打印时间
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");// HH:mm:ss
            Date newdate = new Date(System.currentTimeMillis());

            line="";

            line+="====================="+simpleDateFormat.format(newdate)+"==================="+"\n";
            line+="            谢谢惠顾，请妥善保管小票         "+"\n";
            line+="               开正式发票，当月有效              "+"\n";

            printer.printString(line);

            // 走纸换行、切纸、清理缓存
            printer.setFeedLine(5); // 走纸换行
            printer.setCutPaper(0); // 切纸类型
            printer.setClean();

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
    }


}
