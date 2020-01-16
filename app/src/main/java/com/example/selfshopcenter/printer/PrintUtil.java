package com.example.selfshopcenter.printer;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.selfshopcenter.commoncls.CommonData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


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

        if (!isNormal()) {
            return;
        }

      /*  if (printer == null) {
            synchronized (PrintUtil.class) {
                if (printer == null) {
                    printer = new MsUsbPrinter(UsbPrintManager.mUsbDevice, UsbPrintManager.mUsbDriver);
                }
            }
        }*/

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


    public static void destory(){
        if (printer!=null){
            printer = null;
        }
    }


    /**
     * @return 用于在最大努力的情况下 确保打印机正常
     */
    private static boolean isNormal() {
        UsbPrintManager.getInstance().checkUsbDevice();
        if (printer == null)
        {
            printer = new MsUsbPrinter(UsbPrintManager.mUsbDevice, UsbPrintManager.mUsbDriver);
           /* synchronized (PrintUtil.class)
            {
                if (printer == null) {

                }
            }*/
        }
      /*  int iStatus = -10;
        for (int i = 0; i < 10; i++) {
            iStatus = printer.getStatus();
            if (iStatus == -1) {
                //清空
                printer.setClean();
                try {
                    //sleep  释放锁
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    Log.i("确保打印机异常情况下 出现异常 " , e.toString());
                }
            } else if (iStatus == 0) {
                Log.i("打印机可用","");
                return true;
            } else {
                break;
            }
        }*/
//        Log.i("保证 打印机 状态 " , iStatus+"");
        // TODO: 2019/4/20 暂时性的 不管打印机是否正常可以打印
        return true;
    }

}
