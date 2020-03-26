package com.example.selfshopcenter.bean;

import java.util.List;

public class TenOrderEntity {

    /**
     * code : success
     * appid : kengeek1
     * appname : QUERYORDER
     * msg : 操作成功
     * maxPage : 1
     * currentPage : 0
     * Reqguid : f6f15cdc-27c4-4a2e-bcf8-880558fa7acd
     * data : {"orderList":[{"saledate":"2020/3/24 0:00:00","bill":"0369120200324141413","out_transid":"4200000478202003244482680871","totalAmount":"18.90","totaldisc":"18.90"},{"saledate":"2020/3/24 0:00:00","bill":"0369120200324140142","out_transid":"0369120200324140142","totalAmount":"5.20","totaldisc":"5.20"},{"saledate":"2020/3/24 0:00:00","bill":"0369120200324135244","out_transid":"4200000472202003241456874835","totalAmount":"19.80","totaldisc":"19.80"},{"saledate":"2020/3/24 0:00:00","bill":"0369120200324133720","out_transid":"0369120200324133720","totalAmount":"101.50","totaldisc":"101.50"},{"saledate":"2020/3/24 0:00:00","bill":"0369120200324132647","out_transid":"4200000502202003245537354367","totalAmount":"10","totaldisc":"10"}]}
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
        private List<OrderListBean> orderList;

        public List<OrderListBean> getOrderList() {
            return orderList;
        }

        public void setOrderList(List<OrderListBean> orderList) {
            this.orderList = orderList;
        }

        public static class OrderListBean {
            /**
             * saledate : 2020/3/24 0:00:00
             * bill : 0369120200324141413
             * out_transid : 4200000478202003244482680871
             * totalAmount : 18.90
             * totaldisc : 18.90
             */

            private String saledate;
            private String bill;
            private String out_transid;
            private String totalAmount;
            private String totaldisc;

            public String getSaledate() {
                return saledate;
            }

            public void setSaledate(String saledate) {
                this.saledate = saledate;
            }

            public String getBill() {
                return bill;
            }

            public void setBill(String bill) {
                this.bill = bill;
            }

            public String getOut_transid() {
                return out_transid;
            }

            public void setOut_transid(String out_transid) {
                this.out_transid = out_transid;
            }

            public String getTotalAmount() {
                return totalAmount;
            }

            public void setTotalAmount(String totalAmount) {
                this.totalAmount = totalAmount;
            }

            public String getTotaldisc() {
                return totaldisc;
            }

            public void setTotaldisc(String totaldisc) {
                this.totaldisc = totaldisc;
            }
        }
    }
}
