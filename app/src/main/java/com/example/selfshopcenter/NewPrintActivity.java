package com.example.selfshopcenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.selfshopcenter.bean.SearchOrderEntity;
import com.example.selfshopcenter.commoncls.CommonData;
import com.example.selfshopcenter.commoncls.ToastUtil;
import com.example.selfshopcenter.net.RetrofitHelper;
import com.example.selfshopcenter.printer.PrintUtil;
import com.example.selfshopcenter.printer.UsbPrintManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;


public class NewPrintActivity extends Activity {


    private UsbPrintManager printer = null;

    public TextView  txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newprint);

        txt=findViewById(R.id.alertinfo);

        //返回上一页
        TextView returnback = findViewById(R.id.returnback);
        returnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewPrintActivity.this, IndexActivity.class);
                startActivity(intent);
                finish();
            }
        });





        //确认打印
        TextView print = findViewById(R.id.surePrint);
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView inputqyid = findViewById(R.id.inputqyid);

                String  inputnumber = inputqyid.getText().toString();

                GetPrintStr(inputnumber);

            }
        });


    }


    //扫码枪的回车事件
    String barcode = "";

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            char pressedKey = (char) event.getUnicodeChar();
            barcode += pressedKey;
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            txt.setText(barcode);//设置 文本
            return super.dispatchKeyEvent(event);
        }


        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            if (!TextUtils.isEmpty(barcode)) {
                if (barcode.contains("\n")) {
                    barcode = barcode.substring(0, barcode.length() - 1);
                }
                //开始打印
                //txt.setText(barcode);//设置 文本

                GetPrintStr(barcode);

                barcode = "";
            }

        }
        return super.dispatchKeyEvent(event);

    }

    public  void  GetPrintStr(String transId){

        //拿到 订单流水号接口，去请求 查询订单详情接口
        Call<SearchOrderEntity> OrderDetailEntityCall = RetrofitHelper.getInstance().NewPrintById(transId);
        OrderDetailEntityCall.enqueue(new Callback<SearchOrderEntity>() {
            @Override
            public void onResponse(Call<SearchOrderEntity> call, Response<SearchOrderEntity> response) {

                if (response!=null) {
                    SearchOrderEntity body = response.body();
                    if (null != body) {
                        if (body.getCode().equals("success")) {
                            SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = new Date();
                            String day = form.format(date);

                            String storename = body.getData().getKhsname();


                            String Str = "                   欢迎光临                   " + "\n";
                            Str += "门店名称:" + storename + "\n";
                            Str += "流水号：" + body.getData().getTransId() + "     " + "\n";
                            //Str+="商户订单号："+CommonData.outTransId+"     "+"\n";
                            Str += "打印日期   " + day + "     " + "\n";
                            Str += "===============================================" + "\n";
                            Str += "条码     名称     数量        单价     金额" + "\n";

                            List<SearchOrderEntity.DataBean.PluMapBean> plumap = body.getData().getPluMap();

                            for (int sk = 0; sk < plumap.size(); sk++) {

                                String barcode = plumap.get(sk).getBarcode();
                                String sname = plumap.get(sk).getPluName();
                                double qty = 0;
                                double weight = plumap.get(sk).getNWeight();
                                double dj = 0;
                                dj = plumap.get(sk).getPluPrice();
                                if (weight == 0.00) {
                                    //说明重量是 0.  那就取显示数量
                                    qty = plumap.get(sk).getPluQty();

                                } else {
                                    //净重含量存在值 则显示重量
                                    qty = weight;
                                }

                                double zj = plumap.get(sk).getRealAmount();
                                Str += barcode + "\n";
                                Str += sname + "     " + qty + "     " + dj + "    " + zj + "  " + "\n";
                            }

                            //付款方式
                            String paytype = body.getData().getPayMap().getPayTypeName();
                            String paynet = body.getData().getPayMap().getPayVal();
                            int paycount = body.getData().getTotQty();
                            Str += "===============================================" + "\n";
                            Str += "付款方式             金额          总折扣" + "\n";

                            Str += paytype + "             " + paynet + "          " + body.getData().getDisAmount() + "\n";

                            Str += "总数量         应收        找零" + "\n";
                            Str += "" + paycount + "             " + paynet + "          0.00     " + "\n";

                            Str += "===============" + body.getData().getTrans_xsTime() + "=============" + "\n";
                            Str += "            谢谢惠顾，请妥善保管小票         " + "\n";
                            Str += "               开正式发票，当月有效              " + "\n";

                            usePrint(Str);
                        }
                        else
                        {
                            ToastUtil.showToast(NewPrintActivity.this, "查询失败",body.getMsg());
                        }
                    }
                    else{
                        ToastUtil.showToast(NewPrintActivity.this, "接口访问失败","请检查接口异常");

                    }


                }

            }

            @Override
            public void onFailure(Call<SearchOrderEntity> call, Throwable t) {
                ToastUtil.showToast(NewPrintActivity.this, "接口请求失败","请检查网络异常或接口请求异常");
            }
        });

    }


    public  void  usePrint(String  printstr){


//        TextView showtext = findViewById(R.id.showtext);
//
//        showtext.setText(printstr);
//        showtext.setSingleLine(false);


        try {

            PrintUtil.printReceipt(null,printstr);
            getPrintStatus();

        }
        catch (Exception e) {

            ToastUtil.showToast(NewPrintActivity.this, "异常通知", "打印机信息异常");

            e.printStackTrace();
        }

    }

    //查询打印机状态
    private void getPrintStatus() {
        String msg = "";
        int iRet = PrintUtil.getPrintEndStatus();

        switch (iRet) {
            case 0:
                msg = "正常";
                break;
            case 1:
                msg = "打印机未连接或未上电";
                break;
            case 2:
                msg = "打印机和调用库不匹配";
                break;
            case 3:
                msg = "打印头打开";
                break;
            case 4:
                msg = "切刀未复位";
                break;
            case 5:
                msg = "打印头过热";
                break;
            case 6:
                msg = "黑标错误";
                break;
            case 7:
                msg = "纸尽";
                break;
            case 8:
                msg = "纸将尽";
                break;
            case -1:
                msg = "异常";
                break;
        }
        if (iRet != 0) {

            Toast.makeText(NewPrintActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }


    //USB打印机连接
    private void getPrinter() {
        printer = UsbPrintManager.getInstance();
        printer.init(this);
    }

}
