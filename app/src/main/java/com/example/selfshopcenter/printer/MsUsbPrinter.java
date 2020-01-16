package com.example.selfshopcenter.printer;

import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;

import com.printsdk.cmd.PrintCmd;

/**
 * <pre>
 *     author: szf
 *     time  : 2018/08/02
 *     desc  : 美松USB打印机
 * </pre>
 */
public class MsUsbPrinter {
    private UsbDevice _usbDev;
    private UsbDriver _usbDriver;

    public MsUsbPrinter(UsbDevice usbDev, UsbDriver usbDriver) {
        this._usbDev = usbDev;
        this._usbDriver = usbDriver;
    }

    /**
     * 检测打印机状态
     * return: -1-异常0-正常 1-打印机未连接或未上电 2-打印机和调用库不匹配 3-打印头打开 4-切刀未复位
     * 5-打印头过热 6-黑标错误 7-纸尽 8-纸将尽
     */
    public int getStatus() {
        int iRet = -1;

        byte[] bRead1 = new byte[1];
        byte[] bWrite1 = PrintCmd.GetStatus1();
        if (_usbDriver.read(bRead1, bWrite1, _usbDev) > 0) {
            iRet = PrintCmd.CheckStatus1(bRead1[0]);
        }

        if (iRet != 0)
            return iRet;

        byte[] bRead2 = new byte[1];
        byte[] bWrite2 = PrintCmd.GetStatus2();
        if (_usbDriver.read(bRead2, bWrite2, _usbDev) > 0) {
            iRet = PrintCmd.CheckStatus2(bRead2[0]);
        }

        if (iRet != 0)
            return iRet;

        byte[] bRead3 = new byte[1];
        byte[] bWrite3 = PrintCmd.GetStatus3();
        if (_usbDriver.read(bRead3, bWrite3, _usbDev) > 0) {
            iRet = PrintCmd.CheckStatus3(bRead3[0]);
        }

        if (iRet != 0)
            return iRet;

        byte[] bRead4 = new byte[1];
        byte[] bWrite4 = PrintCmd.GetStatus4();
        if (_usbDriver.read(bRead4, bWrite4, _usbDev) > 0) {
            iRet = PrintCmd.CheckStatus4(bRead4[0]);
        }

        return iRet;
    }

    /**
     * 清除缓存,初始化
     */
    public void setClean() {
        _usbDriver.write(PrintCmd.SetClean());
    }

    /**
     * 走纸换行
     */
    public void setFeedLine(int count) {
        _usbDriver.write(PrintCmd.PrintFeedline(count), _usbDev);      // 走纸换行
    }

    /**
     * 走纸换行、切纸、清理缓存
     * iMode: 0 全切、1 半切
     */
    public void setCutPaper(int iMode) {
        _usbDriver.write(PrintCmd.PrintCutpaper(iMode), _usbDev);     // 切纸类型
    }

    /**
     * 设置行间距
     * linespace:0-127，单位0.125mm
     */
    public void setLinespace(int linespace) {
        _usbDriver.write(PrintCmd.SetLinespace(linespace));
    }

    /**
     * 设置左边界
     * linespace:0-576，单位0.125mm
     */
    public void setLeftMargin(int leftspace) {
        _usbDriver.write(PrintCmd.SetLeftmargin(leftspace));
    }

    /**
     * 设置对齐方式
     * mode; 0-左 1-居中 2-右
     */
    public void setAlignMode(int mode) {
        _usbDriver.write(PrintCmd.SetAlignment(mode), _usbDev);
    }

    /**
     * 设置文本放大
     * h; 放大高度1-8； w:放大宽度1-8
     */
    public void setFontSize(int h, int w) {
        _usbDriver.write(PrintCmd.SetSizetext(h, w), _usbDev);
    }

    /**
     * 设置字体加粗
     * iBold; 0-不加粗 1-加粗
     */
    public void setFontBold(int iBold) {
        _usbDriver.write(PrintCmd.SetBold(iBold), _usbDev);
    }

    public void setFontPage(int iMode){
        //1b 74 00 1c 2e 9d
        /*byte[] b_send = new byte[]{0x1B, 0x74, 0x00, 0x1C, 0x2E, 0x9D};
        _usbDriver.write(b_send);*/
    }

    /**
     * 打印字符串
     */
    public void printString(String data) {
        _usbDriver.write(PrintCmd.PrintString(data, 0), _usbDev);
    }

    /*一维条码打印*/
    public void printBarcode(String data) {
        _usbDriver.write(PrintCmd.SetAlignment(1), _usbDev);
        _usbDriver.write(PrintCmd.Print1Dbar(2, 100, 0, 2, 10, data), _usbDev);
        _usbDriver.write(PrintCmd.SetAlignment(0), _usbDev);
    }

    /*二维条码打印*/
    public void printQrcode(String data) {
        _usbDriver.write(PrintCmd.SetAlignment(0), _usbDev);
        _usbDriver.write(PrintCmd.PrintQrcode(data, 25, 6, 0), _usbDev);
        _usbDriver.write(PrintCmd.PrintFeedline(2), _usbDev);
        _usbDriver.write(PrintCmd.SetAlignment(0), _usbDev);
    }

    /**
     * 设置字体旋转
     * iRotate: 0-解除旋转， 1-顺时针旋转90°
     */
    public void setRotate(int iRotate) {
        _usbDriver.write(PrintCmd.SetRotate(iRotate), _usbDev);      // 字体旋转
    }

    /**
     * 检测打印机状态
     */
    public int getPrintEndStatus() {
        int iRet = -1;
        byte[] bRead5 = new byte[1];
        byte[] bWrite5 = sendCommand();
        if (_usbDriver.read(bRead5, bWrite5, _usbDev) > 0) {
            iRet = checkStatus(bRead5[0]);
        }
        if (iRet == 0 || iRet > 0)
            return iRet;
        return iRet;
    }

    // 发送打印完成指令 1D 72 01
    private byte[] sendCommand() {
        byte[] b_send = new byte[3];
        int iIndex = 0;
        b_send[(iIndex++)] = 0x1D;
        b_send[(iIndex++)] = 0x72;
        b_send[(iIndex++)] = 0x01;
        return b_send;
    }

    /**
     * 图片打印png/jpg/bmp
     */
    public void printImg(Bitmap bitmap) {
        if (bitmap == null)
            return;

        int[] data = getPixelsByBitmap(bitmap);
        _usbDriver.write(PrintCmd.PrintDiskImagefile(data, bitmap.getWidth(), bitmap.getHeight()));
    }


    /**
     * jpg png bmp 彩色图片转换Bitmap数据为int[]数组
     *
     * @param bm
     * @return int[]
     */
    private int[] getPixelsByBitmap(Bitmap bm) {
        int width, heigh;
        width = bm.getWidth();
        heigh = bm.getHeight();
        int iDataLen = width * heigh;
        int[] pixels = new int[iDataLen];
        bm.getPixels(pixels, 0, width, 0, 0, width, heigh);
        return pixels;
    }

    public int checkStatus(byte bRecv) {
        if ((bRecv & 0x00) == 0x00) {
            return 0;  // 打印纸充足
        } else if ((bRecv & 0x03) == 0x03) {
            return 1;  // 打印纸将尽
        } else if ((bRecv & 0x60) != 0x60) {
            return 2;  // 打印机非空闲状态
        }
        return 3;      // 空闲状态
    }


}
