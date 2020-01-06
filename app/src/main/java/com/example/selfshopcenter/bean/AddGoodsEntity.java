package com.example.selfshopcenter.bean;

import java.io.Serializable;

public class AddGoodsEntity implements Serializable {
    /**
     * code : success
     * appid : keengee
     * appname : GetGoodsInfo
     * msg : 操作成功
     * maxPage : 1
     * currentPage : 0
     * Reqguid : d7c2e5c9-1088-4d57-8234-193129a87cdb
     * data : {"barcode":"2591738625454","dprice":"11","price":"12","qyid":"2019053100002","sname":"香片","spid":"10100029","unit":"个","qty":"2","net":"24"}
     */

    private String code;
    private String appid;
    private String appname;
    private String msg;
    private int maxPage;
    private int currentPage;
    private String Reqguid;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getReqguid() {
        return Reqguid;
    }

    public void setReqguid(String Reqguid) {
        this.Reqguid = Reqguid;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * barcode : 2591738625454
         * dprice : 11
         * price : 12
         * qyid : 2019053100002
         * sname : 香片
         * spid : 10100029
         * unit : 个
         * qty : 2
         * net : 24
         */

        private String barcode;
        private String dprice;
        private String price;
        private String qyid;
        private String sname;
        private String spid;
        private String unit;
        private String qty;
        private String net;
        private String disc;

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getDprice() {
            return dprice;
        }

        public void setDprice(String dprice) {
            this.dprice = dprice;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getQyid() {
            return qyid;
        }

        public void setQyid(String qyid) {
            this.qyid = qyid;
        }

        public String getSname() {
            return sname;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }

        public String getSpid() {
            return spid;
        }

        public void setSpid(String spid) {
            this.spid = spid;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getNet() {
            return net;
        }

        public void setNet(String net) {
            this.net = net;
        }
        public String getDisc() {
            return disc;
        }

        public void setDisc(String disc) {
            this.disc = disc;
        }
    }

}
