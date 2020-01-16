package com.example.selfshopcenter.printer;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

//import com.printsdk.usbsdk.UsbDriver;

/**
 * <pre>
 *     author: szf
 *     time  : 2018/08/02
 *     desc  : 美松USB打印机
 * </pre>
 */
public class UsbPrintManager {
    private final static String TAG=UsbPrintManager.class.getName();

    public static UsbDevice mUsbDevice = null;
    public static UsbDriver mUsbDriver = null;

    /*美松打印机设备ID*/
    private static final int PRINTER_MS_PID11 = 8211;
    private static final int PRINTER_MS_PID13 = 8213;
    private static final int PRINTER_MS_PID15 = 8215;
    private static final int PRINTER_MS_VENDORID = 1305;

    private static final String ACTION_USB_PERMISSION = "com.usb.sample.USB_PERMISSION";
    private static UsbPrintManager mUsbPrintManager = null;

    private UsbManager mUsbManager;
    private Context mContext = null;

    public static synchronized UsbPrintManager getInstance(){
        if (mUsbPrintManager==null){
            mUsbPrintManager=new UsbPrintManager();
        }
        return mUsbPrintManager;
    }

    public UsbPrintManager(){

    }

    public void init(Context context){
        this.mContext=context;
        if (mUsbDriver==null){
            getUsbDriverService(context);
        }
        usbConnected();
    }

    //销毁
    public void onDestory() {
        if (mUsbReceiver != null) {
            mContext.unregisterReceiver(mUsbReceiver);
        }
        mUsbDevice = null;
        mUsbDriver = null;
    }

    //USB打印机连接
    public boolean usbConnected() {
        boolean ret = false;
        try {
            if (!mUsbDriver.isConnected()) {
                for (UsbDevice device : mUsbManager.getDeviceList().values()) {
                    if ((device.getProductId() == PRINTER_MS_PID11 && device.getVendorId() == PRINTER_MS_VENDORID)
                            || (device.getProductId() == PRINTER_MS_PID13 && device.getVendorId() == PRINTER_MS_VENDORID)
                            || (device.getProductId() == PRINTER_MS_PID15 && device.getVendorId() == PRINTER_MS_VENDORID)) {

                        Log.d(TAG,"美松打印机:" + device);
                        ret = mUsbDriver.usbAttached(device);
                        if (!ret) {
                            break;
                        }
                        if (mUsbDriver.openUsbDevice(device)) {
                            ret = true;
                            mUsbDevice = device;
                            Log.d(TAG,"USB driver failed");
                        } else {
                            ret = false;
                            Log.d(TAG,"USB driver failed");
                        }
                    }
                }
            } else {
                ret = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    private void getUsbDriverService(final Context context) {
        mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        mUsbDriver = new UsbDriver(mUsbManager, context);

        PendingIntent permissionIntent1 = PendingIntent.getBroadcast(context, 0,
                new Intent(ACTION_USB_PERMISSION), 0);
        mUsbDriver.setPermissionIntent(permissionIntent1);

        //动态注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        context.registerReceiver(mUsbReceiver, filter);
    }

    /**
     * 创建一个广播接收器接收USB插拔信息：当插入USB插头插到一个USB端口，或从一个USB端口，移除装置的USB插头
     */
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG,"获取USB权限：" + action);
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                Log.d(TAG,"获取USB权限：" + intent);
                try {
                    //TODO:此处有bug，USB拔下再插入导致app崩溃
                    if (mUsbDriver.usbAttached(intent)) {
                        UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        Log.d(TAG,"USB设备已接入:" + device);

                        if ((device.getProductId() == PRINTER_MS_PID11 && device.getVendorId() == PRINTER_MS_VENDORID)
                                || (device.getProductId() == PRINTER_MS_PID13 && device.getVendorId() == PRINTER_MS_VENDORID)
                                || (device.getProductId() == PRINTER_MS_PID15 && device.getVendorId() == PRINTER_MS_VENDORID)) {

                            Log.d(TAG,"美松打印机:" + device);

                            if (mUsbDriver.openUsbDevice(device)) {
                                mUsbDevice = device;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                Log.d(TAG,"USB设备已拔出:" + device);

                if ((device.getProductId() == PRINTER_MS_PID11 && device.getVendorId() == PRINTER_MS_VENDORID)
                        || (device.getProductId() == PRINTER_MS_PID13 && device.getVendorId() == PRINTER_MS_VENDORID)
                        || (device.getProductId() == PRINTER_MS_PID15 && device.getVendorId() == PRINTER_MS_VENDORID)) {
                    Log.d(TAG,"美松打印机:" + device);
                    mUsbDriver.closeUsbDevice(device);
                    mUsbDevice = null;
                }
            } else if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    Log.d(TAG,"获取device：" + device);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if ((device.getProductId() == PRINTER_MS_PID11 && device.getVendorId() == PRINTER_MS_VENDORID)
                                || (device.getProductId() == PRINTER_MS_PID13 && device.getVendorId() == PRINTER_MS_VENDORID)
                                || (device.getProductId() == PRINTER_MS_PID15 && device.getVendorId() == PRINTER_MS_VENDORID)) {
                            //美松打印机
                            Log.d(TAG,"美松打印机:" + device);

                            mUsbDriver.usbAttached(device);
                            if (mUsbDriver.openUsbDevice(device)) {
                                mUsbDevice = device;
                                Log.d(TAG,"美松打印机Open success");
                                //myHandler.obtainMessage(PrinterConstant.Connect.SUCCESS).sendToTarget();
                            } else {
                                Log.d(TAG,"美松打印机Open error");
                                //myHandler.obtainMessage(PrinterConstant.Connect.FAILED).sendToTarget();
                            }
                        }
                    } else {
                        Log.d(TAG,"USB打印机权限被拒绝:" + device);
                    }
                }
            }
        }
    };

    


}
