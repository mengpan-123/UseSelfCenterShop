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
            barcode.setText(list.get(position).get("id"));


            if ("0.00".equals(list.get(position).get("disc")) || "0".equals(list.get(position).get("disc")) || "0.0".equals(list.get(position).get("disc"))) {

                y_price.setVisibility(View.GONE);
                y_title.setVisibility(View.GONE);
            } else {

                y_price.setVisibility(View.VISIBLE);
                y_title.setVisibility(View.VISIBLE);
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
                        //ToastUtil.showToast(context, "商品移除通知", "请误操作太过频繁拉哈");
                        return;
                    }
                    is_click = false;

                    //调用减少购物车商品数量接口 ，然后调用接口获取购物车列表
                    if (list.size() == 0) {
                        ToastUtil.showToast(context, "商品移除通知", "请误操作太过于频繁");
                        return;
                    }

                    //初始化报文信息
                    /*List<RequestDeleteGoods.ItemMapBean> itemMap = new ArrayList<RequestDeleteGoods.ItemMapBean>();
                    try {

                        RequestDeleteGoods.ItemMapBean itemMapcls = new RequestDeleteGoods.ItemMapBean();
                        itemMapcls.setBarcode(list.get(position).get("id"));
                        itemMap.add(itemMapcls);


                        ResponseDeleteGoodsCall = RetrofitHelper.getInstance().responseDeleteGoods(itemMap);
                        ResponseDeleteGoodsCall.enqueue(new Callback<ResponseDeleteGoods>() {
                            @Override
                            public void onResponse(Call<ResponseDeleteGoods> call, Response<ResponseDeleteGoods> response) {
                                if (response != null) {
                                    if (null == response.body()) {
                                        ToastUtil.showToast(context, "商品移除通知", "请误操作太快");
                                        return;
                                    }
                                    if (response.body().getReturnX().getNCode() == 0) {

                                        //获取 购物车列表。然后解析购物车列表
                                        getCartItemsEntityCall = RetrofitHelper.getInstance().getCartItems(CommonData.userId, CommonData.khid);
                                        getCartItemsEntityCall.enqueue(new Callback<getCartItemsEntity>() {
                                            @Override
                                            public void onResponse(Call<getCartItemsEntity> call, Response<getCartItemsEntity> response) {
                                                if (response != null) {
                                                    getCartItemsEntity body = response.body();
                                                    if (null == body) {
                                                        ToastUtil.showToast(context, "商品移除通知22", "请误操作太快");
                                                        return;
                                                    }

                                                    if (body.getReturnX().getNCode() == 0) {
                                                        //如果原来的数量只有一个。直接从集合里面移除这一个
                                                        if (Integer.valueOf(list.get(position).get("count")) <= 1) {

                                                            pitchOnMap.remove(list.get(position).get("id"));
                                                            //从集合里面移除
                                                            CommonData.orderInfo.spList.remove(list.get(position).get("id"));
                                                            list.remove(position);
                                                        }

                                                        //下面先  获取到 总价和总金额折扣这些
                                                        CommonData.orderInfo.totalCount = body.getResponse().getTotalQty();
                                                        CommonData.orderInfo.totalPrice = body.getResponse().getShouldAmount();
                                                        CommonData.orderInfo.totalDisc = body.getResponse().getDisAmount();

                                                        //因为 可能存在组合促销啥的，少一个产品，,每一个产品的售价都会不一样，直接修改 单价信息
                                                        List<getCartItemsEntity.ResponseBean.ItemsListBean> itemsList = body.getResponse().getItemsList();
                                                        for (int sm = 0; sm < itemsList.size(); sm++) {
                                                            List<getCartItemsEntity.ResponseBean.ItemsListBean.ItemsBean> sub_itemsList = itemsList.get(sm).getItems();
                                                            for (int sk = 0; sk < sub_itemsList.size(); sk++) {
                                                                HashMap<String, String> map = new HashMap<>();
                                                                //拿到产品编码
                                                                String usebarcode = sub_itemsList.get(sk).getSBarcode();
                                                                double nRealPrice = sub_itemsList.get(sk).getNPluPrice();

                                                                if (CommonData.orderInfo.spList.containsKey(usebarcode)) {

                                                                    //如果存在本产品，重新拿到，拿到集合，增加数量，总价，折扣
                                                                   *//* CommonData.orderInfo.spList.get(usebarcode).get(0).setPackNum(sub_itemsList.get(sk).getNQty());
                                                                    CommonData.orderInfo.spList.get(usebarcode).get(0).setpluPrice(Double.valueOf(sub_itemsList.get(sk).getPluRealAmount()));*//*

                                                                    //如果存在，拿到集合，增加数量，总价，折扣
                                                                    CommonData.orderInfo.spList.get(usebarcode).get(0).setPackNum(sub_itemsList.get(sk).getNQty());
                                                                    CommonData.orderInfo.spList.get(usebarcode).get(0).setMainPrice(sub_itemsList.get(sk).getNRealPrice());
                                                                    CommonData.orderInfo.spList.get(usebarcode).get(0).setRealPrice(String.valueOf(sub_itemsList.get(sk).getPluRealAmount()));  //实际总售价



                                                                    //修改列表的数量
                                                                    for (int k = 0; k < list.size(); k++) {
                                                                        if (list.get(k).get("id").equals(usebarcode)) {
                                                                            list.get(k).put("count", String.valueOf(sub_itemsList.get(sk).getNQty()));
                                                                            list.get(k).put("MainPrice", String.valueOf(nRealPrice));
                                                                            list.get(k).put("realprice", String.valueOf(sub_itemsList.get(sk).getPluRealAmount()));
                                                                            list.get(k).put("actname", itemsList.get(sm).getDisRule());

                                                                        }
                                                                    }
                                                                }

                                                            }
                                                        }
                                                        //在上面 遍历过 之后重新赋值
                                                        //CommonData.orderInfo.spList = MapList;

                                                        //界面上实现  增加一个元素
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

                                                    } else {
                                                        ToastUtil.showToast(context, "商品移除通知", body.getReturnX().getStrInfo());
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<getCartItemsEntity> call, Throwable t) {
                                                ToastUtil.showToast(context, "商品移除通知", "网络异常，请稍后重试");
                                            }
                                        });
                                        mrefreshPriceInterface.refreshPrice(pitchOnMap);
                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseDeleteGoods> call, Throwable t) {
                                ToastUtil.showToast(context, "商品移除通知", "网络异常，请稍后重试 ");
                            }
                        });

                        mrefreshPriceInterface.refreshPrice(pitchOnMap);
                    } catch (Exception ex) {
                        ToastUtil.showToast(context, "商品移除通知", "请误操作太过于频繁");
                        return;
                    }*/
                }
            });


            //商品数量加
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String inputbarcode = list.get(position).get("id");

                    HashMap<String, String> map = new HashMap<>();
                   /* Addgoodsinfo = RetrofitHelper.getInstance().getgoodsinfo(inputbarcode, CommonData.khid, CommonData.userId, "0");
                    Addgoodsinfo.enqueue(new Callback<Addgoods>() {
                        @Override
                        public void onResponse(Call<Addgoods> call, Response<Addgoods> response) {

                            if (response.body() != null) {
                                if (response.body().getReturnX().getNCode() == 0) {

                                    //获取 购物车列表。然后解析购物车列表
                                    getCartItemsEntityCall = RetrofitHelper.getInstance().getCartItems(CommonData.userId, CommonData.khid);
                                    getCartItemsEntityCall.enqueue(new Callback<getCartItemsEntity>() {
                                        @Override
                                        public void onResponse(Call<getCartItemsEntity> call, Response<getCartItemsEntity> response) {
                                            if (response != null) {
                                                getCartItemsEntity body = response.body();
                                                if (body.getReturnX().getNCode() == 0) {
                                                    //下面先  获取到 总价和总金额折扣这些
                                                    CommonData.orderInfo.totalCount = body.getResponse().getTotalQty();
                                                    CommonData.orderInfo.totalPrice = body.getResponse().getShouldAmount();
                                                    CommonData.orderInfo.totalDisc = body.getResponse().getDisAmount();

                                                    List<getCartItemsEntity.ResponseBean.ItemsListBean> itemsList = body.getResponse().getItemsList();
                                                    for (int sm = 0; sm < itemsList.size(); sm++) {
                                                        List<getCartItemsEntity.ResponseBean.ItemsListBean.ItemsBean> sub_itemsList = itemsList.get(sm).getItems();
                                                        for (int sk = 0; sk < sub_itemsList.size(); sk++) {
                                                            //拿到产品编码
                                                            String barcode = sub_itemsList.get(sk).getSBarcode();
                                                            double nRealPrice = sub_itemsList.get(sk).getNPluPrice();

                                                            if (CommonData.orderInfo.spList.containsKey(barcode)) {

                                                                //如果存在，拿到集合，增加数量，总价，折扣
                                                                CommonData.orderInfo.spList.get(barcode).get(0).setPackNum(sub_itemsList.get(sk).getNQty());
                                                                CommonData.orderInfo.spList.get(barcode).get(0).setMainPrice(sub_itemsList.get(sk).getNRealPrice());
                                                                CommonData.orderInfo.spList.get(barcode).get(0).setRealPrice(String.valueOf(sub_itemsList.get(sk).getPluRealAmount()));  //实际总售价

                                                                //修改列表的数量
                                                                String useqty = String.valueOf(sub_itemsList.get(sk).getNQty());

                                                            *//*list.get(position).put("count", useqty);
                                                            list.get(position).put("MainPrice", String.valueOf(nRealPrice));
                                                            list.get(position).put("realprice", String.valueOf(sub_itemsList.get(sk).getPluRealAmount()));
                                                            list.get(position).put("actname", itemsList.get(sm).getDisRule());*//*
                                                                //修改列表的数量
                                                                for (int k = 0; k < list.size(); k++) {
                                                                    if (list.get(k).get("id").equals(barcode)) {
                                                                        list.get(k).put("count", useqty);
                                                                        list.get(k).put("MainPrice", String.valueOf(nRealPrice));
                                                                        list.get(k).put("realprice", String.valueOf(sub_itemsList.get(sk).getPluRealAmount()));
                                                                        list.get(k).put("actname", itemsList.get(sm).getDisRule());

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

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<getCartItemsEntity> call, Throwable t) {


                                        }
                                    });
                                } else {

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Addgoods> call, Throwable t) {

                        }
                    });*/

                }

            });

        } catch (Exception e) {

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
