package com.example.selfshopcenter.net;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.selfshopcenter.R;
import com.example.selfshopcenter.bean.AddGoodsEntity;
import com.example.selfshopcenter.commoncls.CommonData;
import com.example.selfshopcenter.commoncls.SplnfoList;
import com.example.selfshopcenter.commoncls.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAddAdapter extends BaseAdapter {


    private Context context;
    private List<HashMap<String, String>> list;
    private HashMap<String, Integer> pitchOnMap;
    //public Call<ResponseDeleteGoods> ResponseDeleteGoodsCall;
    //private Call<Addgoods> Addgoodsinfo;
    //private Call<com.ceshi.helloworld.bean.getCartItemsEntity> getCartItemsEntityCall;


    private OnItemRemoveListener adapterListener;
    public HashMap<String, Integer> getPitchOnMap() {
        return pitchOnMap;
    }

    public void setPitchOnMap(HashMap<String, Integer> pitchOnMap) {
        this.pitchOnMap = pitchOnMap;
    }

    public   boolean  is_click=true;

    public CreateAddAdapter(Context context, List<HashMap<String, String>> list) {
        this.context = context;
        this.list = list;

        pitchOnMap = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            pitchOnMap.put(list.get(i).get("id"), 0);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try {



            convertView = View.inflate(context, R.layout.activity_shopcar, null);
            final ListView listView1;
            listView1 = convertView.findViewById(R.id.listview);
            convertView = View.inflate(context, R.layout.shopcar_list, null);
            final CheckBox checkBox;
            ImageView icon;
            final TextView name, price, num, type, reduce, add, describe, y_price, y_title,barcode;

            name = convertView.findViewById(R.id.tv_goods_name);//商品名称
            price = convertView.findViewById(R.id.tv_goods_price);//商品价格
            num = convertView.findViewById(R.id.tv_num);//商品数量
            reduce = convertView.findViewById(R.id.tv_reduce);//减号
            y_price = convertView.findViewById(R.id.tv_yuan_price);//原价格
            y_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
            describe = convertView.findViewById(R.id.describe);//促销信息
            y_title = convertView.findViewById(R.id.tv_yuan_title);

            add = convertView.findViewById(R.id.tv_add);

            barcode=convertView.findViewById(R.id.barcode);


            name.setText(list.get(position).get("name"));//产品名称
            y_price.setText(list.get(position).get("MainPrice"));//原价
            price.setText(list.get(position).get("realprice"));//实际售价
            num.setText(list.get(position).get("count"));//商品数量
            describe.setText(list.get(position).get("actname"));//促销活动
            barcode.setText(list.get(position).get("barcode"));


            if ("0.00".equals(list.get(position).get("disc")) || "0".equals(list.get(position).get("disc")) || "0.0".equals(list.get(position).get("disc"))) {

                y_price.setVisibility(View.GONE);
                y_title.setVisibility(View.GONE);
            } else {

                y_price.setVisibility(View.VISIBLE);
                y_title.setVisibility(View.VISIBLE);
            }

            //关于重量的显示

            if ("0.00".equals(list.get(position).get("weight")) || "0".equals(list.get(position).get("weight")) || "0.0".equals(list.get(position).get("weight"))) {
                reduce.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);
            }
            else
            {
                reduce.setVisibility(View.INVISIBLE);
                add.setVisibility(View.INVISIBLE);
                //数量 显示为 重量
                //单位需要从基础数据里面获取
                //num.setText(list.get(position).get("weight")+list.get(position).get("unit"));

                //2020-02-14确定单位协定为kg
                num.setText(list.get(position).get("weight")+"kg");


            }


                /**
                 * Created by zhoupan on 2019/11/18.
                 * 删减购物车数量，因为数据显示原因，重新写
                 * Created by zhoupan on 2019/11/21.
                 * 因为觉得 移除商品的时候 ，为了重新限时促销信息  会导致列表重新刷新了，影响视觉效果，所以改为不显示 促销规则了
                 */

            reduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!is_click) {
                        return;
                    }
                    is_click = false;

                    //调用减少购物车商品数量接口 ，然后调用接口获取购物车列表
                    if (list.size() == 0) {
                        ToastUtil.showToast(context, "商品移除通知", "请误操作太过于频繁");
                        is_click = true;
                        return;
                    }

                    //初始化报文信息
                    try {
                        String getClickbarcode=list.get(position).get("id");
                        String weight = list.get(position).get("weight");
                        if ( !weight.equals("0")&&!weight.equals("")) {
                            ToastUtil.showToast(context, "商品增加通知", "很抱歉，当前称重产品暂不支持移除，您可以选择清空重录");
                            is_click = true;
                            return;
                        }


                        Call<AddGoodsEntity> substratGoods= RetrofitHelper.getInstance().AddGoodInfo(getClickbarcode, CommonData.Reduce);
                        substratGoods.enqueue(new Callback<AddGoodsEntity>() {
                            @Override
                            public void onResponse(Call<AddGoodsEntity> call, Response<AddGoodsEntity> response) {
                                if (response != null) {
                                    if (null == response.body()) {
                                        ToastUtil.showToast(context, "商品移除通知", "请误操作太快");
                                        return;
                                    }
                                    if (response.body().getCode().equals("success")) {
                                        if (Integer.valueOf(list.get(position).get("count")) <= 1) {

                                            pitchOnMap.remove(list.get(position).get("barcode"));
                                            //从集合里面移除
                                            CommonData.orderInfo.spList.remove(list.get(position).get("barcode"));
                                            list.remove(position);
                                        }
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

                                                if (CommonData.orderInfo.spList.containsKey(barcode)) {

                                                    //如果存在，拿到集合，增加数量，总价，折扣
                                                    CommonData.orderInfo.spList.get(barcode).get(0).setPackNum(sub_itemsList.get(sk).getQty());
                                                    CommonData.orderInfo.spList.get(barcode).get(0).setMainPrice(sub_itemsList.get(sk).getPrice());
                                                    CommonData.orderInfo.spList.get(barcode).get(0).setRealPrice(String.valueOf(sub_itemsList.get(sk).getNet()));  //实际总售价
                                                    CommonData.orderInfo.spList.get(barcode).get(0).setTotaldisc(sub_itemsList.get(sk).getDisc());
                                                    //修改列表的数量
                                                    for (int k = 0; k < list.size(); k++) {
                                                        if (list.get(k).get("barcode").equals(barcode)) {
                                                            list.get(k).put("count", String.valueOf(sub_itemsList.get(sk).getQty()));
                                                            list.get(k).put("MainPrice", sub_itemsList.get(sk).getPrice());
                                                            list.get(k).put("realprice", String.valueOf(sub_itemsList.get(sk).getNet()));
                                                            list.get(k).put("actname", itemsList.get(sm).getDisrule());
                                                            list.get(k).put("disc", String.valueOf(sub_itemsList.get(sk).getDisc()));
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        CommonData.list_adaptor = new CreateAddAdapter(CreateAddAdapter.this.context, list);
                                        listView1.setAdapter(CommonData.list_adaptor);
                                        listView1.setSelection(listView1.getBottom());
                                        try {
                                            listView1.setSelection(CommonData.list_adaptor.getCount() - 1);
                                        } catch (Exception ex) {

                                        }
                                        notifyDataSetChanged();
                                        mrefreshPriceInterface.refreshPrice(pitchOnMap);

                                        is_click = true;

                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<AddGoodsEntity> call, Throwable t) {
                                ToastUtil.showToast(context, "商品移除通知", "网络异常，请稍后重试 ");
                            }
                        });

                        mrefreshPriceInterface.refreshPrice(pitchOnMap);
                    } catch (Exception ex) {
                        ToastUtil.showToast(context, "商品移除通知", "请误操作太过于频繁");
                        return;
                    }
                }
            });


            //商品数量加
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String inputbarcode = list.get(position).get("barcode");
                    String weight = list.get(position).get("weight");
                    if ( !weight.equals("0")&&!weight.equals("")){
                        ToastUtil.showToast(context, "商品增加通知", "当前称重产品暂不支持手动增加，请扫描商品条形码");
                        return;
                    }

                    Call<AddGoodsEntity> substratGoods= RetrofitHelper.getInstance().AddGoodInfo(inputbarcode, CommonData.AddCar);
                    substratGoods.enqueue(new Callback<AddGoodsEntity>() {
                        @Override
                        public void onResponse(Call<AddGoodsEntity> call, Response<AddGoodsEntity> response) {

                            if (response.body() != null) {
                                if (response.body().getCode().equals("success")) {
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
                                            String nRealPrice = sub_itemsList.get(sk).getNet();

                                            if (CommonData.orderInfo.spList.containsKey(barcode)) {

                                                //如果存在，拿到集合，增加数量，总价，折扣
                                                CommonData.orderInfo.spList.get(barcode).get(0).setPackNum(sub_itemsList.get(sk).getQty());
                                                CommonData.orderInfo.spList.get(barcode).get(0).setMainPrice(sub_itemsList.get(sk).getPrice());
                                                CommonData.orderInfo.spList.get(barcode).get(0).setTotaldisc(sub_itemsList.get(sk).getDisc());
                                                CommonData.orderInfo.spList.get(barcode).get(0).setRealPrice(String.valueOf(sub_itemsList.get(sk).getNet()));
                                                //修改列表的数量
                                                String useqty = String.valueOf(sub_itemsList.get(sk).getQty());

                                                //修改列表的数量
                                                for (int k = 0; k < list.size(); k++) {
                                                    if (list.get(k).get("barcode").equals(barcode)) {
                                                        list.get(k).put("count", useqty);
                                                        list.get(k).put("MainPrice", sub_itemsList.get(sk).getPrice());
                                                        list.get(k).put("realprice", String.valueOf(sub_itemsList.get(sk).getNet()));
                                                        list.get(k).put("actname", itemsList.get(sm).getDisrule());
                                                        list.get(k).put("disc", String.valueOf(sub_itemsList.get(sk).getDisc()));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    CommonData.list_adaptor = new CreateAddAdapter(CreateAddAdapter.this.context, list);
                                    listView1.setAdapter(CommonData.list_adaptor);
                                    listView1.setSelection(listView1.getBottom());
                                    listView1.setSelection(CommonData.list_adaptor.getCount() - 1);
                                    notifyDataSetChanged();
                                    mrefreshPriceInterface.refreshPrice(pitchOnMap);

                                }
                                else {
                                    ToastUtil.showToast(context, "商品移除通知", response.body().getMsg());
                                    return;
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<AddGoodsEntity> call, Throwable t) {

                        }
                    });

                }

            });

        } catch (Exception e) {

            is_click = true;
            ToastUtil.showToast(context, "商品移除通知", "请误操作太过频繁");
            return  null;
        }

        return convertView;
    }




    /**
     * 创建接口
     */
    public interface RefreshPriceInterface {
        /**
         * 把价格展示到总价上
         * @param pitchOnMap
         */
        void refreshPrice(HashMap<String, Integer> pitchOnMap);
    }

    /**
     * 定义一个接口对象
     */
    private RefreshPriceInterface mrefreshPriceInterface;

    /**
     * 向外部暴露一个方法
     * 把价格展示到总价上
     * @param refreshPriceInterface
     */
    public void setRefreshPriceInterface(RefreshPriceInterface refreshPriceInterface) {
        mrefreshPriceInterface = refreshPriceInterface;
    }

    public static interface OnItemRemoveListener {
        public void onItemRemove(int position);
    }

}
