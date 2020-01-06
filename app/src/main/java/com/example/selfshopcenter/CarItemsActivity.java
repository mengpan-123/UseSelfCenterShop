package com.example.selfshopcenter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.selfshopcenter.bean.*;
import com.example.selfshopcenter.commoncls.*;
import com.example.selfshopcenter.net.CreateAddAdapter;
import com.example.selfshopcenter.net.RetrofitHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarItemsActivity extends AppCompatActivity implements View.OnClickListener, CreateAddAdapter.RefreshPriceInterface{

    //当前添加的产品列表的集合
    private Map<String, List<SplnfoList>> MapList = new HashMap<String, List<SplnfoList>>();

    //用于界面上UI显示 的列表
    private TextView phone_view;  //显示会员名称的控件
    private TextView price;
    public TextView shopcar_num;
    private ImageView text_tip;
    private TextView yhmoney;
    private  TextView storename;
    private TextView tv_go_to_pay;  //去付款按钮的显示作用
    private LinearLayout listtop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopcar);
    }


    @Override
    public void onClick(View v) {
        //必须重写的内容
    }


    //扫码枪的回车事件。扫码产品之后进行添加
    String barcode = "";
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {


        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            char pressedKey = (char) event.getUnicodeChar();
            barcode += pressedKey;
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            // ToastUtil.showToast(InputGoodsActivity.this, "商品录入通知", barcode);
            if (!TextUtils.isEmpty(barcode)) {
                if (barcode.contains("\n")) {
                    barcode = barcode.substring(0, barcode.length() - 1);
                }
                AddnewSpid(barcode);
                barcode = "";
            }

        }
        return true;

    }


    /**
     * Created by zhoupan on 2019/11/7.
     * <p>
     * 扫码添加商品，封装方法，用于单个添加商品，和添加购物袋，仅用于添加商品，
     * 具体流程为 1：使用添加商品接口，
     *          2：使用购物车列表接口，获取到当前产品的实际售价，折扣价，促销内容等
     */

    public void AddnewSpid(String inputbarcode) {

        if (CommonData.orderInfo != null) {
            if (CommonData.orderInfo.spList != null) {
                MapList = CommonData.orderInfo.spList;
            }
        }
        HashMap<String, String> map = new HashMap<>();
        Call<AddGoodsEntity> AddGoods= RetrofitHelper.getInstance().AddGoodInfo(inputbarcode, CommonData.AddCar);
        AddGoods.enqueue(new Callback<AddGoodsEntity>() {
            @Override
            public void onResponse(Call<AddGoodsEntity> call, Response<AddGoodsEntity> response) {

                if (response.body() != null) {

                    if (response.body().getCode().equals("200")){
                        //查询成功之后拿到

                    }
                    else
                    {
                        ToastUtil.showToast(CarItemsActivity.this, "商品查询通知", response.body().getMsg());
                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<AddGoodsEntity> call, Throwable t) {

            }
        });
    }




    @Override
    public void refreshPrice(HashMap<String, Integer> pitchOnMap) {
        priceControl(pitchOnMap);
    }

    /**
     * 控制价格展示总价
     */
    public void priceControl(Map<String, Integer> pitchOnMap) {

        if (CommonData.orderInfo == null) {
            yhmoney.setText(" ￥ " + 0.00);
            shopcar_num.setText("0");
            price.setText(" ￥ 0.00");//应付金额

        } else {
            yhmoney.setText(" ￥ " + CommonData.orderInfo.totalDisc);
            shopcar_num.setText(String.valueOf(CommonData.orderInfo.totalCount));

            price.setText(" ￥ " +CommonData.orderInfo.totalPrice);//应付金额
        }



        if (CommonData.orderInfo.totalCount==0) {
            //listtop.setVisibility(View.GONE); //zhoupan  去掉的，默认一进来就应该显示
            tv_go_to_pay.setText("去付款");
            tv_go_to_pay.setBackgroundResource(R.drawable.button_black); // 使用Color类转换
            tv_go_to_pay.setEnabled(false);
        } else {
            try {
                tv_go_to_pay.setText("去付款(" + CommonData.orderInfo.totalPrice + ")");
            }
            catch(Exception ex){

            }
            //listtop.setVisibility(View.VISIBLE);
            tv_go_to_pay.setEnabled(true);
            tv_go_to_pay.setBackgroundResource(R.drawable.button_shape);

        }
    }
}
