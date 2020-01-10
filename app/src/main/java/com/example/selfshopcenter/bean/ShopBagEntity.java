package com.example.selfshopcenter.bean;

import java.util.List;

public class ShopBagEntity {
    /**
     * code : success
     * appid : keengee
     * appname : GETSHOPBAG
     * msg : 操作成功
     * maxPage : 1
     * currentPage : 0
     * Reqguid : 0efde728-3795-4056-a050-acfea7425c35
     * data : {"khid":"KH2019120900001","shobag":[{"sname":"大购物袋","price":".02"},{"sname":"小购物袋","price":".01"},{"sname":"中购物袋","price":".01"}]}
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
         * khid : KH2019120900001
         * shobag : [{"sname":"大购物袋","price":".02"},{"sname":"小购物袋","price":".01"},{"sname":"中购物袋","price":".01"}]
         */

        private String khid;
        private List<ShobagBean> shobag;

        public String getKhid() {
            return khid;
        }

        public void setKhid(String khid) {
            this.khid = khid;
        }

        public List<ShobagBean> getShobag() {
            return shobag;
        }

        public void setShobag(List<ShobagBean> shobag) {
            this.shobag = shobag;
        }

        public static class ShobagBean {
            /**
             * sname : 大购物袋
             * price : .02
             */

            private String sname;
            private String price;
            private String barcode;

            public String getSname() {
                return sname;
            }

            public void setSname(String sname) {
                this.sname = sname;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getBarcode() {
                return barcode;
            }

            public void setBarcode(String barcode) {
                this.barcode = barcode;
            }
        }
    }

}
