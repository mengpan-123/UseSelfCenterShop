package com.example.selfshopcenter;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.selfshopcenter.bean.*;
import com.example.selfshopcenter.commoncls.*;
import com.example.selfshopcenter.net.CreateAddAdapter;
import com.example.selfshopcenter.net.RetrofitHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarItemsActivity extends AppCompatActivity implements View.OnClickListener, CreateAddAdapter.RefreshPriceInterface{

    //当前添加的产品列表的集合
    private Map<String, List<SplnfoList>> MapList = new HashMap<String, List<SplnfoList>>();
    private List<HashMap<String, String>> listmap = new ArrayList<>();

    private CreateAddAdapter adapter; //用于显示列表的容器，传输数据
    private ListView listview;  //用于画列表的
    View layout = null;

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


        initView();
    }


    @Override
    public void onClick(View v) {
        //必须重写的内容
    }

    private void initView() {
        //一进来就得执行。初始化会员支付，初始化订单号
        Prepay();

        listtop = findViewById(R.id.listtop);

        listview = (ListView) findViewById(R.id.listview);
        text_tip = (ImageView) findViewById(R.id.text_tip);

        price = (TextView) findViewById(R.id.tv_total_price);
        phone_view = (TextView) findViewById(R.id.IamHy);
        shopcar_num = findViewById(R.id.shopcar_num);
        yhmoney=findViewById(R.id.yhmoney);
        storename=findViewById(R.id.storename);
        if (null!=CommonData.deviceid){
            storename.setText(CommonData.khsname);
        }
        tv_go_to_pay = (TextView) findViewById(R.id.tv_go_to_pay);
        adapter = new CreateAddAdapter(CarItemsActivity.this, listmap);
        listview.setAdapter(adapter);
        listview.setEmptyView(text_tip);

        adapter.setRefreshPriceInterface(this);
        priceControl(adapter.getPitchOnMap());
        CommonData.list_adaptor = adapter;
    }


    /**
     * Created by zhoupan on 2019/11/6.
     * 预支付相关信息
     */

    public void Prepay() {

        //设置界面上显示的订单流水号
        SimpleDateFormat form = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String day=form.format(date);

        TextView txtv=findViewById(R.id.ordernumber);
        CommonData.ordernumber=day+String.format("%03d",CommonData.number);

        txtv.setText("流水号:"+CommonData.ordernumber);

        if (null==CommonData.orderInfo) {

            OrderInfo  orders=new  OrderInfo();
            CommonData.orderInfo=orders;
        }
        else
        {
            //预加载之前的产品信息
            if (null!=CommonData.orderInfo.spList){

                for (Map.Entry<String, List<SplnfoList>> entry : CommonData.orderInfo.spList.entrySet()){
                    HashMap<String, String> temp_map = new HashMap<>();
                    temp_map.put("id", entry.getValue().get(0).getBarcode());
                    temp_map.put("name", entry.getValue().get(0).getPluName());
                    temp_map.put("MainPrice", String.valueOf(entry.getValue().get(0).getMainPrice()));
                    temp_map.put("realprice", String.valueOf(entry.getValue().get(0).getRealPrice()));
                    temp_map.put("count", String.valueOf(entry.getValue().get(0).getPackNum()));
                    temp_map.put("actname", String.valueOf(entry.getValue().get(0).getActname()));
                    temp_map.put("disc", String.valueOf(entry.getValue().get(0).getTotaldisc()));
                    listmap.add(temp_map);
                }
                MapList=CommonData.orderInfo.spList;
            }
            adapter = new CreateAddAdapter(CarItemsActivity.this, listmap);
        }
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

                    if (response.body().getCode().equals("success")){
                        //查询成功之后拿到
                        AddGoodsEntity body = response.body();
                        CommonData.orderInfo.totalCount = body.getData().getTotalQty();
                        CommonData.orderInfo.totalPrice = body.getData().getTotAmount();
                        CommonData.orderInfo.totalDisc = body.getData().getDisAmount();


                        List<AddGoodsEntity.DataBean.ItemsListBean> itemsList = body.getData().getItemsList();
                        for (int sm = 0; sm < itemsList.size(); sm++) {
                            List<AddGoodsEntity.DataBean.ItemsListBean.ItemsBean> sub_itemsList = itemsList.get(sm).getItems();
                            for (int sk = 0; sk < sub_itemsList.size(); sk++) {
                                //拿到产品编码
                                String barcode = sub_itemsList.get(sk).getBarcode();
                                String nRealPrice = sub_itemsList.get(sk).getDprice();

                                if (MapList.containsKey(barcode)) {

                                    //如果存在，拿到集合，增加数量，总价，折扣
                                    MapList.get(barcode).get(0).setPackNum(sub_itemsList.get(sk).getQty());
                                    MapList.get(barcode).get(0).setMainPrice(sub_itemsList.get(sk).getPrice());
                                    MapList.get(barcode).get(0).setRealPrice(String.valueOf(sub_itemsList.get(sk).getDprice()));  //实际总售价

                                    //修改列表的数量
                                    for (int k = 0; k < listmap.size(); k++) {
                                        if (listmap.get(k).get("id").equals(barcode)) {
                                            listmap.get(k).put("count", String.valueOf(sub_itemsList.get(sk).getQty()));
                                            listmap.get(k).put("MainPrice", String.valueOf(nRealPrice));
                                            listmap.get(k).put("realprice", String.valueOf(sub_itemsList.get(sk).getDprice()));
                                            listmap.get(k).put("actname", itemsList.get(sm).getDisrule());
                                        }
                                    }
                                } else {
                                    List<SplnfoList> uselist = new ArrayList<SplnfoList>();
                                    SplnfoList usesplnfo = new SplnfoList();
                                    usesplnfo.setBarcode(sub_itemsList.get(sk).getBarcode());
                                    usesplnfo.setGoodsId(sub_itemsList.get(sk).getSpid());
                                    usesplnfo.setMainPrice(nRealPrice); //每一个产品的原价，无需计算
                                    usesplnfo.setPackNum(sub_itemsList.get(sk).getQty());
                                    usesplnfo.setPluName(sub_itemsList.get(sk).getSname());
                                    usesplnfo.setTotaldisc(sub_itemsList.get(sk).getDisc());// 总折扣
                                    usesplnfo.setRealPrice(sub_itemsList.get(sk).getNet());  //总实际售价
                                    usesplnfo.setActname(itemsList.get(sm).getDisrule());
                                    usesplnfo.setNweight(sub_itemsList.get(sk).getWeight());  //产品的重量


                                    uselist.add(usesplnfo);
                                    //说明产品不存在，直接增加进去
                                    MapList.put(barcode, uselist);


                                    //下面是只取几个字段去改变 界面显示的
                                    map.put("barcode", sub_itemsList.get(sk).getBarcode());
                                    map.put("id", barcode);
                                    map.put("name", sub_itemsList.get(sk).getSname());
                                    map.put("MainPrice", String.valueOf(nRealPrice));  //原价
                                    map.put("disc", String.valueOf(sub_itemsList.get(sk).getDisc()));  //折扣
                                    map.put("realprice", sub_itemsList.get(sk).getNet());
                                    map.put("count", String.valueOf(sub_itemsList.get(sk).getQty()));
                                    map.put("actname", itemsList.get(sm).getDisrule());
                                    listmap.add(map);


                                }
                            }
                        }
                        CommonData.orderInfo.spList = MapList;

                        //界面上实现  增加一个元素
                        adapter = new CreateAddAdapter(CarItemsActivity.this, listmap);

                        listview.setAdapter(adapter);
                        listview.setSelection(listview.getBottom());  //加这一句话的目的是，超屏幕现实的时候，自动定位在底部
                        listview.setSelection(adapter.getCount() - 1);
                        adapter.setRefreshPriceInterface(CarItemsActivity.this);
                        priceControl(adapter.getPitchOnMap());

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
                ToastUtil.showToast(CarItemsActivity.this, "商品查询通知", "请求失败");
                return;
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


    /**
     * 选择购物袋
     * input_tiaoma
     **/
    public void input_bags(View view) {


        ToastUtil.showToast(CarItemsActivity.this, "功能异常通知", "功能正在开发，请耐心等待");
        return;
    }

    /**
     * 手动输入条码  和 取消
     * input_tiaoma
     **/
    public void input_tiaoma(View view) {
        // 创建一个Dialog
        final Dialog dialog = new Dialog(this,
                R.style.myNewsDialogStyle);

        // 自定义对话框布局
        layout = View.inflate(this, R.layout.activity_inputbarcode,
                null);

        dialog.setContentView(layout);
        //确定
        TextView title = (TextView) layout.findViewById(R.id.close1);
        //取消
        TextView title2 = (TextView) layout.findViewById(R.id.close2);
        setDialogSize(layout);
        dialog.show();
        EditText editText1 = layout.findViewById(R.id.username);
        editText1 .setInputType(InputType.TYPE_NULL);
        // 设置确定按钮的事件
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText1 = layout.findViewById(R.id.username);
                String inputbarcode = editText1.getText().toString();

                AddnewSpid(inputbarcode);
                dialog.dismiss();

            }
        });
        // 设置取消按钮的事件
        title2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //dialog.dismiss();

                EditText editText1 = layout.findViewById(R.id.username);
                String inputbarcode = editText1.getText().toString();

                AddnewSpid(inputbarcode);
                dialog.dismiss();

            }
        });

    }



    /**
     * 动态控制弹出框的大小
     */
    private void setDialogSize(final View mView) {
        mView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                                       int oldRight, int oldBottom) {
                mView.setLayoutParams(new FrameLayout.LayoutParams(780,
                        1400));
            }
        });
    }


    /**
     * 数字键盘事件
     */
    public void numClick(View view) {
        Button bt = (Button) view;
        String text = bt.getText().toString();
        EditText editText1 = layout.findViewById(R.id.username);
        if (!TextUtils.isEmpty(editText1.getText())) {
            if (editText1.getText().toString().length() <= 23) {
                text = editText1.getText().toString() + text;
                editText1.setText(text);
                editText1.setSelection(text.length());
            }
        } else {
            editText1.setText(text);
            editText1.setSelection(text.length());
        }
    }


}
