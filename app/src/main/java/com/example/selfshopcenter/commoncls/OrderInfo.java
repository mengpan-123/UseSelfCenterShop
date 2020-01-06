package com.example.selfshopcenter.commoncls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderInfo  {

    public   String   prepayId="";


    //订单总价
    public   String   totalPrice="0";


    //订单总数量
    public   int   totalCount=0;


    //订单总折扣
    public   String   totalDisc="0";


    //产品的集合
    public Map<String, List<SplnfoList>> spList=  new HashMap<String, List<SplnfoList>>();

}
