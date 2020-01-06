package com.example.selfshopcenter.commoncls;

public class  SplnfoList {


    //产品编码，用此键 来判断 产品是否存在
    private String goodsId = "";

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getPluName() {
        return pluName;
    }

    public void setPluName(String pluName) {
        this.pluName = pluName;
    }

    public String getPluUnit() {
        return pluUnit;
    }

    public void setPluUnit(String pluUnit) {
        this.pluUnit = pluUnit;
    }

    public int getPluTypeId() {
        return pluTypeId;
    }

    public void setPluTypeId(int pluTypeId) {
        this.pluTypeId = pluTypeId;
    }

    public String getMainPrice() {
        return mainPrice;
    }

    public void setMainPrice(String mainPrice) {
        this.mainPrice = mainPrice;
    }

    public int getPackNum() {
        return packNum;
    }

    public void setPackNum(int packNum) {
        this.packNum = packNum;
    }

    public double getpluPrice() {
        return pluPrice;
    }

    public void setpluPrice(double pluPrice) {
        this.pluPrice = pluPrice;
    }


    public String getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(String realPrice) {
        this.realPrice = realPrice;
    }

    public String getNweight() {
        return nweight;
    }

    public void setNweight(String nweight) {
        this.nweight = nweight;
    }


    public String getActname() {
        return actname;
    }

    public void setActname(String actname) {
        this.actname = actname;
    }



    public double getTotaldisc() {
        return totaldisc;
    }

    public void setTotaldisc(double totaldisc) {
        this.totaldisc = totaldisc;
    }

    //商品条形码
    private String barcode = "";

    //产品名称
    private String pluName = "";

    //产品单位
    private String pluUnit = "";

    //产品类型
    private int pluTypeId;


    private String mainPrice ;

    //产品数量
    private int packNum;


    //称重产品的重量
    private String  nweight;

    //实际售价
    private String realPrice;

    //这个商品对应的 折扣或者促销/满减活动
    private String actname;

    //实际售价
    private double pluPrice;

    //订单折扣
    private double totaldisc;
}
